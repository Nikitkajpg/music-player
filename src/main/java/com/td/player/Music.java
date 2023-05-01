package com.td.player;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Music {
    private Media media;
    private MediaPlayer mediaPlayer;

    private int level;

    public Music(Media media, int level) {
        this.media = media;
        this.level = level;

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
}
