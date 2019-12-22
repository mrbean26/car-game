package com.bean.classicmini;

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
                allBeans.put(data[1], new Bean(data[1]));
            }
            if(data[0].equals("component")){
                try{
                    Class<?> newClass = Class.forName(data[2]);
                    Constructor<?> constructor = newClass.getConstructor();
                    Object newComponent = constructor.newInstance();
                    allBeans.get(data[1]).addComponents((T) newComponent);
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
                    Class<?> newClass = Class.forName(data[2]);
                    Field selectedField = newClass.getField(data[3]);
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

                    selectedField.set(allBeans.get(data[1]).getComponents(newClass), dataToSet);

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
