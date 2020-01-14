package com.bean.classicmini.components;

import com.bean.classicmini.surfaceView;
import com.bean.components.Components;

import glm.vec._2.Vec2;

public class Button extends Components {
    public Object onClickClass = new Transform();

    @Override
    public void mainloop(){
        checkClick();
    }

    public boolean isClicked(){
        Transform buttonTransform = getBean().getComponents(Transform.class);
        float xCenter = (surfaceView.displayWidth / 2.0f) + (buttonTransform.position.x * (surfaceView.displayWidth / 2.0f));
        float yCenter = surfaceView.displayHeight - ((surfaceView.displayHeight / 2.0f) + (buttonTransform.position.y * (surfaceView.displayWidth / 2.0f)));

        Vec2 center = new Vec2(xCenter, yCenter);
        Vec2 touchPoint = new Vec2(surfaceView.xTouch, surfaceView.yTouch);

        float angleRadians = (float) Math.toRadians(-buttonTransform.rotation.z);
        touchPoint.x = touchPoint.x - center.x;
        touchPoint.y = touchPoint.y - center.y;

        float xNew = touchPoint.x * (float) Math.cos(angleRadians) - touchPoint.y * (float) Math.sin(angleRadians);
        float yNew = touchPoint.x * (float) Math.sin(angleRadians) + touchPoint.y * (float) Math.cos(angleRadians);

        touchPoint.x = xNew + center.x;
        touchPoint.y = yNew + center.y;

        float xRight = xCenter + ((surfaceView.displayWidth / 2.0f) * buttonTransform.scale.x);
        float xLeft = xCenter - ((surfaceView.displayWidth / 2.0f) * buttonTransform.scale.x);

        float yTop = yCenter + ((surfaceView.displayWidth / 2.0f) * buttonTransform.scale.y);
        float yBottom = yCenter - ((surfaceView.displayWidth / 2.0f) * buttonTransform.scale.y);

        if(touchPoint.x > xLeft && touchPoint.x < xRight){
            if(touchPoint.y < yTop && touchPoint.y > yBottom){
                return true;
            }
        }

        return false;
    }

    public void checkClick(){
        if(isClicked()){
            runClick();
        }
    }

    public <T extends Components> void runClick(){
        T usedComponent = (T) onClickClass;
        usedComponent.onClick();
    }
}
