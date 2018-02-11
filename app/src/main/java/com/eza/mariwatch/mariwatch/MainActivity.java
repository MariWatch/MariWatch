package com.eza.mariwatch.mariwatch;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback {





    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;
    private GoogleMap mMap;



    private static final int COLOR_RED_ARGB = 0x7FFF0000;
    private static final int POLYGON_STROKE_WIDTH_PX = 8;
    private static LatLng currentLocation;
    private NotificationUtils notifier;


    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Illegal Activity Reported", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


//        drawer.setSelected(true);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.main_fragment);
        mapFragment.getMapAsync(this);

        notifier = new NotificationUtils(this);
        notifier.RedAlert("fish","rod");

        verifyStoragePermissions(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_map) {
            // Handle the camera action
        } else if (id == R.id.nav_restrictioninfo) {
            startActivity(new Intent(getApplicationContext(),RestrictionInfo.class));// RestrictionInfo.class
        }
//        else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }





    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        enableMyLocation();
        setUpMapAndCurrentLocation();
        exportDB();
//        polygonTest(mMap);
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }








    private PolygonOptions drawpolygon(double arr[][]) {

        if (arr.length == 0) {
            return null;
        }

        PolygonOptions poly = new PolygonOptions();
        for (int i = 0; i < arr.length; i++) {
            poly.add(new LatLng(arr[i][0], arr[i][1]));
        }

        mMap.addPolygon(poly);
        return poly;
    }
    private void setUpMapAndCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mMap.setMyLocationEnabled(true); // current location pointer

        //getting Location and putting it on the map
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

        //update the camera to zoom on the location
        CameraUpdate update = CameraUpdateFactory.newLatLng(currentLocation);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(9);
        mMap.moveCamera(update);
        mMap.moveCamera(zoom);

    }
    private void polygonFiller(GoogleMap googleMap, double[][] arr){//polygonTest(GoogleMap googleMap){
        //CORDINATES
        //double[][] arr = {{29.2580, 48.1620}, {29.21, 48.606}, {29.2862, 48.1829}, {29.35, 48.12}}; // latitude,longitude

        // MAKE THE POLYGON
        Polygon polygon1 = googleMap.addPolygon(drawpolygon(arr).clickable(true));

        // CHANGING COLORS..ETC
        polygon1.setStrokeWidth(POLYGON_STROKE_WIDTH_PX);
        polygon1.setStrokeColor(COLOR_RED_ARGB);
        polygon1.setFillColor(COLOR_RED_ARGB);
        // Store a data object with the polygon, used here to indicate an arbitrary type.
        polygon1.setTag("alpha");
        polygon1.setClickable(true);

        // THIS IS NOT WORKING FOR AN INDIVDUAL POLYGON
        mMap.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener() {
            public void onPolygonClick(Polygon polygon) {
                //TODO ACTION ON CLICK
                //Toast.makeText(MainActivity.this, "text2222", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),RestrictionInfo.class));
            }
        });


