package com.td.player.elements;

import com.td.player.managers.MusicManager;

import java.util.ArrayList;

@SuppressWarnings("FieldMayBeFinal")
public class Playlist extends Element {
    private String name;
    private ArrayList<Music> musicArray = new ArrayList<>();

    public Playlist(String name) {
        this.name = name;
    }

    public void add(Music music) {
        musicArray.add(music);
    }

    public void addByName(String name, MusicManager musicManager) {
        for (Music music : musicManager.getMusicArray()) {
            if (music.getFileName().equals(name)) {
                musicArray.add(music);
                break;
            }
        }
    }

    public void deleteByName(String name) {
        musicArray.removeIf(music -> music.getFileName().equals(name));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Music> getMusicArray() {
        return musicArray;
    }

    public Music next(Music currentMusic) {
        for (int i = 0; i < musicArray.size(); i++) {
            if (currentMusic.equals(musicArray.get(i))) {
                if (i + 1 < musicArray.size()) {
                    return musicArray.get(i + 1);
                }
            }
        }
        return musicArray.get(0);
    }

    public Music previous(Music currentMusic) {
        for (int i = 0; i < musicArray.size(); i++) {
            if (currentMusic.equals(musicArray.get(i))) {
                if (i - 1 >= 0) {
                    return musicArray.get(i - 1);
                }
            }
        }
        return musicArray.get(0);
    }
}
