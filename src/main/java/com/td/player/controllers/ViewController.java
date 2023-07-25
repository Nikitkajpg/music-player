package com.td.player.controllers;

import com.td.player.elements.Directory;
import com.td.player.elements.Music;
import com.td.player.elements.Playlist;
import com.td.player.managers.DirectoryManager;
import com.td.player.managers.MusicManager;
import com.td.player.managers.PlaylistManager;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

@SuppressWarnings("FieldMayBeFinal")
public class ViewController {
    private VBox dirsListVBox;
    private VBox musicListVBox;
    private Accordion accordion;
    private Button addPlaylistButton;
    private TextField textField;
    private Button renamePlaylistButton;
    private TitledPane currentTitledPane = new TitledPane();
    private ContextMenu currentContextMenu = new ContextMenu();

    private DirectoryManager directoryManager;
    private MusicManager musicManager;
    private PlaylistManager playlistManager;

    private MediaController mediaController;

    public ViewController(DirectoryManager directoryManager, MusicManager musicManager, PlaylistManager playlistManager,
                          VBox dirsListVBox, VBox musicListVBox, Accordion accordion, MediaController mediaController,
                          Button addPlaylistButton, TextField textField, Button renamePlaylistButton) {
        this.dirsListVBox = dirsListVBox;
        this.musicListVBox = musicListVBox;
        this.directoryManager = directoryManager;
        this.musicManager = musicManager;
        this.playlistManager = playlistManager;
        this.accordion = accordion;
        this.mediaController = mediaController;
        this.addPlaylistButton = addPlaylistButton;
        this.textField = textField;
        this.renamePlaylistButton = renamePlaylistButton;
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

    private void updateMusic() {
        musicListVBox.getChildren().clear();
        for (Music music : musicManager.getMusicArray()) {
            musicListVBox.getChildren().add(getMusicLabel(music.getFileName()));
        }
    }

    private void updatePlaylists() {
        accordion.getPanes().clear();
        for (Playlist playlist : playlistManager.getPlaylistArray()) {
            TitledPane titledPane = new TitledPane();
            titledPane.setText(playlist.getName());
            VBox vBox = new VBox();
            titledPane.setOnMouseClicked(mouseEvent -> {
                if (!titledPane.isExpanded() || vBox.getChildren().isEmpty()) {
                    actionForTitledPane(mouseEvent, titledPane);
                }
            });

            dragAndDrop(vBox, playlist);

            for (Music music : playlist.getMusicArray()) {
                vBox.getChildren().add(getPlaylistLabel(music.getFileName(), vBox, playlist, titledPane));
            }
            titledPane.setContent(vBox);
            accordion.getPanes().add(titledPane);
        }
    }

    public void createTitledPane(String playlistName) {
        TitledPane titledPane = new TitledPane();
        titledPane.setText(playlistName);
        VBox vBox = new VBox();
        playlistManager.createPlaylist(playlistName);
        dragAndDrop(vBox, playlistManager.getLastPlaylist());
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
            currentContextMenu.hide();
            contextMenuForPlaylist(titledPane, mouseEvent);
        }
    }

