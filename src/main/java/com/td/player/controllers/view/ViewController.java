package com.td.player.controllers.view;

import com.td.player.Player;
import com.td.player.controllers.Controller;
import com.td.player.elements.Directory;
import com.td.player.elements.ParentElement;
import com.td.player.elements.Playlist;
import com.td.player.elements.Track;
import com.td.player.util.DragEventUtil;
import com.td.player.util.Mode;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

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
            controller.directoriesVBox.getChildren().add(getDirectoryButton(directory));
        }
    }

    private Button getDirectoryButton(Directory directory) {
        Button button = new Button(directory.getName());
        button.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
//                contextMenuController.showDirectoryCM(directory, button, mouseEvent);
//                openDirectoryInExplorer(button);
            } else if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                showTracks(controller.directoriesVBox, button, directory);
            }
        });
        return button;
    }

    public void openDirectoryInExplorer(Button pressedButtonWithDirectoryPath) {
        File directory = new File(pressedButtonWithDirectoryPath.getText());
        try {
            Desktop.getDesktop().open(directory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updatePlaylists() {
        controller.playlistsVBox.getChildren().clear();
        for (Playlist playlist : controller.getPlaylistManager().getPlaylists()) {
            controller.playlistsVBox.getChildren().add(getPlaylistButton(playlist));
        }
    }

    private Button getPlaylistButton(Playlist playlist) {
        Button button = new Button(playlist.getName());
        button.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                contextMenuController.showPlaylistButtonCM(playlist, button, mouseEvent);
            } else if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                showTracks(controller.playlistsVBox, button, playlist);
            }
        });
        return button;
    }

    private void showTracks(VBox parentElementsVBox, Button pressedButton, ParentElement currentParentElement) {
        int buttonId = parentElementsVBox.getChildren().indexOf(pressedButton);
        int vBoxId = getVBoxId(parentElementsVBox);
        parentElementsVBox.getChildren().removeIf(node -> node.getClass().equals(VBox.class));
        if (vBoxId != -1 && vBoxId != buttonId + 1) {
            showTracksWithId(parentElementsVBox, currentParentElement, buttonId);
        } else if (vBoxId != -1 && vBoxId == buttonId + 1) {
        } else {
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
        if (currentParentElement instanceof Playlist) {
            dragAndDrop(tracksVBox, (Playlist) currentParentElement);
        }
        addTrackButtons(currentParentElement, tracksVBox);
        if (parentElementVBox.getChildren().size() <= buttonId + 1
                && parentElementVBox.getChildren().size() != 1) {
            parentElementVBox.getChildren().add(buttonId, tracksVBox);
        } else {
            parentElementVBox.getChildren().add(buttonId + 1, tracksVBox);
        }
    }

    private void addTrackButtons(ParentElement currentParentElement, VBox tracksVBox) {
        for (Track track : currentParentElement.getTracks()) {
            HBox trackHBox = new HBox();
            Button trackButton = new Button(track.getArtist() + " - " + track.getTitle());
            trackButton.setOnMouseClicked(mouseEvent -> actionWithTrackButton(currentParentElement, mouseEvent, trackButton, track));
            if (currentParentElement instanceof Directory) {
                trackButton.setOnDragDetected(mouseEvent -> DragEventUtil.onDragDetected(mouseEvent, trackButton));
            }
            trackHBox.getChildren().add(trackButton);
            tracksVBox.getChildren().add(trackHBox);
        }
    }

    public void actionWithTrackButton(ParentElement currentParentElement, MouseEvent mouseEvent, Button trackButton, Track track) {
        if (currentParentElement instanceof Directory) {
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                contextMenuController.showDirectoryTrackButtonCM((Directory) currentParentElement, trackButton, track, mouseEvent);
            } else {
                controller.getMediaController().playTrack(track);
            }
        } else if (currentParentElement instanceof Playlist) {
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                contextMenuController.showPlaylistTrackButtonCM((Playlist) currentParentElement, trackButton, track, mouseEvent);
            } else {
                controller.getMediaController().playTrackInPlaylist(track, (Playlist) currentParentElement);
            }
        }
    }

    private void dragAndDrop(VBox trackVBox, Playlist playlist) {
        trackVBox.setOnDragOver(dragEvent ->
                DragEventUtil.onDragOver(dragEvent, trackVBox, playlist));
        trackVBox.setOnDragEntered(dragEvent ->
                DragEventUtil.onDragEntered(dragEvent, trackVBox, playlist));
        trackVBox.setOnDragExited(dragEvent ->
                DragEventUtil.onDragExited(dragEvent, trackVBox, playlist));
        trackVBox.setOnDragDropped(dragEvent ->
                DragEventUtil.onDragDropped(controller, dragEvent, playlist, trackVBox));
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
        Button button = new Button(playlistName);
        Playlist playlist = controller.getPlaylistManager().getNewPlaylist(playlistName);
        button.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
//                contextMenuController.showPlaylistButtonCM(playlist, button, mouseEvent);
            } else if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                showTracks(controller.playlistsVBox, button, playlist);
            }
        });
//        actionForShowingPlaylist(button, playlist);
        button.prefWidthProperty().bind(controller.playlistsScrollPane.widthProperty().subtract(17));
        controller.playlistsVBox.getChildren().add(button);
    }
}
