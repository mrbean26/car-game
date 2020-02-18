package com.bean.classicmini.components;

import com.bean.classicmini.Bean;
import com.bean.classicmini.MainActivity;
import com.bean.classicmini.surfaceView;
import com.bean.components.Components;

import glm.vec._3.Vec3;

public class Collider extends Components {
    public boolean pushes = false;

    public Vec3[] getThisData(){
        if(getBean().hasComponents(Mesh.class)){
            return getBean().getComponents(Mesh.class).getCollisionInfo();
        }
        if(getBean().hasComponents(Sprite.class)){
            return getBean().getComponents(Sprite.class).getCollisionInfo();
        }
        return new Vec3[0];
    }

    private boolean xInRange(Vec3[] checkData){
        Vec3[] thisData = getThisData();

        if(thisData[0].x > checkData[0].x && thisData[0].x < checkData[1].x){
            return true;
        }

        if(thisData[1].x > checkData[0].x && thisData[1].x < checkData[1].x){
            return true;
        }

        return false;
    }

    private boolean yInRange(Vec3[] checkData){
        Vec3[] thisData = getThisData();

        if(thisData[0].y > checkData[0].y && thisData[0].y < checkData[1].y){
            return true;
        }

        if(thisData[1].y > checkData[0].y && thisData[1].y < checkData[1].y){
            return true;
        }

        return false;
    }

    private boolean zInRange(Vec3[] checkData){
        Vec3[] thisData = getThisData();

        if(thisData[0].z > checkData[0].z && thisData[0].z < checkData[1].z){
            return true;
        }

        if(thisData[1].z > checkData[0].z && thisData[1].z < checkData[1].z){
            return true;
        }

        return false;
    }

    public void collide(Collider other){
        Vec3[] thisData = this.getThisData();
        Vec3[] otherData = other.getThisData();

        Vec3 otherCenter = new Vec3(
                otherData[1].x - otherData[0].x,
                otherData[1].y - otherData[0].y,
                otherData[1].z - otherData[0].z
        );

        if(other.pushes){

        }
        if(!other.pushes){
            // x collisions
            if(yInRange(otherData)){
                if(zInRange(otherData)){
                    float xMin = thisData[0].x - otherData[1].x;
                    if(xMin < 0.0f && thisData[0].x > otherCenter.x){
                        getBean().getComponents(Transform.class).position.x -= xMin;
                    }

                    float xMax = thisData[1].x - otherData[0].x;
                    if(xMax > 0.0f && thisData[1].x < otherCenter.x){
                        getBean().getComponents(Transform.class).position.x -= xMax;
                    }
                }
            }

            // y collisions
            if(xInRange(otherData)){
                if(zInRange(otherData)){
                    float yMin = thisData[0].y - otherData[1].y;
                    MainActivity.output(String.valueOf(yMin));
                    if(yMin < 0.0f){
                        getBean().getComponents(Transform.class).position.y -= yMin;
                    }

                    float yMax = thisData[1].y - otherData[0].y;
                    if(yMax > 0.0f && thisData[1].y < otherCenter.y){
                        //getBean().getComponents(Transform.class).position.y -= yMax;
                    }
                }
            }
        }
    }

    @Override
    public void begin(){

    }

    @Override
    public void mainloop(){
        if(!pushes){
            return;
        }

        Bean[] allColliders = surfaceView.currentScene.findBeansWithComponent(Collider.class);
        int count = allColliders.length;

        for(int b = 0; b < count; b++){
            Bean current = allColliders[b];
            if(current.id == this.getBean().id){
                continue;
            }

            collide(current.getComponents(Collider.class));
        }
    }
}
