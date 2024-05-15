package com.td.player.managers;

import com.td.player.elements.Track;
import com.td.player.util.Util;

import java.io.File;
import java.util.ArrayList;

/**
 * Класс для управления списком музыки
 */
@SuppressWarnings("FieldMayBeFinal")
public class TrackManager {
    private ArrayList<Track> tracks = new ArrayList<>();

    //todo usages???
    public void add(String title, String artist, File file, String mediaPath) {
//        Track track = new Track(title, artist, file, mediaPath, 5);// todo level????
//        tracks.add(track);
    }

    public void deleteByPath(String path) {
        tracks.removeIf(track -> (path + "\\" + track.getFileName()).equals(track.getAbsolutePath()));
    }

    public Track get(String name) {
        for (Track track : tracks) {
            if (track.getFileName().equals(name)) {
                return track;
            }
        }
        return null;
    }

    public String getTitleByFileName(String filename) {
        for (Track track : tracks) {
            if (track.getFileName().equals(filename)) {
                return track.getTitle();
            }
        }
        return null;
    }

    public Track getTrackByFileName(String fileName) {
        for (Track track : tracks) {
            if (track.getFileName().equals(fileName)) {
                return track;
            }
        }
        return null;
    }

    public ArrayList<Track> getTracks() {
        return tracks;
    }
}
