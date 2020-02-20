package com.bean.classicmini.components;

import android.opengl.GLES20;

import com.bean.classicmini.surfaceView;
import com.bean.classicmini.utilities.ClassicMiniMath;
import com.bean.classicmini.utilities.ClassicMiniShaders;
import com.bean.components.Components;

import java.util.ArrayList;
import java.util.List;

import glm.vec._2.i.Vec2i;
import glm.vec._3.Vec3;
import glm.vec._4.Vec4;

public class ParticleSystem extends Components {
    private static final float[] lowResVertices = new float[]{-1.0f, -1.0f, 0.0f, 1.0f, -1.0f, 0.0f, -1.0f, 1.0f, 0.0f};
    
    private static final float[] medResVertices = new float[]{-1.0f, -1.0f, -1.0f, 1.0f, -1.0f, -1.0f, -1.0f, 1.0f, -1.0f,
                                                        -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, 1.0f, -1.0f, 1.0f, -1.0f,
                                                        -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, 1.0f, 1.0f, -1.0f, -1.0f,
                                                        -1.0f, -1.0f, 1.0f, 1.0f, -1.0f, -1.0f, -1.0f, 1.0f, -1.0f};

    private static final float[] maxResVertices = new float[]{-1.0f, -1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f,  1.0f, -1.0f,
                                                        1.0f,  1.0f, -1.0f, -1.0f,  1.0f, -1.0f, -1.0f, -1.0f, -1.0f,
                                                        -1.0f, -1.0f,  1.0f, 1.0f, -1.0f,  1.0f, 1.0f,  1.0f,  1.0f,
                                                        1.0f,  1.0f,  1.0f, -1.0f,  1.0f,  1.0f, -1.0f, -1.0f,  1.0f,
                                                        -1.0f,  1.0f,  1.0f, -1.0f,  1.0f, -1.0f, -1.0f, -1.0f, -1.0f,
                                                        -1.0f, -1.0f, -1.0f, -1.0f, -1.0f,  1.0f, -1.0f,  1.0f,  1.0f,
                                                        1.0f,  1.0f,  1.0f, 1.0f,  1.0f, -1.0f, 1.0f, -1.0f, -1.0f,
                                                        1.0f, -1.0f, -1.0f, 1.0f, -1.0f,  1.0f, 1.0f,  1.0f,  1.0f,
                                                        -1.0f, -1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, -1.0f,  1.0f,
                                                        1.0f, -1.0f,  1.0f, -1.0f, -1.0f,  1.0f, -1.0f, -1.0f, -1.0f, 
                                                        -1.0f,  1.0f, -1.0f, 1.0f,  1.0f, -1.0f, 1.0f,  1.0f,  1.0f,
                                                        1.0f,  1.0f,  1.0f, -1.0f,  1.0f,  1.0f, -1.0f,  1.0f, -1.0f};

    private static SimpleMesh lowResParticle = new SimpleMesh();
    private static SimpleMesh medResParticle = new SimpleMesh();
    private static SimpleMesh highResParticle = new SimpleMesh();
    private static SimpleMesh[] allMeshes = new SimpleMesh[]{lowResParticle, medResParticle, highResParticle};

    private List<Transform> allParticles = new ArrayList<>();

    public Vec4 particleColour = new Vec4(1.0f);
    public Vec3 particleDecreaseSpeed = new Vec3(0.1f);

    public float particleGenerateTime = 0.0005f;
    public float particleSpeed = 30f;
    public float particleDeleteDistance = 25f;
    private float counter = particleGenerateTime;

    public Vec2i particleXRotationBounds = new Vec2i(0, 360);
    public Vec2i particleYRotationBounds = new Vec2i(0, 360);
    public Vec2i particleZRotationBounds = new Vec2i(0, 360);

    public int resolution = MED_RESOLUTION;
    public static int LOW_RESOLUTION = 0;
    public static int MED_RESOLUTION = 1;
    public static int MAX_RESOLUTION = 2;

    public static void loadMesh(int usedResolution){
        if(allMeshes[usedResolution].vertexCount == 0){
            float[][] allVertices = new float[][]{lowResVertices, medResVertices, maxResVertices};
            allMeshes[usedResolution].vertices = allVertices[usedResolution];
            allMeshes[usedResolution].begin();
        }
    }

    @Override
    public void begin() {
        loadMesh(resolution);
    }

    @Override
    public void mainloop(){
        // create
        if(counter < 0f){
            Vec3 rotation = new Vec3(
                    ClassicMiniMath.randomInteger(particleXRotationBounds.x, particleXRotationBounds.y),
                    ClassicMiniMath.randomInteger(particleYRotationBounds.x, particleYRotationBounds.y),
                    ClassicMiniMath.randomInteger(particleZRotationBounds.x, particleZRotationBounds.y)
            );

            Transform newParticle = new Transform();
            newParticle.position = ClassicMiniMath.copyVectorThree(getBeansComponent(Transform.class).position);
            newParticle.rotation = rotation;
            newParticle.scale = ClassicMiniMath.copyVectorThree(getBeansComponent(Transform.class).scale);

            allParticles.add(newParticle);
            counter = particleGenerateTime;
        }
        counter -= surfaceView.deltaTime;
        // mainloop
        int count = allParticles.size();
        for(int p = 0; p < count; p++){
            allParticles.get(p).scale.sub(particleDecreaseSpeed.mul(surfaceView.deltaTime));
            if(ClassicMiniMath.vectorThreeLessThan(allParticles.get(p).scale, new Vec3(0.0f))){
                allParticles.remove(allParticles.get(p));
                p -= 1;
                count -= 1;
                continue;
            }

            Vec3 added = allParticles.get(p).upVector();
            added.mul(new Vec3(surfaceView.deltaTime * particleSpeed));
            allParticles.get(p).position.add(added);

            if(ClassicMiniMath.vectorThreeDistance(allParticles.get(p).position, getBeansComponent(Transform.class).position) > particleDeleteDistance){
                allParticles.remove(allParticles.get(p));
                p -= 1;
                count -= 1;
                continue;
            }
        }
        // render
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        allMeshes[resolution].vertexBuffer.position(0);

        int positionHandle = GLES20.glGetAttribLocation(SimpleMesh.simpleMeshShader, "inPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 12, allMeshes[resolution].vertexBuffer);
        GLES20.glUseProgram(SimpleMesh.simpleMeshShader);

        ClassicMiniShaders.setMatrix4(Camera.perspectiveMatrix(), "projection", SimpleMesh.simpleMeshShader);
        ClassicMiniShaders.setMatrix4(Camera.viewMatrix(), "view", SimpleMesh.simpleMeshShader);
        ClassicMiniShaders.setVector4(particleColour, "colour", SimpleMesh.simpleMeshShader);

        for(int p = 0; p < count; p++){
            ClassicMiniShaders.setMatrix4(allParticles.get(p).toMatrix4(), "model", SimpleMesh.simpleMeshShader);
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, allMeshes[resolution].vertexCount);
        }

        GLES20.glDisable(GLES20.GL_BLEND);
    }
}
