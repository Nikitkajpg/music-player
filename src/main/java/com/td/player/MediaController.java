package com.td.player;

import javafx.scene.control.Button;
import javafx.scene.media.MediaPlayer;

@SuppressWarnings("FieldMayBeFinal")
public class MediaController {
    private MusicController musicController;

    private MediaPlayer currentMediaPlayer;
    private Button playButton;

    public MediaController(MusicController musicController, Button playButton) {
        this.musicController = musicController;
        this.playButton = playButton;
    }

    public void play() {
        currentMediaPlayer.play();
        playButton.setText("Pause");
    }

    public void playByName(String name) {
        Music music = musicController.getMusicByName(name);
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
