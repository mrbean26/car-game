package com.bean.classicmini;

import com.bean.classicmini.components.Transform;
import com.bean.components.Components;

import java.util.HashMap;
import java.util.UUID;

public class Bean {
    public String objectName;
    public UUID id;

    public static Bean getBean(String name){
        Bean result = surfaceView.currentScene.allBeans.get(name);
        if(result == null){
            MainActivity.error("Cannot find bean with name " + name);
            return null;
        }
        return result;
    }

    public Bean(String name){
        objectName = name;
        id = UUID.randomUUID();
        addComponents(new Transform());
    }
    public HashMap<Class, HashMap<UUID, ? extends Components>> components = new HashMap<>();

    public void mainloop(){
        for(HashMap<UUID, ? extends Components> currentHashmap : components.values()){
            if(currentHashmap.get(id).enabled){
                currentHashmap.get(id).mainloop();
            }
        }
    }

    public <T extends Components> void addComponents(T component){
        synchronized(components){
            HashMap<UUID, ? extends Components> store = components.get(component);
            if(store == null){
                store = new HashMap<UUID, T>();
                components.put(component.getClass(), store);
            }
            component.objectName = objectName;
            ((HashMap<UUID, T>) store).put(this.id, component);
        }
    }

    public <T> void removeComponents(Class<T> component){
        HashMap<UUID, ? extends Components> result = components.get(component);
        if(result != null){
            components.remove(component);
        }
        else{
            MainActivity.error("Cannot remove component as it does not exist on this bean.");
        }
    }

    public <T> T getComponents( Class<T> component) {
        HashMap<UUID, ? extends Components> store = components.get(component);
        T results = (T) store.get(this.id);
        if(results == null)
            throw new IllegalArgumentException("Get Fail: " + objectName + " does not posses Component of Class " + component);
        return results;
    }

    public <T> boolean hasComponents( Class<T> component) {
        return components.containsKey(component);
    }
}
