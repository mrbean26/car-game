package com.bean.classicmini.utilities;

import android.opengl.GLES20;

import com.bean.classicmini.MainActivity;

import java.nio.IntBuffer;

public class ClassicMiniShaders {
    public static int createShader(int codePath, int shaderType){
        String code = ClassicMiniSavefiles.readLinesString(codePath);
        int shaderNumber = GLES20.glCreateShader(shaderType);

        GLES20.glShaderSource(shaderNumber, code);
        GLES20.glCompileShader(shaderNumber);

        IntBuffer isCompiled = IntBuffer.allocate(1);
        GLES20.glGetShaderiv(shaderNumber, GLES20.GL_COMPILE_STATUS, isCompiled);
        if(isCompiled.array()[0] == 0){
            GLES20.glDeleteShader(shaderNumber);
            MainActivity.error("Couldnt create shader: " + code);
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
}
