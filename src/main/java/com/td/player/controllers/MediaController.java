package com.td.player.controllers;

import com.td.player.elements.Music;
import com.td.player.managers.MusicManager;
import javafx.scene.control.Button;
import javafx.scene.media.MediaPlayer;

@SuppressWarnings("FieldMayBeFinal")
public class MediaController {
    private MusicManager musicManager;

    private MediaPlayer currentMediaPlayer;
    private Button playButton;

    public MediaController(MusicManager musicManager, Button playButton) {
        this.musicManager = musicManager;
        this.playButton = playButton;
    }

    public void play() {
        currentMediaPlayer.play();
        playButton.setText("Pause");
    }

    public void playByName(String name) {
        Music music = musicManager.get(name);
        currentMediaPlayer = music.getMediaPlayer();
        currentMediaPlayer.setOnEndOfMedia(() -> {
            //todo
        });
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

    public MediaPlayer getCurrentMediaPlayer() {
        return currentMediaPlayer;
    }
}
