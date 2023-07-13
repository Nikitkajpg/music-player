package com.td.player.elements;

import com.td.player.elements.Element;
import com.td.player.elements.Music;

import java.util.ArrayList;

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
}
