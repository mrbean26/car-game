package com.bean.classicmini.utilities;

import android.util.Log;

import glm.vec._2.Vec2;
import glm.vec._3.Vec3;
import glm.vec._4.Vec4;

public class ClassicMiniOutput {
    public static void error(String message){
        Log.d("Bean:Error", message);
    }

    public static void output(String message){
        Log.d("Bean:Output", message);
    }

    public static void output(Vec4 message){
        Log.d("Bean:Output", String.valueOf(message));
    }

    public static void output(Vec3 message){
        Log.d("Bean:Output", String.valueOf(message));
    }

    public static void output(Vec2 message){
        Log.d("Bean:Output", String.valueOf(message));
    }

    public static void output(float message){
        Log.d("Bean:Output", String.valueOf(message));
    }

    public static void output(int message){
        Log.d("Bean:Output", String.valueOf(message));
    }

    public static void output(double message){
        Log.d("Bean:Output", String.valueOf(message));
    }

    public static void output(boolean message){
        Log.d("Bean:Output", String.valueOf(message));
    }
}
