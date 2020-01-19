package com.bean.classicmini;

import android.opengl.GLES20;
import android.opengl.GLES31Ext;

import androidx.core.content.res.TypedArrayUtils;

import com.bean.classicmini.components.Camera;
import com.bean.classicmini.components.Light;
import com.bean.classicmini.components.Mesh;
import com.bean.classicmini.components.Sprite;
import com.bean.classicmini.components.Transform;
import com.bean.classicmini.utilities.ClassicMiniSavefiles;
import com.bean.components.Components;

import java.io.ObjectInputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import glm.vec._3.Vec3;
import glm.vec._4.Vec4;

/*
WHEN CREATING A SCENE FILE:
Follow this format.

Bean adam
component adam Transform
field adam com.bean.classicmini.Transform position_x float 5.0

TO LOAD RESOURCE
field name componentNameAndPackage fieldName intResource fileUnderResName fileName
EXAMPLE
field toby com.bean.classicmini.components.Image textureResourcePath intResource drawable image

PARENTS
Bean toby
Bean adam toby (create a bean for child under toby)

SUBCLASS FIELDS
field componentName top-bottom-bottom int 44
field com.bean.classicmini.components.Image usedTexture-id 44

Make sure fields are public
Make sure component extends from components
Make sure name is specified in default constructor of component
Only one name of object per scene I think
When adding component in scene don't include package
When changing fields include package in component name
 */

