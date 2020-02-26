package com.bean.classicmini.components;

import com.bean.classicmini.Bean;
import com.bean.classicmini.surfaceView;
import com.bean.classicmini.utilities.ClassicMiniMath;
import com.bean.components.Components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import glm.mat._4.Mat4;
import glm.vec._3.Vec3;
import glm.vec._4.Vec4;

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

    public Mat4 toMatrix4(){ // single false as default
        Mat4 returned = new Mat4(1.0f);
        // get parent matrix
        List<Transform> allTransforms = new ArrayList<>();
        allTransforms.add(this);

        Transform currentTransform = this;
        Vec3 currentScale = new Vec3(1.0f);
        while(currentTransform.parent != null){
            allTransforms.add(currentTransform.parent.getComponents(Transform.class));
            currentTransform = currentTransform.parent.getComponents(Transform.class);
        }

        int size = allTransforms.size();
        Collections.reverse(allTransforms);

        for(int i = 0; i < size; i++){
            returned.translate(ClassicMiniMath.copyVectorThree(allTransforms.get(i).position).mul(currentScale));

            Vec3 currentRotation = allTransforms.get(i).rotation;
            returned.rotate((float) Math.toRadians(currentRotation.x), new Vec3(1.0f, 0.0f, 0.0f));
            returned.rotate((float) Math.toRadians(currentRotation.y), new Vec3(0.0f, 1.0f, 0.0f));
            returned.rotate((float) Math.toRadians(currentRotation.z), new Vec3(0.0f, 0.0f, 1.0f));

            currentScale.mul(allTransforms.get(i).scale);
        }

        returned.scale(currentScale);
        return returned;
    }

    public Vec3 getRelativePosition(){
        if(parent == null){
            return position;
        }

        Vec4 returned = new Vec4(0.0f, 0.0f, 0.0f, 1.0f).mul(toMatrix4());
        return new Vec3(returned);
    }

    public Vec3 getRelativeScale(){
        if(parent == null){
            return scale;
        }

        Vec3 returned = ClassicMiniMath.copyVectorThree(scale);
        Bean current = parent;
        while(current != null){
            returned.mul(current.getComponents(Transform.class).scale);
            current = current.getComponents(Transform.class).parent;
        }
        return returned;
    }

    public Bean parent;
    public HashMap<String, Bean> children = new HashMap<>();
}
