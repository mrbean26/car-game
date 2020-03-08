package com.bean.classicmini.components;

import com.bean.classicmini.Bean;
import com.bean.components.Components;

import glm.vec._3.Vec3;
import glm.vec._4.Vec4;

public class Toggle extends Components {
    public Bean backgroundBean = null;
    public Bean sliderBean = null;

    public Vec4 colour = new Vec4(1.0f, 1.0f, 1.0f, 1.0f);
    private boolean toggled = false;

    public boolean isToggled(){
        return toggled;
    }

    @Override
    public void onClick(Bean clicked){
        toggled = !toggled;
    }

    @Override
    public void begin() {
        backgroundBean = new Bean(objectName + "_toggleBackground");
        backgroundBean.getComponents(Transform.class).parent = this.getBean();

        Image backgroundImage = new Image();
        backgroundImage.roundEdgeRadius = 0.2f;
        backgroundImage.material.colourHex = "#B2B2B2";
        backgroundBean.addComponents(backgroundImage);
        backgroundBean.getComponents(Image.class).begin();

        sliderBean = new Bean(objectName + "_toggleSlider");
        sliderBean.getComponents(Transform.class).parent = backgroundBean;
        sliderBean.addComponents(new Button());
        sliderBean.getComponents(Button.class).onClickClass = this;
        sliderBean.getComponents(Button.class).begin();

        Image toggleImage = new Image();
        toggleImage.roundEdgeRadius = 0.2f;
        toggleImage.material.colourHex = "#FFFFFF";
        sliderBean.addComponents(toggleImage);
        sliderBean.getComponents(Image.class).begin();

        Bean.addBean(backgroundBean);
        Bean.addBean(sliderBean);

        this.getBeansComponent(Transform.class).children.put(backgroundBean.objectName, backgroundBean);
        backgroundBean.getComponents(Transform.class).children.put(sliderBean.objectName, sliderBean);
    }

    public void setRoundEdgeRadius(float newRadius){
        backgroundBean.getComponents(Image.class).roundEdgeRadius = newRadius;
        sliderBean.getComponents(Image.class).roundEdgeRadius = newRadius;
    }

    private void updateScales(){
        backgroundBean.getComponents(Transform.class).scale = getBeansComponent(Transform.class).scale;

        Vec3 backgroundScale = backgroundBean.getComponents(Transform.class).getRelativeScale();
        backgroundScale.x = backgroundScale.x / 2.0f;
        sliderBean.getComponents(Transform.class).setRelativeScale(backgroundScale);
    }

    private void updatePositions(){
        if(toggled){
            sliderBean.getComponents(Transform.class).position.x = 0.5f;
        } if(!toggled){
            sliderBean.getComponents(Transform.class).position.x = -0.5f;
        }
    }

    @Override
    public void mainloop() {
        updateScales();
        updatePositions();

        backgroundBean.getComponents(Image.class).colour = colour;
        sliderBean.getComponents(Image.class).colour = colour;
    }


}
