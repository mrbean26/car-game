package com.bean.classicmini.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import com.bean.classicmini.MainActivity;
import com.bean.classicmini.R;

public class ClassicMiniMaterial {
    public int textureNum;
    public int textureWidth, textureHeight;

    public String type = "colour"; // "colour" or "image"
    public String colourHex = "#FF0000";
    public int imagePath = R.drawable.no_texture_image;

    public ClassicMiniMaterial(){
        int[] textureId = new int[1];
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glGenTextures(1, textureId, 0);
        if(textureId[0] != 0) {
            textureNum = textureId[0];
        } else{
            MainActivity.error("Error loading texture.");
        }
    }

    public void begin(){
        if(imagePath != R.drawable.no_texture_image){
            type = "image";
        } if(type.equals("colour")){
            loadColour(colourHex);
        } if(type.equals("image")){
            loadImage(imagePath);
        }
    }

    public void bind(){
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureNum);
    }

    public void loadImage(int resourcePath){
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureNum);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        Bitmap image = BitmapFactory.decodeResource(MainActivity.getAppContext().getResources(), resourcePath, options);
        textureWidth = image.getWidth();
        textureHeight = image.getHeight();

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, image, 0);
        image.recycle();

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
    }

    public void loadColour(String hexColour){
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureNum);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        Bitmap newColour = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        newColour.setPixel(0, 0, Color.parseColor(hexColour));
        textureWidth = 0; textureHeight = 0;

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, newColour, 0);
        newColour.recycle();

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
    }
}
