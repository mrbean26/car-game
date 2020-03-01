package com.bean.classicmini.components;

import com.bean.classicmini.surfaceView;
import com.bean.components.Components;

import glm.vec._2.Vec2;
import glm.vec._3.Vec3;

public class Button extends Components {
    public Object onClickClass = new Transform();
    public int type = BUTTON_CLICK_DOWN;

    public static final int BUTTON_CLICK_DOWN = 0;
    public static final int BUTTON_CLICK_UP = 1;
    public static final int BUTTON_HOLD = 2;

    private boolean clickedLast = false;

    @Override
    public void mainloop(){
        checkClick();
    }

    public boolean isClicked(float xTouch, float yTouch){
        if(xTouch == -1.0f || yTouch == -1.0f){
            return false;
        }
        ;
        Transform buttonTransform = getBeansComponent(Transform.class);
        Vec3 localPosition = buttonTransform.getRelativePosition();
        Vec3 localScale = buttonTransform.getRelativeScale();
        
        float xCenter = (surfaceView.displayWidth / 2.0f) + (localPosition.x * (surfaceView.displayWidth / 2.0f));
        float yCenter = surfaceView.displayHeight - ((surfaceView.displayHeight / 2.0f) + (localPosition.y * (surfaceView.displayWidth / 2.0f)));

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

        float xRight = xCenter + ((surfaceView.displayWidth / 2.0f) * localScale.x);
        float xLeft = xCenter - ((surfaceView.displayWidth / 2.0f) * localScale.x);

        float yTop = yCenter + ((surfaceView.displayWidth / 2.0f) * localScale.y);
        float yBottom = yCenter - ((surfaceView.displayWidth / 2.0f) * localScale.y);

        if(touchPoint.x > xLeft && touchPoint.x < xRight){
            if(touchPoint.y < yTop && touchPoint.y > yBottom){
                return true;
            }
        }

        return false;
    }

    public void checkClick(){
        if(type == BUTTON_CLICK_DOWN){
            if(isClicked(surfaceView.xTouchDown, surfaceView.yTouchDown)){
                runClick();
            }
        }
        if(type == BUTTON_CLICK_UP){
            if(isClicked(surfaceView.xTouchUp, surfaceView.yTouchUp)){
                runClick();
            }
        }
        if(type == BUTTON_HOLD){
            if(isClicked(surfaceView.xTouch, surfaceView.yTouch)){
                runClick();
            }
        }
    }

    public <T extends Components> void runClick(){
        T usedComponent = (T) onClickClass;
        usedComponent.onClick(getBean());
    }
}
