package com.bean.classicmini;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    public surfaceView mainView;
    private static Context appContext;
    private static Activity mainActivity;

    public static Context getAppContext(){
        return appContext;
    }
    public static Activity getMainActivity() { return mainActivity; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appContext = getApplicationContext();
        mainActivity = this;

        mainView = new surfaceView(this);
        setContentView(mainView);
    }
}
