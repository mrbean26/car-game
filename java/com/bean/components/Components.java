package com.bean.components;

import com.bean.classicmini.Bean;
import com.bean.classicmini.surfaceView;

public class Components implements ComponentsInterface {
    public String objectName = "Unassigned Object";
    public boolean enabled = true;

    public void mainloop(){

    }

    public void begin(){

    }

    public void onClick(){

    }

    public Bean getBean(){
        return surfaceView.currentScene.allBeans.get(objectName);
    }
}
