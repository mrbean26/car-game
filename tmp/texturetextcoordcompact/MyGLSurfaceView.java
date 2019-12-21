package com.example.texturetextcoordcompact;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class MyGLSurfaceView extends GLSurfaceView {
    public MyRenderer mainRenderer;

    public MyGLSurfaceView(Context ctx){
        super(ctx);
        setEGLContextClientVersion(2);

        mainRenderer = new MyRenderer(ctx);
        setRenderer(mainRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

}
