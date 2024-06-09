package com.td.player.controllers.view;

import com.td.player.Player;
import com.td.player.controllers.Controller;
import com.td.player.elements.Directory;
import com.td.player.elements.ParentElement;
import com.td.player.elements.Playlist;
import com.td.player.elements.Track;
import com.td.player.util.Mode;
import com.td.player.util.Util;
import javafx.application.Platform;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Objects;

@SuppressWarnings("FieldMayBeFinal")
public class ViewController {
    private Controller controller;
    protected ContextMenuController contextMenuController;
    private double xOffset = 0;
    private double yOffset = 0;

    public ViewController(Controller controller) {
        this.controller = controller;
        contextMenuController = new ContextMenuController(controller);
        updateView();
    }

    public void updateView() {
        updateDirectories();
        updatePlaylists();
    }

    private void updateDirectories() {
        controller.directoriesVBox.getChildren().clear();
        for (Directory directory : controller.getDirectoryManager().getDirectories()) {
            controller.directoriesVBox.getChildren().add(getDirectoryView(directory));
        }
    }

    private ParentElementView getDirectoryView(Directory directory) {
        ParentElementView parentElementView = new ParentElementView(directory, false, controller);
        parentElementView.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                Util.openDirectoryInExplorer(directory.getPath());
            } else if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                showTracks(controller.directoriesVBox, parentElementView, directory);
            }
        });
        return parentElementView;
    }

    private void updatePlaylists() {
        controller.playlistsVBox.getChildren().clear();
        for (Playlist playlist : controller.getPlaylistManager().getPlaylists()) {
            controller.playlistsVBox.getChildren().add(getPlaylistView(playlist));
        }
    }

    private ParentElementView getPlaylistView(Playlist playlist) {
        ParentElementView parentElementView = new ParentElementView(playlist, true, controller);
        parentElementView.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                showTracks(controller.playlistsVBox, parentElementView, playlist);
            }
        });
        return parentElementView;
    }

    private void showTracks(VBox parentElementsVBox, ParentElementView parentElementView, ParentElement currentParentElement) {
        int vBoxId = getVBoxId(parentElementsVBox);
        parentElementsVBox.getChildren().removeIf(node -> node.getClass().equals(VBox.class));
        int buttonId = parentElementsVBox.getChildren().indexOf(parentElementView);
        if (vBoxId - 1 != buttonId) {
            showTracksWithId(parentElementsVBox, currentParentElement, buttonId);
        }
    }

    private int getVBoxId(VBox parentElementVBox) {
        for (int i = 0; i < parentElementVBox.getChildren().size(); i++) {
            if (parentElementVBox.getChildren().get(i).getClass().equals(VBox.class)) {
                return i;
            }
        }
        return -1;
    }

    private void showTracksWithId(VBox parentElementVBox, ParentElement currentParentElement, int buttonId) {
        parentElementVBox.getChildren().removeIf(node -> node.getClass().equals(VBox.class));
        VBox tracksVBox = new VBox();
        tracksVBox.setSpacing(10);
        addTrackView(currentParentElement, tracksVBox);
        parentElementVBox.getChildren().add(buttonId + 1, tracksVBox);
    }

    private void addTrackView(ParentElement currentParentElement, VBox tracksVBox) {
        for (Track track : currentParentElement.getTracks()) {
            TrackView trackView;
            trackView = new TrackView(track.getId(), track, currentParentElement, controller);
            trackView.setOnMouseClicked(mouseEvent -> actionWithTrackView(currentParentElement, mouseEvent, trackView, track));
            tracksVBox.getChildren().add(trackView);
        }
    }

    public void actionWithTrackView(ParentElement currentParentElement, MouseEvent mouseEvent, TrackView trackView, Track track) {
        if (Util.currentTrackView != null) {
            Util.currentTrackView.setHighlighted(false);
        }
        Util.currentTrackView = trackView;
        Util.currentTrackView.setHighlighted(true);
        if (currentParentElement instanceof Directory) {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                controller.getMediaController().playTrackInPlaylist(track, controller.getPlaylistManager().getPlaylists().get(0));
            }
        } else if (currentParentElement instanceof Playlist) {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                controller.getMediaController().playTrackInPlaylist(track, (Playlist) currentParentElement);
            }
        }
        if (mouseEvent.getButton() == MouseButton.SECONDARY) {
            contextMenuController.showTrackCM(trackView, track, mouseEvent);
        }
    }

    public void clickingOnPreference() {
        if (controller.preferenceToggleButton.isSelected()) {
            enableMode(true, Mode.PREFERENCE);
        } else {
            enableMode(false, Mode.DEFAULT);
        }
    }

    public void clickingOnRandom() {
        if (controller.randomToggleButton.isSelected()) {
            enableMode(true, Mode.RANDOM);
        } else {
            enableMode(false, Mode.DEFAULT);
        }
    }

    private void enableMode(boolean disable, Mode newMode) {
        controller.splitPane.setDisable(disable);
        controller.getMediaController().setCurrentMode(newMode);
        controller.getMediaController().playInMode();
    }

    public void dragStage(Stage stage) {
        controller.topMenuBorderPane.setOnMousePressed(mouseEvent -> {
            xOffset = mouseEvent.getSceneX();
            yOffset = mouseEvent.getSceneY();
        });
        controller.topMenuBorderPane.setOnMouseDragged(mouseEvent -> {
            stage.setX(mouseEvent.getScreenX() - xOffset);
            stage.setY(mouseEvent.getScreenY() - yOffset);
        });
    }

    public void changeToPause() {
        controller.playButton.setGraphic(new ImageView(
                new Image(Objects.requireNonNull(Player.class.getResource("img/pause.png")).toExternalForm())));
    }

    public void changeToPlay() {
        controller.playButton.setGraphic(new ImageView(
                new Image(Objects.requireNonNull(Player.class.getResource("img/play.png")).toExternalForm())));
    }

    public void addPlaylist() {
        controller.addPlaylistHBox.getChildren().remove(controller.addPlaylistHBox.getChildren().size() - 1);
        TextField textField = new TextField();
        textField.setPromptText("Playlist name...");
        textField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                String playlistName = textField.getText();
                if (playlistName != null && !playlistName.equals("") && controller.getPlaylistManager().isUnique(playlistName)) {
                    controller.getViewController().createPlaylistNameButton(playlistName);
                    controller.addPlaylistHBox.getChildren().remove(textField);
                    controller.addPlaylistHBox.getChildren().add(controller.playlistsLabel);
                }
            }
        });
        controller.addPlaylistHBox.getChildren().add(textField);
    }

    public void createPlaylistNameButton(String playlistName) {
        Playlist playlist = controller.getPlaylistManager().getNewPlaylist(playlistName);
        ParentElementView parentElementView = new ParentElementView(playlist, true, controller);
        parentElementView.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                showTracks(controller.playlistsVBox, parentElementView, playlist);
            }
        });
        parentElementView.prefWidthProperty().bind(controller.playlistsScrollPane.widthProperty().subtract(17));
        controller.playlistsVBox.getChildren().add(parentElementView);
    }

    /**
     * Метод для установки громкости музыки через перемещение ползунка
     */
    public void addVolumeSliderListener(Track currentTrack) {
        controller.volumeSlider.setValue(controller.volumeSlider.getMax());
        controller.volumeSlider.valueProperty().addListener(observable -> {
            if (controller.volumeSlider.isValueChanging()) {
                currentTrack.getMediaPlayer().setVolume(controller.volumeSlider.getValue() / 100);
            }
        });
    }

    /**
     * Метод для перемотки музыки на положение курсора при удержании ползунка.
     */
    public void addTimeSliderListener(Track currentTrack, Duration duration) {
        controller.timeSlider.valueProperty().addListener(observable -> {
            if (controller.timeSlider.isValueChanging() && duration != null) {
                controller.timeSlider.setOnMouseReleased(mouseEvent ->
                        currentTrack.getMediaPlayer().seek(duration.multiply(controller.timeSlider.getValue() / 100)));
            }
        });
    }

    /**
     * Метод для управления дорожкой музыки {@link Controller#timeSlider} и
     * дорожкой громкости {@link Controller#volumeSlider}.
     * <p>1. При нажатии на {@link Controller#timeSlider} музыка воспроизводится с точки нажатия,
     * при нажатии на {@link Controller#volumeSlider} громкость устанавливается согласно ползунку.
     * <p>2. Создается отдельный поток для положения ползунка на музыкальной дорожке и дорожке громкости,
     * ведется подсчет текущего времени.
     */
    public void addMediaPlayerListener(Track currentTrack, Duration duration) {
        controller.timeSlider.setOnMousePressed(mouseEvent ->
                currentTrack.getMediaPlayer().seek(duration.multiply(controller.timeSlider.getValue() / 100)));
        controller.volumeSlider.setOnMousePressed(mouseEvent ->
                currentTrack.getMediaPlayer().setVolume(controller.volumeSlider.getValue() / 100));
        currentTrack.getMediaPlayer().currentTimeProperty().addListener(observable -> Platform.runLater(() -> {
            Duration currentTime = currentTrack.getMediaPlayer().getCurrentTime();
            if (!controller.timeSlider.isValueChanging()) {
                controller.timeSlider.setValue(currentTime.divide(duration.toMillis()).toMillis() * 100.0);
                controller.currentTimeLabel.setText(controller.getMediaController().getTotalTrackTime(currentTime));
            }
            if (!controller.volumeSlider.isValueChanging()) {
                controller.volumeSlider.setValue((int) Math.round(currentTrack.getMediaPlayer().getVolume() * 100));
            }
        }));
    }

    public void maximize() {
        if (Player.stage.isMaximized()) {
            controller.maximizeButton.setGraphic(new ImageView(Objects.requireNonNull(getClass().getResource("/com/td/player/img/maximize.png")).toExternalForm()));
            Player.stage.setMaximized(false);
        } else {
            controller.maximizeButton.setGraphic(new ImageView(Objects.requireNonNull(getClass().getResource("/com/td/player/img/downsize.png")).toExternalForm()));
            Player.stage.setMaximized(true);
        }
    }
}
