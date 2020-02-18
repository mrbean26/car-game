package com.bean.classicmini.components;

import android.opengl.GLES20;

import com.bean.classicmini.MainActivity;
import com.bean.classicmini.R;
import com.bean.classicmini.utilities.ClassicMiniShaders;
import com.bean.components.Components;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import glm.vec._4.Vec4;

public class SimpleMesh extends Components {
    public float[] vertices = new float[]{-1.0f, -1.0f, 0.0f, 1.0f, -1.0f, 0.0f, -1.0f, 1.0f, 0.0f};
    public int vertexCount = 0;
    public FloatBuffer vertexBuffer;
    public Vec4 colour = new Vec4(1.0f);

    public static int simpleMeshShader = -1;

    @Override
    public void begin(){
        // buffer data
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());

        vertexBuffer = byteBuffer.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        if(simpleMeshShader == -1){
            int fragmentShader = ClassicMiniShaders.createShader(R.raw.simplemeshfragment, GLES20.GL_FRAGMENT_SHADER);
            int vertexShader = ClassicMiniShaders.createShader(R.raw.simplemeshvertex, GLES20.GL_VERTEX_SHADER);

            int[] programs = {vertexShader, fragmentShader};
            simpleMeshShader = ClassicMiniShaders.createProgram(programs);
        }
        vertexCount = vertices.length / 3;
    }

    @Override
    public void mainloop(){
        render();
    }

    public void render(){
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        vertexBuffer.position(0);
        int positionHandle = GLES20.glGetAttribLocation(simpleMeshShader, "inPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 12, vertexBuffer);

        GLES20.glUseProgram(simpleMeshShader);

        ClassicMiniShaders.setMatrix4(Camera.perspectiveMatrix(), "projection", simpleMeshShader);
        ClassicMiniShaders.setMatrix4(Camera.viewMatrix(), "view", simpleMeshShader);
        ClassicMiniShaders.setMatrix4(getBeansComponent(Transform.class).toMatrix4(false), "model", simpleMeshShader);

        ClassicMiniShaders.setVector4(colour, "colour", simpleMeshShader);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
        GLES20.glDisable(GLES20.GL_BLEND);
    }
}
