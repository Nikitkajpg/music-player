package com.td.player.controllers;

import com.td.player.elements.Playlist;
import com.td.player.elements.Track;
import com.td.player.util.TrackTimer;
import javafx.application.Platform;
import javafx.util.Duration;

/**
 * Класс для управления воспроизведением музыки
 */
@SuppressWarnings("FieldMayBeFinal")
public class MediaController {
    private Controller controller;

    private Track currentTrack;
    private Playlist currentPlayList;

    private Duration duration;
    private boolean paused = true;

    /**
     * Конструктор создает текущий плейлист {@link Playlist} и текущую песню {@link Track}
     */
    public MediaController(Controller controller) {
        this.controller = controller;
        addTimeSliderListener();
        addVolumeSliderListener();

        currentPlayList = controller.getPlaylistManager().getDefaultPlaylist();
        if (currentPlayList.getTrackArray().size() > 0) {
            currentTrack = currentPlayList.getTrackArray().get(0);
        }
    }

    public void playInMode() {
        switch (controller.mode) {
            case DEFAULT -> playInDefault();
            case PREFERENCE -> playInPreference();
            case RANDOM -> playInRandom();
        }
    }

    /**
     * Метод воспроизводит музыку в обычном режиме
     * <p>Если воспроизведение на паузе, проигрывается текущая песня.
     * Если в данный момент воспроизводится песня, проигрывается плейлист по умолчанию
     * (возможно только после запуска программы)
     */
    private void playInDefault() {
        if (paused) {
            playTrack(false, currentTrack);
        } else {
            Playlist defaultPlaylist = controller.getPlaylistManager().getDefaultPlaylist();
            playTrackInPlaylist(defaultPlaylist.getTrackArray().get(0), defaultPlaylist);
        }
        paused = false;
    }

    private void playInPreference() {
        Playlist preferencePlaylist = controller.getPlaylistManager().createPreferencePlaylist();
        playTrackInPlaylist(preferencePlaylist.getTrackArray().get(0), preferencePlaylist);
        paused = false;
    }

    private void playInRandom() {
        Playlist randomPlaylist = controller.getPlaylistManager().getRandomPlaylist();
        playTrackInPlaylist(randomPlaylist.getTrackArray().get(0), randomPlaylist);
        paused = false;
    }

    public void playTrackInPlaylist(Track currentTrack, Playlist currentPlaylist) {
        Track previousTrack = this.currentTrack;
        this.currentTrack.getMediaPlayer().stop();
        this.currentPlayList = currentPlaylist;
        this.currentTrack = currentTrack;
        playTrack(true, previousTrack);
    }

    public void pause() {
        TrackTimer.setFlag(false, currentTrack);
        currentTrack.getMediaPlayer().pause();
        paused = true;
    }

    public void switchTrack(boolean nextTrack) {
        Track previousTrack = currentTrack;
        currentTrack.getMediaPlayer().stop();
        if (nextTrack) {
            currentTrack = currentPlayList.getNext(currentTrack);
        } else {
            currentTrack = currentPlayList.getPrevious(currentTrack);
        }
        playTrack(true, previousTrack);
    }

    /**
     * Метод, содержащий общие действия для запуска музыки.
     * <p>Проигрывание музыки, обновление текста для {@link javafx.scene.control.Label}
     *
     * @param turningNewTrack флаг, true - была включена новая песня, false - снята с паузы текущая песня
     */
    private void playTrack(boolean turningNewTrack, Track previousTrack) {
        TrackTimer.setFlag(turningNewTrack, previousTrack);
        currentTrack.getMediaPlayer().play();
        currentTrack.getMediaPlayer().setOnEndOfMedia(() -> switchTrack(true));

        duration = currentTrack.getMediaPlayer().getMedia().getDuration();
        controller.endTimeLabel.setText(getTotalTrackTime(duration));
        controller.titleLabel.setText(currentTrack.getTitle() + " - " + currentTrack.getArtist());

        addMediaPlayerListener();
    }

    private String getTotalTrackTime(Duration duration) {
        int seconds = (int) duration.toSeconds();
        String time;
        if (seconds / 3600 < 1) {
            time = String.format("%02d:%02d", (seconds % 3600) / 60, seconds % 60);
        } else {
            time = String.format("%2d:%02d:%02d", seconds / 3600, (seconds % 3600) / 60, seconds % 60);
        }
        return time;
    }

    /**
     * Метод для управления дорожкой музыки {@link Controller#timeSlider} и
     * дорожкой громкости {@link Controller#volumeSlider}.
     * <p>1. При нажатии на {@link Controller#timeSlider} музыка воспроизводится с точки нажатия,
     * при нажатии на {@link Controller#volumeSlider} громкость устанавливается согласно ползунку.
     * <p>2. Создается отдельный поток для положения ползунка на музыкальной дорожке и дорожке громкости,
     * ведется подсчет текущего времени.
     */
    private void addMediaPlayerListener() {
        controller.timeSlider.setOnMousePressed(mouseEvent ->
                currentTrack.getMediaPlayer().seek(duration.multiply(controller.timeSlider.getValue() / 100)));
        controller.volumeSlider.setOnMousePressed(mouseEvent ->
                currentTrack.getMediaPlayer().setVolume(controller.volumeSlider.getValue() / 100));
        currentTrack.getMediaPlayer().currentTimeProperty().addListener(observable -> Platform.runLater(() -> {
            Duration currentTime = currentTrack.getMediaPlayer().getCurrentTime();
            if (!controller.timeSlider.isValueChanging()) {
                controller.timeSlider.setValue(currentTime.divide(duration.toMillis()).toMillis() * 100.0);
                controller.currentTimeLabel.setText(getTotalTrackTime(currentTime));
            }
            if (!controller.volumeSlider.isValueChanging()) {
                controller.volumeSlider.setValue((int) Math.round(currentTrack.getMediaPlayer().getVolume() * 100));
            }
        }));
    }

    /**
     * Метод для перемотки музыки на положение курсора при удержании ползунка.
     */
    private void addTimeSliderListener() {
        controller.timeSlider.valueProperty().addListener(observable -> {
            if (controller.timeSlider.isValueChanging() && duration != null) {
                controller.timeSlider.setOnMouseReleased(mouseEvent ->
                        currentTrack.getMediaPlayer().seek(duration.multiply(controller.timeSlider.getValue() / 100)));
            }
        });
    }

    /**
     * Метод для установки громкости музыки через перемещение ползунка
     */
    private void addVolumeSliderListener() {
        controller.volumeSlider.setValue(controller.volumeSlider.getMax());
        controller.volumeSlider.valueProperty().addListener(observable -> {
            if (controller.volumeSlider.isValueChanging()) {
                currentTrack.getMediaPlayer().setVolume(controller.volumeSlider.getValue() / 100);
            }
        });
    }

    public boolean isPaused() {
        return paused;
    }
}
