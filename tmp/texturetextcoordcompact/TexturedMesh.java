package com.example.texturetextcoordcompact;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLU;
import android.opengl.GLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import glm.mat._4.Mat4;

public class TexturedMesh {

    FloatBuffer vertexBuffer;
    ShortBuffer indexBuffer;

    int positionLocation, textureLocation;
    int textureNum;

    int vertexCount;

    public TexturedMesh(int texturePath, int shaderNum, Context ctx, boolean triangle){
        float[] vertices = {0.0f};
        short[] indices = {0};
        if(triangle){
            vertices = new float[]{
                0.0f, 1.0f, 0.0f, 0.5f, 0.0f,
                -1.0f, -1.0f, 0.0f, 0.0f, 1.0f,
                1.0f, -1.0f, 0.0f, 1.0f, 1.0f
            };

        }
        if(!triangle){
            vertices = new float[]{
                    -1.0f, -1.0f, 0.0f, 0.0f, 1.0f,
                    1.0f,  -1.0f, 0.0f, 1.0f, 1.0f,
                    -1.0f, 1.0f, 0.0f, 0.0f, 0.0f,

                    -1.0f, 1.0f, 0.0f, 0.0f, 0.0f,
                    1.0f, 1.0f, 0.0f, 1.0f, 0.0f,
                    1.0f, -1.0f, 0.0f, 1.0f, 1.0f,
            };
        }

        vertexCount = vertices.length / 5;
        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        // load texture
        int[] textureId = new int[1];
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glGenTextures(1, textureId, 0);

        if(textureId[0] != 0){
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId[0]);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;

            Bitmap image = BitmapFactory.decodeResource(ctx.getResources(), texturePath, options);
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, image, 0);
            image.recycle();

            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
            textureNum = textureId[0];

        }
        else{
            throw new RuntimeException("Error loading texture.");
        }
    }

    public void draw(Mat4 model, int shaderNum){
        vertexBuffer.position(0);
        int positionHandle = GLES20.glGetAttribLocation(shaderNum, "in_position");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 20, vertexBuffer);

        vertexBuffer.position(3);
        int texHandle = GLES20.glGetAttribLocation(shaderNum, "in_texCoord");
        GLES20.glEnableVertexAttribArray(texHandle);
        GLES20.glVertexAttribPointer(texHandle, 2, GLES20.GL_FLOAT, false, 20, vertexBuffer);

        GLES20.glUseProgram(shaderNum);
        float[] totalMat = model.toFa_();
        int modelMatLocation = GLES20.glGetUniformLocation(shaderNum, "model");
        GLES20.glUniformMatrix4fv(modelMatLocation, 1, false, totalMat, 0);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureNum);

        int textureLocation = GLES20.glGetUniformLocation(shaderNum, "sampler");
        GLES20.glUniform1i(textureLocation, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
    }

}
