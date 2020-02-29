package com.bean.classicmini.components;

import com.bean.classicmini.Bean;
import com.bean.classicmini.R;
import com.bean.classicmini.utilities.ClassicMiniMaterial;
import com.bean.components.Components;

import java.util.LinkedHashMap;

import glm.vec._4.Vec4;

class dropdownEntry{
    public String text = "";
    public ClassicMiniMaterial textMaterial = new ClassicMiniMaterial();
}

public class Dropdown extends Components {
    private Bean dropdownEntryBackground;
    private LinkedHashMap<String, dropdownEntry> allDropdownEntries = new LinkedHashMap<>();

    public String[] dropdownItemsBegin;
    public float dropdownItemInterval = 0.0f;

    @Override
    public void begin() {
        dropdownItemsBegin = new String[]{"NiceOneMate", "Adam", "5"};
        // start background
        dropdownEntryBackground = new Bean(objectName + "_dropdownEntryBackground");
        dropdownEntryBackground.getComponents(Transform.class).parent = this.getBean();
        this.getBeansComponent(Transform.class).children.put(dropdownEntryBackground.objectName, dropdownEntryBackground);

        Image newImage = new Image();
        newImage.material.colourHex = "#FFFFFF";
        newImage.backgroundColour = new Vec4(1.0f, 0.0f, 0.0f, 1.0f);
        dropdownEntryBackground.addComponents(newImage);
        newImage.begin();
        // load entries
        int count = dropdownItemsBegin.length;
        for(int i = 0; i < count; i++){
            dropdownEntry newEntry = new dropdownEntry();
            newEntry.text = dropdownItemsBegin[i];

            newEntry.textMaterial.type = "text";
            newEntry.textMaterial.textMaterialInfo.displayedText = dropdownItemsBegin[i];
            newEntry.textMaterial.textMaterialInfo.fontPath = R.font.default_font;
            newEntry.textMaterial.textMaterialInfo.textCentered = true;

            newEntry.textMaterial.begin();
            allDropdownEntries.put(dropdownItemsBegin[i], newEntry);
        }
    }

    @Override
    public void mainloop() {
        render();
    }

    @Override
    public void onClick() {

    }

    private void render(){
        Object[] allValues = allDropdownEntries.values().toArray();
        int count = allDropdownEntries.size();

        for(int i = 0; i < count; i++){
            dropdownEntry currentEntry = (dropdownEntry) allValues[i];
            float defaultInterval = -2.0f * dropdownEntryBackground.getComponents(Transform.class).scale.y;
            dropdownEntryBackground.getComponents(Transform.class).position.y = (defaultInterval - dropdownItemInterval) * (float) i;

            dropdownEntryBackground.getComponents(Image.class).material = currentEntry.textMaterial;
            dropdownEntryBackground.getComponents(Image.class).mainloop();
        }
    }
}
