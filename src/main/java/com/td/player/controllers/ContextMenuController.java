package com.td.player.controllers;

import com.td.player.elements.Playlist;
import com.td.player.managers.DirectoryManager;
import com.td.player.managers.MusicManager;
import com.td.player.managers.PlaylistManager;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;

import java.awt.*;
import java.io.File;
import java.io.IOException;

@SuppressWarnings("FieldMayBeFinal")
public class ContextMenuController {
    private MediaController mediaController;
    private DirectoryManager directoryManager;
    private MusicManager musicManager;
    private PlaylistManager playlistManager;

    private ContextMenu currentContextMenu = new ContextMenu();

    public ContextMenuController(DirectoryManager directoryManager, MusicManager musicManager, PlaylistManager playlistManager, MediaController mediaController) {
        this.directoryManager = directoryManager;
        this.musicManager = musicManager;
        this.playlistManager = playlistManager;
        this.mediaController = mediaController;
    }

    public void showMusicCM(Label label, MouseEvent mouseEvent) {
        currentContextMenu.hide();
        ContextMenu contextMenu = new ContextMenu();
        MenuItem showMenuItem = new MenuItem("Show in explorer");
        showMenuItem.setOnAction(actionEvent -> {
            File directory = new File(musicManager.get(label.getText()).getAbsolutePath());
            try {
                // FIXME: 28.07.2023 it works only on windows
                Runtime.getRuntime().exec("explorer /select, " + directory.getAbsolutePath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        contextMenu.getItems().add(showMenuItem);
        contextMenu.show(label, mouseEvent.getScreenX(), mouseEvent.getScreenY());
        currentContextMenu = contextMenu;
    }

    public void showDirCM(Label label, MouseEvent mouseEvent, ViewController viewController) {
        currentContextMenu.hide();
        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteMenuItem = new MenuItem("Delete directory");
        deleteMenuItem.setOnAction(actionEvent -> {
            directoryManager.deleteByPath(label.getText());
            musicManager.deleteByPath(label.getText());
            playlistManager.deleteByPath(label.getText());
            viewController.update();
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

    public void showTitledPaneLabelCM(Playlist playlist, Label label, TitledPane titledPane, MouseEvent mouseEvent, ViewController viewController) {
        currentContextMenu.hide();
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems().addAll(
                getDeletePlaylistMenuItem(titledPane, viewController),
                getRenameMenuItem(titledPane, viewController),
                getDeleteMusicMenuItem(label, playlist, viewController));
        contextMenu.show(label, mouseEvent.getScreenX(), mouseEvent.getScreenY());
        currentContextMenu = contextMenu;
    }

    public void showTitledMenuCM(TitledPane titledPane, MouseEvent mouseEvent, ViewController viewController) {
        currentContextMenu.hide();
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems().addAll(
                getRenameMenuItem(titledPane, viewController),
                getDeletePlaylistMenuItem(titledPane, viewController),
                getPlayMenuItem(titledPane, viewController));
        contextMenu.show(titledPane, mouseEvent.getScreenX(), mouseEvent.getScreenY());
        currentContextMenu = contextMenu;
    }

    private MenuItem getPlayMenuItem(TitledPane titledPane, ViewController viewController) {
        MenuItem playMenuItem = new MenuItem("Play playlist");
        playMenuItem.setOnAction(actionEvent -> {
            mediaController.setCurrentPlayList(playlistManager.getPlaylistByName(titledPane.getText()));
            mediaController.playPlaylist();
        });
        return playMenuItem;
    }

    private MenuItem getDeleteMusicMenuItem(Label label, Playlist playlist, ViewController viewController) {
        MenuItem deleteMusicMenuItem = new MenuItem("Delete music");
        deleteMusicMenuItem.setOnAction(actionEvent -> {
            playlist.deleteByName(label.getText());
            viewController.updatePlaylists();
        });
        return deleteMusicMenuItem;
    }

    private MenuItem getDeletePlaylistMenuItem(TitledPane titledPane, ViewController viewController) {
        MenuItem deletePlaylistMenuItem = new MenuItem("Delete playlist");
        deletePlaylistMenuItem.setOnAction(event -> {
            playlistManager.deleteByName(titledPane.getText());
            viewController.updatePlaylists();
        });
        return deletePlaylistMenuItem;
    }

    private MenuItem getRenameMenuItem(TitledPane titledPane, ViewController viewController) {
        MenuItem renamePlaylistMenuItem = new MenuItem("Rename playlist");
        renamePlaylistMenuItem.setOnAction(actionEvent -> viewController.methodToRename(false, true, titledPane));
        return renamePlaylistMenuItem;
    }
}
