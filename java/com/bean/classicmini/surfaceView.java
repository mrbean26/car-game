package com.bean.classicmini;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import com.bean.classicmini.components.Camera;
import com.bean.classicmini.utilities.ClassicMiniAudio;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class surfaceView extends GLSurfaceView implements GLSurfaceView.Renderer {
    public static Scene currentScene;
    public static Camera mainCamera;
    public static int displayWidth, displayHeight;

    public static float startTime, currentTime, deltaTime;
    public static int framePerSecond;
    public static int framesThrough = 0;

    public surfaceView(Context ctx){
        super(ctx);
        setEGLContextClientVersion(2);

        setRenderer(this);
    }

    private void getFrameData(){
        deltaTime = ((System.nanoTime() / 1000000000.0f) - startTime) - currentTime;
        currentTime = (System.nanoTime() / 1000000000.0f) - startTime;
        framePerSecond = Math.round(1.0f / deltaTime);
        framesThrough = framesThrough + 1;

        updateTouch();
    }
    
    // touch
    public static float xTouchDown = -1.0f, yTouchDown = -1.0f;
    public static float xTouchUp = -1.0f, yTouchUp = -1.0f;
    public static float xTouch = -1.0f, yTouch = -1.0f;
    private void updateTouch(){
        xTouchDown = -1.0f;
        yTouchDown = -1.0f;

        xTouchUp = -1.0f;
        yTouchUp = -1.0f;

        xTouch = -1.0f;
        yTouch = -1.0f;
    }
    
    // renderer
    public void onSurfaceCreated(GL10 unused, EGLConfig config){
        currentScene = new Scene(R.raw.start);

        startTime = System.nanoTime() / 1000000000.0f;
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
    }

    public void onDrawFrame(GL10 unused) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        currentScene.mainloop();

        getFrameData();
        ClassicMiniAudio.ClassicMiniAudioMainloop();
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
        if(e.getAction() == MotionEvent.ACTION_UP){
            xTouchDown = e.getX();
            yTouchDown = e.getY();
        }
        if(e.getAction() == MotionEvent.ACTION_DOWN){
            xTouchUp = e.getX();
            yTouchUp = e.getY();
        }
        return true;
    }
}
