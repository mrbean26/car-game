package com.bean.classicmini.utilities;

import com.bean.classicmini.surfaceView;

import java.util.Random;

import glm.Glm;
import glm.mat._4.Mat4;
import glm.vec._2.Vec2;
import glm.vec._3.Vec3;
import glm.vec._4.Vec4;

public class ClassicMiniMath {
    private static Random classicMiniRandom = new Random();

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

    public static boolean vectorThreeEquals(Vec3 one, Vec3 two){
        if(one.x == two.x){
            if(one.y == two.y){
                if(one.z == two.z){
                    return true;
                }
            }
        }
        return false;
    }

    public static float vectorThreeDistance(Vec3 one, Vec3 two){
        Vec3 newOne = new Vec3();
        newOne.x = one.x - two.x;
        newOne.y = one.y - two.y;
        newOne.z = one.z - two.z;

        return (float) Math.sqrt(newOne.x * newOne.x + newOne.y * newOne.y + newOne.z * newOne.z);
    }

    public static boolean vectorThreeLessThan(Vec3 one, Vec3 two){
        if(one.x < two.x){
            if(one.y < two.y){
                if(one.z < two.z){
                    return true;
                }
            }
        }
        return false;
    }

    public static Vec4 copyVectorFour(Vec4 used){
        return new Vec4(used.x, used.y, used.z, used.w);
    }

    public static Vec3 copyVectorThree(Vec3 used){
        return new Vec3(used.x, used.y, used.z);
    }

    public static Vec2 copyVectorTwo(Vec2 used){
        return new Vec2(used.x, used.y);
    }

    public static float bearing(Vec2 pointOne, Vec2 pointTwo){
        double angle = Math.atan2(pointTwo.x - pointOne.x, pointTwo.y - pointOne.y);
        if(angle < 0.0f){
            angle = angle + Math.PI * 2;
        }
        return (float) Math.toDegrees(angle);
    }

    public static int randomInteger(int lowInclusive, int maxInclusive){
        return classicMiniRandom.nextInt(maxInclusive + 1) + lowInclusive;
    }

    public static Vec2 touchToUICoords(float x, float y){
        Vec2 returned = new Vec2(x, y);
        returned.sub(new Vec2(surfaceView.displayWidth / 2.0f, surfaceView.displayHeight / 2.0f));
        returned.div(new Vec2(surfaceView.displayWidth / 2.0f, surfaceView.displayHeight / 2.0f));
        returned.y = -returned.y;

        return returned;
    }
}
