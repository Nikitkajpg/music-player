package com.td.player.controllers;

import com.td.player.elements.Music;
import com.td.player.elements.Playlist;
import com.td.player.managers.MusicManager;
import com.td.player.managers.PlaylistManager;
import com.td.player.util.MusicTimer;
import javafx.application.Platform;
import javafx.util.Duration;

@SuppressWarnings("FieldMayBeFinal")
public class MediaController {
    private Controller controller;
    private MusicManager musicManager;
    private PlaylistManager playlistManager;

    private Music currentMusic;
    private Playlist currentPlayList;

    private Duration duration;
    private boolean paused = false;

    public MediaController(Controller controller) {
        this.controller = controller;
        musicManager = controller.getMusicManager();
        playlistManager = controller.getPlaylistManager();
        addTimeSliderListener();
        addVolumeSliderListener();

        currentPlayList = playlistManager.getDefaultPlaylist();
        currentMusic = currentPlayList.getMusicArray().get(0);
    }

    public void play() {
        switch (controller.mode) {
            case DEFAULT -> playInDefault();
            case PREFERENCE -> playInPreference();
            case RANDOM -> playInRandom();
        }
    }

    private void playInDefault() {
        if (paused) {
            commonPart(false, currentMusic);
        } else {
            playMusicInPlaylist(playlistManager.getDefaultPlaylist().getMusicArray().get(0), playlistManager.getDefaultPlaylist());
        }
        paused = false;
    }

    private void playInPreference() {
        Playlist preferencePlaylist = playlistManager.createPreferencePlaylist(musicManager);
        playMusicInPlaylist(preferencePlaylist.getMusicArray().get(0), preferencePlaylist);
        paused = false;
    }

    private void playInRandom() {
        int x = (int) (Math.random() * (musicManager.getMusicArray().size() - 1));
        Playlist playlist = playlistManager.getDefaultPlaylist();
        playMusicInPlaylist(playlist.getMusicArray().get(x), playlist);
        paused = false;
    }

    public void playMusicInPlaylist(Music music, Playlist playlist) {
        Music previousMusic = currentMusic;
        currentMusic.getMediaPlayer().stop();
        currentPlayList = playlist;
        currentMusic = music;
        commonPart(true, previousMusic);
    }

    public void pause() {
        MusicTimer.setFlag(false, currentMusic);
        currentMusic.getMediaPlayer().pause();
        paused = true;
        controller.playButton.setText("Play");
    }

    public void switchMusic(boolean next) {
        Music previousMusic = currentMusic;
        currentMusic.getMediaPlayer().stop();
        if (next) {
            currentMusic = currentPlayList.getNext(currentMusic);
        } else {
            currentMusic = currentPlayList.getPrevious(currentMusic);
        }
        commonPart(true, previousMusic);
    }

    private void commonPart(boolean turningNewMusic, Music previousMusic) {
        MusicTimer.setFlag(turningNewMusic, previousMusic);
        currentMusic.getMediaPlayer().play();
        currentMusic.getMediaPlayer().setOnEndOfMedia(() -> switchMusic(true));

        duration = currentMusic.getMediaPlayer().getMedia().getDuration();
        controller.endTimeLabel.setText(getTotalTime(duration));
        controller.playButton.setText("Pause");
        controller.titleLabel.setText(currentMusic.getTitle());
        controller.artistLabel.setText(currentMusic.getArtist());

        addMediaPlayerListener();
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
