package com.bean.classicmini;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import com.bean.classicmini.components.Camera;
import com.bean.classicmini.components.Mesh;
import com.bean.classicmini.components.Transform;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.egl.EGLConfig;

public class surfaceView extends GLSurfaceView implements GLSurfaceView.Renderer {
    public static Scene currentScene;
    public static Camera mainCamera;
    public static int displayWidth, displayHeight;
    public static float xTouch = -1.0f, yTouch = -1.0f;

    public static float startTime, currentTime, deltaTime;

    public surfaceView(Context ctx){
        super(ctx);
        setEGLContextClientVersion(2);

        setRenderer(this);
    }

    // renderer
    public void onSurfaceCreated(GL10 unused, EGLConfig config){
        currentScene = new Scene(R.raw.start);
        startTime = System.nanoTime() / 1000000000.0f;
    }

    public void onDrawFrame(GL10 unused){
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        currentScene.mainloop();
        xTouch = -1.0f; yTouch = -1.0f; // after all touch events

        deltaTime = ((System.nanoTime() / 1000000000.0f) - startTime) - currentTime;
        currentTime = (System.nanoTime() / 1000000000.0f) - startTime;
    }

    public void onSurfaceChanged(GL10 gl, int width, int height){
        GLES20.glViewport(0, 0, width, height);
        displayWidth = width;
        displayHeight = height;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e){
        xTouch = e.getX();
        yTouch = e.getY();
        return true;
    }
}
