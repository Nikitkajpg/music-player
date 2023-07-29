package com.td.player.controllers;

import com.td.player.elements.Music;
import com.td.player.elements.Playlist;
import com.td.player.managers.MusicManager;
import com.td.player.managers.PlaylistManager;
import javafx.scene.control.Button;
import javafx.scene.media.MediaPlayer;

@SuppressWarnings("FieldMayBeFinal")
public class MediaController {
    private MusicManager musicManager;
    private PlaylistManager playlistManager;
    private ViewController viewController;

    private Music currentMusic;
    private Playlist currentPlayList;
    private MediaPlayer currentMediaPlayer;
    private Button playButton;

    public MediaController(MusicManager musicManager, PlaylistManager playlistManager, Button playButton) {
        this.musicManager = musicManager;
        this.playlistManager = playlistManager;
        this.playButton = playButton;
    }

    public void play() {
        if (currentMediaPlayer != null && currentMediaPlayer.getStatus().equals(MediaPlayer.Status.PAUSED)) {
            currentMediaPlayer.play();
        } else {
            String playlistName = viewController.getExpandedPlaylistName();
            if (playlistName != null) {
                for (Playlist p : playlistManager.getPlaylistArray()) {
                    if (p.getName().equals(playlistName)) {
                        currentPlayList = p;
                        playPlaylist();
                        break;
                    }
                }
            } else {
                currentPlayList = playlistManager.getDefaultPlaylist();
                playPlaylist();
            }
        }
        playButton.setText("Pause");
    }

    private void playPlaylist() {
        if (currentPlayList.getMusicArray().size() > 0) {
            currentMusic = currentPlayList.getMusicArray().get(0);
            currentMediaPlayer = currentMusic.getMediaPlayer();
            currentMediaPlayer.setOnEndOfMedia(this::next);
            currentMediaPlayer.play();
        } else {
            currentPlayList = playlistManager.getDefaultPlaylist();
            playPlaylist();
        }
    }

    public void playByName(String name) {
        if (currentMediaPlayer != null) {
            currentMediaPlayer.stop();
        }
        currentPlayList = playlistManager.getDefaultPlaylist();
        currentMusic = musicManager.get(name);
        currentMediaPlayer = currentMusic.getMediaPlayer();
        currentMediaPlayer.setOnEndOfMedia(this::next);
        currentMediaPlayer.play();
        playButton.setText("Pause");
    }

    public void pause() {
        currentMediaPlayer.pause();
        playButton.setText("Play");
    }

    public void next() {
        if (currentMediaPlayer != null) {
            currentMediaPlayer.stop();
        }
        currentMusic = currentPlayList.next(currentMusic);
        currentMediaPlayer = currentMusic.getMediaPlayer();
        currentMediaPlayer.setOnEndOfMedia(this::next);
        currentMediaPlayer.play();
    }

    public void previous() {
        if (currentMediaPlayer != null) {
            currentMediaPlayer.stop();
        }
        currentMusic = currentPlayList.previous(currentMusic);
        currentMediaPlayer = currentMusic.getMediaPlayer();
        currentMediaPlayer.setOnEndOfMedia(this::next);
        currentMediaPlayer.play();
    }

    public void setViewController(ViewController viewController) {
        this.viewController = viewController;
    }
}
