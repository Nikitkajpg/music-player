package com.td.player.controllers;

import com.td.player.elements.Directory;
import com.td.player.elements.Music;
import com.td.player.elements.Playlist;
import com.td.player.managers.MusicManager;
import com.td.player.managers.PlaylistManager;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.VBox;

@SuppressWarnings("FieldMayBeFinal")
public class ViewController {
    private Accordion accordion;
    private Button addPlaylistButton;
    private Button renamePlaylistButton;
    private TitledPane currentTitledPane = new TitledPane();
    private Slider musicSlider;

    private Controller controller;
    private MusicManager musicManager;
    private PlaylistManager playlistManager;
    private ContextMenuController contextMenuController;

    private MediaController mediaController;

    public ViewController(Controller controller) {
        this.controller = controller;
        musicManager = controller.getMusicManager();
        playlistManager = controller.getPlaylistManager();
        accordion = controller.getAccordion();
        mediaController = controller.getMediaController();
        addPlaylistButton = controller.getAddPlaylistButton();
        renamePlaylistButton = controller.getRenamePlaylistButton();
        musicSlider = controller.getTimeSlider();
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
        accordion.getPanes().clear();
        for (Playlist playlist : playlistManager.getPlaylistArray()) {
            TitledPane titledPane = new TitledPane();
            titledPane.setText(playlist.getName());
            VBox vBox = new VBox();

            dragAndDrop(vBox, playlist, titledPane);

            VBox labelsVBox = new VBox();
            for (Music music : playlist.getMusicArray()) {
                labelsVBox.getChildren().add(getPlaylistTitleLabel(music.getTitle(), playlist, titledPane));
                labelsVBox.getChildren().add(new Label(music.getArtist()));
            }
            vBox.getChildren().add(labelsVBox);
            titledPane.setOnMouseClicked(mouseEvent -> actionForTitledPane(mouseEvent, titledPane));
            labelsVBox.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                    mouseEvent.consume();
                }
            });
            titledPane.setContent(vBox);
            accordion.getPanes().add(titledPane);
        }
    }

    public void createTitledPane(String playlistName) {
        TitledPane titledPane = new TitledPane();
        titledPane.setText(playlistName);
        VBox vBox = new VBox();
        playlistManager.createPlaylist(playlistName);
        dragAndDrop(vBox, playlistManager.getLastPlaylist(), titledPane);
        vBox.setMinHeight(15);
        titledPane.setContent(vBox);
        titledPane.setOnMouseClicked(mouseEvent -> {
            if (!titledPane.isExpanded() || vBox.getChildren().isEmpty()) {
                actionForTitledPane(mouseEvent, titledPane);
            }
        });
        accordion.getPanes().add(titledPane);
    }

    private void actionForTitledPane(MouseEvent mouseEvent, TitledPane titledPane) {
        if (mouseEvent.getButton() == MouseButton.SECONDARY) {
            contextMenuController.showTitledMenuCM(titledPane, mouseEvent, this);
        }
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

    private Label getPlaylistTitleLabel(String fileName, Playlist playlist, TitledPane titledPane) {
        Label label = new Label(fileName);
        label.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                contextMenuController.showTitledPaneLabelCM(playlist, label, titledPane, mouseEvent, this);
            } else if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                if (mouseEvent.getClickCount() == 2) {
                    mediaController.playInPlaylist(label.getText(), playlist.getName());
                }
            }
        });
        return label;
    }

    public void renameTitledPane(String playlistName) {
        playlistManager.renamePlaylist(currentTitledPane.getText(), playlistName);
        currentTitledPane.setText(playlistName);
        renamePlaylistButton.setDisable(true);
        addPlaylistButton.setDisable(false);
    }

    private void dragAndDrop(VBox vBox, Playlist playlist, TitledPane titledPane) {
        vBox.setOnDragOver(dragEvent -> {
            // data is dragged over the target
            // accept it only if it is  not dragged from the same node and if it has a string data
            if (dragEvent.getGestureSource() != vBox && dragEvent.getDragboard().hasString()) {
                // allow for both copying and moving, whatever user chooses
                dragEvent.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            dragEvent.consume();
        });
        vBox.setOnDragEntered(dragEvent -> {
            // the drag-and-drop gesture entered the target
            // show to the user that it is an actual gesture target
            if (dragEvent.getGestureSource() != vBox && dragEvent.getDragboard().hasString()) {
//                vBox.setStyle("-fx-background-color: green");
            }
            dragEvent.consume();
        });
        vBox.setOnDragExited(dragEvent -> {
            // mouse moved away, remove the graphical cues
//            vBox.setStyle("-fx-background-color: #333333");
            dragEvent.consume();
        });
        vBox.setOnDragDropped(dragEvent -> {
            // data dropped
            // if there is a string data on dragBoard, read it and use it
            Dragboard dragboard = dragEvent.getDragboard();
            boolean success = false;
            if (dragboard.hasString()) {
                VBox labelsVBox = new VBox();
                labelsVBox.getChildren().addAll(getPlaylistTitleLabel(dragboard.getString(), playlist, titledPane),
                        new Label(musicManager.get(dragboard.getString()).getArtist()));
                vBox.getChildren().add(labelsVBox);
                playlist.addByName(dragboard.getString(), musicManager);
                success = true;
            }
            // let the source know whether the string was successfully transferred and used
            dragEvent.setDropCompleted(success);
            dragEvent.consume();
        });
    }

    public void methodToRename(boolean renameDisable, boolean addDisable, TitledPane titledPane) {
        renamePlaylistButton.setDisable(renameDisable);
        addPlaylistButton.setDisable(addDisable);
        controller.getTextField().requestFocus();
        currentTitledPane = titledPane;
    }
}
