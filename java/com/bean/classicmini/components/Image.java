package com.bean.classicmini.components;

import android.opengl.GLES20;

import com.bean.classicmini.R;
import com.bean.classicmini.utilities.ClassicMiniMath;
import com.bean.classicmini.utilities.ClassicMiniShaders;
import com.bean.classicmini.utilities.ClassicMiniTexture;
import com.bean.components.Components;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import glm.mat._4.Mat4;
import glm.vec._3.Vec3;

public class Image extends Components {
    public FloatBuffer vertexBuffer;
    public int textureNum, vertexCount, textureResourcePath = R.drawable.no_texture_image;
    public static int imageShader = -1;
    public int imageWidth, imageHeight;

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
        Transform objectTransform = getBean().getComponents(Transform.class);
        currentMatrix.translate(objectTransform.position());
        currentMatrix.rotate((float) Math.toRadians(objectTransform.zRotation), new Vec3(0.0f, 0.0f, 1.0f));
        currentMatrix.scale(objectTransform.scale());

        GLES20.glUseProgram(imageShader);
        ClassicMiniShaders.setMatrix4(currentMatrix, "model", imageShader);
        ClassicMiniShaders.setInt(0, "sampler", imageShader);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureNum);

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

        ClassicMiniTexture newTexture = new ClassicMiniTexture(textureResourcePath);
        imageWidth = newTexture.imageWidth;
        imageHeight = newTexture.imageHeight;
        textureNum = newTexture.textureNum;

        if(imageShader == -1){
            int fragmentShader = ClassicMiniShaders.createShader(R.raw.imagefragment, GLES20.GL_FRAGMENT_SHADER);
            int vertexShader = ClassicMiniShaders.createShader(R.raw.imagevertex, GLES20.GL_VERTEX_SHADER);

            int[] programs = {vertexShader, fragmentShader};
            imageShader = ClassicMiniShaders.createProgram(programs);
        }
    }
}
