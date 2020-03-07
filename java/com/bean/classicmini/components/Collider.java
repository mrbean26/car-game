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

	private boolean isinsideYZ(Vec3 minInfo, Vec3 maxInfo, Vec3 position){
		if(position.y >= minInfo.y && position.y <= maxInfo.y){
			if(position.z >= minInfo.z && position.z <= maxInfo.z){
				return true;
			}
		}
		return false;
	}

	private boolean isinsideXZ(Vec3 minInfo, Vec3 maxInfo, Vec3 position){
		if(position.x >= minInfo.x && position.x <= maxInfo.x){
			if(position.z >= minInfo.z && position.z <= maxInfo.z){
				return true;
			}
		}
		return false;
	}

	private boolean isinsideXY(Vec3 minInfo, Vec3 maxInfo, Vec3 position){
		if(position.x >= minInfo.x && position.x <= maxInfo.x){
			if(position.z >= minInfo.z && position.z <= maxInfo.z){
				return true;
			}
		}
		return false;
	}

	private void push(float firstFloat, float secondFloat, Collider firstCollider, Collider secondCollider,
			Transform firstTransform, Transform secondTransform){
		float dist = thisMaxInfo - secondFloat;

		if(Math.abs(dist) > MIN_COLLISION_DISTANCE){
			if(firstCollider.solid){
				firstTransform.position.add(firstTransform.forwardVector().mul(dist));
			}
			if(secondCollider.solid && !firstCollider.solid){
				secondTransform.position.add(secondTransform.forwardVector().mul(dist));
			}
		}
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
			if(isinsideYZ(thisMinInfo, thisMaxInfo, otherTransform.position)){
				if(otherTransform.position.x >= thisTransform.position.x){
					if(otherMinInfo.x <= thisMaxInfo.x && otherMinInfo.x >= thisMinInfo.x){
						push(thisMaxInfo.x, otherMinInfo.x, otherCollider, this,
							otherTransform, thisTransform);
					}
				}
				if(otherTransform.position.x <= thisTransform.position.x){
					if(otherMaxInfo.x <= thisMaxInfo.x && otherMaxInfo.x >= thisMinInfo.x){
						push(thisMinInfo.x, otherMaxInfo.x, otherCollider, this,
							otherTransform, thisTransform);
					}
				}
			}

            // y collisions
			if(isinsideXZ(thisMinInfo, thisMaxInfo, otherTransform)){
				if(otherTransform.position.y >= thisTransform.position.y){
					if(otherMinInfo.y <= thisMaxInfo.y && otherMinInfo.y >= thisMinInfo.y){
						push(thisMaxInfo.y, otherMinInfo.y, otherCollider, this,
							otherTransform, thisTransform);
					}
				}
				if(otherTransform.position.y <= thisTransform.position.y){
					if(otherMaxInfo.y <= thisMaxInfo.y && otherMaxInfo.y >= thisMinInfo.y){
						push(thisMinInfo.y, otherMaxInfo.y, otherCollider, this,
							otherTransform, thisTransform);
					}
				}
			}

            // z collisions
			if(isinsideXY(thisMinInfo, thisMaxInfo, otherTransform.position)){
				if(otherTransform.position.z >= thisTransform.position.z){
					if(otherMinInfo.z <= thisMaxInfo.z && otherMinInfo.x >= thisMinInfo.x){
						push(thisMaxInfo.z, otherMinInfo.z, otherCollider, this,
							otherTransform, thisTransform);
					}
				}
				if(otherTransform.position.z <= thisTransform.position.z){
					if(otherMaxInfo.z <= thisMaxInfo.z && otherMaxInfo.x >= thisMinInfo.x){
						push(thisMinInfo.z, otherMaxInfo.z, otherCollider, this,
							otherTransform, thisTransform);
					}
				}
			}
        }
    }
}
