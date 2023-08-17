package com.td.player.controllers;

import com.td.player.controllers.ContextMenuController;
import com.td.player.controllers.Controller;
import com.td.player.controllers.MediaController;
import com.td.player.elements.Directory;
import com.td.player.elements.Music;
import com.td.player.elements.Playlist;
import com.td.player.managers.MusicManager;
import com.td.player.managers.PlaylistManager;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.*;
import javafx.scene.layout.VBox;

@SuppressWarnings("FieldMayBeFinal")
public class ViewController {
    private Button addPlaylistButton;
    private Button renamePlaylistButton;
    private Button currentButton = new Button();
    private Slider musicSlider;
    private VBox playlistNamesVBox, playlistMusicVBox;

    private Controller controller;
    private MusicManager musicManager;
    private PlaylistManager playlistManager;
    private ContextMenuController contextMenuController;

    private MediaController mediaController;

    public ViewController(Controller controller) {
        this.controller = controller;
        musicManager = controller.getMusicManager();
        playlistManager = controller.getPlaylistManager();
        mediaController = controller.getMediaController();
        addPlaylistButton = controller.getAddPlaylistButton();
        renamePlaylistButton = controller.getRenamePlaylistButton();
        musicSlider = controller.getTimeSlider();
        playlistNamesVBox = controller.getPlaylistNamesVBox();
        playlistMusicVBox = controller.getPlaylistMusicVBox();
        contextMenuController = new ContextMenuController(
                controller.getDirectoryManager(), musicManager, playlistManager, mediaController);
        update();
    }

    public void update() {
        updateDirs();
        updateMusic();
        updatePlaylists();
    }

    public void updateDirs() {
        controller.getDirsListVBox().getChildren().clear();
        for (Directory directory : controller.getDirectoryManager().getDirectoryArray()) {
            controller.getDirsListVBox().getChildren().add(getDirLabel(directory.getPath()));
        }
    }

    public void updateMusic() {
        controller.getMusicListVBox().getChildren().clear();
        for (Music music : musicManager.getMusicArray()) {
            controller.getMusicListVBox().getChildren().add(getMusicLabel(music.getFileName()));
        }
    }

    public void updatePlaylists() {
        playlistNamesVBox.getChildren().clear();
        playlistMusicVBox.getChildren().clear();

        for (Playlist playlist : playlistManager.getPlaylistArray()) {
            Button button = new Button(playlist.getName());

            dragAndDrop(playlist, button);

            button.setOnMouseClicked(mouseEvent -> actionForPlaylistLabel(mouseEvent, button));
            actionForShowingPlaylist(button, playlist);
            playlistNamesVBox.getChildren().add(button);
        }
    }

    public void removeSongFromPlaylist(Label label) {
        for (int i = 0; i < playlistMusicVBox.getChildren().size() - 1; i++) {
            VBox vBox = (VBox) playlistMusicVBox.getChildren().get(i);
            Label musicLabel = (Label) vBox.getChildren().get(0);
            if (musicLabel.getText().equals(label.getText())) {
                playlistMusicVBox.getChildren().remove(vBox);
            }
        }
    }

    public void createPlaylistNameButton(String playlistName) {
        Button button = new Button(playlistName);
        playlistManager.createPlaylist(playlistName);

        dragAndDrop(playlistManager.getLastPlaylist(), button);

        button.setOnMouseClicked(mouseEvent -> {
            if (playlistMusicVBox.getChildren().isEmpty()) {
                actionForPlaylistLabel(mouseEvent, button);
            }
        });
        actionForShowingPlaylist(button, playlistManager.getPlaylistByName(playlistName));
        playlistNamesVBox.getChildren().add(button);
    }

    private void actionForPlaylistLabel(MouseEvent mouseEvent, Button button) {
        if (mouseEvent.getButton() == MouseButton.SECONDARY) {
            contextMenuController.showPlaylistButtonCM(button, mouseEvent, this);
        }
    }

