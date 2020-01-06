package com.bean.classicmini.components;

import com.bean.classicmini.Bean;
import com.bean.classicmini.MainActivity;
import com.bean.classicmini.surfaceView;
import com.bean.components.Components;

import java.nio.FloatBuffer;

public class Light extends Components {
    public float constant = 1.0f;
    public float linear = 0.09f;
    public float quadratic = 0.032f;
    public float ambient = 0.05f;
    public float diffuse = 0.8f;
    public float specular = 1.0f;

    public float xColour = 1.0f;
    public float yColour = 1.0f;
    public float zColour = 1.0f;

    // 1 - enabled, 2 - constant, 3 - linear, 4 - quadratic, 5 - ambient, 6 - diffuse, 7 - specular, 8 - xColour, 9 - yColour, 10 - zColour,
    // 11 - xPosition, 12 - yPosition, 13 - zPosition
    public static FloatBuffer getLightInfo(){
        float[] array = new float[130]; // ten lights
        int currentLightCount = 0;
        for(Bean current : surfaceView.currentScene.allBeans.values()){
            if(current.hasComponents(Light.class)){
                Light component = current.getComponents(Light.class);
                array[currentLightCount * 13 + 0] = component.enabled? 1f : 0f;
                array[currentLightCount * 13 + 1] = component.constant;
                array[currentLightCount * 13 + 2] = component.linear;
                array[currentLightCount * 13 + 3] = component.quadratic;
                array[currentLightCount * 13 + 4] = component.ambient;
                array[currentLightCount * 13 + 5] = component.diffuse;
                array[currentLightCount * 13 + 6] = component.specular;
                array[currentLightCount * 13 + 7] = component.xColour;
                array[currentLightCount * 13 + 8] = component.yColour;
                array[currentLightCount * 13 + 9] = component.zColour;

                Transform position = current.getComponents(Transform.class);
                array[currentLightCount * 13 + 10] = position.xPosition;
                array[currentLightCount * 13 + 11] = position.yPosition;
                array[currentLightCount * 13 + 12] = position.zPosition;

                currentLightCount = currentLightCount + 1;
                if(currentLightCount == 10){
                    break;
                }
            }
        }
        return FloatBuffer.wrap(array);
    }
}
