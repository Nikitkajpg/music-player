package com.td.player.elements;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

@SuppressWarnings("FieldMayBeFinal")
public class Music extends Element {
    private String title;
    private String artist;
    private int level;
    private String absolutePath;
    private String fileName;
    private MediaPlayer mediaPlayer;
//    AudioEqualizer
//    AudioSpectrumListener


    public Music(String title, String artist, int level, File file, String mediaPath) {
        this.title = title;
        this.artist = artist;
        this.level = level;
        this.absolutePath = file.getAbsolutePath();
        this.fileName = file.getName();
        this.mediaPlayer = new MediaPlayer(new Media(mediaPath));
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public String getArtist() {
        return artist;
    }

    public String getTitle() {
        return title;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public String getFileName() {
        return fileName;
    }

    public int getLevel() {
        return level;
    }
}

