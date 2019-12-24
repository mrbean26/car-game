package com.bean.classicmini.components;

import com.bean.classicmini.Bean;
import com.bean.classicmini.MainActivity;
import com.bean.classicmini.utilities.ClassicMiniMath;
import com.bean.components.Components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import glm.mat._4.Mat4;
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

    public Transform getHighestParent(){
        Transform upperParentTransform = this;
        while(upperParentTransform.parent != null){
            upperParentTransform = upperParentTransform.parent.getComponents(Transform.class);
        }
        return upperParentTransform;
    }

    public Mat4 toMatrix4(boolean single){
        Mat4 parentMatrix = new Mat4(1.0f);
        if(!single){
            // get parent matrix
            List<Transform> allParents = new ArrayList<>();
            Transform currentTransform = this;

            while(currentTransform.parent != null){
                allParents.add(currentTransform.parent.getComponents(Transform.class));
                currentTransform = currentTransform.parent.getComponents(Transform.class);
            }

            int size = allParents.size();
            for(int i = 0; i < size; i++){
                parentMatrix = allParents.get(i).toMatrix4(true).mul(parentMatrix);
            }
        }
        // get local matrix
        Mat4 newMatrix = new Mat4(1.0f);
        newMatrix.translate(position());

        newMatrix.rotate((float) Math.toRadians(xRotation), new Vec3(1.0f, 0.0f, 0.0f));
        newMatrix.rotate((float) Math.toRadians(yRotation), new Vec3(0.0f, 1.0f, 0.0f));
        newMatrix.rotate((float) Math.toRadians(zRotation), new Vec3(0.0f, 0.0f, 1.0f));

        newMatrix.scale(scale());
        return parentMatrix.mul(newMatrix);
    }

    public Bean parent;
    public HashMap<String, Bean> children = new HashMap<>();
}
