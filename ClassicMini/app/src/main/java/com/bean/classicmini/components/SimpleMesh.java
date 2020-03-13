package com.bean.classicmini.components;

import android.opengl.GLES20;

import com.bean.classicmini.R;
import com.bean.classicmini.utilities.ClassicMiniMath;
import com.bean.classicmini.utilities.ClassicMiniOutput;
import com.bean.classicmini.utilities.ClassicMiniSavefiles;
import com.bean.classicmini.utilities.ClassicMiniShaders;
import com.bean.components.Components;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import glm.vec._3.Vec3;
import glm.vec._3.i.Vec3i;
import glm.vec._4.Vec4;

public class SimpleMesh extends Components {
    public float[] vertices = new float[]{-1.0f, -1.0f, 0.0f, 1.0f, -1.0f, 0.0f, -1.0f, 1.0f, 0.0f};
    public int vertexCount = 0;
    public FloatBuffer vertexBuffer;
    public Vec4 colour = new Vec4(1.0f);

    public static int simpleMeshShader = -1;

    private Vec3[] allPoints = new Vec3[0];
    public Vec3[] getCollisionInfo(){ // one min vec3, one max vec3
        Vec3 maxPoint = null;
        Vec3 minPoint = null;

        int count = allPoints.length;
        for(int p = 0; p < count; p++){
            Vec4 currentPointFour = new Vec4(ClassicMiniMath.copyVectorThree(allPoints[p]), 1.0f);
            currentPointFour = currentPointFour.mul(getBeansComponent(Transform.class).toMatrix4());
            Vec3 currentPoint = new Vec3(currentPointFour);

            if(minPoint == null){
                minPoint = ClassicMiniMath.copyVectorThree(currentPoint);
                maxPoint = ClassicMiniMath.copyVectorThree(currentPoint);
                continue;
            }

            minPoint.x = Math.min(currentPoint.x, minPoint.x);
            minPoint.y = Math.min(currentPoint.y, minPoint.y);
            minPoint.z = Math.min(currentPoint.z, minPoint.z);

            maxPoint.x = Math.max(currentPoint.x, maxPoint.x);
            maxPoint.y = Math.max(currentPoint.y, maxPoint.y);
            maxPoint.z = Math.max(currentPoint.z, maxPoint.z);
        }

        if(minPoint == null){
            minPoint = new Vec3(0f);
            maxPoint = new Vec3(0f);
        }

        return new Vec3[]{minPoint, maxPoint};
    }

    @Override
    public void begin(){
        vertexCount = vertices.length / 3;

        // get all points
        allPoints = new Vec3[vertexCount];
        for(int v = 0; v < vertexCount; v++){
            Vec3 newPoint = new Vec3();
            newPoint.x = vertices[v * 3 + 0];
            newPoint.y = vertices[v * 3 + 1];
            newPoint.z = vertices[v * 3 + 2];

            allPoints[v] = newPoint;
        }

        // buffer data
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());

        vertexBuffer = byteBuffer.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        if(simpleMeshShader == -1){
            int fragmentShader = ClassicMiniShaders.createShader(R.raw.simplemeshfragment, GLES20.GL_FRAGMENT_SHADER);
            int vertexShader = ClassicMiniShaders.createShader(R.raw.simplemeshvertex, GLES20.GL_VERTEX_SHADER);

            int[] programs = {vertexShader, fragmentShader};
            simpleMeshShader = ClassicMiniShaders.createProgram(programs);
        }
    }

    @Override
    public void mainloop(){
        render();
    }

    public void render(){
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        vertexBuffer.position(0);
        int positionHandle = GLES20.glGetAttribLocation(simpleMeshShader, "inPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 12, vertexBuffer);

        GLES20.glUseProgram(simpleMeshShader);

        ClassicMiniShaders.setMatrix4(Camera.perspectiveMatrix(), "projection", simpleMeshShader);
        ClassicMiniShaders.setMatrix4(Camera.viewMatrix(), "view", simpleMeshShader);
        ClassicMiniShaders.setMatrix4(getBeansComponent(Transform.class).toMatrix4(), "model", simpleMeshShader);

        ClassicMiniShaders.setVector4(colour, "colour", simpleMeshShader);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
        GLES20.glDisable(GLES20.GL_BLEND);
    }

    public static void outputOBJVertices(int resourceId){
        List<String> allLines = ClassicMiniSavefiles.readLines(resourceId);
        int lineCount = allLines.size();
        List<Float> allVerts = new ArrayList<>();

        List<Vec3> points = new ArrayList<>();
        List<List<Vec3i>> faces = new ArrayList<>();

        for(int l = 0; l < lineCount; l++){
            String line = allLines.get(l);

            if(line.length() > 0){
                String firstTwoCharacters = line.substring(0, 2);
                if(firstTwoCharacters.equals("v ")){
                    line = line.replace("v ", "");
                    String[] data = line.split(" ");

                    Vec3 newPoint = new Vec3(
                            Float.parseFloat(data[0]),
                            Float.parseFloat(data[1]),
                            Float.parseFloat(data[2])
                    );
                    points.add(newPoint);
                }
                if(line.toCharArray()[0] == 'f'){ // vector then texture then normal
                    line = line.replace("f ", "");
                    String[] data = line.split(" ");
                    List<Vec3i> newFace = new ArrayList<>();

                    for(int i = 0; i < 3; i++){
                        String[] newData = data[i].split("/");
                        Vec3i newIndex = new Vec3i(0);

                        if(newData[0].equals("")){
                            newIndex.x = -1;
                        }
                        if(!newData[0].equals("")){
                            newIndex.x = Integer.parseInt(newData[0]) - 1;
                        }

                        newFace.add(newIndex);
                    }
                    faces.add(newFace);
                }
            }
        }

        boolean hasPositions = false;
        for(List<Vec3i> face : faces){
            for(Vec3i indexes : face){
                if(indexes.x != -1){
                    ClassicMiniOutput.output("HI");
                    allVerts.add(ClassicMiniMath.roundDecimal(points.get(indexes.x).x, 2));
                    allVerts.add(ClassicMiniMath.roundDecimal(points.get(indexes.x).y, 2));
                    allVerts.add(ClassicMiniMath.roundDecimal(points.get(indexes.x).z, 2));

                    hasPositions = true;
                }
            }

            if(!hasPositions){
                ClassicMiniOutput.output("This mesh is missing vertex attributes for positions.");
            }
        }

        String output = "";
        for(Float f : allVerts){
            output = output + String.valueOf(f) + ",";
        }
        output = output.substring(0, output.length() - 1);

        ClassicMiniOutput.output(output);
    }
}
