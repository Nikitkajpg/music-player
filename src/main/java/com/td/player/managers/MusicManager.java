package com.td.player.managers;

import com.td.player.elements.Music;

import java.io.File;
import java.util.ArrayList;

@SuppressWarnings("FieldMayBeFinal")
public class MusicManager {
    private ArrayList<Music> musicArrayList = new ArrayList<>();

    public void add(String title, String artist, File file, String mediaPath) {
        Music music = new Music(setId(), title, artist, setLevel(), file, mediaPath);
        musicArrayList.add(music);
    }

    private int setLevel() {
        return 50;
    }

    public int setId() {
        return 0;
    }

    public void delete(int id) {
        musicArrayList.removeIf(music -> music.getId() == id);
    }

    public ArrayList<Music> getMusicArray() {
        return musicArrayList;
    }

    public void delete(String path) {
        musicArrayList.removeIf(music -> music.getPath().contains(path));
    }

    public Music get(String name) {
        for (Music music : musicArrayList) {
            if (music.getFileName().equals(name)) {
                return music;
            }
        }
        return null;
    }

    public Music get(int id) {
        for (Music music : musicArrayList) {
            if (music.getId() == id) {
                return music;
            }
        }
        return null;
    }
}
