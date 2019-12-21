package com.example.texturetextcoordcompact;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import org.w3c.dom.Text;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import glm.mat._4.Mat4;
import glm.vec._3.Vec3;


public class MyRenderer extends MainActivity implements GLSurfaceView.Renderer {
    public Context rendererContext;

    private TexturedMesh newImage;
    private TexturedMesh secondImage;

    public MyRenderer(Context ctx) {
        rendererContext = ctx;
    }

    private String readFile(Context ctx, int id){
        InputStream inputStream = ctx.getResources().openRawResource(id);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String nextLine;
        StringBuilder body = new StringBuilder();

        try {
            while((nextLine = bufferedReader.readLine()) != null) {
                body.append(nextLine);
                body.append('\n');
            }
        }
        catch(IOException e) {
            return null;
        }

        return body.toString();
    }

    public int shaderNum;
    private void beginShader(){
        String sVertexShader = readFile(rendererContext, R.raw.vertex_shader);
        String sFragmentShader = readFile(rendererContext, R.raw.fragment_shader);

        int vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        int fragmentShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);

        GLES20.glShaderSource(vertexShader, sVertexShader);
        GLES20.glShaderSource(fragmentShader, sFragmentShader);

        GLES20.glCompileShader(vertexShader);
        GLES20.glCompileShader(fragmentShader);

        shaderNum = GLES20.glCreateProgram();
        GLES20.glAttachShader(shaderNum, vertexShader);
        GLES20.glAttachShader(shaderNum, fragmentShader);

        GLES20.glLinkProgram(shaderNum);

        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
    }

    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        GLES20.glClearColor(0.0f, 1.0f, 0.0f, 1.0f);
        beginShader();
        newImage = new TexturedMesh(R.drawable.picture, shaderNum, rendererContext, true);
        secondImage = new TexturedMesh(R.drawable.image, shaderNum, rendererContext, false);


        Integer num = 34;
        LinkedList<Integer> newList = new LinkedList<>();

        newList.add(num);

        num = 22;
        //Log.d("game12", String.valueOf(newList.get(0)));



    }

    public void onDrawFrame(GL10 unused) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        Mat4 firstMat = new Mat4(1.0f);
        firstMat.translate(new Vec3(0.5f, 0.0f, 0.0f));
        firstMat.scale(new Vec3(0.2f));
        newImage.draw(firstMat, shaderNum);

        Mat4 secondMat = new Mat4(1.0f);
        secondMat.translate(new Vec3(-0.5f, 0.0f, 0.0f));
        secondMat.scale(new Vec3(0.2f));
        secondImage.draw(secondMat, shaderNum);
        Log.d("HE", "HO");


    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }
}
