package com.bean.classicmini.components;

import android.opengl.GLES20;

import com.bean.classicmini.MainActivity;
import com.bean.classicmini.surfaceView;
import com.bean.components.Components;

import glm.mat._4.Mat4;
import glm.vec._3.Vec3;
import glm.vec._4.Vec4;

public class Camera extends Components {
    public float nearPlane = 0.1f, farPlane = 100.0f;

    public Vec4 backgroundColour = new Vec4(0.0f);
    @Override
    public void begin(){
        GLES20.glClearColor(backgroundColour.x, backgroundColour.y, backgroundColour.z, backgroundColour.z);
    }

    public void setBackgroundColour(Vec4 usedColour){
        backgroundColour = usedColour;
        GLES20.glClearColor(backgroundColour.x, backgroundColour.y, backgroundColour.z, backgroundColour.z);
    }

    public static Mat4 perspectiveMatrix(){
        Mat4 newMatrix = new Mat4(1.0f);
        try{
            newMatrix.perspectiveFov((float) Math.toRadians(45.0f), surfaceView.displayWidth, surfaceView.displayHeight, surfaceView.mainCamera.nearPlane, surfaceView.mainCamera.farPlane);
        } catch (NullPointerException e){
            MainActivity.error("No camera available");
        }
        return newMatrix;
    }

    public static Mat4 viewMatrix(){
        Mat4 newMatrix = new Mat4(1.0f);
        try{
            Camera mainCamera = surfaceView.mainCamera;
            Transform objectTransform = mainCamera.getBean().getComponents(Transform.class);
            Vec3 oppositePosition = new Vec3(-objectTransform.position.x, -objectTransform.position.y, -objectTransform.position.z);

            newMatrix = newMatrix.translate(oppositePosition);
            newMatrix = newMatrix.rotateX(Math.toRadians(objectTransform.rotation.x));
            newMatrix = newMatrix.rotateY(Math.toRadians(objectTransform.rotation.y));
            newMatrix = newMatrix.rotateZ(Math.toRadians(objectTransform.rotation.z));
        } catch (NullPointerException e){
            MainActivity.error("No Camera available");
        }
        return newMatrix;
    }

    public void setMain(){
        surfaceView.mainCamera = this;
    }
}
