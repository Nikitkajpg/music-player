package com.td.player.elements;

import java.util.ArrayList;

public abstract class ParentElement {
    protected int id;
    protected ArrayList<Track> tracks = new ArrayList<>();
    protected String name;

    public ParentElement(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public void addTrack(Track track) {
        tracks.add(track);
    }

    public void delete(Track track) {
        tracks.remove(track);
    }

    public ArrayList<Track> getTracks() {
        return tracks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }
}
