package com.td.player.elements;

import com.td.player.managers.TrackManager;

@SuppressWarnings("FieldMayBeFinal")
public class Playlist extends ParentElement {
    public Playlist(String name) {
        super(name);
    }

    public void addTrackByFilename(String trackFileName, TrackManager trackManager) {
        for (Track track : trackManager.getTracks()) {
            if (track.getFileName().equals(trackFileName)) {
                tracks.add(track);
                break;
            }
        }
    }

    public Track getNextTrack(Track currentTrack) {
        for (int i = 0; i < tracks.size(); i++) {
            if (currentTrack.equals(tracks.get(i))) {
                if (i + 1 < tracks.size()) {
                    return tracks.get(i + 1);
                }
            }
        }
        return tracks.get(0);
    }

    public Track getPreviousTrack(Track currentTrack) {
        for (int i = 0; i < tracks.size(); i++) {
            if (currentTrack.equals(tracks.get(i))) {
                if (i - 1 >= 0) {
                    return tracks.get(i - 1);
                }
            }
        }
        return tracks.get(tracks.size() - 1);
    }
}
