package com.bean.classicmini.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import com.bean.classicmini.MainActivity;

public class ClassicMiniTexture {
    public int imageWidth, imageHeight;
    public int textureNum;

    public ClassicMiniTexture(int resourcePath){
        int[] textureId = new int[1];
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glGenTextures(1, textureId, 0);

        if(textureId[0] != 0){
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId[0]);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;

            Bitmap image = BitmapFactory.decodeResource(MainActivity.getAppContext().getResources(), resourcePath, options);
            imageWidth = image.getWidth();
            imageHeight = image.getHeight();

            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, image, 0);
            image.recycle();

            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
            textureNum = textureId[0];
        }
        else{
            MainActivity.error("Failed to load texture.");
        }
    }
}
