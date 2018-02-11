package com.eza.mariwatch.mariwatch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        try{
            getSupportActionBar().hide();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(3000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    //Intent intent = new Intent(SplashScreen.this,MainActivity.class);
                    //startActivity(intent);
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            }
        };
        timerThread.start();
    }
}
