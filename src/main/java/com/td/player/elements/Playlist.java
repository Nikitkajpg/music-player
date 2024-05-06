package com.td.player.elements;

import com.td.player.managers.TrackManager;

import java.util.ArrayList;

@SuppressWarnings("FieldMayBeFinal")
public class Playlist {
    private String name;
    private ArrayList<Track> trackArray = new ArrayList<>();

    public Playlist(String name) {
        this.name = name;
    }

    public void addTrack(Track track) {
        trackArray.add(track);
    }

    public void addTrackByFilename(String trackFileName, TrackManager trackManager) {
        for (Track track : trackManager.getTrackArray()) {
            if (track.getFileName().equals(trackFileName)) {
                trackArray.add(track);
                break;
            }
        }
    }

    public void delete(Track track) {
        trackArray.remove(track);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Track> getTrackArray() {
        return trackArray;
    }

    public Track getNext(Track currentTrack) {
        for (int i = 0; i < trackArray.size(); i++) {
            if (currentTrack.equals(trackArray.get(i))) {
                if (i + 1 < trackArray.size()) {
                    return trackArray.get(i + 1);
                }
            }
        }
        return trackArray.get(0);
    }

    public Track getPrevious(Track currentTrack) {
        for (int i = 0; i < trackArray.size(); i++) {
            if (currentTrack.equals(trackArray.get(i))) {
                if (i - 1 >= 0) {
                    return trackArray.get(i - 1);
                }
            }
        }
        return trackArray.get(trackArray.size() - 1);
    }
}
