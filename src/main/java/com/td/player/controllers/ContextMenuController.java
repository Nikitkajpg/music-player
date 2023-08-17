package com.td.player.controllers;

import com.td.player.elements.Playlist;
import com.td.player.managers.DirectoryManager;
import com.td.player.managers.MusicManager;
import com.td.player.managers.PlaylistManager;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
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

    // КМ для каждой песни в списке музыки
    public void showMusicCM(Label label, MouseEvent mouseEvent) {
        currentContextMenu.hide();
        ContextMenu contextMenu = new ContextMenu();
        MenuItem playMusicMenuItem = new MenuItem("Play music");
        playMusicMenuItem.setOnAction(actionEvent -> mediaController.playByName(label.getText()));

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
        contextMenu.getItems().addAll(playMusicMenuItem, showMenuItem);
        contextMenu.show(label, mouseEvent.getScreenX(), mouseEvent.getScreenY());
        currentContextMenu = contextMenu;
    }

    // КМ для каждой папки в списке папок
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

    public void showPlaylistSongLabelCM(Playlist playlist, Label label, Button button, MouseEvent mouseEvent, ViewController viewController) {
        currentContextMenu.hide();
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems().addAll(
                getPlayMusicMenuItem(label, button),
                getDeleteMusicMenuItem(label, playlist, viewController));
        contextMenu.show(label, mouseEvent.getScreenX(), mouseEvent.getScreenY());
        currentContextMenu = contextMenu;
    }

    // КМ для каждого плейлиста в списке плейлистов
    public void showPlaylistButtonCM(Button button, MouseEvent mouseEvent, ViewController viewController) {
        currentContextMenu.hide();
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems().addAll(
                getRenameMenuItem(button, viewController),
                getDeletePlaylistMenuItem(button, viewController),
                getPlayMenuItem(button));
        contextMenu.show(button, mouseEvent.getScreenX(), mouseEvent.getScreenY());
        currentContextMenu = contextMenu;
    }

    private MenuItem getPlayMenuItem(Button button) {
        MenuItem playMenuItem = new MenuItem("Play playlist");
        playMenuItem.setOnAction(actionEvent -> mediaController.playPlaylistByName(button.getText()));
        return playMenuItem;
    }

    private MenuItem getDeleteMusicMenuItem(Label label, Playlist playlist, ViewController viewController) {
        MenuItem deleteMusicMenuItem = new MenuItem("Delete music");
        deleteMusicMenuItem.setOnAction(actionEvent -> {
            playlist.deleteByName(label.getText());
            viewController.removeSongFromPlaylist(label);
        });
        return deleteMusicMenuItem;
    }

    private MenuItem getPlayMusicMenuItem(Label label, Button button) {
        MenuItem playMusicMenuItem = new MenuItem("Play music");
        playMusicMenuItem.setOnAction(actionEvent -> mediaController.playInPlaylist(label.getText(), button.getText()));
        return playMusicMenuItem;
    }

    private MenuItem getDeletePlaylistMenuItem(Button button, ViewController viewController) {
        MenuItem deletePlaylistMenuItem = new MenuItem("Delete playlist");
        deletePlaylistMenuItem.setOnAction(event -> {
            playlistManager.deleteByName(button.getText());
            viewController.updatePlaylists();
        });
        return deletePlaylistMenuItem;
    }

    private MenuItem getRenameMenuItem(Button button, ViewController viewController) {
        MenuItem renamePlaylistMenuItem = new MenuItem("Rename playlist");
        renamePlaylistMenuItem.setOnAction(actionEvent -> viewController.methodToRename(false, true, button));
        return renamePlaylistMenuItem;
    }
}