//        double[][] arr2 = { {29.3862, 48.2829}, {29.45, 48.22},{29.3580, 48.2620}, {29.31, 48.706}}; // latitude,longitude
//        Polygon polygon2 = googleMap.addPolygon(drawpolygon(arr2).clickable(true));
//        polygon2.setStrokeWidth(POLYGON_STROKE_WIDTH_PX);
//        polygon2.setStrokeColor(COLOR_RED_ARGB);
//        polygon2.setFillColor(COLOR_RED_ARGB);
//        // Store a data object with the polygon, used here to indicate an arbitrary type.
//        polygon2.setTag("alpha");
//        polygon2.setClickable(true);
//
//        // THIS IS NOT WORKING FOR AN INDIVDUAL POLYGON
//        mMap.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener() {
//            public void onPolygonClick(Polygon polygon) {
//                //TODO ACTION ON CLICK
//                Toast.makeText(MainActivity.this, "text", Toast.LENGTH_SHORT).show();
//            }
//        });


        // CHECK IF THE LOCATION INSIDE THE POLYGON
        ArrayList<LatLng> vert = new ArrayList<>();
        for (int i = 0; i < vert.size(); i++) {
            LatLng op = new LatLng(arr[i][0], arr[i][1]);
            vert.add(op);
        }
        if (isPointInPolygon(currentLocation, vert)) {
            Toast.makeText(this, "INSIDE RESTRICTED AREA", Toast.LENGTH_SHORT).show();
        }


    }
    private void stylePolygon(Polygon polygon){
        //TODO
    } //TODO

    //BOTH USED TO CHECK IF INSIDE THE POLYGON
    private boolean isPointInPolygon(LatLng tap, ArrayList<LatLng> vertices) {
        int intersectCount = 0;
        for (int j = 0; j < vertices.size() - 1; j++) {
            if (rayCastIntersect(tap, vertices.get(j), vertices.get(j + 1))) {
                intersectCount++;
            }
        }

        return ((intersectCount % 2) == 1); // odd = inside, even = outside;
    }
    private boolean rayCastIntersect(LatLng tap, LatLng vertA, LatLng vertB) {

        double aY = vertA.latitude;
        double bY = vertB.latitude;
        double aX = vertA.longitude;
        double bX = vertB.longitude;
        double pY = tap.latitude;
        double pX = tap.longitude;

        if ((aY > pY && bY > pY) || (aY < pY && bY < pY)
                || (aX < pX && bX < pX)) {
            return false; // a and b can't both be above or below pt.y, and a or
            // b must be east of pt.x
        }

        double m = (aY - bY) / (aX - bX); // Rise over run
        double bee = (-aX) * m + aY; // y = mx + b
        double x = (pY - bee) / m; // algebra is neat!

        return x > pX;
    }
//    RestrictionInfo ri = new RestrictionInfo();
    private void exportDB() {
        try {
            Uri easya = Uri.parse("android.resource://com.example.yara.easya/raw/easya");
            CSVReader reader = new CSVReader(new InputStreamReader(getResources().openRawResource(R.raw.easya)));//(new FileReader(easya.getEncodedPath()));
            String[] test;
            do{
                test = reader.readNext();
                if(test!=null) {
                    //Toast.makeText(this, test[6], Toast.LENGTH_LONG).show();
//                    ri.changefishinfo("hi");
//                    ri.changecalinfo("hello");
//                    ri.changesizeinfo("anyone");
//                    ri.changeseasoninfo("bla");
                    polygonFiller(mMap,getItAs2DArray(test[6]));
                }
                //polygonFiller(mMap,getItAs2DArray(reader.readNext()));
            }while(reader.getHasNext());
        }

        catch (Exception sqlEx) {
            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
            Toast.makeText(this, sqlEx.getMessage(), Toast.LENGTH_LONG).show();
        }
        //polygonTest(mMap);

    }

    public static double[][] getItAs2DArray(String theLine) {
        //YOU CALL THIS METHOD TO CREATE A 2D ARRAY AND THEN YOU WILL HAVE TO CALL drawpolygon();
        ArrayList<StringBuilder> storage = new ArrayList<>();
        String line = theLine;//HERE WE PUT THE CORDINATES LINE
        line = line.substring(line.indexOf('{') + 1, line.indexOf('}'));
        StringBuilder num = new StringBuilder("");
        for (int j = 0; j < line.length(); j++) {
            char x = line.charAt(j);
            if (x != '~' && x != '!') {
                num.append(x);
            } else {
                storage.add(num);
                num = new StringBuilder("");
            }
        }
        double[][] arr = new double[storage.size()/2 ][2];
        int counter = 0;
        for (int i = 0; i < storage.size()-1; i += 2) {
            arr[counter][1] = Double.parseDouble(storage.get(i).toString());
            arr[counter][0] = Double.parseDouble(storage.get(i + 1).toString());
            counter++;
        }
        return arr;
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}
