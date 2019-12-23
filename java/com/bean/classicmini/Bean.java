package com.bean.classicmini;

import com.bean.classicmini.components.Transform;
import com.bean.components.Components;

import java.util.HashMap;
import java.util.UUID;

public class Bean {
    public String objectName;
    public UUID id;

    public Bean(String name){
        objectName = name;
        id = UUID.randomUUID();
        addComponents(new Transform());
    }
    public HashMap<Class, HashMap<UUID, ? extends Components>> components = new HashMap<>();

    public void mainloop(){
        for(HashMap<UUID, ? extends Components> currentHashmap : components.values()){
            currentHashmap.get(id).mainloop();
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
