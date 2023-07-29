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
    private Media media;
//    AudioEqualizer
//    AudioSpectrumListener

    public Music(String title, String artist, int level, File file, String mediaPath) {
        this.title = title;
        this.artist = artist;
        this.level = level;
        this.absolutePath = file.getAbsolutePath();
        this.fileName = file.getName();
        media = new Media(mediaPath);

//        setData();
    }

//    private void setData() {
//        mediaPlayer.setOnReady(() -> {
//            setTitle((String) mediaPlayer.getMedia().getMetadata().get("title"));
//            setArtist((String) mediaPlayer.getMedia().getMetadata().get("artist"));
//            System.out.println(mediaPlayer.getMedia().getMetadata().get("title"));
//        });
//    }

    public void setLevel(int level) {
        this.level = level;
    }

    public MediaPlayer getMediaPlayer() {
        return new MediaPlayer(media);
    }

    public String getArtist() {
        if (artist == null || artist.equals("")) {
            return "no data";
        } else {
            return artist;
        }
    }

    public String getTitle() {
        if (title == null || title.equals("")) {
            return fileName;
        } else {
            return title;
        }
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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}

