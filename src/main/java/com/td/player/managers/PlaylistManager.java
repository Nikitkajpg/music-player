package com.td.player.managers;

import com.td.player.elements.Playlist;

import java.util.ArrayList;

@SuppressWarnings("FieldMayBeFinal")
public class PlaylistManager {
    private ArrayList<Playlist> playlistArray = new ArrayList<>();

    public void add(String name) {
        Playlist playlist = new Playlist(setId(), name);
        playlistArray.add(playlist);
    }

    public int setId() {
        return 0;
    }

    public void delete(int id) {
        playlistArray.removeIf(playlist -> playlist.getId() == id);
    }

    public Playlist get(int id) {
        for (Playlist playlist : playlistArray) {
            if (playlist.getId() == id) {
                return playlist;
            }
        }
        return null;
    }
}
