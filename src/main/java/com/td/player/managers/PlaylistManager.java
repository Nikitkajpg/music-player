package com.td.player.managers;

import com.td.player.controllers.Controller;
import com.td.player.elements.Track;
import com.td.player.elements.Playlist;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Класс для управления списком плейлистов
 */
@SuppressWarnings("FieldMayBeFinal")
public class PlaylistManager {
    private Controller controller;
    private ArrayList<Playlist> playlistArray = new ArrayList<>();

    public PlaylistManager(Controller controller) {
        this.controller = controller;
    }

    public void add(String name) {
        playlistArray.add(new Playlist(name));
    }

    public void deleteByPath(String path) {
        for (Playlist playlist : playlistArray) {
            ArrayList<Track> trackArray = playlist.getTrackArray();
            trackArray.removeIf(track -> (path + "\\" + track.getFileName()).equals(track.getAbsolutePath()));
        }
    }

    public ArrayList<Playlist> getPlaylistArray() {
        return playlistArray;
    }

    public void addTrackToPlaylist(String playlistName, Track track) {
        for (Playlist playlist : playlistArray) {
            if (playlist.getName().equals(playlistName)) {
                playlist.addTrack(track);
                break;
            }
        }
    }

    /**
     * Метод создает плейлист для "системы предпочтений".
     * <p>В начале плейлиста находится музыка с самым высоким уровнем (приоритетом), далее по убыванию
     */
    public Playlist createPreferencePlaylist() {
        Playlist preferencePlaylist = new Playlist("Preference");
        for (int i = 10; i >= 0; i--) {
            for (Track track : controller.getTrackManager().getTrackArray()) {
                if (track.getLevel() == i) {
                    preferencePlaylist.addTrack(track);
                }
            }
        }
        return preferencePlaylist;
    }

    public void createDefaultPlaylist() {
        Playlist playlist = new Playlist("All tracks");
        for (Track track : controller.getTrackManager().getTrackArray()) {
            playlist.addTrack(track);
        }
        playlistArray.add(playlist);
    }

    public void updateDefaultPlaylist() {
        Playlist defaultPlaylist = playlistArray.get(0);
        defaultPlaylist.getTrackArray().clear();
        for (Track track : controller.getTrackManager().getTrackArray()) {
            defaultPlaylist.addTrack(track);
        }
    }

    public Playlist createAndGetPlaylist(String playlistName) {
        Playlist playlist = new Playlist(playlistName);
        playlistArray.add(playlist);
        return playlist;
    }

    public void renamePlaylist(String oldName, String newName) {
        for (Playlist playlist : playlistArray) {
            if (playlist.getName().equals(oldName)) {
                playlist.setName(newName);
                break;
            }
        }
    }

    public boolean isUnique(String name) {
        for (Playlist playlist : playlistArray) {
            if (playlist.getName().equals(name)) {
                return false;
            }
        }
        return true;
    }

    public boolean isUniqueInPlaylist(Playlist playlist, String name) {
        for (Track track : playlist.getTrackArray()) {
            if (track.getTitle().equals(name)) {
                return false;
            }
        }
        return true;
    }

    public Playlist getDefaultPlaylist() {
        return playlistArray.get(0);
    }

    public Playlist getPlaylistByName(String name) {
        for (Playlist playlist : playlistArray) {
            if (playlist.getName().equals(name)) {
                return playlist;
            }
        }
        return getDefaultPlaylist();
    }

    public Playlist getRandomPlaylist() {
        Playlist randomPlaylist = getDefaultPlaylist();
        Collections.shuffle(randomPlaylist.getTrackArray());
        return randomPlaylist;
    }

    public void delete(Playlist playlist) {
        playlistArray.remove(playlist);
    }
}
