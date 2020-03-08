package com.bean.classicmini;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    public surfaceView mainView;
    private static Context appContext;

    public static Context getAppContext(){
        return appContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appContext = getApplicationContext();

        mainView = new surfaceView(this);
        setContentView(mainView);
    }
}
