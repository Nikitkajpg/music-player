package com.td.player.controllers;

import com.td.player.elements.Music;
import com.td.player.elements.Playlist;
import com.td.player.managers.MusicManager;
import com.td.player.managers.PlaylistManager;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.util.Duration;

@SuppressWarnings("FieldMayBeFinal")
public class MediaController {
    private MusicManager musicManager;
    private PlaylistManager playlistManager;

    private Music currentMusic;
    private Playlist currentPlayList;
    private Button playButton;

    private Controller controller;
    private Duration duration;

    public MediaController(Controller controller) {
        this.controller = controller;
        musicManager = controller.getMusicManager();
        playlistManager = controller.getPlaylistManager();
        playButton = controller.playButton;
        addTimeSliderListener();
        addVolumeSliderListener();
    }

    public void play() {
        if (currentMusic == null && currentPlayList == null) {
            currentPlayList = playlistManager.getDefaultPlaylist();
            if (currentPlayList.getMusicArray().size() > 0) {
                currentMusic = currentPlayList.getMusicArray().get(0);
                currentMusic.getMediaPlayer().play();
                currentMusic.getMediaPlayer().setOnEndOfMedia(() -> switchMusic(true));
                duration = currentMusic.getMediaPlayer().getMedia().getDuration();

                controller.endTimeLabel.setText(getTotalTime(duration));
                controller.titleLabel.setText(currentMusic.getTitle());
                controller.artistLabel.setText(currentMusic.getArtist());

                addMediaPlayerListener();
            }
        } else if (currentMusic != null) {
            currentMusic.getMediaPlayer().play();
        }
        playButton.setText("Pause");

    }

    private String getTotalTime(Duration duration) {
        int seconds = (int) duration.toSeconds();
        String time;
        if (seconds / 3600 < 1) {
            time = String.format("%02d:%02d", (seconds % 3600) / 60, seconds % 60);
        } else {
            time = String.format("%2d:%02d:%02d", seconds / 3600, (seconds % 3600) / 60, seconds % 60);
        }
        return time;
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
        methodToPlay();
    }

    public void playInPlaylist(String musicName, String playlistName) {
        if (currentMusic != null) {
            currentMusic.getMediaPlayer().stop();
        }
        currentPlayList = playlistManager.getPlaylistByName(playlistName);
        currentMusic = musicManager.getByTitle(musicName);
        methodToPlay();
    }

    public void playByName(String name) {
        if (currentMusic != null) {
            currentMusic.getMediaPlayer().stop();
        }
        currentPlayList = playlistManager.getDefaultPlaylist();
        currentMusic = musicManager.get(name);
        methodToPlay();
    }

    public void switchMusic(boolean next) {
        if (currentMusic != null) {
            currentMusic.getMediaPlayer().stop();
            if (next) {
                currentMusic = currentPlayList.getNext(currentMusic);
            } else {
                currentMusic = currentPlayList.getPrevious(currentMusic);
            }
            methodToPlay();
        }
    }

    private void methodToPlay() {
        currentMusic.getMediaPlayer().play();
        currentMusic.getMediaPlayer().setOnEndOfMedia(() -> switchMusic(true));
        duration = currentMusic.getMediaPlayer().getMedia().getDuration();
        controller.endTimeLabel.setText(getTotalTime(duration));
        addMediaPlayerListener();
        playButton.setText("Pause");
        controller.titleLabel.setText(currentMusic.getTitle());
        controller.artistLabel.setText(currentMusic.getArtist());
    }

    private void addMediaPlayerListener() {
        controller.timeSlider.setOnMousePressed(mouseEvent -> currentMusic.getMediaPlayer()
                .seek(duration.multiply(controller.timeSlider.getValue() / 100)));
        currentMusic.getMediaPlayer().currentTimeProperty().addListener(
                observable -> Platform.runLater(() -> {
                            Duration currentTime = currentMusic.getMediaPlayer().getCurrentTime();
                            if (!controller.timeSlider.isValueChanging()) {
                                controller.timeSlider.setValue(currentTime.divide(duration.toMillis()).toMillis() * 100.0);
                                controller.currentTimeLabel.setText(getTotalTime(currentTime));
                            }
                            if (!controller.volumeSlider.isValueChanging()) {
                                controller.volumeSlider.setValue(
                                        (int) Math.round(currentMusic.getMediaPlayer().getVolume() * 100));
                            }
                        }
                )
        );
    }

    private void addTimeSliderListener() {
        controller.timeSlider.valueProperty().addListener(observable -> {
            if (controller.timeSlider.isValueChanging()) {
                controller.timeSlider.setOnMouseReleased(mouseEvent ->
                        currentMusic.getMediaPlayer().seek(duration.multiply(controller.timeSlider.getValue() / 100))
                );
            }
        });
    }

    private void addVolumeSliderListener() {
        controller.volumeSlider.valueProperty().addListener(observable -> {
            if (controller.volumeSlider.isValueChanging()) {
                currentMusic.getMediaPlayer().setVolume(controller.volumeSlider.getValue() / 100);
            }
        });
    }
}
