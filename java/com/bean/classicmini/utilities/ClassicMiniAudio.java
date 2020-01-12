package com.bean.classicmini.utilities;

import android.media.MediaPlayer;

import com.bean.classicmini.MainActivity;

import java.util.HashMap;

public class ClassicMiniAudio {
    private static HashMap<Integer, MediaPlayer> allMediaPlayers = new HashMap<>();

    public static void loadAudio(int resourceId){
        MediaPlayer newMediaPlayer = MediaPlayer.create(MainActivity.getAppContext(), resourceId);
        allMediaPlayers.put(resourceId, newMediaPlayer);
    }

    public static void playAudio(int resourceId){ // load audio first
        allMediaPlayers.get(resourceId).start();
    }

    public static void stopAudio(int resourceId){
        allMediaPlayers.get(resourceId).stop();
    }
}
