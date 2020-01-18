package com.bean.classicmini.utilities;

import com.bean.classicmini.surfaceView;

import glm.Glm;
import glm.mat._4.Mat4;

public class  ClassicMiniMath {
    public static Mat4 getOrtho(){
        float height = (float) surfaceView.displayHeight / (float) surfaceView.displayWidth;
        Mat4 newMat = new Mat4(1.0f);
        return newMat.ortho(-1.0f, 1.0f, -height, height, -1.0f, 1.0f);
    }

    public static float classicMiniCos(float degrees){
        return (float) Math.cos(Math.toRadians(degrees));
    }

    public static float classicMiniSin(float degrees){
        return (float) Math.sin(Math.toRadians(degrees));
    }

    public static int highestCommonFactor(int numOne, int numTwo){
        int maximum = Glm.max(numOne, numTwo);
        int minimum = Glm.min(numOne, numTwo);

        for(int min = minimum; min > 0; min--){
            float divided = (float) maximum / (float) min;
            float dividedOne = (float) minimum / (float) min;

            float floor = Glm.floor((float) maximum / (float) min);
            float floorOne = Glm.floor((float) minimum / (float) min);

            if(divided == floor){
                if(dividedOne == floorOne){
                    return min;
                }
            }
        }
        return 1;
    }

    public static float roundDecimal(float decimal, int places){
        decimal = decimal * (100.0f * places);
        decimal = Math.round(decimal);
        decimal = decimal / (100.0f * places);

        return decimal;
    }
}
