package com.bean.classicmini;

import com.bean.classicmini.utilities.Savefiles;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/*
WHEN CREATING A SCENE FILE:
Follow this format.

Bean adam
component adam Transform
field adam com.bean.classicmini.Transform position_x float 5.0

Make sure fields are public
Make sure component extends from components
Make sure name is specified in default constructor of component
Only one name of object per scene I think
When adding component in scene don't include package
When changing fields include package in component name
 */

public class Scene {
    public Scene(int resourceId){
        List<String> sceneInfo = Savefiles.readLines(resourceId);
        int lineCount = sceneInfo.size();

        for(int l = 0; l < lineCount; l++){
            String current = sceneInfo.get(l);
            String[] data = current.split(" ");

            if(data[0].equals("Bean")){
                allBeans.add(new Bean(data[1]));
            }
            if(data[0].equals("component")){
                int wantedIndex = findBean(data[1]);

                try{
                    Class<?> newClass = Class.forName(data[2]);
                    Constructor<?> constructor = newClass.getConstructor();
                    Object newComponent = constructor.newInstance();
                    allBeans.get(wantedIndex).addComponent(newComponent);
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
                try{
                    int wantedIndex = findBean(data[1]);
                    int componentIndex = allBeans.get(wantedIndex).getComponent(data[2]);

                    Bean selectedBean = allBeans.get(wantedIndex);
                    Object selectedComponent = selectedBean.components.get(componentIndex);

                    Field selectedField = selectedComponent.getClass().getField(data[3]);
                    Object dataToSet = new Object();

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

                    selectedField.set(selectedComponent, dataToSet);
                    selectedBean.components.set(wantedIndex, selectedComponent);
                    allBeans.set(wantedIndex, selectedBean);

                } catch (NoSuchFieldException e){
                    MainActivity.error("NoSuchFieldException" + e.toString());
                } catch (IllegalAccessException e){
                    MainActivity.error("IllegalAccessException");
                }
            }
        }
    }

    public List<Bean> allBeans = new ArrayList<>();
    public void mainloop(){
        int beanCount = allBeans.size();
        for(int b = 0; b < beanCount; b++){
            allBeans.get(b).mainloop();
        }
    }

    public int findBean(String name){
        int beanCount = allBeans.size();
        for(int b = 0; b < beanCount; b++){
            String currentName = allBeans.get(b).objectName;
            if(currentName.equals(name)){
                return b;
            }
        }
        MainActivity.error("Couldn't find bean: " + name);
        return -1;
    }
}
