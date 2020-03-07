package com.bean.classicmini.components;

import com.bean.classicmini.surfaceView;
import com.bean.components.Components;

import glm.vec._2.Vec2;
import glm.vec._3.Vec3;

public class Button extends Components {
    public Object onClickClass = new Transform();
    public int type = BUTTON_CLICK_UP;

    public static final int BUTTON_CLICK_DOWN = 0;
    public static final int BUTTON_CLICK_UP = 1;
    public static final int BUTTON_HOLD = 2;

    private boolean clickedLast = false;

    @Override
    public void mainloop(){
        checkClick();
    }

    private boolean isBelowButton(xTouch, yTouch){
		Bean[] allButtonBeans = surfaceView.currentScene.findBeansWithComponent(Button.class);
		int count = allButtonBeans.length;

		int thisBeansIndex = -1;
		for(int i = 0; i < count; i++){
			if(thisBeansIndex == -1){
				if(getBean().id == allButtonBeans[i].id){
					thisBeansIndex = i;
					continue;
				}
			}

			if(thisBeansIndex != -1){
				// anything here is rendered / run after this button
				if(allButtonBeans[i].getComponents(Button.class).isClicked(xTouch, yTouch)){
					return true;
				}
			}
		}
		return false;
	}

    public boolean isClicked(float xTouch, float yTouch){
        if(xTouch == -1.0f || yTouch == -1.0f){
            return false;
        }

        Transform buttonTransform = getBeansComponent(Transform.class);
        Vec3 localPosition = buttonTransform.getRelativePosition();
        Vec3 localScale = buttonTransform.getRelativeScale();

        float halfWidth = surfaceView.displayWidth / 2.0f;
        float halfHeight = surfaceView.displayHeight / 2.0f;

        float xCenter = halfWidth + (localPosition.x * halfWidth);
        float yCenter = surfaceView.displayHeight - (halfHeight + (localPosition.y * halfWidth));

        Vec2 center = new Vec2(xCenter, yCenter);
        Vec2 touchPoint = new Vec2(xTouch, yTouch);

        float angleDegrees = -buttonTransform.rotation.z;
        Transform currentTransform = buttonTransform;
        while(currentTransform.parent != null){
            angleDegrees = angleDegrees + currentTransform.parent.getComponents(Transform.class).rotation.z;

            currentTransform = currentTransform.parent.getComponents(Transform.class);
        }

        float angleRadians = (float) Math.toRadians(angleDegrees);
        touchPoint.x = touchPoint.x - center.x;
        touchPoint.y = touchPoint.y - center.y;

        float xNew = touchPoint.x * (float) Math.cos(angleRadians) - touchPoint.y * (float) Math.sin(angleRadians);
        float yNew = touchPoint.x * (float) Math.sin(angleRadians) + touchPoint.y * (float) Math.cos(angleRadians);

        touchPoint.x = xNew + center.x;
        touchPoint.y = yNew + center.y;

        float xRight = xCenter + (halfWidth * localScale.x);
        float xLeft = xCenter - (halfWidth * localScale.x);

        float yTop = yCenter + (halfWidth * localScale.y);
        float yBottom = yCenter - (halfWidth * localScale.y); // this might have to be half height instead

        if(touchPoint.x > xLeft && touchPoint.x < xRight){
            if(touchPoint.y < yTop && touchPoint.y > yBottom){
                if(!isBelowButton(xTouch, yTouch)){
					return true;
				}
            }
        }

        return false;
    }

    public void checkClick(){
        boolean currentClicked = isClicked(surfaceView.xTouch, surfaceView.yTouch);
        if(type == BUTTON_CLICK_DOWN){
            if(currentClicked && !clickedLast){
                runClick();
                clickedLast = true;
            }
            if(!currentClicked){
                clickedLast = false;
            }
        }
        if(type == BUTTON_CLICK_UP){
            if(isClicked(surfaceView.xTouch, surfaceView.yTouch)){
                clickedLast = true;
                return;
            }
            if(clickedLast && !currentClicked){
                runClick();
                clickedLast = false;
            }
        }
        if(type == BUTTON_HOLD){
            if(currentClicked){
                runClick();
            }
        }
    }

    public <T extends Components> void runClick(){
        T usedComponent = (T) onClickClass;
        usedComponent.onClick(getBean());
    }
}
