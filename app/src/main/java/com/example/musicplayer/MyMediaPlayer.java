package com.example.musicplayer;

import android.media.MediaPlayer;

public class MyMediaPlayer {

    public static MediaPlayer instance;
    public static int currentIndex = -1;

    public static MediaPlayer getInstance() {
        if (instance == null)
            return new MediaPlayer();
        else {
            return instance;
        }
    }

}