public class Scene {
    public HashMap<String, Bean> allBeans = new HashMap<>();
    public <T extends Components> Scene(int resourceId){
        List<String> sceneInfo = ClassicMiniSavefiles.readLines(resourceId);
        int lineCount = sceneInfo.size();

        for(int l = 0; l < lineCount; l++){
            String current = sceneInfo.get(l);
            if(current.length() > 2){
                String firstTwo = current.substring(0, 2);
                if(firstTwo.equals("//")){ // comment
                    continue;
                }
            }

            String[] data = current.split(" ");
            if(data[0].equals("Bean")){
                Bean newBean = new Bean(data[1]);
                int dataLength = data.length;

                allBeans.put(newBean.objectName, newBean);
                if(dataLength > 2){
                    newBean.getComponents(Transform.class).parent = allBeans.get(data[2]);
                    allBeans.get(data[2]).getComponents(Transform.class).children.put(data[1], newBean);
                }
            }
            if(data[0].equals("component")){
                data[2] = "com.bean." + data[2];
                try{
                    Class<?> newClass = Class.forName(data[2]);
                    Constructor<?> constructor = newClass.getConstructor();

                    if(newClass == Mesh.class){
                        if(allBeans.get(data[1]).hasComponents(Sprite.class)){
                            MainActivity.error("You can only attach one type of renderer to a bean.");
                            return;
                        }
                    }

                    if(newClass == Sprite.class){
                        if(allBeans.get(data[1]).hasComponents(Mesh.class)){
                            MainActivity.error("You can only attach one type of renderer to a bean.");
                            return;
                        }
                    }

                    Object newComponent = constructor.newInstance();
                    allBeans.get(data[1]).addComponents((T) newComponent);

                    if(newClass == Camera.class){
                        if(surfaceView.mainCamera == null){
                            surfaceView.mainCamera = allBeans.get(data[1]).getComponents(Camera.class);
                        }
                    }
                } catch(ClassNotFoundException e){
                    MainActivity.error("ClassNotFoundException");
                } catch(NoSuchMethodException e){
                    MainActivity.error("NoSuchMethodException");
                } catch(IllegalAccessException e){
                    MainActivity.error("IllegalAccessException");
                } catch(InstantiationException e){
                    MainActivity.error("InstantiationException");
                } catch(InvocationTargetException e){
                    MainActivity.error("InvocationTargetException");
                }
            }
            if(data[0].equals("field")){
                data[2] = "com.bean." + data[2];
                try{
                    Class<?> selectedComponent = Class.forName(data[2]);

                    Object dataToSet = null;
                    Object[] dataArrayToSet = new Object[0];
                    Object dataToSetTo = allBeans.get(data[1]).getComponents(selectedComponent);

                    Field selectedField = selectedComponent.getField("objectName"); // just for initialisaton
                    if(!data[3].contains("-")){
                        selectedField = selectedComponent.getField(data[3]);
                    }
                    if(data[3].contains("-")){
                        String[] secondaryData = data[3].split("-");
                        int length = secondaryData.length;

                        for(int i = 0; i < length; i++){
                            Class<?> currentClass = dataToSetTo.getClass();
                            selectedField = currentClass.getField(secondaryData[i]);

                            if(i != length - 1){
                                dataToSetTo = selectedField.get(dataToSetTo);
                            }
                        }
                    }

                    if(data[4].equals("string")){
                        dataToSet = data[5];
                    }
                    if(data[4].equals("int")){
                        dataToSet = Integer.valueOf(data[5]);
                    }
                    if(data[4].equals("float")){
                        dataToSet = Float.valueOf(data[5]);
                    }
                    if(data[4].equals("bool")){
                        if(data[5].equals("true")){
                            dataToSet = true;
                        }
                        if(data[5].equals("false")){
                            dataToSet = false;
                        }
                    }
                    if(data[4].equals("intResource")){
                        dataToSet = MainActivity.getAppContext().getResources().getIdentifier(data[6], data[5], MainActivity.getAppContext().getPackageName());
                    }
                    if(data[4].equals("floatArray")){
                        String[] allItems = data[5].split(",");
                        int length = allItems.length;
                        float[] setData = new float[length];

                        for(int i = 0; i < length; i++){
                            setData[i] = Float.parseFloat(allItems[i]);
                        }
                        selectedField.set(dataToSetTo, setData);
                    }
                    if(data[4].equals("intArray")){
                        String[] allItems = data[5].split(",");
                        int length = allItems.length;
                        int[] setData = new int[length];

                        for(int i = 0; i < length; i++){
                            setData[i] = Integer.parseInt(allItems[i]);
                        }
                        selectedField.set(dataToSetTo, setData);
                    }
                    if(data[4].equals("stringArray")){
                        dataToSet = data[5].split(",");
                    }
                    if(data[4].equals("vec3")){
                        String[] info = data[5].split(",");

                        Vec3 newVector = new Vec3();
                        newVector.x = Float.parseFloat(info[0]);
                        newVector.y = Float.parseFloat(info[1]);
                        newVector.z = Float.parseFloat(info[2]);

                        dataToSet = newVector;
                    }
                    if(data[4].equals("vec4")){
                        String[] info = data[5].split(",");

                        Vec4 newVector = new Vec4();
                        newVector.x = Float.parseFloat(info[0]);
                        newVector.y = Float.parseFloat(info[1]);
                        newVector.z = Float.parseFloat(info[2]);
                        newVector.w = Float.parseFloat(info[3]);

                        dataToSet = newVector;
                    }
                    // set
                    if(dataToSet != null){
                        selectedField.set(dataToSetTo, dataToSet);
                    }

                } catch (NoSuchFieldException e){
                    MainActivity.error("NoSuchFieldException" + e.toString());
                } catch (IllegalAccessException e){
                    MainActivity.error("IllegalAccessException");
                } catch(ClassNotFoundException e){
                    MainActivity.error("ClassNotFoundException");
                }
            }
        }
    }

    public void mainloop(){
        for(Bean bean : allBeans.values()){
            if(surfaceView.framesThrough == 0){
                for(HashMap<UUID, ? extends Components> component : bean.components.values()){
                    if(component.get(bean.id).enabled){
                        component.get(bean.id).begin();
                    }
                }
            }
            bean.mainloop();
        }
    }

    public <T extends Components> Bean[] findBeansWithComponent(Class<T> component){
        List<Bean> returnedList = new ArrayList<>();
        for(Bean current : allBeans.values()){
            if(current.hasComponents(component)){
                returnedList.add(current);
            }
        }
        Bean[] array = new Bean[returnedList.size()];
        int count = returnedList.size();
        for(int i = 0; i < count; i++){
            array[i] = returnedList.get(i);
        }

        return array;
    }

    // shadows require android OPEN GL 3.1+, maybe roll out an update when all other updates are finished with shadows so compatible devices can use
    // or when OPEN GL 3.1 + is standard
}
