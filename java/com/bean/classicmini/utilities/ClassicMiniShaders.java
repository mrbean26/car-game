package com.bean.classicmini.utilities;

import android.opengl.GLES20;

import com.bean.classicmini.MainActivity;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import glm.mat._4.Mat4;
import glm.vec._3.Vec3;
import glm.vec._4.Vec4;

public class ClassicMiniShaders { // OpenGL ES GLSL ES 3.00
    public static int createShader(int codePath, int shaderType){
        String code = ClassicMiniSavefiles.readLinesString(codePath);
        int shaderNumber = GLES20.glCreateShader(shaderType);

        GLES20.glShaderSource(shaderNumber, code);
        GLES20.glCompileShader(shaderNumber);

        IntBuffer isCompiled = IntBuffer.allocate(1);
        GLES20.glGetShaderiv(shaderNumber, GLES20.GL_COMPILE_STATUS, isCompiled);
        if(isCompiled.array()[0] == 0){
            ClassicMiniOutput.error("Couldnt create shader: " + MainActivity.getAppContext().getResources().getResourceName(codePath) + " error: " + GLES20.glGetShaderInfoLog(shaderNumber));
            GLES20.glDeleteShader(shaderNumber);
        }
        return shaderNumber;
    }

    public static int createProgram(int[] shaders){
        int newProgram = GLES20.glCreateProgram();
        for(int shader : shaders){
            GLES20.glAttachShader(newProgram, shader);
        }

        GLES20.glLinkProgram(newProgram);
        return newProgram;
    }

    public static void setMatrix4(Mat4 used, String name, int number){
        int modelMatLocation = GLES20.glGetUniformLocation(number, name);
        GLES20.glUniformMatrix4fv(modelMatLocation, 1, false, used.toFa_(), 0);
    }

    public static void setInt(int used, String name, int number){
        int location = GLES20.glGetUniformLocation(number, name);
        GLES20.glUniform1i(location, used);
    }

    public static void setVector3(Vec3 used, String name, int shader){
        int location = GLES20.glGetUniformLocation(shader, name);
        GLES20.glUniform3f(location, used.x, used.y, used.z);
    }

    public static void setFloatArray(FloatBuffer used, String name, int shader){
        int location = GLES20.glGetUniformLocation(shader, name);
        GLES20.glUniform1fv(location, used.array().length, used);
    }

    public static void setVector4(Vec4 used, String name, int shader){
        int location = GLES20.glGetUniformLocation(shader, name);
        GLES20.glUniform4f(location, used.x, used.y, used.z, used.w);
    }

    public static void setFloat(float used, String name, int shader){
        int location = GLES20.glGetUniformLocation(shader, name);
        GLES20.glUniform1f(location, used);
    }
}
