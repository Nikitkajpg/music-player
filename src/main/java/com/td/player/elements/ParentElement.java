package com.td.player.elements;

import java.util.ArrayList;

public abstract class ParentElement {
    protected ArrayList<Track> tracks = new ArrayList<>();
    protected String name;

    public ParentElement(String name) {
        this.name = name;
    }

    public Track getTrackByProperties(String propertiesBase) {
        String[] properties = propertiesBase.split(" - ");
        for (Track track : tracks) {
            if (track.getArtist().equals(properties[0]) && track.getTitle().equals(properties[1])) {
                return track;
            }
        }
        return null;
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
}