    private void actionForShowingPlaylist(Button button, Playlist playlist) {
        button.setOnAction(actionEvent -> {
            playlistMusicVBox.getChildren().clear();
            for (Music music : playlist.getMusicArray()) {
                VBox vBox = new VBox();
                Label titleLabel = getPlaylistSongLabel(music.getTitle(), playlist, button);
                Label artistLabel = new Label(music.getArtist());
                vBox.getChildren().addAll(titleLabel, artistLabel);
                playlistMusicVBox.getChildren().add(vBox);
            }
            playlistMusicVBox.getChildren().add(new Label("Put song here..."));
        });
    }

    private Label getDirLabel(String path) {
        Label label = new Label(path);
        label.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                contextMenuController.showDirCM(label, mouseEvent, this);
            }
        });
        return label;
    }

    private Label getMusicLabel(String name) {
        Label label = new Label(name);
        label.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                if (mouseEvent.getClickCount() == 2) {
                    mediaController.playByName(name);
                }
            } else if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                contextMenuController.showMusicCM(label, mouseEvent);
            }
        });
        label.setOnDragDetected(mouseEvent -> {
            // drag was detected, start drag-and-drop gesture
            // allow any transfer mode
            Dragboard dragboard = label.startDragAndDrop(TransferMode.ANY);
            // put a string on dragBoard
            ClipboardContent clipboardContent = new ClipboardContent();
            clipboardContent.putString(label.getText());
            dragboard.setContent(clipboardContent);
            mouseEvent.consume();
        });
        return label;
    }

    private Label getPlaylistSongLabel(String fileName, Playlist playlist, Button button) {
        Label label = new Label(fileName);
        label.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                contextMenuController.showPlaylistSongLabelCM(playlist, label, button, mouseEvent, this);
            } else if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                if (mouseEvent.getClickCount() == 2) {
                    mediaController.playInPlaylist(label.getText(), playlist.getName());
                }
            }
        });
        return label;
    }

    public void renameTitledPane(String playlistName) {
        playlistManager.renamePlaylist(currentButton.getText(), playlistName);
        currentButton.setText(playlistName);
        renamePlaylistButton.setDisable(true);
        addPlaylistButton.setDisable(false);
    }

    private void dragAndDrop(Playlist playlist, Button button) {
        playlistMusicVBox.setOnDragOver(dragEvent -> {
            // data is dragged over the target
            // accept it only if it is  not dragged from the same node and if it has a string data
            if (dragEvent.getGestureSource() != playlistMusicVBox && dragEvent.getDragboard().hasString()) {
                // allow for both copying and moving, whatever user chooses
                dragEvent.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            dragEvent.consume();
        });
        playlistMusicVBox.setOnDragEntered(dragEvent -> {
            // the drag-and-drop gesture entered the target
            // show to the user that it is an actual gesture target
            if (dragEvent.getGestureSource() != playlistMusicVBox && dragEvent.getDragboard().hasString()) {
//                vBox.setStyle("-fx-background-color: green");
            }
            dragEvent.consume();
        });
        playlistMusicVBox.setOnDragExited(dragEvent -> {
            // mouse moved away, remove the graphical cues
//            vBox.setStyle("-fx-background-color: #333333");
            dragEvent.consume();
        });
        playlistMusicVBox.setOnDragDropped(dragEvent -> {
            // data dropped
            // if there is a string data on dragBoard, read it and use it
            Dragboard dragboard = dragEvent.getDragboard();
            boolean success = false;
            if (dragboard.hasString() && playlistManager.isUniqueInPlaylist(playlist, dragboard.getString())) {
                VBox labelsVBox = new VBox();
                labelsVBox.getChildren().addAll(getPlaylistSongLabel(dragboard.getString(), playlist, button),
                        new Label(musicManager.get(dragboard.getString()).getArtist()));
                playlistMusicVBox.getChildren().add(playlistMusicVBox.getChildren().size() - 1, labelsVBox);
                playlist.addByName(dragboard.getString(), musicManager);
                success = true;
            }
            // let the source know whether the string was successfully transferred and used
            dragEvent.setDropCompleted(success);
            dragEvent.consume();
        });
    }

    public void methodToRename(boolean renameDisable, boolean addDisable, Button button) {
        renamePlaylistButton.setDisable(renameDisable);
        addPlaylistButton.setDisable(addDisable);
        controller.getTextField().requestFocus();
        currentButton = button;
    }
}
