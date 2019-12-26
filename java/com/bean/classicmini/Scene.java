package com.bean.classicmini;

import com.bean.classicmini.components.Camera;
import com.bean.classicmini.components.Transform;
import com.bean.classicmini.utilities.ClassicMiniSavefiles;
import com.bean.components.Components;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

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
    public <T extends Components> Scene(int resourceId){
        List<String> sceneInfo = ClassicMiniSavefiles.readLines(resourceId);
        int lineCount = sceneInfo.size();

        for(int l = 0; l < lineCount; l++){
            String current = sceneInfo.get(l);
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
                    Object newComponent = constructor.newInstance();
                    allBeans.get(data[1]).addComponents((T) newComponent);

                    if(newClass.getName().equals("com.bean.classicmini.components.Camera")){
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

                    Object dataToSet = new Object();
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

                    selectedField.set(dataToSetTo, dataToSet);

                } catch (NoSuchFieldException e){
                    MainActivity.error("NoSuchFieldException" + e.toString());
                } catch (IllegalAccessException e){
                    MainActivity.error("IllegalAccessException");
                } catch(ClassNotFoundException e){
                    MainActivity.error("ClassNotFoundException");
                }
            }
        }

        for(Bean bean : allBeans.values()){
            for(HashMap<UUID, ? extends Components> component : bean.components.values()){
                component.get(bean.id).begin();
            }
        }
    }

    public HashMap<String, Bean> allBeans = new HashMap<>();
    public void mainloop(){
        for(Bean bean : allBeans.values()){
            bean.mainloop();
        }
    }
}
