package com.bean.classicmini;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;

import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.bean.classicmini.components.KeyboardInput;
import com.bean.classicmini.utilities.ClassicMiniAdverts;
import com.bean.classicmini.utilities.ClassicMiniOutput;

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

        ClassicMiniAdverts.begin();
        ClassicMiniAdverts.getMainRelativeLayout().addView(mainView);
        ClassicMiniAdverts.getMainRelativeLayout().addView(ClassicMiniAdverts.getBannerAd(), ClassicMiniAdverts.getAdParams());

        setContentView(ClassicMiniAdverts.getMainRelativeLayout());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        surfaceView.keyPresses.put(keyCode, event);
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (KeyboardInput.getIfKeyboardOpen()) {
            InputMethodManager manager = (InputMethodManager) MainActivity.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }
}
