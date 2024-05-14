package com.td.player.controllers.view;

import com.td.player.controllers.Controller;
import com.td.player.elements.Directory;
import com.td.player.elements.Playlist;
import com.td.player.elements.Track;
import com.td.player.managers.TrackManager;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.io.IOException;

/**
 * Класс, содержащий методы для управления {@link ContextMenu}
 */

@SuppressWarnings("FieldMayBeFinal")
public class ContextMenuController {
    private Controller controller;

    private ContextMenu currentContextMenu = new ContextMenu();

    public ContextMenuController(Controller controller) {
        this.controller = controller;
    }

    /**
     * Контекстное меню для каждой песни в списке музыки
     */
    public void showTrackCM(Button pressedButton, MouseEvent mouseEvent, Track currentTrack) {
        currentContextMenu.hide();
        ContextMenu contextMenu = new ContextMenu();

        MenuItem playTrackMenuItem = new MenuItem("Play track");
        playTrackMenuItem.setOnAction(actionEvent -> controller.getMediaController()
                .playTrackInPlaylist(currentTrack, controller.getPlaylistManager().getDefaultPlaylist()));

        MenuItem showMenuItem = new MenuItem("Show in explorer");
        showMenuItem.setOnAction(actionEvent -> openFile(controller.getTrackManager(), pressedButton));

        contextMenu.getItems().addAll(playTrackMenuItem, showMenuItem);
        contextMenu.show(pressedButton, mouseEvent.getScreenX(), mouseEvent.getScreenY());
        currentContextMenu = contextMenu;
    }

    public void showDirectoryTrackButtonCM(Directory currentDirectory, Button trackButton, Track track, MouseEvent mouseEvent) {

    }

    public void openFile(TrackManager trackManager, Button pressedButtonWithTrackFilename) {
        File directory = new File(trackManager.get(pressedButtonWithTrackFilename.getText()).getAbsolutePath());
        try {
            boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
            if (isWindows) {
                ProcessBuilder pb = new ProcessBuilder("explorer.exe", "/select," + directory.toURI());
                pb.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Контекстное меню для каждой папки в списке папок
     */
    public void showDirCM(Directory currentDirectory, Button pressedButton, MouseEvent mouseEvent) {
        currentContextMenu.hide();
        ContextMenu contextMenu = new ContextMenu();

        MenuItem deleteMenuItem = new MenuItem("Delete directory");
        deleteMenuItem.setOnAction(actionEvent -> deleteDirectory(controller, currentDirectory, pressedButton));

        contextMenu.getItems().add(deleteMenuItem);
        contextMenu.show(pressedButton, mouseEvent.getScreenX(), mouseEvent.getScreenY());
        currentContextMenu = contextMenu;
    }

    public void deleteDirectory(Controller controller, Directory directory, Button button) {
        controller.getDirectoryManager().delete(directory);
        controller.getTrackManager().deleteByPath(button.getText());
        controller.getPlaylistManager().deleteByPath(button.getText());
        controller.getViewController().updateView();
    }

    /**
     * Контекстное меню для каждой песни в списке песен плейлистов
     */
    public void showPlaylistTrackButtonCM(Playlist currentPlaylist, Button pressedButton, Track currentTrack, MouseEvent mouseEvent) {
        currentContextMenu.hide();
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems().addAll(
                getPlayTrackInPlaylistMenuItem(currentTrack, currentPlaylist),
                getDeleteTrackMenuItem(currentTrack, currentPlaylist));
        contextMenu.show(pressedButton, mouseEvent.getScreenX(), mouseEvent.getScreenY());
        currentContextMenu = contextMenu;
    }

    /**
     * Контекстное меню для каждого плейлиста в списке плейлистов
     */
    public void showPlaylistButtonCM(Playlist currentPlaylist, Button pressedButton, MouseEvent mouseEvent) {
        currentContextMenu.hide();
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems().addAll(
                getRenameMenuItem(pressedButton),
                getDeletePlaylistMenuItem(currentPlaylist, pressedButton),
                getPlayMenuItem(pressedButton));
        contextMenu.show(pressedButton, mouseEvent.getScreenX(), mouseEvent.getScreenY());
        currentContextMenu = contextMenu;
    }

    /**
     * меню в контекстном меню для проигрывания целого плейлиста
     */
    private MenuItem getPlayMenuItem(Button pressedButton) {
        MenuItem playMenuItem = new MenuItem("Play playlist");
        Playlist playlist = controller.getPlaylistManager().getPlaylistByName(pressedButton.getText());
        playMenuItem.setOnAction(actionEvent -> {
            if (!playlist.getTracks().isEmpty()) {
                controller.getMediaController().playTrackInPlaylist(playlist.getTracks().get(0), playlist);
            }
        });
        return playMenuItem;
    }

    /**
     * меню в контекстном меню для удаления песни в плейлисте
     */
    private MenuItem getDeleteTrackMenuItem(Track currentTrack, Playlist currentPlaylist) {
        MenuItem deleteTrackMenuItem = new MenuItem("Delete track");
        deleteTrackMenuItem.setOnAction(actionEvent -> {
            currentPlaylist.delete(currentTrack);
//            controller.getViewController().removeTrackFromPlaylist(currentTrack);
        });
        return deleteTrackMenuItem;
    }

    /**
     * меню в контекстном меню для проигрывания музыки в плейлисте
     */
    private MenuItem getPlayTrackInPlaylistMenuItem(Track currentTrack, Playlist currentPlaylist) {
        MenuItem playTrackMenuItem = new MenuItem("Play track");
        playTrackMenuItem.setOnAction(actionEvent -> controller.getMediaController().playTrackInPlaylist(currentTrack, controller.getPlaylistManager().getPlaylistByName(currentPlaylist.getName())));
        return playTrackMenuItem;
    }

    /**
     * меню в контекстном меню для удаления плейлиста
     */
    private MenuItem getDeletePlaylistMenuItem(Playlist currentPlaylist, Button pressedButton) {
        MenuItem deletePlaylistMenuItem = new MenuItem("Delete playlist");
        deletePlaylistMenuItem.setOnAction(event -> {
            controller.getPlaylistManager().delete(currentPlaylist);
//            controller.getViewController().removePlaylist(currentPlaylist, pressedButton);
        });
        return deletePlaylistMenuItem;
    }

    /**
     * меню в контекстном меню для переименовывания плейлиста
     */
    private MenuItem getRenameMenuItem(Button pressedButton) {
        MenuItem renamePlaylistMenuItem = new MenuItem("Rename playlist");
//        renamePlaylistMenuItem.setOnAction(actionEvent -> controller.getViewController().renamePlaylistButton(pressedButton));
        return renamePlaylistMenuItem;
    }
}
