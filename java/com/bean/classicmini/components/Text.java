package com.bean.classicmini.components;

import android.opengl.GLES20;

import com.bean.classicmini.R;
import com.bean.classicmini.utilities.ClassicMiniMaterial;
import com.bean.classicmini.utilities.ClassicMiniMath;
import com.bean.classicmini.utilities.ClassicMiniShaders;
import com.bean.components.Components;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import glm.mat._4.Mat4;
import glm.vec._4.Vec4;

import static com.bean.classicmini.components.Image.imageShader;

public class Text extends Components {
    public ClassicMiniMaterial material = new ClassicMiniMaterial();

    public FloatBuffer vertexBuffer;
    public int vertexCount;
    public Vec4 colour = new Vec4(1.0f);
    public boolean realTextScale = false;
    public Vec4 backgroundColour = new Vec4(0.0f);

    public void setText(String newText){
        material.textMaterialInfo.displayedText = newText;
        material.begin();
    }

    public void setTextSize(int newSize){
        material.textMaterialInfo.textSize = newSize;
        material.begin();
    }

    public void setTextCentered(boolean as){
        material.textMaterialInfo.textCentered = as;
        material.begin();
    }

    public void setTextFont(int newFont){
        material.textMaterialInfo.fontPath = newFont;
        material.begin();
    }

    public void draw(){
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        vertexBuffer.position(0);
        int positionHandle = GLES20.glGetAttribLocation(imageShader, "inPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 20, vertexBuffer);

        vertexBuffer.position(3);
        int texHandle = GLES20.glGetAttribLocation(imageShader, "inTexCoord");
        GLES20.glEnableVertexAttribArray(texHandle);
        GLES20.glVertexAttribPointer(texHandle, 2, GLES20.GL_FLOAT, false, 20, vertexBuffer);

        // matrix
        float xTempScale = getBeansComponent(Transform.class).scale.x;
        if(realTextScale){
            getBeansComponent(Transform.class).scale.x = getBeansComponent(Transform.class).scale.x * material.getXTextMultiplier();
        }
        Mat4 currentMatrix = ClassicMiniMath.getOrtho(); // ortho then transform bit
        currentMatrix = currentMatrix.mul(getBeansComponent(Transform.class).toMatrix4());
        getBeansComponent(Transform.class).scale.x = xTempScale;

        GLES20.glUseProgram(imageShader);
        ClassicMiniShaders.setMatrix4(currentMatrix, "model", imageShader);
        ClassicMiniShaders.setInt(0, "sampler", imageShader);
        ClassicMiniShaders.setVector4(colour, "colour", imageShader);
        ClassicMiniShaders.setVector4(backgroundColour, "backgroundColour", imageShader);

        material.bind();

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
        GLES20.glDisable(GLES20.GL_BLEND);
    }

    @Override
    public void mainloop(){
        draw();setText("");
    }

    @Override
    public void begin(){
        material.type = "text";
        material.begin();

        float[] vertices = new float[]{
                -1.0f, -1.0f, 0.0f, 0.0f, 1.0f,
                1.0f,  -1.0f, 0.0f, 1.0f, 1.0f,
                -1.0f, 1.0f, 0.0f, 0.0f, 0.0f,

                -1.0f, 1.0f, 0.0f, 0.0f, 0.0f,
                1.0f, 1.0f, 0.0f, 1.0f, 0.0f,
                1.0f, -1.0f, 0.0f, 1.0f, 1.0f,
        };
        vertexCount = vertices.length / 5;

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());

        vertexBuffer = byteBuffer.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        if(imageShader == -1){
            int fragmentShader = ClassicMiniShaders.createShader(R.raw.imagefragment, GLES20.GL_FRAGMENT_SHADER);
            int vertexShader = ClassicMiniShaders.createShader(R.raw.imagevertex, GLES20.GL_VERTEX_SHADER);

            int[] programs = {vertexShader, fragmentShader};
            imageShader = ClassicMiniShaders.createProgram(programs);
        }
    }
}
