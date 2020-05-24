package com.bean.classicmini;

import com.bean.classicmini.components.Transform;
import com.bean.classicmini.utilities.ClassicMiniOutput;
import com.bean.components.Components;

import java.util.LinkedHashMap;
import java.util.UUID;

public class Bean {
    public String objectName;
    public UUID id;

    public static boolean addBean(Bean added){
        surfaceView.currentScene.allBeans.put(added.objectName, added);
        return true;
    }

    public static boolean removeBean(String name){
        surfaceView.currentScene.allBeans.remove(name);
        return true;
    }

    public static Bean getBean(String name){
        Bean result = surfaceView.currentScene.allBeans.get(name);
        if(result == null){
            ClassicMiniOutput.error("Cannot find bean with name " + name);
            return null;
        }
        return result;
    }

    public Bean(String name){
        objectName = name;
        id = UUID.randomUUID();
        addComponents(new Transform());
    }
    public LinkedHashMap<Class, LinkedHashMap<UUID, ? extends Components>> components = new LinkedHashMap<>();  // linkedhashmap so when iterate over values, theyre the same order each time
                                                                                                                // so when added in scene you can add collider after renderer
    public void mainloop(){
        for(LinkedHashMap<UUID, ? extends Components> currentHashmap : components.values()){
            if(currentHashmap.get(id).enabled){
                currentHashmap.get(id).mainloop();
            }
        }
    }

    public <T extends Components> void addComponents(T component){
        synchronized(components){
            LinkedHashMap<UUID, ? extends Components> store = components.get(component);
            if(store == null){
                store = new LinkedHashMap<UUID, T>();
                components.put(component.getClass(), store);
            }
            component.objectName = objectName;
            component.thisBean = this;
            ((LinkedHashMap<UUID, T>) store).put(this.id, component);
        }
    }

    public <T> void removeComponents(Class<T> component){
        LinkedHashMap<UUID, ? extends Components> result = components.get(component);
        if(result != null){
            components.remove(component);
        }
        else{
            ClassicMiniOutput.error("Cannot remove component as it does not exist on this bean.");
        }
    }

    public <T> T getComponents( Class<T> component) {
        LinkedHashMap<UUID, ? extends Components> store = components.get(component);
        T results = (T) store.get(this.id);
        if(results == null)
            throw new IllegalArgumentException("Get Fail: " + objectName + " does not posses Component of Class " + component);
        return results;
    }

    public <T> boolean hasComponents( Class<T> component) {
        return components.containsKey(component);
    }
}
