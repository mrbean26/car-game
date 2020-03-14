package com.bean.components;

import com.bean.classicmini.Bean;

public class Components implements ComponentsInterface {
    public String objectName = "Unassigned Object";
    public boolean enabled = true;
    public Bean thisBean;

    public void mainloop(){

    }

    public void begin(){

    }

    public void onClick(Bean clicked, float x, float y){ // run when assigned object is clicked

    }

    public Bean getBean(){
        return thisBean;
    }

    public <T extends Components> T getBeansComponent(Class<T> component){
        return getBean().getComponents(component);
    }
}
