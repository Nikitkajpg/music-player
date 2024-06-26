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
        playlists.add(new Playlist(controller.getPlaylistManager().getPlaylists().size() + 1, name));
    }

    /**
     * Метод создает плейлист для "системы предпочтений".
     * <p>В начале плейлиста находится музыка с самым высоким уровнем (приоритетом), далее по убыванию
     */
    public Playlist createPreferencePlaylist() {
        Playlist preferencePlaylist = new Playlist(controller.getPlaylistManager().getPlaylists().size() + 1, "Preference");
        for (int i = 10; i >= 0; i--) {
            for (Directory directory : controller.getDirectoryManager().getDirectories()) {
                for (Track track : directory.getTracks()) {
                    if (track.getLevel() == i) {
                        preferencePlaylist.addTrack(track);
                    }
                }
            }
        }
        return preferencePlaylist;
    }

    public void createDefaultPlaylist() {
        Playlist playlist = new Playlist(controller.getPlaylistManager().getPlaylists().size() + 1, "All tracks");
        for (Directory directory : controller.getDirectoryManager().getDirectories()) {
            for (Track track : directory.getTracks()) {
                playlist.addTrack(track);
            }
        }
        playlists.add(playlist);
    }

    public void updateDefaultPlaylist() {
        Playlist defaultPlaylist = playlists.get(0);
        defaultPlaylist.getTracks().clear();
        for (Directory directory : controller.getDirectoryManager().getDirectories()) {
            for (Track track : directory.getTracks()) {
                defaultPlaylist.addTrack(track);
            }
        }
    }

    public Playlist getNewPlaylist(String playlistName) {
        Playlist playlist = new Playlist(controller.getPlaylistManager().getPlaylists().size() + 1, playlistName);
        playlists.add(playlist);
        return playlist;
    }

    public void renamePlaylist(String id, String newName) {
        for (Playlist playlist : playlists) {
            if (Integer.parseInt(id) == playlist.getId()) {
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

    public Playlist getDefaultPlaylist() {
        return playlists.get(0);
    }

    public Playlist getRandomPlaylist() {
        Playlist randomPlaylist = getDefaultPlaylist();
        Collections.shuffle(randomPlaylist.getTracks());
        return randomPlaylist;
    }

    @Override
    public void delete(int id) {
        playlists.removeIf(playlist -> playlist.getId() == id);
    }

    @Override
    public void add(ParentElement parentElement) {
        playlists.add((Playlist) parentElement);
    }

    public ArrayList<Playlist> getPlaylists() {
        return playlists;
    }

    public void deleteTrack(int id, int playlistId) {
        for (Playlist playlist : playlists) {
            if (playlist.getId() == playlistId) {
                playlist.getTracks().removeIf(track -> track.getId() == id);
            }
        }
    }
}
