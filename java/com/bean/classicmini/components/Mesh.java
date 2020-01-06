package com.bean.classicmini.components;

import android.opengl.GLES20;

import com.bean.classicmini.MainActivity;
import com.bean.classicmini.R;
import com.bean.classicmini.surfaceView;
import com.bean.classicmini.utilities.ClassicMiniMaterial;
import com.bean.classicmini.utilities.ClassicMiniMath;
import com.bean.classicmini.utilities.ClassicMiniSavefiles;
import com.bean.classicmini.utilities.ClassicMiniShaders;
import com.bean.components.Components;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import glm.mat._4.Mat4;
import glm.vec._2.Vec2;
import glm.vec._3.Vec3;
import glm.vec._3.i.Vec3i;

public class Mesh extends Components {
    public float[] vertices = new float[0];
    public int vertexCount = 0;
    public FloatBuffer vertexBuffer;

    public ClassicMiniMaterial material = new ClassicMiniMaterial();
    public boolean useLight = false;
    public static int meshShader = -1;

    public void render(){
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        vertexBuffer.position(0);
        int positionHandle = GLES20.glGetAttribLocation(meshShader, "inPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 32, vertexBuffer);

        vertexBuffer.position(3);
        int normalHandle = GLES20.glGetAttribLocation(meshShader, "inNormal");
        GLES20.glEnableVertexAttribArray(normalHandle);
        GLES20.glVertexAttribPointer(normalHandle, 3, GLES20.GL_FLOAT, false, 32, vertexBuffer);

        vertexBuffer.position(6);
        int texHandle = GLES20.glGetAttribLocation(meshShader, "inTexCoord");
        GLES20.glEnableVertexAttribArray(texHandle);
        GLES20.glVertexAttribPointer(texHandle, 2, GLES20.GL_FLOAT, false, 32, vertexBuffer);

        GLES20.glUseProgram(meshShader);

        ClassicMiniShaders.setInt(0, "sampler", meshShader);
        ClassicMiniShaders.setMatrix4(Camera.perspectiveMatrix(), "projection", meshShader);
        ClassicMiniShaders.setMatrix4(Camera.viewMatrix(), "view", meshShader);
        ClassicMiniShaders.setMatrix4(getBean().getComponents(Transform.class).toMatrix4(false), "model", meshShader);

        Mat4 newMatrix = getBean().getComponents(Transform.class).toMatrix4(false);
        newMatrix = newMatrix.inverse();
        newMatrix = newMatrix.transpose();
        ClassicMiniShaders.setMatrix4(newMatrix, "transposedInversedModel", meshShader);

        ClassicMiniShaders.setVector3(surfaceView.mainCamera.getBean().getComponents(Transform.class).position(), "viewPos", meshShader);
        ClassicMiniShaders.setFloatArray(Light.getLightInfo(), "lightInfoArray", meshShader);
        ClassicMiniShaders.setInt(useLight? 1 : 0, "useLight", meshShader);

        material.bind();
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
        GLES20.glDisable(GLES20.GL_BLEND);

        GLES20.glDisable(GLES20.GL_BLEND);
    }

    @Override
    public void begin(){
        vertexCount = vertices.length / 8;
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());

        vertexBuffer = byteBuffer.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        if(meshShader == -1){
            int fragmentShader = ClassicMiniShaders.createShader(R.raw.meshfragment, GLES20.GL_FRAGMENT_SHADER);
            int vertexShader = ClassicMiniShaders.createShader(R.raw.meshvertex, GLES20.GL_VERTEX_SHADER);

            int[] programs = {vertexShader, fragmentShader};
            meshShader = ClassicMiniShaders.createProgram(programs);
        }
        material.begin();
    }

    @Override
    public void mainloop(){
        render();
    }

    public static void outputOBJVertices(int resourceId){ // development version
        List<String> allLines = ClassicMiniSavefiles.readLines(resourceId);
        int lineCount = allLines.size();
        List<Float> allVerts = new ArrayList<>();

        List<Vec3> points = new ArrayList<>();
        List<Vec3> normals = new ArrayList<>();
        List<Vec2> texCoords = new ArrayList<>();
        List<List<Vec3i>> faces = new ArrayList<>();

        for(int l = 0; l < lineCount; l++){
            String line = allLines.get(l);

            if(line.length() > 0){
                String firstTwoCharacters = line.substring(0, 2);
                if(firstTwoCharacters.equals("v ")){
                    line = line.replace("v ", "");
                    String[] data = line.split(" ");

                    Vec3 newPoint = new Vec3(
                      Float.valueOf(data[0]),
                      Float.valueOf(data[1]),
                      Float.valueOf(data[2])
                    );
                    points.add(newPoint);
                }
                if(firstTwoCharacters.equals("vn")){
                    line = line.replace("vn ", "");
                    String[] data = line.split(" ");

                    Vec3 newNormal = new Vec3(
                            Float.valueOf(data[0]),
                            Float.valueOf(data[1]),
                            Float.valueOf(data[2])
                    );
                    normals.add(newNormal);
                }
                if(firstTwoCharacters.equals("vt")){
                    line = line.replace("vt ", "");
                    String[] data = line.split(" ");

                    Vec2 newTexcoord = new Vec2(
                            Float.valueOf(data[0]),
                            Float.valueOf(data[1])
                    );
                    texCoords.add(newTexcoord);
                }
                if(line.toCharArray()[0] == 'f'){ // vector then texture then normal
                    line = line.replace("f ", "");
                    String[] data = line.split(" ");
                    List<Vec3i> newFace = new ArrayList<>();

                    for(int i = 0; i < 3; i++){
                        String[] newData = data[i].split("/");
                        Vec3i newIndex = new Vec3i(
                                Integer.valueOf(newData[0]) - 1,
                                Integer.valueOf(newData[1]) - 1,
                                Integer.valueOf(newData[2]) - 1
                        );
                        newFace.add(newIndex);
                    }
                    faces.add(newFace);
                }
            }
        }

        for(List<Vec3i> face : faces){
            for(Vec3i indexes : face){
                allVerts.add(ClassicMiniMath.roundDecimal(points.get(indexes.x).x, 2));
                allVerts.add(ClassicMiniMath.roundDecimal(points.get(indexes.x).y, 2));
                allVerts.add(ClassicMiniMath.roundDecimal(points.get(indexes.x).z, 2));

                allVerts.add(ClassicMiniMath.roundDecimal(normals.get(indexes.z).x, 2));
                allVerts.add(ClassicMiniMath.roundDecimal(normals.get(indexes.z).y, 2));
                allVerts.add(ClassicMiniMath.roundDecimal(normals.get(indexes.z).z, 2));

                allVerts.add(ClassicMiniMath.roundDecimal(texCoords.get(indexes.y).x, 2));
                allVerts.add(ClassicMiniMath.roundDecimal(texCoords.get(indexes.y).y, 2));
            }
        }

        String output = "";
        for(Float f : allVerts){
            output = output + String.valueOf(f) + ",";
        }
        output = output.substring(0, output.length() - 1);

        MainActivity.output(output);
    }
}
