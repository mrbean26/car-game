package com.bean.classicmini;

import android.util.Log;

import com.bean.components.Components;

import java.util.ArrayList;
import java.util.List;

public class Bean {
    public String objectName;
    public Bean(String name){
        objectName = name;
    }
    List<Object> components = new ArrayList<>();

    public <T extends Components> void mainloop(){
        int compCount = components.size();
        for(int c = 0; c < compCount; c++){
            T currentComponent = (T) components.get(c);
            currentComponent.mainloop();
        }
    }

    public <T extends Components> void addComponent(Object object){
        T component = (T) object;
        if(!hasComponents(component.name)){
            components.add(component);

            int componentLength = components.size() - 1;
            ((T) components.get(componentLength)).begin();
            ((T) components.get(componentLength)).objectName = objectName;
        }
    }

    public <T extends Components> boolean hasComponents(String name){
        int compCount = components.size();
        for(int c = 0; c < compCount; c++){
            T component = (T) components.get(c);
            if(component.name.equals(name)){
                return true;
            }
        }
        return false;
    }

    public <T extends Components> int getComponent(String name){
        int compCount = components.size();
        for(int c = 0; c < compCount; c++){
            T currentComponent = (T) components.get(c);
            if(name.equals(currentComponent.name)){
                return c;
            }
        }
        Log.d("Bean:Output", "Component not found.");
        return -1;
    }

    public <T extends Components> void listComponents(){
        String total = "This item contains components: ";
        int compCount = components.size();
        for(int c = 0; c < compCount; c++){
            T component = (T) components.get(c);
            total = total + component.name + ", ";
        }

        Log.d("Bean:Output", total);
    }
}
