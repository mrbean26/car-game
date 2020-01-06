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

    public float xBackground = 0.0f, yBackground = 0.0f, zBackground = 0.0f, wBackground = 0.0f;
    @Override
    public void begin(){
        GLES20.glClearColor(xBackground, yBackground, zBackground, wBackground);
    }

    public void setBackgroundColour(Vec4 usedColour){
        xBackground = usedColour.x;
        yBackground = usedColour.y;
        zBackground = usedColour.z;
        wBackground = usedColour.w;

        GLES20.glClearColor(xBackground, yBackground, zBackground, wBackground);
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

            Vec3 lookAt = objectTransform.position();
            lookAt = lookAt.add(objectTransform.forwardVector());

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
