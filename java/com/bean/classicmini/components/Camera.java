package com.bean.classicmini.components;

import com.bean.classicmini.MainActivity;
import com.bean.classicmini.surfaceView;
import com.bean.components.Components;

import glm.mat._4.Mat4;
import glm.vec._3.Vec3;

public class Camera extends Components {
    public float nearPlane = 0.1f, farPlane = 100.0f;

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

            Vec3 lookAt = objectTransform.position();
            lookAt = lookAt.add(objectTransform.forward());

            newMatrix.lookAt(objectTransform.position(), lookAt,
                    new Vec3(0.0f, 1.0f, 0.0f));
        } catch (NullPointerException e){
            MainActivity.error("No Camera available");
        }
        return newMatrix;
    }

    public void setMain(){
        surfaceView.mainCamera = this;
    }
}
