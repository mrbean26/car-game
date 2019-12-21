package com.bean.classicmini.components;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import com.bean.classicmini.MainActivity;
import com.bean.components.Components;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Image extends Components {
    public static FloatBuffer vertexBuffer;
    public int textureNum, vertexCount;

    private void loadTexture(int resourcePath){
        int[] textureId = new int[1];
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glGenTextures(1, textureId, 0);

        if(textureId[0] != 0){
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId[0]);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;

            Bitmap image = BitmapFactory.decodeResource(MainActivity.getAppContext().getResources(), resourcePath, options);
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, image, 0);
            image.recycle();

            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
            textureNum = textureId[0];
        }
        else{
            MainActivity.error("Failed to load texture for " + objectName + "'s image.");
        }
    }

    public Image(int textureResourcePath){
        float[] vertices = new float[]{
                -1.0f, -1.0f, 0.0f, 0.0f, 1.0f,
                1.0f,  -1.0f, 0.0f, 1.0f, 1.0f,
                -1.0f, 1.0f, 0.0f, 0.0f, 0.0f,

                -1.0f, 1.0f, 0.0f, 0.0f, 0.0f,
                1.0f, 1.0f, 0.0f, 1.0f, 0.0f,
                1.0f, -1.0f, 0.0f, 1.0f, 1.0f,
        };
        vertexCount = vertices.length / 5;

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());

        vertexBuffer = byteBuffer.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        loadTexture(textureResourcePath);
    }

    public void draw(){

    }
}
