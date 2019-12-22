package com.bean.classicmini.components;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import com.bean.classicmini.MainActivity;
import com.bean.classicmini.R;
import com.bean.classicmini.utilities.ClassicMiniMath;
import com.bean.classicmini.utilities.ClassicMiniShaders;
import com.bean.components.Components;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import glm.mat._4.Mat4;
import glm.vec._3.Vec3;

public class Image extends Components {
    public static FloatBuffer vertexBuffer;
    public int textureNum, vertexCount, textureResourcePath = R.drawable.notextureimage;
    public static int imageShader = -1;

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

    public void draw(){
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        vertexBuffer.position(0);
        int positionHandle = GLES20.glGetAttribLocation(imageShader, "in_position");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 20, vertexBuffer);

        vertexBuffer.position(3);
        int texHandle = GLES20.glGetAttribLocation(imageShader, "in_texCoord");
        GLES20.glEnableVertexAttribArray(texHandle);
        GLES20.glVertexAttribPointer(texHandle, 2, GLES20.GL_FLOAT, false, 20, vertexBuffer);

        // matrix
        Mat4 currentMatrix = ClassicMiniMath.getOrtho(); // ortho then transform bit

        if(getBean().hasComponents(Transform.class)){
            Transform objectTransform = getBean().getComponents(Transform.class);
            currentMatrix.scale(objectTransform.scale());
            currentMatrix.translate(objectTransform.position());

            currentMatrix.rotate(objectTransform.xRotation, new Vec3(1.0f, 0.0f, 0.0f));
            currentMatrix.rotate(objectTransform.yRotation, new Vec3(0.0f, 1.0f, 0.0f));
            currentMatrix.rotate(objectTransform.zRotation, new Vec3(0.0f, 0.0f, 1.0f));
        }

        float[] totalMat = currentMatrix.toFa_();

        GLES20.glUseProgram(imageShader);
        int modelMatLocation = GLES20.glGetUniformLocation(imageShader, "model");
        GLES20.glUniformMatrix4fv(modelMatLocation, 1, false, totalMat, 0);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureNum);

        int textureLocation = GLES20.glGetUniformLocation(imageShader, "sampler");
        GLES20.glUniform1i(textureLocation, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

        GLES20.glDisable(GLES20.GL_BLEND);
    }

    @Override
    public void mainloop(){
        draw();
    }

    @Override
    public void begin(){
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

        if(imageShader == -1){
            int fragmentShader = ClassicMiniShaders.createShader(R.raw.imagefragment, GLES20.GL_FRAGMENT_SHADER);
            int vertexShader = ClassicMiniShaders.createShader(R.raw.imagevertex, GLES20.GL_VERTEX_SHADER);

            int[] programs = {vertexShader, fragmentShader};
            imageShader = ClassicMiniShaders.createProgram(programs);
        }
    }
}
