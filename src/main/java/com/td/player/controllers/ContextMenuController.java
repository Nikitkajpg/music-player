package com.td.player.controllers;

import com.td.player.elements.Music;
import com.td.player.elements.Playlist;
import com.td.player.managers.DirectoryManager;
import com.td.player.managers.MusicManager;
import com.td.player.managers.PlaylistManager;
import com.td.player.util.Actions;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;

@SuppressWarnings("FieldMayBeFinal")
public class ContextMenuController {
    private MediaController mediaController;
    private DirectoryManager directoryManager;
    private MusicManager musicManager;
    private PlaylistManager playlistManager;

    private ContextMenu currentContextMenu = new ContextMenu();

    public ContextMenuController(Controller controller) {
        this.directoryManager = controller.getDirectoryManager();
        this.musicManager = controller.getMusicManager();
        this.playlistManager = controller.getPlaylistManager();
        this.mediaController = controller.getMediaController();
    }

    // КМ для каждой песни в списке музыки
    public void showMusicCM(Button button, MouseEvent mouseEvent, Music music) {
        currentContextMenu.hide();
        ContextMenu contextMenu = new ContextMenu();
        MenuItem playMusicMenuItem = new MenuItem("Play music");
        playMusicMenuItem.setOnAction(actionEvent -> mediaController.playMusicInPlaylist(music, playlistManager.getDefaultPlaylist()));
        MenuItem showMenuItem = new MenuItem("Show in explorer");
        showMenuItem.setOnAction(actionEvent -> Actions.openFile(musicManager, button));
        contextMenu.getItems().addAll(playMusicMenuItem, showMenuItem);
        contextMenu.show(button, mouseEvent.getScreenX(), mouseEvent.getScreenY());
        currentContextMenu = contextMenu;
    }

    // КМ для каждой папки в списке папок
    public void showDirCM(Button button, MouseEvent mouseEvent, ViewController viewController) {
        currentContextMenu.hide();
        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteMenuItem = new MenuItem("Delete directory");
        deleteMenuItem.setOnAction(actionEvent -> Actions.deleteDirectory(directoryManager, musicManager, viewController, button, playlistManager));
        contextMenu.getItems().add(deleteMenuItem);
        contextMenu.show(button, mouseEvent.getScreenX(), mouseEvent.getScreenY());
        currentContextMenu = contextMenu;
    }

    // КМ для каждой песни в списке песен плейлистов
    public void showPlaylistMusicButtonCM(Playlist playlist, Button musicButton, Music music, Button playlistButton, MouseEvent mouseEvent, ViewController viewController) {
        currentContextMenu.hide();
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems().addAll(
                getPlayMusicInPlaylistMenuItem(music, playlistButton),
                getDeleteMusicMenuItem(music, playlist, viewController));
        contextMenu.show(musicButton, mouseEvent.getScreenX(), mouseEvent.getScreenY());
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
        Playlist playlist = playlistManager.getPlaylistByName(button.getText());
        playMenuItem.setOnAction(actionEvent -> mediaController.playMusicInPlaylist(playlist.getMusicArray().get(0), playlist));
        return playMenuItem;
    }

    private MenuItem getDeleteMusicMenuItem(Music music, Playlist playlist, ViewController viewController) {
        MenuItem deleteMusicMenuItem = new MenuItem("Delete music");
        deleteMusicMenuItem.setOnAction(actionEvent -> {
            playlist.deleteByName(music.getTitle());
            viewController.removeSongFromPlaylist(music);
        });
        return deleteMusicMenuItem;
    }

    private MenuItem getPlayMusicInPlaylistMenuItem(Music music, Button playlistButton) {
        MenuItem playMusicMenuItem = new MenuItem("Play music");
        playMusicMenuItem.setOnAction(actionEvent -> mediaController.playMusicInPlaylist(music, playlistManager.getPlaylistByName(playlistButton.getText())));
        return playMusicMenuItem;
    }

    private MenuItem getDeletePlaylistMenuItem(Button button, ViewController viewController) {
        MenuItem deletePlaylistMenuItem = new MenuItem("Delete playlist");
        deletePlaylistMenuItem.setOnAction(event -> {
            playlistManager.deleteByName(button.getText());
            viewController.removePlaylist(button);
        });
        return deletePlaylistMenuItem;
    }

    private MenuItem getRenameMenuItem(Button button, ViewController viewController) {
        MenuItem renamePlaylistMenuItem = new MenuItem("Rename playlist");
        renamePlaylistMenuItem.setOnAction(actionEvent -> viewController.startRename(false, true, button));
        return renamePlaylistMenuItem;
    }
}
