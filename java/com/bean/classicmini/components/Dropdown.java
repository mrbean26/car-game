package com.bean.classicmini.components;

import com.bean.classicmini.Bean;
import com.bean.classicmini.R;
import com.bean.classicmini.utilities.ClassicMiniMaterial;
import com.bean.classicmini.utilities.ClassicMiniMath;
import com.bean.components.Components;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import glm.vec._4.Vec4;

class dropdownEntry{
    public String text = "";
    public ClassicMiniMaterial textMaterial = new ClassicMiniMaterial();
}

public class Dropdown extends Components {
    private Bean dropdownEntryBackground;
    private LinkedHashMap<String, dropdownEntry> allDropdownEntries = new LinkedHashMap<>();
    private List<Bean> allDropdownButtons = new ArrayList<>();

    public String[] dropdownItemsBegin;
    public float dropdownItemInterval = 0.0f;
    private boolean opened = false;
    private int selectedIndex = 0;

    @Override
    public void begin() {
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
        // buttons
        float defaultInterval = -2.0f * dropdownEntryBackground.getComponents(Transform.class).scale.y;
        for(int i = 0; i < count; i++){
            Bean newBean = new Bean(objectName + "_dropdownButton_" + String.valueOf(i));
            newBean.getComponents(Transform.class).parent = getBean();
            this.getBeansComponent(Transform.class).children.put(newBean.objectName, newBean);
            newBean.getComponents(Transform.class).position.y = (defaultInterval - dropdownItemInterval) * (float) i;

            Button newButton = new Button();
            newButton.onClickClass = this;
            newButton.type = Button.BUTTON_CLICK_DOWN;
            newBean.addComponents(newButton);

            allDropdownButtons.add(newBean);
            Bean.addBean(newBean);
        }
    }

    @Override
    public void mainloop() {
        render();
        updateButtonPositions();
    }

    @Override
    public void onClick(Bean clicked) {
        int count = allDropdownButtons.size();
        if(count < 1){
            return;
        }

        if(clicked.id == allDropdownButtons.get(0).id){
            if(!opened){
                opened = true;
                return;
            }
        }

        if(opened){
            for(int i = 0; i < count; i++){
                if(clicked.id == allDropdownButtons.get(i).id){
                    selectedIndex = i;
                    opened = false;
                }
            }
        }
    }

    private void render(){
        Object[] allValues = allDropdownEntries.values().toArray();
        int count = allDropdownEntries.size();

        float defaultInterval = -2.0f * dropdownEntryBackground.getComponents(Transform.class).scale.y;
        for(int i = 0; i < count; i++){
            if(!opened && i > 0){
                break;
            }

            dropdownEntry currentEntry = (dropdownEntry) allValues[i];
            dropdownEntryBackground.getComponents(Transform.class).position.y = (defaultInterval - dropdownItemInterval) * (float) i;

            Vec4 tempForegroundColour = ClassicMiniMath.copyVectorFour(dropdownEntryBackground.getComponents(Image.class).colour);
            Vec4 tempBackgroundColour = ClassicMiniMath.copyVectorFour(dropdownEntryBackground.getComponents(Image.class).backgroundColour);

            if(selectedIndex != i && opened){
                dropdownEntryBackground.getComponents(Image.class).backgroundColour.mul(new Vec4(0.4f, 0.4f, 0.4f, 1.0f));
                dropdownEntryBackground.getComponents(Image.class).colour.mul(new Vec4(0.4f, 0.4f, 0.4f, 1.0f));
            }

            dropdownEntryBackground.getComponents(Image.class).material = currentEntry.textMaterial;
            if(!opened){
                dropdownEntryBackground.getComponents(Image.class).material = ((dropdownEntry) allValues[selectedIndex]).textMaterial;
            }


            dropdownEntryBackground.getComponents(Image.class).mainloop();

            dropdownEntryBackground.getComponents(Image.class).backgroundColour = tempBackgroundColour;
            dropdownEntryBackground.getComponents(Image.class).colour = tempForegroundColour;
        }
    }

    private void updateButtonPositions(){
        float defaultInterval = -2.0f * dropdownEntryBackground.getComponents(Transform.class).scale.y;
        int count = allDropdownButtons.size();

        for(int i = 0; i < count; i++){
            Bean currentBean = allDropdownButtons.get(i);
            currentBean.getComponents(Transform.class).position.y = (defaultInterval - dropdownItemInterval) * (float) i;
        }
    }

}
