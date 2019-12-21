package com.bean.components;

import com.bean.classicmini.Bean;
import com.bean.classicmini.surfaceView;

public class Components implements ComponentsInterface {
    public String name = "New Component";
    public String objectName = "Unassigned Object";

    public void mainloop(){

    }

    public Bean getBean(){
        return surfaceView.currentScene.allBeans.get(objectName);
    }
}
