package com.bean.classicmini;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    public surfaceView mainView;
    private static Context appContext;

    public static Context getAppContext(){
        return appContext;
    }

    public static void output(String output){
        Log.d("Bean:Output", output);
    }

    public static void error(String output){
        Log.d("Bean:Error", output);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appContext = getApplicationContext();

        mainView = new surfaceView(this);
        setContentView(mainView);
    }
}
