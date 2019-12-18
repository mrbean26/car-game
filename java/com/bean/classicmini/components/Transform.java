package com.bean.classicmini.components;

import com.bean.components.Components;

import glm.vec._3.Vec3;

public class Transform extends Components {
    public Transform(){
        name = "Transform";
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

    public float xScale = 0.0f;
    public float yScale = 0.0f;
    public float zScale = 0.0f;

    public Vec3 scale(){
        return new Vec3(xScale, yScale, zScale);
    }

    public Vec3 up(){
        Vec3 returned = new Vec3(0.0f);

        return returned;
    }

    public Vec3 forward(){
        Vec3 returned = new Vec3(0.0f);

        return returned;
    }

    public Vec3 right(){
        Vec3 returned = new Vec3(0.0f);

        return returned;
    }
}
