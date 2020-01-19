package com.bean.classicmini.components;

import android.opengl.GLES20;

import com.bean.classicmini.MainActivity;
import com.bean.classicmini.R;
import com.bean.classicmini.surfaceView;
import com.bean.classicmini.utilities.ClassicMiniMaterial;
import com.bean.classicmini.utilities.ClassicMiniShaders;
import com.bean.components.Components;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import glm.mat._4.Mat4;
import glm.vec._2.Vec2;
import glm.vec._3.Vec3;
import glm.vec._4.Vec4;

public class Sprite extends Components {
    public FloatBuffer vertexBuffer;
    public int vertexCount;

    public static int spriteShader = -1;
    public boolean useLight = false;
    public ClassicMiniMaterial material = new ClassicMiniMaterial();
    public Vec3 colour = new Vec3(1.0f);

    public Vec2 xData = null;
    public Vec2 yData = null;
    public Vec2 zData = null;

    public Vec3[] getCollisionInfo(){
        if(xData == null || yData == null || zData == null){
            return new Vec3[]{new Vec3(0.0f), new Vec3(0.0f)};
        }

        Vec3 minimum = new Vec3(xData.x, yData.x, zData.x);
        Vec3 maximum = new Vec3(xData.y, yData.y, zData.y);

        minimum = new Vec3(new Vec4(minimum, 1.0f).mul(getBean().getComponents(Transform.class).toMatrix4(false)));
        maximum = new Vec3(new Vec4(maximum, 1.0f).mul(getBean().getComponents(Transform.class).toMatrix4(false)));

        return new Vec3[]{minimum, maximum};
    }

    public void draw(){
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        vertexBuffer.position(0);
        int positionHandle = GLES20.glGetAttribLocation(spriteShader, "inPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 32, vertexBuffer);

        vertexBuffer.position(3);
        int normalHandle = GLES20.glGetAttribLocation(spriteShader, "inNormal");
        GLES20.glEnableVertexAttribArray(normalHandle);
        GLES20.glVertexAttribPointer(normalHandle, 3, GLES20.GL_FLOAT, false, 32, vertexBuffer);

        vertexBuffer.position(6);
        int texHandle = GLES20.glGetAttribLocation(spriteShader, "inTexCoord");
        GLES20.glEnableVertexAttribArray(texHandle);
        GLES20.glVertexAttribPointer(texHandle, 2, GLES20.GL_FLOAT, false, 32, vertexBuffer);

        GLES20.glUseProgram(spriteShader);

        ClassicMiniShaders.setInt(0, "sampler", spriteShader);
        ClassicMiniShaders.setMatrix4(Camera.perspectiveMatrix(), "projection", spriteShader);
        ClassicMiniShaders.setMatrix4(Camera.viewMatrix(), "view", spriteShader);

        Mat4 model = getBean().getComponents(Transform.class).toMatrix4(false);
        ClassicMiniShaders.setMatrix4(model, "model", spriteShader);
        model = model.inverse();
        model = model.transpose();
        ClassicMiniShaders.setMatrix4(model, "transposedInversedModel", spriteShader);

        ClassicMiniShaders.setVector3(surfaceView.mainCamera.getBean().getComponents(Transform.class).position, "viewPos", spriteShader);
        ClassicMiniShaders.setFloatArray(Light.getLightInfo(), "lightInfoArray", spriteShader);
        ClassicMiniShaders.setInt(useLight? 1 : 0, "useLight", spriteShader);

        ClassicMiniShaders.setVector3(colour, "multiplyColour", spriteShader);
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
                -1.0f, -1.0f, 0.0f, 0.0f, 0.0f, -1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 0.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f,
                1.0f, -1.0f, 0.0f, 0.0f, 0.0f, -1.0f, 0.0f, 1.0f,

                -1.0f, -1.0f, 0.0f, 0.0f, 0.0f, -1.0f, 1.0f, 1.0f,
                -1.0f, 1.0f, 0.0f, 0.0f, 0.0f, -1.0f, 1.0f, 0.0f,
                1.0f, 1.0f, 0.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f
        };
        vertexCount = vertices.length / 8;

        // get lowest xyz for collisions
        xData = new Vec2(-1.0f, 1.0f);
        yData = new Vec2(-1.0f, 1.0f);
        zData = new Vec2(0.0f);

        // buffer
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