    private Label getDirLabel(String path) {
        Label label = new Label(path);
        label.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                currentContextMenu.hide();
                contextMenuForDir(label, mouseEvent);
            }
        });
        return label;
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
            } else if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                currentContextMenu.hide();
                contextMenuForMusic(label, mouseEvent);
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

    private Label getPlaylistLabel(String fileName, VBox vBox, Playlist playlist, TitledPane titledPane) {
        Label label = new Label(fileName);
        label.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                currentContextMenu.hide();
                contextMenuForPlaylistLabel(label, mouseEvent, vBox, playlist, titledPane);
            }
        });
        return label;
    }

    private void contextMenuForDir(Label label, MouseEvent mouseEvent) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteMenuItem = new MenuItem("Delete directory");
        deleteMenuItem.setOnAction(actionEvent -> {
            dirsListVBox.getChildren().remove(label);       // удаление метки из списка меток путей
            directoryManager.delete(label.getText()); // удаление пути из списка путей
            musicManager.delete(label.getText());  // удаление музыки с этим путем из списка музыки
            playlistManager.deleteByPath(label.getText()); // удаление всей музыки с этим путем из списка плейлистов
            updateMusic();                                  // вывод обновленного списка музыки на экран
            updatePlaylists();
        });
        MenuItem showMenuItem = new MenuItem("Show in explorer");
        showMenuItem.setOnAction(event -> {
            File directory = new File(label.getText());
            try {
                Desktop.getDesktop().open(directory);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        contextMenu.getItems().addAll(showMenuItem, deleteMenuItem);
        contextMenu.show(label, mouseEvent.getScreenX(), mouseEvent.getScreenY());
        currentContextMenu = contextMenu;
    }

    private void contextMenuForMusic(Label label, MouseEvent mouseEvent) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem showMenuItem = new MenuItem("Show in explorer");
        showMenuItem.setOnAction(event -> {
            File directory = new File(musicManager.get(label.getText()).getAbsolutePath());
            try {
                Runtime.getRuntime().exec("explorer /select, " + directory.getAbsolutePath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        contextMenu.getItems().add(showMenuItem);
        contextMenu.show(label, mouseEvent.getScreenX(), mouseEvent.getScreenY());
        currentContextMenu = contextMenu;
    }

    private void contextMenuForPlaylistLabel(Label label, MouseEvent mouseEvent, VBox vBox, Playlist playlist, TitledPane titledPane) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem deletePlaylistMenuItem = new MenuItem("Delete playlist");
        deletePlaylistMenuItem.setOnAction(event -> {
            playlistManager.deleteByName(titledPane.getText());
            updatePlaylists();
        });

        MenuItem renamePlaylistMenuItem = new MenuItem("Rename playlist");
        renamePlaylistMenuItem.setOnAction(actionEvent -> {
            //todo
            renamePlaylistButton.setDisable(false);
            addPlaylistButton.setDisable(true);
            textField.requestFocus();
            currentTitledPane = titledPane;
        });

        MenuItem deleteMusicMenuItem = new MenuItem("Delete music");
        deleteMusicMenuItem.setOnAction(actionEvent -> {
            playlist.deleteByName(label.getText());
            vBox.getChildren().remove(label);
        });
        contextMenu.getItems().addAll(deletePlaylistMenuItem, renamePlaylistMenuItem, deleteMusicMenuItem);
        contextMenu.show(label, mouseEvent.getScreenX(), mouseEvent.getScreenY());
        currentContextMenu = contextMenu;
    }

    private void contextMenuForPlaylist(TitledPane titledPane, MouseEvent mouseEvent) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem deletePlaylistMenuItem = new MenuItem("Delete playlist");
        deletePlaylistMenuItem.setOnAction(actionEvent -> {
            playlistManager.deleteByName(titledPane.getText());
            updatePlaylists();
        });

        MenuItem renamePlaylistMenuItem = new MenuItem("Rename playlist");
        renamePlaylistMenuItem.setOnAction(actionEvent -> {
            //todo
        });

        contextMenu.getItems().addAll(renamePlaylistMenuItem, deletePlaylistMenuItem);
        contextMenu.show(titledPane, mouseEvent.getScreenX(), mouseEvent.getScreenY());
        currentContextMenu = contextMenu;
    }

    public void renameTitledPane(String playlistName) {
        playlistManager.renamePlaylist(currentTitledPane.getText(), playlistName);
        currentTitledPane.setText(playlistName);
        renamePlaylistButton.setDisable(true);
        addPlaylistButton.setDisable(false);
    }

    private void dragAndDrop(VBox vBox, Playlist playlist) {
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
    }
}
