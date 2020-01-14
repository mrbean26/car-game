package com.bean.classicmini.utilities;

import android.media.MediaPlayer;

import com.bean.classicmini.MainActivity;

import java.util.HashMap;

class ClassicMiniMediaPlayer{
    public MediaPlayer mediaPlayer;
    public int position = 0;
}

public class ClassicMiniAudio {
    private static HashMap<Integer, ClassicMiniMediaPlayer> allMediaPlayers = new HashMap<>(); // string is resource id plus duplicate id

    public static void ClassicMiniAudioMainloop(){
        for(ClassicMiniMediaPlayer classicMiniMediaPlayer : allMediaPlayers.values()){
            classicMiniMediaPlayer.position = classicMiniMediaPlayer.mediaPlayer.getCurrentPosition();
        }
    }

    // Functions for non duplicate audio samples

    public static void loadAudio(int resourceId){
        MediaPlayer newMediaPlayer = MediaPlayer.create(MainActivity.getAppContext(), resourceId);
        ClassicMiniMediaPlayer newPlayer = new ClassicMiniMediaPlayer();
        newPlayer.mediaPlayer = newMediaPlayer;

        allMediaPlayers.put(resourceId, newPlayer);
    }

    public static void playAudio(int resourceId){ // load audio first
        ClassicMiniMediaPlayer result = allMediaPlayers.get(resourceId);
        if(result == null){
            MainActivity.error("Audio has not been loaded and cannot be played with resource name: " + MainActivity.getAppContext().getResources().getResourceName(resourceId));
            return;
        }
        result.mediaPlayer.start();
    }

    public static void stopAudio(int resourceId){
        ClassicMiniMediaPlayer result = allMediaPlayers.get(resourceId);
        if(result == null){
            MainActivity.error("Audio has not been loaded and cannot be stopped with resource name: " + MainActivity.getAppContext().getResources().getResourceName(resourceId));
            return;
        }
        result.mediaPlayer.stop();
    }

    public static void pauseAudio(int resourceId){
        ClassicMiniMediaPlayer result = allMediaPlayers.get(resourceId);
        if(result == null){
            MainActivity.error("Audio has not been loaded and cannot be paused with resource name: " + MainActivity.getAppContext().getResources().getResourceName(resourceId));
            return;
        }
        result.mediaPlayer.pause();
    }

    public static void resumeAudio(int resourceId){
        ClassicMiniMediaPlayer result = allMediaPlayers.get(resourceId);
        if(result == null){
            MainActivity.error("Audio has not been loaded and cannot be paused with resource name: " + MainActivity.getAppContext().getResources().getResourceName(resourceId));
            return;
        }
        result.mediaPlayer.seekTo(result.position);
        result.mediaPlayer.start();
    }

    public static void deleteAudio(int resourceId){
        ClassicMiniMediaPlayer result = allMediaPlayers.get(resourceId);
        if(result == null){
            MainActivity.error("Audio has not been loaded and cannot be stopped with resource name: " + MainActivity.getAppContext().getResources().getResourceName(resourceId));
            return;
        }
        allMediaPlayers.remove(resourceId);
    }

    // Functions for duplicate audio samples
    // duplicate count must be > 1

    // The resource id is x100 then added to duplicate account, meaning there can be 99 safe duplicates before there is a worry of audio being overwritten

    public static void loadAudio(int resourceId, int duplicateCount){
        MediaPlayer newMediaPlayer = MediaPlayer.create(MainActivity.getAppContext(), resourceId);
        ClassicMiniMediaPlayer newPlayer = new ClassicMiniMediaPlayer();
        newPlayer.mediaPlayer = newMediaPlayer;

        resourceId = resourceId * 100;
        resourceId = resourceId + duplicateCount;

        allMediaPlayers.put(resourceId, newPlayer);
    }

    public static void playAudio(int resourceId, int duplicateCount){ // load audio first
        resourceId = resourceId * 100;
        resourceId = resourceId + duplicateCount;

        ClassicMiniMediaPlayer result = allMediaPlayers.get(resourceId);
        if(result == null){
            MainActivity.error("Audio has not been loaded and cannot be played with resource name: " + MainActivity.getAppContext().getResources().getResourceName(resourceId));
            return;
        }
        result.mediaPlayer.start();
    }

    public static void stopAudio(int resourceId, int duplicateCount){
        resourceId = resourceId * 100;
        resourceId = resourceId + duplicateCount;

        ClassicMiniMediaPlayer result = allMediaPlayers.get(resourceId);
        if(result == null){
            MainActivity.error("Audio has not been loaded and cannot be stopped with resource name: " + MainActivity.getAppContext().getResources().getResourceName(resourceId));
            return;
        }
        result.mediaPlayer.stop();
    }

    public static void pauseAudio(int resourceId, int duplicateCount){
        resourceId = resourceId * 100;
        resourceId = resourceId + duplicateCount;

        ClassicMiniMediaPlayer result = allMediaPlayers.get(resourceId);
        if(result == null){
            MainActivity.error("Audio has not been loaded and cannot be paused with resource name: " + MainActivity.getAppContext().getResources().getResourceName(resourceId));
            return;
        }
        result.mediaPlayer.pause();
    }

    public static void resumeAudio(int resourceId, int duplicateCount){
        resourceId = resourceId * 100;
        resourceId = resourceId + duplicateCount;

        ClassicMiniMediaPlayer result = allMediaPlayers.get(resourceId);
        if(result == null){
            MainActivity.error("Audio has not been loaded and cannot be paused with resource name: " + MainActivity.getAppContext().getResources().getResourceName(resourceId));
            return;
        }
        result.mediaPlayer.seekTo(result.position);
        result.mediaPlayer.start();
    }

    public static void deleteAudio(int resourceId, int duplicateCount){
        resourceId = resourceId * 100;
        resourceId = resourceId + duplicateCount;

        ClassicMiniMediaPlayer result = allMediaPlayers.get(resourceId);
        if(result == null){
            MainActivity.error("Audio has not been loaded and cannot be stopped with resource name: " + MainActivity.getAppContext().getResources().getResourceName(resourceId));
            return;
        }
        allMediaPlayers.remove(resourceId);
    }
}
