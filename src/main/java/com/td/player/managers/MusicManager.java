package com.td.player.managers;

import com.td.player.elements.Music;

import java.io.File;
import java.util.ArrayList;

/**
 * Класс для управления списком музыки. Конструктор отсутствует
 */
@SuppressWarnings("FieldMayBeFinal")
public class MusicManager {
    private ArrayList<Music> musicArray = new ArrayList<>();

    public void add(String title, String artist, File file, String mediaPath) {
        Music music = new Music(title, artist, setLevel(), file, mediaPath);
        musicArray.add(music);
    }

    public void deleteByPath(String path) {
        musicArray.removeIf(music -> (path + "\\" + music.getFileName()).equals(music.getAbsolutePath()));
    }

    public Music get(String name) {
        for (Music music : musicArray) {
            if (music.getFileName().equals(name)) {
                return music;
            }
        }
        return null;
    }

    public String getTitleByFileName(String filename) {
        for (Music music : musicArray) {
            if (music.getFileName().equals(filename)) {
                return music.getTitle();
            }
        }
        return null;
    }

    public Music getMusicByFileName(String fileName) {
        for (Music music : musicArray) {
            if (music.getFileName().equals(fileName)) {
                return music;
            }
        }
        return null;
    }

    public ArrayList<Music> getMusicArray() {
        return musicArray;
    }

    private int setLevel() {
        return 5;
    }
}
