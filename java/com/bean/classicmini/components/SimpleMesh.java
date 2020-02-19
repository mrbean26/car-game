package com.bean.classicmini.components;

import android.opengl.GLES20;

import com.bean.classicmini.R;
import com.bean.classicmini.utilities.ClassicMiniShaders;
import com.bean.components.Components;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import glm.vec._2.Vec2;
import glm.vec._3.Vec3;
import glm.vec._4.Vec4;

public class SimpleMesh extends Components {
    public float[] vertices = new float[]{-1.0f, -1.0f, 0.0f, 1.0f, -1.0f, 0.0f, -1.0f, 1.0f, 0.0f};
    public int vertexCount = 0;
    public FloatBuffer vertexBuffer;
    public Vec4 colour = new Vec4(1.0f);

    public static int simpleMeshShader = -1;

    public Vec2 xData = null;
    public Vec2 yData = null;
    public Vec2 zData = null;

    public Vec3[] getCollisionInfo(){ // one min vec3, one max vec3
        if(xData == null || yData == null || zData == null){
            return new Vec3[]{new Vec3(0.0f), new Vec3(0.0f)};
        }

        Vec3 minimum = new Vec3(xData.x, yData.x, zData.x);
        Vec3 maximum = new Vec3(xData.y, yData.y, zData.y);

        return new Vec3[]{minimum, maximum};
    }

    @Override
    public void begin(){
        int verticesLength = vertices.length;
        for(int v = 0; v < verticesLength; v++){
            float point = vertices[v];
            if(v % 3 == 0){ // x
                if(xData == null){
                    xData = new Vec2(vertices[v]);
                    continue;
                }
                xData.x = point < xData.x ? point : xData.x;
                xData.y = point > xData.y ? point : xData.y;
            }
            if(v % 3 == 1){
                if(yData == null){
                    yData = new Vec2(vertices[v]);
                    continue;
                }
                yData.x = point < yData.x ? point : yData.x;
                yData.y = point > yData.y ? point : yData.y;
            }
            if(v % 3 == 2){
                if(zData == null){
                    zData = new Vec2(vertices[v]);
                    continue;
                }
                zData.x = point < zData.x ? point : zData.x;
                zData.y = point > zData.y ? point : zData.y;
            }
        }

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
        ClassicMiniShaders.setMatrix4(getBeansComponent(Transform.class).toMatrix4(), "model", simpleMeshShader);

        ClassicMiniShaders.setVector4(colour, "colour", simpleMeshShader);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
        GLES20.glDisable(GLES20.GL_BLEND);
    }
}
