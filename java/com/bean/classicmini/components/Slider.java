package com.bean.classicmini.components;

import com.bean.classicmini.Bean;
import com.bean.classicmini.surfaceView;
import com.bean.classicmini.utilities.ClassicMiniMath;
import com.bean.components.Components;

import glm.Glm;
import glm.vec._2.Vec2;
import glm.vec._3.Vec3;
import glm.vec._4.Vec4;

public class Slider extends Components {
    public Vec4 colour = new Vec4(1.0f, 1.0f, 1.0f, 1.0f);

    public Bean backgroundBean;
    public Bean sliderBean;

    public float minimumValue = 0.0f;
    public float maximumValue = 4.0f;
    private float xSlidePosition = 0.0f;

    public float getValue(){
        float returned = xSlidePosition + 1.0f;
        returned = returned / 2.0f;

        float valueRange = maximumValue - minimumValue;
        return (returned * valueRange) + minimumValue;
    }

    private void updateRadius(){
        Vec3 sliderRelativeScale = sliderBean.getComponents(Transform.class).getRelativeScale();
        sliderBean.getComponents(Image.class).roundEdgeRadius = Glm.max(sliderRelativeScale.x, sliderRelativeScale.y);

        Vec3 backgroundRelativeScale = backgroundBean.getComponents(Transform.class).getRelativeScale();
        backgroundBean.getComponents(Image.class).roundEdgeRadius = Glm.max(backgroundRelativeScale.x, backgroundRelativeScale.y);
    }

    @Override
    public void begin() {
        backgroundBean = new Bean(objectName + "_sliderBackground");
        backgroundBean.getComponents(Transform.class).parent = this.getBean();
        backgroundBean.getComponents(Transform.class).scale = new Vec3(1.0f, 0.2f, 1.0f);

        Button newButton = new Button();
        newButton.onClickClass = this;
        newButton.type = Button.BUTTON_HOLD;

        backgroundBean.addComponents(newButton);
        backgroundBean.getComponents(Button.class).begin();

        Image firstImage = new Image();
        firstImage.material.colourHex = "#C8C8C8";

        backgroundBean.addComponents(firstImage);
        backgroundBean.getComponents(Image.class).begin();

        sliderBean = new Bean(objectName + "_sliderSlider");
        sliderBean.getComponents(Transform.class).parent = this.getBean();
        sliderBean.getComponents(Transform.class).scale = new Vec3(0.25f, 0.25f, 1.0f);

        Image newImage = new Image();
        newImage.material.colourHex = "#FFFFFF";

        sliderBean.addComponents(newImage);
        sliderBean.getComponents(Image.class).begin();

        this.getBeansComponent(Transform.class).children.put(backgroundBean.objectName, backgroundBean);
        this.getBeansComponent(Transform.class).children.put(sliderBean.objectName, sliderBean);

        Bean.addBean(sliderBean);
        Bean.addBean(backgroundBean);
    }

    @Override
    public void mainloop() {
        backgroundBean.getComponents(Image.class).colour = new Vec4(0.78f, 0.78f, 0.78f, 1.0f).mul(colour);
        sliderBean.getComponents(Image.class).colour = new Vec4(1.0f, 1.0f, 1.0f, 1.0f).mul(colour);

        updateRadius();
    }

    @Override
    public void onClick() {
        Vec2 uiTouchPoint = ClassicMiniMath.touchToUICoords(surfaceView.xTouch, surfaceView.yTouch);
        uiTouchPoint.y = uiTouchPoint.y * ((float) surfaceView.displayHeight / (float) surfaceView.displayWidth);

        float xPosition = uiTouchPoint.x * ClassicMiniMath.classicMiniCos(getBeansComponent(Transform.class).rotation.z);
        xPosition = xPosition + (uiTouchPoint.y * ClassicMiniMath.classicMiniSin(getBeansComponent(Transform.class).rotation.z));

        xPosition = xPosition - getBeansComponent(Transform.class).getRelativePosition().x;
        xPosition = xPosition * (1.0f / getBeansComponent(Transform.class).getRelativeScale().x);

        sliderBean.getComponents(Transform.class).position.x = xPosition;
        xSlidePosition = xPosition;
    }
}
