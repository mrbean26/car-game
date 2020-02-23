package com.bean.classicmini.components;

import com.bean.classicmini.Bean;
import com.bean.classicmini.surfaceView;
import com.bean.components.Components;

import glm.mat._4.Mat4;
import glm.vec._3.Vec3;
import glm.vec._4.Vec4;

public class Collider extends Components {
    private Vec4 minData = new Vec4(0.0f);
    private Vec4 maxData = new Vec4(0.0f);
    public boolean solid = false; // solid means gets pushed by another non-solid collider
                                  // non-solid on non-solid goes through each other
                                  // solid on solid pushes each other
                                  // solid boolean works like rigidbody component

    public static float MIN_COLLISION_DISTANCE = 0.0001f;

    public void updateCollisionInfo(){
        if(getBean().hasComponents(Mesh.class)){
            Vec3[] collisionInfo = getBeansComponent(Mesh.class).getCollisionInfo();
            minData = new Vec4(collisionInfo[0], 1.0f);
            maxData = new Vec4(collisionInfo[1], 1.0f);
        }
        if(getBean().hasComponents(Sprite.class)){
            Vec3[] collisionInfo = getBeansComponent(Sprite.class).getCollisionInfo();
            minData = new Vec4(collisionInfo[0], 1.0f);
            maxData = new Vec4(collisionInfo[1], 1.0f);
        }
        if(getBean().hasComponents(SimpleMesh.class)){
            Vec3[] collisionInfo = getBeansComponent(SimpleMesh.class).getCollisionInfo();
            minData = new Vec4(collisionInfo[0], 1.0f);
            maxData = new Vec4(collisionInfo[1], 1.0f);
        }
    }

    @Override
    public void begin() {

    }

    @Override
    public void mainloop(){
        Bean[] allColliders = surfaceView.currentScene.findBeansWithComponent(Collider.class);
        int count = allColliders.length;

        // this
        this.updateCollisionInfo();
        Transform thisTransform = getBeansComponent(Transform.class);
        Mat4 thisModel = getBeansComponent(Transform.class).toMatrix4();

        Vec3 thisMinInfo = new Vec3(this.minData);
        Vec3 thisMaxInfo = new Vec3(this.maxData);

        for(int c = 0; c < count; c++){
            if(allColliders[c].id == getBean().id){
                continue;
            }
            // different colliders from now
            if(!allColliders[c].getComponents(Collider.class).enabled){
                continue;
            }
            Collider otherCollider = allColliders[c].getComponents(Collider.class);
            otherCollider.updateCollisionInfo();
            Transform otherTransform = otherCollider.getBeansComponent(Transform.class);

            Mat4 otherModel = otherCollider.getBeansComponent(Transform.class).toMatrix4();

            Vec3 otherMinInfo = new Vec3(otherCollider.minData);
            Vec3 otherMaxInfo = new Vec3(otherCollider.maxData);
            // compare - x collisions
            if(otherTransform.position.y >= thisMinInfo.y && otherTransform.position.y <= thisMaxInfo.y){
                if(otherTransform.position.z >= thisMinInfo.z && otherTransform.position.z <= thisMaxInfo.z){
                    if(otherTransform.position.x >= thisTransform.position.x){
                        if(otherMinInfo.x <= thisMaxInfo.x && otherMinInfo.x >= thisMinInfo.x){
                            float dist = thisMaxInfo.x - otherMinInfo.x;
                            if(Math.abs(dist) >  MIN_COLLISION_DISTANCE){
                                if(otherCollider.solid){
                                    otherTransform.position.add(otherTransform.forwardVector().mul(dist));
                                }
                                if(solid && !otherCollider.solid){
                                    thisTransform.position.add(thisTransform.forwardVector().mul(dist));
                                }
                            }
                        }
                    }
                    if(otherTransform.position.x <= thisTransform.position.x){
                        if(otherMaxInfo.x <= thisMaxInfo.x && otherMaxInfo.x >= thisMinInfo.x){
                            float dist = thisMinInfo.x - otherMaxInfo.x;
                            if(Math.abs(dist) >  MIN_COLLISION_DISTANCE){
                                if(otherCollider.solid){
                                    otherTransform.position.add(otherTransform.forwardVector().mul(dist));
                                }
                                if(this.solid && !otherCollider.solid){
                                    thisTransform.position.add(thisTransform.forwardVector().mul(dist));
                                }
                            }
                        }
                    }
                }
            }

            // y collisions
            if(otherTransform.position.x >= thisMinInfo.x && otherTransform.position.x <= thisMaxInfo.x){
                if(otherTransform.position.z >= thisMinInfo.z && otherTransform.position.z <= thisMaxInfo.z){
                    if(otherTransform.position.y >= thisTransform.position.y){
                        if(otherMinInfo.y <= thisMaxInfo.y && otherMinInfo.y >= thisMinInfo.y){
                            float dist = thisMaxInfo.y - otherMinInfo.y;
                            if(Math.abs(dist) > MIN_COLLISION_DISTANCE){
                                if(otherCollider.solid){
                                    otherTransform.position.add(otherTransform.upVector().mul(dist));
                                }
                                if(this.solid && !otherCollider.solid){
                                   thisTransform.position.add(thisTransform.upVector().mul(dist));
                                }
                            }
                        }
                    }
                    if(otherTransform.position.y <= thisTransform.position.y){
                        if(otherMaxInfo.y <= thisMaxInfo.y && otherMaxInfo.y >= thisMinInfo.y){
                            float dist = thisMinInfo.y - otherMaxInfo.y;
                            if(Math.abs(dist) > MIN_COLLISION_DISTANCE){
                                if(otherCollider.solid){
                                    otherTransform.position.add(otherTransform.upVector().mul(dist));
                                }
                                if(this.solid && !otherCollider.solid){
                                    thisTransform.position.add(thisTransform.upVector().mul(dist));
                                }
                            }
                        }
                    }
                }
            }

            // z collisions
            if(otherTransform.position.x >= thisMinInfo.x && otherTransform.position.x <= thisMaxInfo.x){
                if(otherTransform.position.y >= thisMinInfo.y && otherTransform.position.y <= thisMaxInfo.y){
                    if(otherTransform.position.z >= thisTransform.position.z){
                        if(otherMinInfo.z <= thisMaxInfo.z && otherMinInfo.x >= thisMinInfo.x){
                            float dist = thisMaxInfo.z - otherMinInfo.z;
                            if(Math.abs(dist) > MIN_COLLISION_DISTANCE){
                                if(otherCollider.solid){
                                    otherTransform.position.add(otherTransform.rightVector().mul(dist));
                                }
                                if(this.solid && !otherCollider.solid){
                                    thisTransform.position.add(thisTransform.rightVector().mul(dist));
                                }
                            }
                        }
                    }
                    if(otherTransform.position.z <= thisTransform.position.z){
                        if(otherMaxInfo.z <= thisMaxInfo.z && otherMaxInfo.x >= thisMinInfo.x){
                            float dist = thisMinInfo.z - otherMaxInfo.z;
                            if(Math.abs(dist) > MIN_COLLISION_DISTANCE){
                                if(otherCollider.solid){
                                    otherTransform.position.add(otherTransform.rightVector().mul(dist));
                                }
                                if(this.solid && !otherCollider.solid){
                                    thisTransform.position.add(thisTransform.rightVector().mul(dist));
                                }
                            }
                        }
                    }
                }
            }


        }
    }
}
