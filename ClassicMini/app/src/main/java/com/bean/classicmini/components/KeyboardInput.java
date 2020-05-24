package com.bean.classicmini.components;

import android.content.Context;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;

import com.bean.classicmini.Bean;
import com.bean.classicmini.MainActivity;
import com.bean.classicmini.surfaceView;
import com.bean.components.Components;

public class KeyboardInput extends Components {
    public Bean childText;
    public Bean childButton;
    
    public String text = "Text Input"; // cannot change this via runtime, use "setText()"

    private static boolean keyboardOpen = false;
    public boolean selected = false;
    public boolean realTextScale = false;

    public int inputType = INPUT_ALPHANUMERIC;
    public static int INPUT_ALPHANUMERIC = 0;
    public static int INPUT_ALPHA = 1;
    public static int INPUT_NUMERIC = 2;

    public static boolean getIfKeyboardOpen(){
        return keyboardOpen;
    }

    public void setText(String usedText){
        text = usedText;
        childText.getComponents(Text.class).setText(text);

        if(realTextScale){
            childButton.getComponents(Transform.class).scale.x = childText.getComponents(Text.class).material.getXTextMultiplier();
        }
    }

    @Override
    public void begin() {
        childText = new Bean(objectName + "_childText");

        Text childTextText = new Text();
        childTextText.material.textMaterialInfo.displayedText = text;
        childTextText.realTextScale = realTextScale;

        childText.addComponents(childTextText);
        childText.getComponents(Text.class).begin();

        childText.getComponents(Transform.class).parent = this.getBean();
        this.getBeansComponent(Transform.class).children.put(childText.objectName, childText);

        childButton = new Bean(objectName + "_childButton");

        Button selectButton = new Button();
        selectButton.onClickClass = this;
        childButton.addComponents(selectButton);

        childButton.getComponents(Transform.class).parent = this.getBean();
        this.getBeansComponent(Transform.class).children.put(childButton.objectName, childButton);

        Bean.addBean(childText);
        Bean.addBean(childButton);
    }

    @Override
    public void mainloop(){
        if(selected){
            use();
        }
    }

    private void use(){
        for(Integer key : surfaceView.keyPresses.keySet()){
            if(surfaceView.keyPresses.get(key).getKeyCode() == KeyEvent.KEYCODE_ENTER){
                toggleKeyboard();
                break;
            }
            if(surfaceView.keyPresses.get(key).getKeyCode() == KeyEvent.KEYCODE_DEL){
                if(text.length() > 0){
                    setText(text.substring(0, text.length() - 1));
                }
            } else{
                int asciiCode = surfaceView.keyPresses.get(key).getUnicodeChar();
                char newCharacter = (char) asciiCode;

                if((int) newCharacter == 0){
                    continue; // null character
                }

                if(this.inputType == KeyboardInput.INPUT_NUMERIC){
                    if(asciiCode < 48 || asciiCode > 57){
                        continue; // not a number
                    }
                }

                if(this.inputType == KeyboardInput.INPUT_ALPHA){
                    if(asciiCode > 47 && asciiCode < 58){
                        continue; // a number
                    }
                }

                setText(text + newCharacter);
            }
        }
        surfaceView.keyPresses.clear();
    }

    private void toggleKeyboard(){
        Runnable newRunnable = new Runnable() {
            @Override
            public void run() {
                InputMethodManager manager = (InputMethodManager) MainActivity.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

            }
        };

        MainActivity.getMainActivity().runOnUiThread(newRunnable);
        keyboardOpen = !keyboardOpen;
        selected = !selected;
    }

    @Override
    public void onClick(Bean clicked, float x, float y) {
        toggleKeyboard();
    }
}
