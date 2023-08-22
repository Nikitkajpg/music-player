package com.td.player.controllers;

import com.td.player.elements.Directory;
import com.td.player.elements.Music;
import com.td.player.elements.Playlist;
import com.td.player.managers.MusicManager;
import com.td.player.managers.PlaylistManager;
import com.td.player.util.Actions;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.*;
import javafx.scene.layout.VBox;

@SuppressWarnings("FieldMayBeFinal")
public class ViewController {
    private Button currentButton = new Button();
    private VBox playlistNamesVBox, playlistMusicVBox;

    private Controller controller;
    private ContextMenuController contextMenuController;
    private MediaController mediaController;

    private MusicManager musicManager;
    private PlaylistManager playlistManager;

    public ViewController(Controller controller) {
        this.controller = controller;
        musicManager = controller.getMusicManager();
        playlistManager = controller.getPlaylistManager();
        mediaController = controller.getMediaController();
        playlistNamesVBox = controller.playlistNamesVBox;
        playlistMusicVBox = controller.playlistMusicVBox;
        contextMenuController = new ContextMenuController(controller);
        showLists();
    }

    public void showLists() {
        showDirectories();
        showMusic();
        showPlaylists();
    }

    public void showDirectories() {
        controller.dirsListVBox.getChildren().clear();
        for (Directory directory : controller.getDirectoryManager().getDirectoryArray()) {
            controller.dirsListVBox.getChildren().add(getDirButton(directory.getPath()));
        }
    }

    public void showMusic() {
        controller.musicListVBox.getChildren().clear();
        for (Music music : musicManager.getMusicArray()) {
            controller.musicListVBox.getChildren().add(getMusicButton(music.getFileName()));
        }
    }

    public void showPlaylists() {
        playlistNamesVBox.getChildren().clear();
        playlistMusicVBox.getChildren().clear();

        for (Playlist playlist : playlistManager.getPlaylistArray()) {
            Button button = new Button(playlist.getName());
            button.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                    contextMenuController.showPlaylistButtonCM(button, mouseEvent, this);
                }
            });
            button.setOnAction(actionEvent -> actionForShowingPlaylist(button, playlist));
            playlistNamesVBox.getChildren().add(button);
        }
    }

    private Button getDirButton(String path) {
        Button button = new Button(path);
        button.setOnAction(actionEvent -> Actions.openDirectory(button));
        button.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                contextMenuController.showDirCM(button, mouseEvent, this);
            }
        });
        return button;
    }

    private Button getMusicButton(String name) {
        Button button = new Button(name);
        button.setOnAction(actionEvent -> mediaController.playByName(name));
        button.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                contextMenuController.showMusicCM(button, mouseEvent);
            }
        });
        button.setOnDragDetected(mouseEvent -> Actions.onDragDetected(mouseEvent, button));
        return button;
    }

    public Button getPlaylistMusicButton(String fileName, Playlist playlist, Button button) {
        Button musicButton = new Button(fileName);
        musicButton.setOnAction(actionEvent -> mediaController.playInPlaylist(musicButton.getText(), playlist.getName()));
        musicButton.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                contextMenuController.showPlaylistMusicButtonCM(playlist, musicButton, button, mouseEvent, this);
            }
        });
        return musicButton;
    }

    public Button getPlaylistMusicArtistButton(Playlist playlist, Button button, Button musicButton) {
        Button artistButton = new Button(musicManager.getByTitle(musicButton.getText()).getArtist());
        artistButton.setOnAction(actionEvent -> mediaController.playInPlaylist(musicButton.getText(), playlist.getName()));
        artistButton.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                contextMenuController.showPlaylistMusicButtonCM(playlist, artistButton, button, mouseEvent, this);
            }
        });
        return artistButton;
    }

    private void actionForShowingPlaylist(Button button, Playlist playlist) {
        currentButton = button;
        dragAndDrop(playlist, button);
        playlistMusicVBox.getChildren().clear();
        for (Music music : playlist.getMusicArray()) {
            VBox vBox = new VBox();
            Button titleButton = getPlaylistMusicButton(music.getTitle(), playlist, button);
            Button artistButton = getPlaylistMusicArtistButton(playlist, button, titleButton);
            vBox.getChildren().addAll(titleButton, artistButton);
            playlistMusicVBox.getChildren().add(vBox);
            titleButton.prefWidthProperty().bind(controller.playlistMusicScrollPane.widthProperty());
            artistButton.prefWidthProperty().bind(controller.playlistMusicScrollPane.widthProperty());
        }
        if (!playlist.getName().equals("All music")) {
            playlistMusicVBox.getChildren().add(new Label("Put song here..."));
        }
    }

    public void removeSongFromPlaylist(Button button) {
        for (int i = 0; i < playlistMusicVBox.getChildren().size() - 1; i++) {
            VBox vBox = (VBox) playlistMusicVBox.getChildren().get(i);
            Button musicLabel = (Button) vBox.getChildren().get(0);
            if (musicLabel.getText().equals(button.getText())) {
                playlistMusicVBox.getChildren().remove(vBox);
            }
        }
    }

    public void removePlaylist(Button button) {
        for (int i = 0; i < playlistNamesVBox.getChildren().size(); i++) {
            Button buttonToRemove = (Button) playlistNamesVBox.getChildren().get(i);
            if (buttonToRemove.getText().equals(button.getText())) {
                playlistNamesVBox.getChildren().remove(buttonToRemove);
                if (button == currentButton) {
                    playlistMusicVBox.getChildren().clear();
                }
            }
        }
    }

    public void createPlaylistNameButton(String playlistName) {
        Button button = new Button(playlistName);
        Playlist playlist = playlistManager.createAndGetPlaylist(playlistName);
        button.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                contextMenuController.showPlaylistButtonCM(button, mouseEvent, this);
            }
        });
        actionForShowingPlaylist(button, playlist);
        playlistNamesVBox.getChildren().add(button);
    }

    public void renamePlaylist(String playlistName) {
        playlistManager.renamePlaylist(currentButton.getText(), playlistName);
        currentButton.setText(playlistName);
        controller.renamePlaylistButton.setDisable(true);
        controller.addPlaylistButton.setDisable(false);
    }

    private void dragAndDrop(Playlist playlist, Button button) {
        playlistMusicVBox.setOnDragOver(dragEvent -> Actions.onDragOver(dragEvent, playlistMusicVBox, playlist));
        playlistMusicVBox.setOnDragEntered(dragEvent -> Actions.onDragEntered(dragEvent, playlistMusicVBox, playlist));
        playlistMusicVBox.setOnDragExited(dragEvent -> Actions.onDragExited(dragEvent, playlistMusicVBox, playlist));
        playlistMusicVBox.setOnDragDropped(dragEvent -> Actions.onDragDropped(
                dragEvent, playlistManager, playlist, musicManager, playlistMusicVBox, button, this));
    }

    public void startRename(boolean renameDisable, boolean addDisable, Button button) {
        controller.renamePlaylistButton.setDisable(renameDisable);
        controller.addPlaylistButton.setDisable(addDisable);
        controller.textField.requestFocus();
        currentButton = button;
    }
}
