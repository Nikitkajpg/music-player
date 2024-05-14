package com.td.player.managers;

import com.td.player.controllers.Controller;
import com.td.player.elements.Directory;
import com.td.player.elements.ParentElement;
import com.td.player.elements.Playlist;
import com.td.player.elements.Track;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Класс для управления списком плейлистов
 */
@SuppressWarnings("FieldMayBeFinal")
public class PlaylistManager implements ParentElementManager {
    private Controller controller;
    private ArrayList<Playlist> playlists = new ArrayList<>();

    public PlaylistManager(Controller controller) {
        this.controller = controller;
    }

    public void add(String name) {
        playlists.add(new Playlist(name));
    }

    public void deleteByPath(String path) {
        for (Playlist playlist : playlists) {
            ArrayList<Track> trackArray = playlist.getTracks();
            trackArray.removeIf(track -> (path + "\\" + track.getFileName()).equals(track.getAbsolutePath()));
        }
    }

    public void addTrackToPlaylist(String playlistName, Track track) {
        for (Playlist playlist : playlists) {
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
            for (Track track : controller.getTrackManager().getTracks()) {
                if (track.getLevel() == i) {
                    preferencePlaylist.addTrack(track);
                }
            }
        }
        return preferencePlaylist;
    }

    public void createDefaultPlaylist() {
        Playlist playlist = new Playlist("All tracks");
        for (Track track : controller.getTrackManager().getTracks()) {
            playlist.addTrack(track);
        }
        playlists.add(playlist);
    }

    public void updateDefaultPlaylist() {
        Playlist defaultPlaylist = playlists.get(0);
        defaultPlaylist.getTracks().clear();
        for (Track track : controller.getTrackManager().getTracks()) {
            defaultPlaylist.addTrack(track);
        }
    }

    public Playlist getNewPlaylist(String playlistName) {
        Playlist playlist = new Playlist(playlistName);
        playlists.add(playlist);
        return playlist;
    }

    public void renamePlaylist(String oldName, String newName) {
        for (Playlist playlist : playlists) {
            if (playlist.getName().equals(oldName)) {
                playlist.setName(newName);
                break;
            }
        }
    }

    public boolean isUnique(String name) {
        for (Playlist playlist : playlists) {
            if (playlist.getName().equals(name)) {
                return false;
            }
        }
        return true;
    }

    public boolean isUniqueInPlaylist(Playlist playlist, Track newTrack) {
        for (Track track : playlist.getTracks()) {
            if (track.equals(newTrack)) {
                return false;
            }
        }
        return true;
    }

    public Playlist getDefaultPlaylist() {
        Playlist defaultPlaylist = new Playlist("All tracks");
        for (Directory directory : controller.getDirectoryManager().getDirectories()) {
            for (Track track : directory.getTracks()) {
                defaultPlaylist.addTrack(track);
            }
        }
        playlists.add(0, defaultPlaylist);
        return playlists.get(0);
    }

    public Playlist getPlaylistByName(String name) {
        for (Playlist playlist : playlists) {
            if (playlist.getName().equals(name)) {
                return playlist;
            }
        }
        return getDefaultPlaylist();
    }

    public Playlist getRandomPlaylist() {
        Playlist randomPlaylist = getDefaultPlaylist();
        Collections.shuffle(randomPlaylist.getTracks());
        return randomPlaylist;
    }

    @Override
    public void delete(ParentElement parentElement) {
        playlists.remove((Playlist) parentElement);
    }

    @Override
    public void add(ParentElement parentElement) {
        playlists.add((Playlist) parentElement);
    }

    public ArrayList<Playlist> getPlaylists() {
        return playlists;
    }
}
