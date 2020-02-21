package com.bean.classicmini.components;

import android.opengl.GLES20;

import com.bean.classicmini.R;
import com.bean.classicmini.surfaceView;
import com.bean.classicmini.utilities.ClassicMiniMaterial;
import com.bean.classicmini.utilities.ClassicMiniMath;
import com.bean.classicmini.utilities.ClassicMiniShaders;
import com.bean.components.Components;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import glm.mat._4.Mat4;
import glm.vec._3.Vec3;
import glm.vec._4.Vec4;

public class Sprite extends Components {
    public FloatBuffer vertexBuffer;
    public int vertexCount;

    public static int spriteShader = -1;
    public boolean useLight = false;
    public ClassicMiniMaterial material = new ClassicMiniMaterial();
    public Vec3 colour = new Vec3(1.0f);

    private Vec3[] allPoints = new Vec3[0];
    public Vec3[] getCollisionInfo(){ // one min vec3, one max vec3
        Vec3 maxPoint = null;
        Vec3 minPoint = null;

        int count = allPoints.length;
        for(int p = 0; p < count; p++){
            Vec4 currentPointFour = new Vec4(ClassicMiniMath.copyVectorThree(allPoints[p]), 1.0f);
            currentPointFour = currentPointFour.mul(getBeansComponent(Transform.class).toMatrix4());
            Vec3 currentPoint = new Vec3(currentPointFour);

            if(minPoint == null){
                minPoint = currentPoint;
                maxPoint = currentPoint;
                continue;
            }

            minPoint.x = currentPoint.x < minPoint.x ? currentPoint.x : minPoint.x;
            minPoint.y = currentPoint.y < minPoint.y ? currentPoint.y : minPoint.y;
            minPoint.z = currentPoint.z < minPoint.z ? currentPoint.z : minPoint.z;

            maxPoint.x = currentPoint.x > maxPoint.x ? currentPoint.x : maxPoint.x;
            maxPoint.y = currentPoint.y > maxPoint.y ? currentPoint.y : maxPoint.y;
            maxPoint.z = currentPoint.z > maxPoint.z ? currentPoint.z : maxPoint.z;
        }

        if(minPoint == null){
            minPoint = new Vec3(0f);
            maxPoint = new Vec3(0f);
        }

        return new Vec3[]{minPoint, maxPoint};
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

        Mat4 model = getBeansComponent(Transform.class).toMatrix4();
        ClassicMiniShaders.setMatrix4(model, "model", spriteShader);
        model = model.inverse();
        model = model.transpose();
        ClassicMiniShaders.setMatrix4(model, "transposedInversedModel", spriteShader);

        ClassicMiniShaders.setVector3(surfaceView.mainCamera.getBeansComponent(Transform.class).position, "viewPos", spriteShader);
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

        allPoints = new Vec3[vertexCount];
        for(int v = 0; v < vertexCount; v++){
            Vec3 newPoint = new Vec3();
            newPoint.x = vertices[v * 8 + 0];
            newPoint.y = vertices[v * 8 + 1];
            newPoint.z = vertices[v * 8 + 2];

            allPoints[v] = newPoint;
        }

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
