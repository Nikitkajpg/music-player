package com.td.player.controllers;

import com.td.player.elements.Playlist;
import com.td.player.elements.Track;
import com.td.player.util.Mode;
import com.td.player.util.TrackTimer;
import com.td.player.util.Util;
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

    private Mode currentMode = Mode.DEFAULT;
    private Duration duration;
    private boolean paused = true;

    public MediaController(Controller controller) {
        this.controller = controller;
        controller.getViewController().addTimeSliderListener(currentTrack, duration);
        controller.getViewController().addVolumeSliderListener(currentTrack);
        defineDefaultPlaylist();
    }

    private void defineDefaultPlaylist() {
        currentPlayList = controller.getPlaylistManager().getDefaultPlaylist();
        if (currentPlayList.getTracks().size() > 0) {
            currentTrack = currentPlayList.getTracks().get(0);
        }
    }

    public void play() {
        if (!controller.getDirectoryManager().noTracks()) {
            if (isPaused()) {
                playInMode();
            } else {
                pause();
            }
        }
    }

    public void playInMode() {
        switch (currentMode) {
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
            playTrackInPlaylist(defaultPlaylist.getTracks().get(0), defaultPlaylist);
        }
        paused = false;
    }

    private void playInPreference() {
        Playlist preferencePlaylist = controller.getPlaylistManager().createPreferencePlaylist();
        playTrackInPlaylist(preferencePlaylist.getTracks().get(0), preferencePlaylist);
        paused = false;
    }

    private void playInRandom() {
        Playlist randomPlaylist = controller.getPlaylistManager().getRandomPlaylist();
        playTrackInPlaylist(randomPlaylist.getTracks().get(0), randomPlaylist);
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
        controller.getViewController().changeToPlay();
        TrackTimer.setFlag(false, currentTrack);
        currentTrack.getMediaPlayer().pause();
        paused = true;
    }

    public void switchTrack(boolean nextTrack) {
        Track previousTrack = currentTrack;
        currentTrack.getMediaPlayer().stop();
        if (nextTrack) {
            currentTrack = currentPlayList.getNextTrack(currentTrack);
        } else {
            currentTrack = currentPlayList.getPreviousTrack(currentTrack);
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
        if (currentTrack == null) {
            currentTrack = controller.getDirectoryManager().getTrackById(1);
        }
        controller.getViewController().changeToPause();
        TrackTimer.setFlag(turningNewTrack, previousTrack);
        currentTrack.getMediaPlayer().play();
        currentTrack.getMediaPlayer().setOnEndOfMedia(() -> switchTrack(true));

        duration = currentTrack.getMediaPlayer().getMedia().getDuration();
        controller.endTimeLabel.setText(getTotalTrackTime(duration));
        controller.titleLabel.setText(currentTrack.getArtist() + " - " + currentTrack.getTitle());

        controller.getViewController().addMediaPlayerListener(currentTrack, duration);
    }

    public String getTotalTrackTime(Duration duration) {
        return Util.getTime((int) duration.toSeconds());
    }

    public boolean isPaused() {
        return paused;
    }

    public void setCurrentMode(Mode currentMode) {
        this.currentMode = currentMode;
    }
}
