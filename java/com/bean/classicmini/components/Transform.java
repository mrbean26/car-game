package com.bean.classicmini.components;

import com.bean.classicmini.utilities.ClassicMiniMath;
import com.bean.components.Components;

import glm.vec._3.Vec3;

public class Transform extends Components {
    public Transform(){
        name = "Transform";
    }

    @Override
    public void mainloop(){
        xPosition = xPosition + xVelocity;
        yPosition = yPosition + yVelocity;
        zPosition = zPosition + zVelocity;
    }

    public float xPosition = 0.0f;
    public float yPosition = 0.0f;
    public float zPosition = 0.0f;

    public Vec3 position(){
        return new Vec3(xPosition, yPosition, zPosition);
    }

    public float xRotation = 0.0f;
    public float yRotation = 0.0f;
    public float zRotation = 0.0f;

    public Vec3 rotation(){
        return new Vec3(xRotation, yRotation, zRotation);
    }

    public float xScale = 1.0f;
    public float yScale = 1.0f;
    public float zScale = 1.0f;

    public Vec3 scale(){
        return new Vec3(xScale, yScale, zScale);
    }

    public float xVelocity = 0.0f;
    public float yVelocity = 0.0f;
    public float zVelocity = 0.0f;

    public Vec3 up(){
        Vec3 returned = new Vec3(0.0f);
        returned.x = ClassicMiniMath.classicMiniCos(yRotation) * ClassicMiniMath.classicMiniSin(xRotation);
        returned.y = ClassicMiniMath.classicMiniCos(xRotation);
        returned.z = ClassicMiniMath.classicMiniSin(yRotation) * ClassicMiniMath.classicMiniSin(xRotation);
        return returned;
    }

    public Vec3 forward(){
        Vec3 returned = new Vec3(0.0f);
        returned.x = ClassicMiniMath.classicMiniCos(yRotation) * ClassicMiniMath.classicMiniCos(xRotation);
        returned.y = ClassicMiniMath.classicMiniSin(xRotation);
        returned.z = ClassicMiniMath.classicMiniSin(yRotation) * ClassicMiniMath.classicMiniCos(xRotation);
        return returned;
    }

    public Vec3 right(){
        Vec3 returned = new Vec3(0.0f);
        returned.x = ClassicMiniMath.classicMiniCos(yRotation + 90.0f) * ClassicMiniMath.classicMiniCos(xRotation);
        returned.y = ClassicMiniMath.classicMiniSin(xRotation);
        returned.z = ClassicMiniMath.classicMiniSin(yRotation + 90.0f) * ClassicMiniMath.classicMiniCos(xRotation);
        return returned;
    }
}
