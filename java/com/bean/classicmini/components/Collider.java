package com.bean.classicmini.components;

import com.bean.classicmini.Bean;
import com.bean.classicmini.MainActivity;
import com.bean.classicmini.surfaceView;
import com.bean.classicmini.utilities.ClassicMiniMath;
import com.bean.components.Components;

import java.util.ArrayList;
import java.util.List;

import glm.vec._2.Vec2;
import glm.vec._3.Vec3;
import glm.vec._4.Vec4;

public class Collider extends Components {
    public static float COLLIDER_COLLISION_MAX_DISTANCE = 0.01f;

    public boolean isTouching(Sprite check){
        Vec3[] thisData = null;
        if(getBean().hasComponents(Mesh.class)){
            thisData = getBean().getComponents(Mesh.class).getCollisionInfo();
        }
        if(getBean().hasComponents(Sprite.class)){
            thisData = getBean().getComponents(Sprite.class).getCollisionInfo();
        }
        Vec3[] nextData = check.getCollisionInfo();

        if(thisData[0].x <= nextData[1].x && thisData[1].x >= nextData[0].x){
            if(thisData[0].y <= nextData[1].y && thisData[1].y >= nextData[0].y){
                if(thisData[0].z <= nextData[1].z && thisData[1].z >= nextData[0].z){
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isTouching(Mesh check){
        Vec3[] thisData = null;
        if(getBean().hasComponents(Mesh.class)){
            thisData = getBean().getComponents(Mesh.class).getCollisionInfo();
        }
        if(getBean().hasComponents(Sprite.class)){
            thisData = getBean().getComponents(Sprite.class).getCollisionInfo();
        }
        Vec3[] nextData = check.getCollisionInfo();

        if(thisData[0].x <= nextData[1].x && thisData[1].x >= nextData[0].x){
            if(thisData[0].y <= nextData[1].y && thisData[1].y >= nextData[0].y){
                if(thisData[0].z <= nextData[1].z && thisData[1].z >= nextData[0].z){
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void mainloop(){
        if(!getBean().hasComponents(Mesh.class) && !getBean().hasComponents(Sprite.class)){
            return;
        }

        Bean[] allColliderBeans = surfaceView.currentScene.findBeansWithComponent(Collider.class);
        for(Bean current : allColliderBeans){
            if(current.id == getBean().id){
                continue;
            }
            if(current.hasComponents(Mesh.class)){
                if(!isTouching(current.getComponents(Mesh.class))){
                    continue;
                }
            }
            if(current.hasComponents(Sprite.class)){
                if(!isTouching(current.getComponents(Sprite.class))){
                    continue;
                }
            }

            // at this stage, bean is touching another bean
            MainActivity.output("Collide");
        }
    }
}
