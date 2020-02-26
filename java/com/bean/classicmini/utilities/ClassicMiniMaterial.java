package com.bean.classicmini.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Movie;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import androidx.core.content.res.ResourcesCompat;

import com.bean.classicmini.MainActivity;
import com.bean.classicmini.R;
import com.bean.classicmini.surfaceView;

import java.io.InputStream;

import glm.Glm;
import glm.vec._3.Vec3;
import glm.vec._3.i.Vec3i;

public class ClassicMiniMaterial {
    // Program Functions
    public static String rgbToHex(Vec3i higherValues){
        higherValues = Glm.clamp_(higherValues, new Vec3i(0), new Vec3i(255));
        String returned = "#";
        returned = returned + Integer.toHexString(higherValues.x).toUpperCase();
        returned = returned + Integer.toHexString(higherValues.y).toUpperCase();
        returned = returned + Integer.toHexString(higherValues.z).toUpperCase();

        return returned;
    }

    public static String rgbToHex(Vec3 openGLValues){
        Vec3i integerValues = new Vec3i(Math.round(openGLValues.x * 255f), Math.round(openGLValues.y * 255f), Math.round(openGLValues.z * 255f));
        return rgbToHex(integerValues);
    }

    // Main Details
    public int textureNum;
    public int textureWidth, textureHeight;
    public String type = "colour"; // "colour" or "image" or "text" or "gif"

    public ClassicMiniMaterial(){
        if(!type.equals("gif")){
            int[] textureId = new int[1];
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glGenTextures(1, textureId, 0);
            if(textureId[0] != 0) {
                textureNum = textureId[0];
            } else{
                ClassicMiniOutput.error("Error loading texture.");
            }
        }
    }

    public void begin(){
        if(type.equals("colour")){
            loadColour(colourHex);
        } if(type.equals("image")){
            loadImage(imagePath);
        } if(type.equals("text")){
            loadText(textMaterialInfo.textSize,
                    textMaterialInfo.displayedText, textMaterialInfo.textCentered);
        } if(type.equals("gif")){
            loadGIF(gifMaterialInfo.gifPath);
        }
    }

    public void bind(){
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        if(!type.equals("gif")){
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureNum);
        }
        if(type.equals("gif")){
            if(gifMaterialInfo.textureIDs.size() == 0){
                return;
            }
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,
                    gifMaterialInfo.textureIDs.get(gifMaterialInfo.gifTimer));

            if(gifMaterialInfo.currentTimer < 0f){
                gifMaterialInfo.gifTimer = gifMaterialInfo.gifTimer + 1;
                if(gifMaterialInfo.gifTimer > gifMaterialInfo.maxGifTimer - 1){
                    gifMaterialInfo.gifTimer = 0;
                }
                gifMaterialInfo.currentTimer = gifMaterialInfo.timers.get(gifMaterialInfo.gifTimer);
            }
            gifMaterialInfo.currentTimer -= surfaceView.deltaTime;
        }
    }

    // Image Texture
    public int imagePath = R.drawable.no_texture_image;
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

    // Text Texture
    public ClassicMiniTextMaterialInfo textMaterialInfo = new ClassicMiniTextMaterialInfo();
    public float getXTextMultiplier(){
        return textMaterialInfo.xTextMultiplier; // read only variable from outside class
    }
    public void loadText(int size, String text, boolean centered){
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureNum);

        Bitmap texture = Bitmap.createBitmap(512, 512, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(texture);
        texture.eraseColor(0);

        Typeface font = ResourcesCompat.getFont(MainActivity.getAppContext(), textMaterialInfo.fontPath);

        Paint textPaint = new Paint();
        textPaint.setTextSize(size);
        textPaint.setAntiAlias(true);
        textPaint.setARGB(255, 255, 255, 255);
        textPaint.setTypeface(font);

        // resize to image (not outside)
        float scale = 512f / textPaint.measureText(text);
        textMaterialInfo.xTextMultiplier = textPaint.measureText(text) / 512f;
        textPaint.setTextScaleX(scale);

        int xPosition = (canvas.getWidth() / 2);
        int yPosition = (int) ((canvas.getHeight() / 2) - ((textPaint.descent() + textPaint.ascent()) / 2));
        if(centered){ xPosition = xPosition  - ((int) textPaint.measureText(text) / 2); }

        canvas.drawText(text, xPosition, yPosition, textPaint);

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, texture, 0);
        texture.recycle();

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
    }

    // Colour Texture
    public String colourHex = "#FF0000";
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

    // GIF Texture
    public ClassicMiniGIFMaterialInfo gifMaterialInfo = new ClassicMiniGIFMaterialInfo();
    public static Matrix gifMatrix = null;
    public void loadGIF(int resourcePath){
        if(gifMatrix == null){
            gifMatrix = new Matrix();
            gifMatrix.preScale(-1f, 1f);
        }

        InputStream inputStream = MainActivity.getAppContext().getResources().openRawResource(gifMaterialInfo.gifPath);
        gifMaterialInfo.gifData = Movie.decodeStream(inputStream);

        gifMaterialInfo.gifCanvasTexture = Bitmap.createBitmap(gifMaterialInfo.gifData.width(), gifMaterialInfo.gifData.height(), Bitmap.Config.ARGB_8888);
        gifMaterialInfo.gifCanvas = new Canvas(gifMaterialInfo.gifCanvasTexture);
        gifMaterialInfo.gifCanvasTexture.eraseColor(0);

        // first image
        gifMaterialInfo.gifData.draw(gifMaterialInfo.gifCanvas, 0f, 0f);
       Bitmap lastImage = Bitmap.createBitmap(gifMaterialInfo.gifCanvasTexture);

        // loop
        int differentFrames = 0;
        int lastFrameTimestamp = 0;
        for(int i = 0; i < gifMaterialInfo.gifData.duration(); i++){
            lastFrameTimestamp += 1;
            gifMaterialInfo.gifData.setTime(i);
            gifMaterialInfo.gifData.draw(gifMaterialInfo.gifCanvas, 0f, 0f);

            if(!gifMaterialInfo.gifCanvasTexture.sameAs(lastImage)){
                differentFrames += 1;
                lastImage = Bitmap.createBitmap(gifMaterialInfo.gifCanvasTexture);

                int[] textureId = new int[1];
                GLES20.glGenTextures(1, textureId, 0);

                Bitmap flippedTextureImage = Bitmap.createBitmap(gifMaterialInfo.gifCanvasTexture, 0, 0, gifMaterialInfo.gifCanvasTexture.getWidth(),
                        gifMaterialInfo.gifCanvasTexture.getHeight(), gifMatrix, true); // flip horizontally to make it normal
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId[0]);
                GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, flippedTextureImage, 0);

                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

                gifMaterialInfo.textureIDs.add(textureId[0]);
                gifMaterialInfo.timers.add(lastFrameTimestamp / 1000f);
                lastFrameTimestamp = 0;
            }
        }

        gifMaterialInfo.maxGifTimer = differentFrames;
        if(differentFrames > 0){
            gifMaterialInfo.currentTimer = gifMaterialInfo.timers.get(0);
        }
    }
}