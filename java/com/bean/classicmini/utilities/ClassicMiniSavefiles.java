package com.bean.classicmini.utilities;

import android.util.Log;

import com.bean.classicmini.MainActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ClassicMiniSavefiles {
    public static List<String> readLines(int resourceId){
        List<String> returned = new ArrayList<>();

        try {
            InputStream inputStream = MainActivity.getAppContext().getResources().openRawResource(resourceId);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String currentLine = null;
            while((currentLine = bufferedReader.readLine()) != null){
                returned.add(currentLine);
            }

            return returned;
        }
        catch(IOException e) {
            Log.d("Bean:Error", "IOException when opening file");
        }

        return returned;
    }
}
