package com.td.player.managers;

import com.td.player.elements.Track;

import java.io.File;
import java.util.ArrayList;

/**
 * Класс для управления списком музыки
 */
@SuppressWarnings("FieldMayBeFinal")
public class TrackManager {
    private ArrayList<Track> trackArray = new ArrayList<>();

    public void add(String title, String artist, File file, String mediaPath) {
        Track track = new Track(title, artist, file, mediaPath);
        trackArray.add(track);
    }

    public void deleteByPath(String path) {
        trackArray.removeIf(track -> (path + "\\" + track.getFileName()).equals(track.getAbsolutePath()));
    }

    public Track get(String name) {
        for (Track track : trackArray) {
            if (track.getFileName().equals(name)) {
                return track;
            }
        }
        return null;
    }

    public String getTitleByFileName(String filename) {
        for (Track track : trackArray) {
            if (track.getFileName().equals(filename)) {
                return track.getTitle();
            }
        }
        return null;
    }

    public Track getTrackByFileName(String fileName) {
        for (Track track : trackArray) {
            if (track.getFileName().equals(fileName)) {
                return track;
            }
        }
        return null;
    }

    public ArrayList<Track> getTrackArray() {
        return trackArray;
    }
}
