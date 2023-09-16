package com.td.player.controllers;

import com.td.player.elements.Music;
import com.td.player.elements.Playlist;
import com.td.player.managers.MusicManager;
import com.td.player.managers.PlaylistManager;
import com.td.player.util.MusicTimer;
import javafx.application.Platform;
import javafx.util.Duration;

/**
 * Класс для управления воспроизведением музыки
 */
@SuppressWarnings("FieldMayBeFinal")
public class MediaController {
    private Controller controller;
    private MusicManager musicManager;
    private PlaylistManager playlistManager;

    private Music currentMusic;
    private Playlist currentPlayList;

    private Duration duration;
    private boolean paused = true;

    /**
     * Конструктор создает текущий плейлист {@link Playlist} и текущую песню {@link Music}
     */
    public MediaController(Controller controller) {
        this.controller = controller;
        musicManager = controller.getMusicManager();
        playlistManager = controller.getPlaylistManager();
        addTimeSliderListener();
        addVolumeSliderListener();

        currentPlayList = playlistManager.getDefaultPlaylist();
        if (currentPlayList.getMusicArray().size() > 0) {
            currentMusic = currentPlayList.getMusicArray().get(0);
        }
    }

    /**
     * При нажатии на кнопку {@link Controller#playButton} воспроизводится
     * песня в соответсявии с выбранным режимом {@link com.td.player.util.Mode}
     */
    public void play() {
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
            commonPart(false, currentMusic);
        } else {
            playMusicInPlaylist(playlistManager.getDefaultPlaylist().getMusicArray().get(0), playlistManager.getDefaultPlaylist());
        }
        paused = false;
    }

    /**
     * Метод воспроизводит музыку в "режиме предпочтений"
     * <p>Создается новый плейлист, а затем проигрывается.
     */
    private void playInPreference() {
        Playlist preferencePlaylist = playlistManager.createPreferencePlaylist(musicManager);
        playMusicInPlaylist(preferencePlaylist.getMusicArray().get(0), preferencePlaylist);
        paused = false;
    }

    /**
     * Метод воспроизводит музыку в случайном режиме.
     * <p>Создается новый плейлист, а затем проигрывается
     */
    private void playInRandom() {
        Playlist playlist = playlistManager.getRandomPlaylist();
        playMusicInPlaylist(playlist.getMusicArray().get(0), playlist);
        paused = false;
    }

    /**
     * Метод для проигрывания музыки в плейлисте
     *
     * @param music    текущая песня
     * @param playlist текущий плейлист
     */
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
    }

    /**
     * Метод для переключения следующей или предыдущей музыки в текущем плейлисте
     *
     * @param next флаг, true - если включается следующая песня, false - если предыдущая
     */
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

    /**
     * Метод, содержащий общие действия для запуска музыки.
     * <p>Проигрывание музыки, обновление текста для {@link javafx.scene.control.Label}
     *
     * @param turningNewMusic флаг, true - была включена новая песня, false - снята с паузы текущая песня
     * @param previousMusic   предыдущая песня
     */
    private void commonPart(boolean turningNewMusic, Music previousMusic) {
        MusicTimer.setFlag(turningNewMusic, previousMusic);
        currentMusic.getMediaPlayer().play();
        currentMusic.getMediaPlayer().setOnEndOfMedia(() -> switchMusic(true));

        duration = currentMusic.getMediaPlayer().getMedia().getDuration();
        controller.endTimeLabel.setText(getTotalTime(duration));
        controller.titleLabel.setText(currentMusic.getTitle() + " - " + currentMusic.getArtist());

        addMediaPlayerListener();
    }

    /**
     * Метод для подсчета общего времени песни
     *
     * @param duration продолжительность
     * @return время в формате MM:SS или HH:MM:SS
     */
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

    /**
     * Метод для управления дорожкой музыки {@link Controller#timeSlider} и
     * дорожкой громкости {@link Controller#volumeSlider}.
     * <p>1. При нажатии на {@link Controller#timeSlider} музыка воспроизводится с точки нажатия.
     * <p>2. Создается отдельный поток для положения ползунка на музыкальной дорожке и дорожке громкости,
     * ведется подсчет текущего времени.
     */
    private void addMediaPlayerListener() {
        controller.timeSlider.setOnMousePressed(mouseEvent ->
                currentMusic.getMediaPlayer().seek(duration.multiply(controller.timeSlider.getValue() / 100)));
        currentMusic.getMediaPlayer().currentTimeProperty().addListener(observable -> Platform.runLater(() -> {
            Duration currentTime = currentMusic.getMediaPlayer().getCurrentTime();
            if (!controller.timeSlider.isValueChanging()) {
                controller.timeSlider.setValue(currentTime.divide(duration.toMillis()).toMillis() * 100.0);
                controller.currentTimeLabel.setText(getTotalTime(currentTime));
            }
            if (!controller.volumeSlider.isValueChanging()) {
                controller.volumeSlider.setValue((int) Math.round(currentMusic.getMediaPlayer().getVolume() * 100));
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
                        currentMusic.getMediaPlayer().seek(duration.multiply(controller.timeSlider.getValue() / 100)));
            }
        });
    }

    /**
     * Метод для установки громкости музыки через перемещение ползунка
     */
    private void addVolumeSliderListener() {
        controller.volumeSlider.valueProperty().addListener(observable -> {
            if (controller.volumeSlider.isValueChanging()) {
                currentMusic.getMediaPlayer().setVolume(controller.volumeSlider.getValue() / 100);
            }
        });
    }

    public boolean isPaused() {
        return paused;
    }
}
