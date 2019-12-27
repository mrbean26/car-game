package com.bean.classicmini.components;

import android.opengl.GLES20;

import com.bean.classicmini.R;
import com.bean.classicmini.utilities.ClassicMiniMaterial;
import com.bean.classicmini.utilities.ClassicMiniShaders;
import com.bean.components.Components;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Sprite extends Components {
    public FloatBuffer vertexBuffer;
    public int vertexCount;
    public static int spriteShader = -1;
    public ClassicMiniMaterial material = new ClassicMiniMaterial();

    public void draw(){
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        vertexBuffer.position(0);
        int positionHandle = GLES20.glGetAttribLocation(spriteShader, "in_position");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 20, vertexBuffer);

        vertexBuffer.position(3);
        int texHandle = GLES20.glGetAttribLocation(spriteShader, "in_texCoord");
        GLES20.glEnableVertexAttribArray(texHandle);
        GLES20.glVertexAttribPointer(texHandle, 2, GLES20.GL_FLOAT, false, 20, vertexBuffer);

        GLES20.glUseProgram(spriteShader);

        ClassicMiniShaders.setInt(0, "sampler", spriteShader);
        ClassicMiniShaders.setMatrix4(Camera.perspectiveMatrix(), "projection", spriteShader);
        ClassicMiniShaders.setMatrix4(Camera.viewMatrix(), "view", spriteShader);
        ClassicMiniShaders.setMatrix4(getBean().getComponents(Transform.class).toMatrix4(false), "model", spriteShader);

        material.bind();

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

        if(spriteShader == -1){
            int fragmentShader = ClassicMiniShaders.createShader(R.raw.spritefragment, GLES20.GL_FRAGMENT_SHADER);
            int vertexShader = ClassicMiniShaders.createShader(R.raw.spritevertex, GLES20.GL_VERTEX_SHADER);

            int[] programs = {vertexShader, fragmentShader};
            spriteShader = ClassicMiniShaders.createProgram(programs);
        }
        material.begin();
    }
}
