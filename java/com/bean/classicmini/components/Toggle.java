package com.bean.classicmini.components;

import com.bean.classicmini.Bean;
import com.bean.classicmini.utilities.ClassicMiniMath;
import com.bean.components.Components;

import glm.vec._3.Vec3;
import glm.vec._4.Vec4;

public class Toggle extends Components {
    public Bean backgroundBean = null;
    public Bean sliderBean = null;

    public Vec4 backgroundColour = new Vec4(0.7f, 0.7f, 0.7f, 1.0f);
    public Vec4 toggleColour = new Vec4(1.0f);
    private boolean toggled = false;

    public boolean isToggled(){
        return toggled;
    }

    @Override
    public void onClick(){
        toggled = !toggled;
    }

    @Override
    public void begin() {
        backgroundBean = new Bean(objectName + "_toggleBackground");
        backgroundBean.getComponents(Transform.class).parent = this.getBean();

        Image backgroundImage = new Image();
        backgroundImage.roundEdgeRadius = 0.2f;
        backgroundImage.colour = backgroundColour;
        backgroundImage.material.colourHex = "#FFFFFF";
        backgroundBean.addComponents(backgroundImage);
        backgroundBean.getComponents(Image.class).begin();

        sliderBean = new Bean(objectName + "_toggleSlider");
        sliderBean.getComponents(Transform.class).parent = this.getBean();
        sliderBean.addComponents(new Button());
        sliderBean.getComponents(Button.class).onClickClass = this;
        sliderBean.getComponents(Button.class).begin();

        Image toggleImage = new Image();
        toggleImage.roundEdgeRadius = 0.2f;
        toggleImage.colour = toggleColour;
        toggleImage.material.colourHex = "#FFFFFF";
        sliderBean.addComponents(toggleImage);
        sliderBean.getComponents(Image.class).begin();

        Bean.addBean(backgroundBean);
        Bean.addBean(sliderBean);

        this.getBeansComponent(Transform.class).children.put(backgroundBean.objectName, backgroundBean);
        this.getBeansComponent(Transform.class).children.put(sliderBean.objectName, sliderBean);
    }

    public void setRoundEdgeRadius(float newRadius){
        backgroundBean.getComponents(Image.class).roundEdgeRadius = newRadius;
        sliderBean.getComponents(Image.class).roundEdgeRadius = newRadius;
    }

    private void updateScales(){
        Vec3 thisTransformScale = getBeansComponent(Transform.class).scale;
        backgroundBean.getComponents(Transform.class).scale = thisTransformScale;
        sliderBean.getComponents(Transform.class).scale = ClassicMiniMath.copyVectorThree(thisTransformScale).div(new Vec3(2.0f, 1.0f, 1.0f));
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
    }


}
