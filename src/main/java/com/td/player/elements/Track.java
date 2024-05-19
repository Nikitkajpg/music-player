package com.td.player.elements;

import com.td.player.util.Util;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

@SuppressWarnings("FieldMayBeFinal")
public class Track {
    private int id;
    private String title;
    private String artist;
    private String time;
    private int level;
    private String absolutePath;
    private String fileName;
    private MediaPlayer mediaPlayer;

    /**
     * Конструктор для создания нового объекта.
     * <p>Создает {@link MediaPlayer}. Заполняет поля {@link #title} и {@link #artist} метаданными.
     */
    public Track(int id, String title, String artist, File file, String mediaPath, int level) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.level = level;
        this.absolutePath = file.getAbsolutePath();
        this.fileName = file.getName();
        mediaPlayer = new MediaPlayer(new Media(mediaPath));
        setData();
    }

    /**
     * Метод заполняет поля {@link #title} и {@link #artist} метаданными.
     * <p>Выполняется в самом конце инициализации, по сути костыль
     */
    private void setData() {
        mediaPlayer.setOnReady(() -> {
            setTitle((String) mediaPlayer.getMedia().getMetadata().get("title"));
            setArtist((String) mediaPlayer.getMedia().getMetadata().get("artist"));
            setTime(mediaPlayer.getMedia().getDuration().toSeconds());
        });
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
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

    private void setTime(double duration) {
        this.time = Util.getTime((int) duration);
    }

    public String getTime() {
        return time;
    }

    public void upgradeLevel() {
        if (level < 10) {
            level++;
        } else {
            level = 10;
        }
    }

    public void downgradeLevel() {
        if (level > 0) {
            level--;
        } else {
            level = 0;
        }
    }

    public int getId() {
        return id;
    }
}

