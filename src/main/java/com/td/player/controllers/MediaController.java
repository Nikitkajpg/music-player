package com.td.player.controllers;

import com.td.player.elements.Music;
import com.td.player.elements.Playlist;
import com.td.player.managers.MusicManager;
import com.td.player.managers.PlaylistManager;
import javafx.scene.control.Button;

@SuppressWarnings("FieldMayBeFinal")
public class MediaController {
    private MusicManager musicManager;
    private PlaylistManager playlistManager;

    private Music currentMusic;
    private Playlist currentPlayList;
    private Button playButton;

    public MediaController(MusicManager musicManager, PlaylistManager playlistManager, Button playButton) {
        this.musicManager = musicManager;
        this.playlistManager = playlistManager;
        this.playButton = playButton;
    }

    public void play() {
        if (currentMusic == null && currentPlayList == null) {
            currentPlayList = playlistManager.getDefaultPlaylist();
            if (currentPlayList.getMusicArray().size() > 0) {
                currentMusic = currentPlayList.getMusicArray().get(0);
                currentMusic.getMediaPlayer().play();
                currentMusic.getMediaPlayer().setOnEndOfMedia(() -> switchMusic(true));
            }
        } else if (currentMusic != null) {
            currentMusic.getMediaPlayer().play();
        }
        playButton.setText("Pause");
    }

    public void pause() {
        currentMusic.getMediaPlayer().pause();
        playButton.setText("Play");
    }

    public void playPlaylistByName(String name) {
        if (currentMusic != null) {
            currentMusic.getMediaPlayer().stop();
        }
        currentPlayList = playlistManager.getPlaylistByName(name);
        currentMusic = currentPlayList.getMusicArray().get(0);
        currentMusic.getMediaPlayer().play();
        currentMusic.getMediaPlayer().setOnEndOfMedia(() -> switchMusic(true));
        playButton.setText("Pause");
    }

    public void playInPlaylist(String musicName, String playlistName) {
        if (currentMusic != null) {
            currentMusic.getMediaPlayer().stop();
        }
        currentPlayList = playlistManager.getPlaylistByName(playlistName);
        currentMusic = musicManager.get(musicName);
        currentMusic.getMediaPlayer().play();
        currentMusic.getMediaPlayer().setOnEndOfMedia(() -> switchMusic(true));
        playButton.setText("Pause");
    }

    public void playByName(String name) {
        if (currentMusic != null) {
            currentMusic.getMediaPlayer().stop();
        }
        currentPlayList = playlistManager.getDefaultPlaylist();
        currentMusic = musicManager.get(name);
        currentMusic.getMediaPlayer().play();
        currentMusic.getMediaPlayer().setOnEndOfMedia(() -> switchMusic(true));
        playButton.setText("Pause");
    }

    public void switchMusic(boolean next) {
        if (currentMusic != null) {
            currentMusic.getMediaPlayer().stop();
            if (next) {
                currentMusic = currentPlayList.getNext(currentMusic);
            } else {
                currentMusic = currentPlayList.getPrevious(currentMusic);
            }
            currentMusic.getMediaPlayer().play();
            currentMusic.getMediaPlayer().setOnEndOfMedia(() -> switchMusic(true));
            playButton.setText("Pause");
        }
    }
}
