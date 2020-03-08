package com.bean.classicmini.utilities;

import android.util.Log;

import com.bean.classicmini.MainActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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

    public static String readLinesString(int resourceId){
        String returned = "";
        try {
            InputStream inputStream = MainActivity.getAppContext().getResources().openRawResource(resourceId);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String currentLine = null;
            while((currentLine = bufferedReader.readLine()) != null){
                returned = returned + currentLine + "\n";
            }

            return returned;
        }
        catch(IOException e) {
            Log.d("Bean:Error", "IOException when opening file");
        }
        return returned;
    }

    public static List<String> readAsset(String path){
        List<String> returned = new ArrayList<>();

        try{
            File newFile = new File(MainActivity.getAppContext().getFilesDir(), path);
            Scanner newScanner = new Scanner(newFile);
            while(newScanner.hasNextLine()){
                returned.add(newScanner.nextLine());
            }
        } catch(FileNotFoundException exception){
            ClassicMiniOutput.error("Asset file not found with path: " + path);
        }

        return returned;
    }

    public static void writeAsset(String path, List<String> lines, boolean append){
        boolean createdFile = false;
        File newFile = new File(MainActivity.getAppContext().getFilesDir(), path);

        while (true){
            try{
                FileWriter fileWriter = new FileWriter(newFile, append);
                int lineCount = lines.size();

                for(int i = 0; i < lineCount; i++){
                    fileWriter.write(lines.get(i) + "\n");
                }

                fileWriter.close();
                ClassicMiniOutput.output("Lines written to file with mode append: " + String.valueOf(append));
                break;
            }

            catch(FileNotFoundException fileNotFoundException){
                if(!createAsset(path)){
                    break;
                }
            }

            catch (IOException ioException){
                ClassicMiniOutput.error("IOException when trying to write to asset at: " + path);
                break;
            }
        }
    }

    public static boolean createAsset(String path){
        boolean returned = false;
        File newFile = new File(MainActivity.getAppContext().getFilesDir(), path);
        try{
            newFile.createNewFile();
            returned = true;
        } catch (IOException exception){
            ClassicMiniOutput.error("Couldn't create file at: " + path);
        }
        return returned;
    }

    public static boolean deleteAsset(String path){
        boolean returned = false;
        File newFile = new File(MainActivity.getAppContext().getFilesDir(), path);
        if(newFile.delete()){
            ClassicMiniOutput.output("File was deleted at: " + path);
            returned = true;
        } else{
            ClassicMiniOutput.error("File was not able to delete at: " + path);
        }
        return returned;
    }
}
