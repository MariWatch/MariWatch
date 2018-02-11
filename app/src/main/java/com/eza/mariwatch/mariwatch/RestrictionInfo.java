package com.eza.mariwatch.mariwatch;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

@RequiresApi(api = Build.VERSION_CODES.M)
public class RestrictionInfo extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restrictioninfo);
        changetext();

    }
    public void changetext(){
        ImageView v = findViewById(R.id.fish_photo);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.sp);
        v.setImageBitmap(bm);
        //cal
        ImageView v2 = findViewById(R.id.cal_photo);
        Bitmap bm2 = BitmapFactory.decodeResource(getResources(), R.mipmap.cal);
        v2.setImageBitmap(bm2);
        //size
        ImageView v3 = findViewById(R.id.size_photo);
        Bitmap bm3 = BitmapFactory.decodeResource(getResources(), R.mipmap.si);
        v3.setImageBitmap(bm3);
        //season
        ImageView v4 = findViewById(R.id.season_photo);
        Bitmap bm4 = BitmapFactory.decodeResource(getResources(), R.mipmap.sea);
        v4.setImageBitmap(bm4);

        // set fish info
        TextView t = findViewById(R.id.fish_info);
        t.setText("All Fish are not allowed except when using Lines or hooks");
        // set e info
        TextView t2 = findViewById(R.id.cal_info);
        t2.setText("All Year");
        TextView t3 = findViewById(R.id.size_info);
        t3.setText("Zubaidi < 20cm");
        TextView t4 = findViewById(R.id.season_info);
        t4.setText("All seasons");
    }
    public void changefishinfo(String str){
        TextView t = findViewById(R.id.fish_info);
        t.setText(str);
    }
    public void changecalinfo(String str){
        TextView t2 = findViewById(R.id.cal_info);
        t2.setText(str);
    }
    public void changesizeinfo(String str){
        TextView t3 = findViewById(R.id.size_info);
        t3.setText(str);
    }
    public void changeseasoninfo(String str){
        TextView t4 = findViewById(R.id.season_info);
        t4.setText(str);
    }

}
