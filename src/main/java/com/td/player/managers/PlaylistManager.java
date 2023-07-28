package com.td.player.managers;

import com.td.player.elements.Music;
import com.td.player.elements.Playlist;

import java.util.ArrayList;

@SuppressWarnings("FieldMayBeFinal")
public class PlaylistManager {
    private ArrayList<Playlist> playlistArray = new ArrayList<>();

    public void add(String name) {
        playlistArray.add(new Playlist(name));
    }

    public void deleteByPath(String path) {
        for (Playlist playlist : playlistArray) {
            ArrayList<Music> musicArray = playlist.getMusicArray();
            musicArray.removeIf(music -> (path + "\\" + music.getFileName()).equals(music.getAbsolutePath()));
        }
    }

    public void deleteByName(String name) {
        playlistArray.removeIf(playlist -> playlist.getName().equals(name));
    }

    public Playlist getLastPlaylist() {
        return playlistArray.get(playlistArray.size() - 1);
    }

    public ArrayList<Playlist> getPlaylistArray() {
        return playlistArray;
    }

    public void addToPlaylist(String playlistName, Music music) {
        for (Playlist playlist : playlistArray) {
            if (playlist.getName().equals(playlistName)) {
                playlist.add(music);
                break;
            }
        }
    }

    public void createDefaultPlaylist(MusicManager musicManager) {
        Playlist playlist = new Playlist("All music");
        for (Music music : musicManager.getMusicArray()) {
            playlist.add(music);
        }
        playlistArray.add(playlist);
    }

    public void updateDefaultPlaylist(MusicManager musicManager) {
        Playlist defaultPlaylist = playlistArray.get(0);
        defaultPlaylist.getMusicArray().clear();
        for (Music music : musicManager.getMusicArray()) {
            defaultPlaylist.add(music);
        }
    }

    public void createPlaylist(String playlistName) {
        playlistArray.add(new Playlist(playlistName));
    }

    public void renamePlaylist(String oldName, String newName) {
        for (Playlist playlist : playlistArray) {
            if (playlist.getName().equals(oldName)) {
                playlist.setName(newName);
                break;
            }
        }
    }
}
