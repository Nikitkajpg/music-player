package com.td.player.managers;

import com.td.player.elements.Music;
import com.td.player.util.Util;

import java.io.File;
import java.util.ArrayList;

@SuppressWarnings("FieldMayBeFinal")
public class MusicManager {
    private ArrayList<Music> musicArray = new ArrayList<>();

    public void add(String title, String artist, File file, String mediaPath) {
        Music music = new Music(Util.setId(musicArray), title, artist, setLevel(), file, mediaPath);
        musicArray.add(music);
    }

    public void delete(int id) {
        musicArray.removeIf(music -> music.getId() == id);
    }

    public void delete(String path) {
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

    public Music get(int id) {
        for (Music music : musicArray) {
            if (music.getId() == id) {
                return music;
            }
        }
        return null;
    }

    public ArrayList<Music> getMusicArray() {
        return musicArray;
    }

    private int setLevel() {
        return 50;
    }
}
