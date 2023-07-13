package com.td.player.elements;

import com.td.player.elements.Element;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class Music extends Element {
    private int id;
    private String title;
    private String artist;
    private int level;
    private String path;
    private String fileName;
    private MediaPlayer mediaPlayer;
//    AudioEqualizer
//    AudioSpectrumListener


    public Music(int id, String title, String artist, int level, File file, String mediaPath) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.level = level;
        this.path = file.getAbsolutePath();
        this.fileName = file.getName();
        this.mediaPlayer = new MediaPlayer(new Media(mediaPath));
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public int getId() {
        return id;
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

    public String getPath() {
        return path;
    }

    public String getFileName() {
        return fileName;
    }

    public int getLevel() {
        return level;
    }
}

