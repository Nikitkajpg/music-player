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
                        playPlaylist(p);
                        break;
                    }
                }
            } else {
                playPlaylist(playlistManager.getDefaultPlaylist());
            }
        }
        playButton.setText("Pause");
    }

    private void playPlaylist(Playlist playlist) {
        if (playlist.getMusicArray().size() > 0) {
            int id = 0;
            currentMediaPlayer = playlist.getMusicArray().get(id).getMediaPlayer();
            currentMediaPlayer.play();
        } else {
            playPlaylist(playlistManager.getDefaultPlaylist());
        }
    }

    public void playByName(String name) {
        if (currentMediaPlayer != null) {
            currentMediaPlayer.stop();
        }
        currentMediaPlayer = musicManager.get(name).getMediaPlayer();
        currentMediaPlayer.play();
        playButton.setText("Pause");
    }

    public void pause() {
        currentMediaPlayer.pause();
        playButton.setText("Play");
    }

    public void stop() {
        currentMediaPlayer.stop();
        playButton.setText("Play");
    }

    public void setViewController(ViewController viewController) {
        this.viewController = viewController;
    }
}
