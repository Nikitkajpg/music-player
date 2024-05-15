package com.td.player.elements;

@SuppressWarnings("FieldMayBeFinal")
public class Playlist extends ParentElement {
    public Playlist(int id, String name) {
        super(id, name);
    }

    public Track getNextTrack(Track currentTrack) {
        if (tracks.indexOf(currentTrack) + 1 < tracks.size()) {
            return tracks.get(tracks.indexOf(currentTrack) + 1);
        }
        return tracks.get(0);
    }

    public Track getPreviousTrack(Track currentTrack) {
        if (tracks.indexOf(currentTrack) - 1 >= 0) {
            return tracks.get(tracks.indexOf(currentTrack) - 1);
        }
        return tracks.get(tracks.size() - 1);
    }
}
