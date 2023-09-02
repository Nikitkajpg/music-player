package com.td.player.managers;

import com.td.player.elements.Music;
import com.td.player.elements.Playlist;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Класс для управления списком плейлистов. Конструктор отсутствует
 */
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

    public ArrayList<Playlist> getPlaylistArray() {
        return playlistArray;
    }

    public void addMusicToPlaylist(String playlistName, Music music) {
        for (Playlist playlist : playlistArray) {
            if (playlist.getName().equals(playlistName)) {
                playlist.add(music);
                break;
            }
        }
    }

    /**
     * Метод создает плейлист для "системы предпочтений".
     * <p>В начале плейлиста находится музыка с самым высоким уровнем (приоритетом), далее по убыванию
     */
    public Playlist createPreferencePlaylist(MusicManager musicManager) {
        Playlist preferencePlaylist = new Playlist("Preference");
        for (int i = 10; i >= 0; i--) {
            for (Music music : musicManager.getMusicArray()) {
                if (music.getLevel() == i) {
                    preferencePlaylist.add(music);
                }
            }
        }
        return preferencePlaylist;
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
        for (Music music : playlist.getMusicArray()) {
            if (music.getTitle().equals(name)) {
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
        Collections.shuffle(randomPlaylist.getMusicArray());
        return randomPlaylist;
    }

    public void delete(Playlist playlist) {
        playlistArray.remove(playlist);
    }
}
