package com.td.player;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Music {
    private Media media;
    private MediaPlayer mediaPlayer;

    private String fileName, artist, title;

    private int level;

    public Music(Media media, int level, String fileName) {
        this.media = media;
        this.level = level;
        this.fileName = fileName;

        mediaPlayer = new MediaPlayer(media);

    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
