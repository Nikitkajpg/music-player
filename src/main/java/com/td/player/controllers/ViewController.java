package com.td.player.controllers;

import com.td.player.elements.Directory;
import com.td.player.elements.Music;
import com.td.player.elements.Playlist;
import com.td.player.managers.DirectoryManager;
import com.td.player.managers.MusicManager;
import com.td.player.managers.PlaylistManager;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

@SuppressWarnings("FieldMayBeFinal")
public class ViewController {
    private VBox dirsListVBox;
    private VBox musicListVBox;
    private Accordion accordion;
    private Label sourceLabel;

    private DirectoryManager directoryManager;
    private MusicManager musicManager;
    private PlaylistManager playlistManager;

    private MediaController mediaController;

    public ViewController(DirectoryManager directoryManager, MusicManager musicManager, PlaylistManager playlistManager, VBox dirsListVBox, VBox musicListVBox, Accordion accordion, MediaController mediaController) {
        this.dirsListVBox = dirsListVBox;
        this.musicListVBox = musicListVBox;
        this.directoryManager = directoryManager;
        this.musicManager = musicManager;
        this.playlistManager = playlistManager;
        this.accordion = accordion;
        this.mediaController = mediaController;
        update();
    }

    public void update() {
        updateDirs();
        updateMusic();
        updatePlaylists();
    }

    private void updateDirs() {
        dirsListVBox.getChildren().clear();
        for (Directory directory : directoryManager.getDirectoryArray()) {
            dirsListVBox.getChildren().add(getDirLabel(directory.getPath()));
        }
    }

    private Label getDirLabel(String path) {
        Label label = new Label(path);
        label.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                contextMenuForDir(label, mouseEvent);
            }
        });
        return label;
    }

    private void contextMenuForDir(Label label, MouseEvent mouseEvent) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem menuItem = new MenuItem("Delete");
        menuItem.setOnAction(actionEvent -> {
            dirsListVBox.getChildren().remove(label);       // удаление метки из списка меток путей
            directoryManager.delete(label.getText()); // удаление пути из списка путей
            musicManager.delete(label.getText());  // удаление музыки с этим путем из списка музыки
            playlistManager.deleteByPath(label.getText()); // удаление всей музыки с этим путем из списка плейлистов
            updateMusic();                                  // вывод обновленного списка музыки на экран
            updatePlaylists();
        });
        contextMenu.getItems().add(menuItem);
        contextMenu.show(label, mouseEvent.getScreenX(), mouseEvent.getScreenY());
    }

    private void updateMusic() {
        musicListVBox.getChildren().clear();
        for (Music music : musicManager.getMusicArray()) {
            musicListVBox.getChildren().add(getMusicLabel(music.getFileName()));
        }
    }

    private Label getMusicLabel(String name) {
        Label label = new Label(name);
        label.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                if (mouseEvent.getClickCount() == 2) {
                    if (mediaController.getCurrentMediaPlayer() != null) {
                        mediaController.getCurrentMediaPlayer().stop();
                    }
                    mediaController.playByName(name);
                }
            }
        });
        label.setOnDragDetected(mouseEvent -> {
            // drag was detected, start drag-and-drop gesture
            // allow any transfer mode
            sourceLabel = label;
            Dragboard dragboard = label.startDragAndDrop(TransferMode.ANY);
            // put a string on dragBoard
            ClipboardContent clipboardContent = new ClipboardContent();
            clipboardContent.putString(label.getText());
            dragboard.setContent(clipboardContent);
            mouseEvent.consume();
        });
        return label;
    }

    private void updatePlaylists() {
        accordion.getPanes().clear();
        for (Playlist playlist : playlistManager.getPlaylistArray()) {
            TitledPane titledPane = new TitledPane();
            titledPane.setText(playlist.getName());
            titledPane.setOnMouseClicked(mouseEvent -> actionForTitledPane(mouseEvent, titledPane));
            VBox vBox = new VBox();

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
                if (dragEvent.getGestureSource() != vBox &&
                        dragEvent.getDragboard().hasString()) {
                    vBox.setStyle("-fx-background-color: green");
                }
                dragEvent.consume();
            });
            vBox.setOnDragExited(dragEvent -> {
                // mouse moved away, remove the graphical cues
                vBox.setStyle("-fx-background-color: #333333");
                dragEvent.consume();
            });
            vBox.setOnDragDropped(dragEvent -> {
                // data dropped
                // if there is a string data on dragBoard, read it and use it
                Dragboard dragboard = dragEvent.getDragboard();
                boolean success = false;
                if (dragboard.hasString()) {
                    vBox.getChildren().add(new Label(dragboard.getString()));
                    playlist.addByName(dragboard.getString(), musicManager);
                    success = true;
                }
                // let the source know whether the string was successfully transferred and used
                dragEvent.setDropCompleted(success);
                dragEvent.consume();
            });

            for (Music music : playlist.getMusicArray()) {
                vBox.getChildren().add(new Label(music.getFileName()));
            }
            titledPane.setContent(vBox);
            accordion.getPanes().add(titledPane);
        }
    }

    public void createTitledPane(String playlistName) {
        TitledPane titledPane = new TitledPane();
        titledPane.setText(playlistName);
        titledPane.setContent(new VBox());
        titledPane.setOnMouseClicked(mouseEvent -> actionForTitledPane(mouseEvent, titledPane));
        accordion.getPanes().add(titledPane);
    }

    private void actionForTitledPane(MouseEvent mouseEvent, TitledPane titledPane) {
        if (mouseEvent.getButton() == MouseButton.SECONDARY) {
            contextMenuForPlaylist(titledPane, mouseEvent);
        }
    }

    private void contextMenuForPlaylist(TitledPane titledPane, MouseEvent mouseEvent) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem menuItem = new MenuItem("Delete");
        menuItem.setOnAction(actionEvent -> {
            playlistManager.deleteByName(titledPane.getText());
            updatePlaylists();
        });
        contextMenu.getItems().add(menuItem);
        contextMenu.show(titledPane, mouseEvent.getScreenX(), mouseEvent.getScreenY());
    }
}
