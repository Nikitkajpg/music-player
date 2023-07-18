package com.td.player.elements;

import com.td.player.managers.MusicManager;

import java.util.ArrayList;

@SuppressWarnings("FieldMayBeFinal")
public class Playlist extends Element {
    private int id;
    private String name;
    private ArrayList<Music> musicArray = new ArrayList<>();

    public Playlist(int id, String name) {
        this.id = id;
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

    public void delete(int id) {
        musicArray.removeIf(music -> music.getId() == id);
    }

    @Override
    public int getId() {
        return 0;
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
}
