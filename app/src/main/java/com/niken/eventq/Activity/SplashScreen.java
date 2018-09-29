package com.niken.eventq.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.niken.eventq.R;
import com.niken.eventq.Session.SessionManager;

public class SplashScreen extends AppCompatActivity {

    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        final SessionManager sessionManager=new SessionManager(this);
        Thread timer=new Thread(){
            public void run(){
                try{
                    sleep(2000);
                    if(sessionManager.isLoggedIn() && sessionManager.IsLoggedUser()){
                        startActivity(new Intent(SplashScreen.this,MainActivity.class));
                    }
                    else if(sessionManager.isLoggedIn()){
                        startActivity(new Intent(SplashScreen.this,EditProfilActivity.class));
                    }
                    else
                        startActivity(new Intent(SplashScreen.this,LoginActivity.class));
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
        timer.start();
    }
}
