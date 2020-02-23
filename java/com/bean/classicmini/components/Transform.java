package com.bean.classicmini.components;

import com.bean.classicmini.Bean;
import com.bean.classicmini.surfaceView;
import com.bean.classicmini.utilities.ClassicMiniMath;
import com.bean.components.Components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import glm.mat._4.Mat4;
import glm.vec._3.Vec3;

public class Transform extends Components {
    @Override
    public void mainloop(){
        updateVelocity();
    }

    public void updateVelocity(){
        Vec3 start = position;
        start = start.add(forwardVector().mul(velocity.x * surfaceView.deltaTime));
        start = start.add(upVector().mul(velocity.y * surfaceView.deltaTime));
        start = start.add(rightVector().mul(velocity.z * surfaceView.deltaTime));

        position.x = start.x;
        position.y = start.y;
        position.z = start.z;
    }

    public Vec3 position = new Vec3();
    public Vec3 rotation = new Vec3();
    public Vec3 scale = new Vec3(1.0f);

    public Vec3 velocity = new Vec3();

    public Vec3 upVector(){
        Vec3 returned = new Vec3(0.0f);
        returned.x = ClassicMiniMath.classicMiniCos(rotation.y) * ClassicMiniMath.classicMiniSin(rotation.x);
        returned.y = ClassicMiniMath.classicMiniCos(rotation.x);
        returned.z = ClassicMiniMath.classicMiniSin(rotation.y) * ClassicMiniMath.classicMiniSin(rotation.x);
        return returned;
    }

    public Vec3 forwardVector(){
        Vec3 returned = new Vec3(0.0f);
        returned.x = ClassicMiniMath.classicMiniCos(rotation.y) * ClassicMiniMath.classicMiniCos(rotation.x);
        returned.y = ClassicMiniMath.classicMiniSin(rotation.x);
        returned.z = ClassicMiniMath.classicMiniSin(rotation.y) * ClassicMiniMath.classicMiniCos(rotation.x);
        return returned;
    }

    public Vec3 rightVector(){
        Vec3 returned = new Vec3(0.0f);
        returned.x = ClassicMiniMath.classicMiniCos(rotation.y + 90.0f) * ClassicMiniMath.classicMiniCos(rotation.x);
        returned.y = ClassicMiniMath.classicMiniSin(rotation.x);
        returned.z = ClassicMiniMath.classicMiniSin(rotation.y + 90.0f) * ClassicMiniMath.classicMiniCos(rotation.x);
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
        newMatrix.translate(position);

        newMatrix.rotate((float) Math.toRadians(rotation.x), new Vec3(1.0f, 0.0f, 0.0f));
        newMatrix.rotate((float) Math.toRadians(rotation.y), new Vec3(0.0f, 1.0f, 0.0f));
        newMatrix.rotate((float) Math.toRadians(rotation.z), new Vec3(0.0f, 0.0f, 1.0f));

        newMatrix.scale(scale);
        return parentMatrix.mul(newMatrix);
    }

    public Mat4 toMatrix4(){ // single false as default
        Mat4 parentMatrix = new Mat4(1.0f);
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
        // get local matrix
        Mat4 newMatrix = new Mat4(1.0f);
        newMatrix.translate(position);

        newMatrix.rotate((float) Math.toRadians(rotation.x), new Vec3(1.0f, 0.0f, 0.0f));
        newMatrix.rotate((float) Math.toRadians(rotation.y), new Vec3(0.0f, 1.0f, 0.0f));
        newMatrix.rotate((float) Math.toRadians(rotation.z), new Vec3(0.0f, 0.0f, 1.0f));

        newMatrix.scale(scale);
        return parentMatrix.mul(newMatrix);
    }

    public Mat4 toMatrix4(boolean useTranslate, boolean useRotate, boolean useScale){ // single = false as default
        Mat4 parentMatrix = new Mat4(1.0f);
        // get parent matrix
        List<Transform> allParents = new ArrayList<>();
        Transform currentTransform = this;

        while(currentTransform.parent != null){
            allParents.add(currentTransform.parent.getComponents(Transform.class));
            currentTransform = currentTransform.parent.getComponents(Transform.class);
        }

        int size = allParents.size();
        for(int i = 0; i < size; i++){
            parentMatrix = allParents.get(i).toMatrix4(useTranslate, useRotate, useScale, true).mul(parentMatrix);
        }
        // get local matrix
        Mat4 newMatrix = new Mat4(1.0f);
        if(useTranslate){
            newMatrix.translate(position);
        }

        if(useRotate){
            newMatrix.rotate((float) Math.toRadians(rotation.x), new Vec3(1.0f, 0.0f, 0.0f));
            newMatrix.rotate((float) Math.toRadians(rotation.y), new Vec3(0.0f, 1.0f, 0.0f));
            newMatrix.rotate((float) Math.toRadians(rotation.z), new Vec3(0.0f, 0.0f, 1.0f));
        }

        if(useScale){
            newMatrix.scale(scale);
        }
        return parentMatrix.mul(newMatrix);
    }

    public Mat4 toMatrix4(boolean useTranslate, boolean useRotate, boolean useScale, boolean single){
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
        if(useTranslate){
            newMatrix.translate(position);
        }

        if(useTranslate){
            newMatrix.rotate((float) Math.toRadians(rotation.x), new Vec3(1.0f, 0.0f, 0.0f));
            newMatrix.rotate((float) Math.toRadians(rotation.y), new Vec3(0.0f, 1.0f, 0.0f));
            newMatrix.rotate((float) Math.toRadians(rotation.z), new Vec3(0.0f, 0.0f, 1.0f));
        }

        if(useScale){
            newMatrix.scale(scale);
        }
        return parentMatrix.mul(newMatrix);
    }

    public Bean parent;
    public HashMap<String, Bean> children = new HashMap<>();
}
