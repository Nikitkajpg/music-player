package com.td.player.controllers;

import com.td.player.elements.Directory;
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

/**
 * Класс, содержащий методы для управления {@link ContextMenu}
 */

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

    /**
     * Контекстное меню для каждой песни в списке музыки
     *
     * @param button нажатая кнопка
     * @param music  текущая песня
     */
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

    /**
     * Контекстное меню для каждой папки в списке папок
     *
     * @param directory текущая папка
     * @param button нажатая кнопка
     */
    public void showDirCM(Directory directory, Button button, MouseEvent mouseEvent, ViewController viewController) {
        currentContextMenu.hide();
        ContextMenu contextMenu = new ContextMenu();

        MenuItem deleteMenuItem = new MenuItem("Delete directory");
        deleteMenuItem.setOnAction(actionEvent -> Actions.deleteDirectory(directoryManager, musicManager, viewController, directory, button, playlistManager));

        contextMenu.getItems().add(deleteMenuItem);
        contextMenu.show(button, mouseEvent.getScreenX(), mouseEvent.getScreenY());
        currentContextMenu = contextMenu;
    }

    /**
     * Контекстное меню для каждой песни в списке песен плейлистов
     *
     * @param playlist текущий плейлист
     * @param button   нажатая кнопка
     * @param music    текущая песня
     */
    public void showPlaylistMusicButtonCM(Playlist playlist, Button button, Music music, MouseEvent mouseEvent, ViewController viewController) {
        currentContextMenu.hide();
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems().addAll(
                getPlayMusicInPlaylistMenuItem(music, playlist),
                getDeleteMusicMenuItem(music, playlist, viewController));
        contextMenu.show(button, mouseEvent.getScreenX(), mouseEvent.getScreenY());
        currentContextMenu = contextMenu;
    }

    /**
     * Контекстное меню для каждого плейлиста в списке плейлистов
     *
     * @param playlist текущий плейлист
     * @param button нажатая кнопка
     */
    public void showPlaylistButtonCM(Playlist playlist, Button button, MouseEvent mouseEvent, ViewController viewController) {
        currentContextMenu.hide();
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems().addAll(
                getRenameMenuItem(button, viewController),
                getDeletePlaylistMenuItem(playlist, button, viewController),
                getPlayMenuItem(button));
        contextMenu.show(button, mouseEvent.getScreenX(), mouseEvent.getScreenY());
        currentContextMenu = contextMenu;
    }

    /**
     * меню в контекстном меню для проигрывания целого плейлиста
     *
     * @param button нажатая кнопка
     */
    private MenuItem getPlayMenuItem(Button button) {
        MenuItem playMenuItem = new MenuItem("Play playlist");
        Playlist playlist = playlistManager.getPlaylistByName(button.getText());
        playMenuItem.setOnAction(actionEvent -> {
            if (!playlist.getMusicArray().isEmpty()) {
                mediaController.playMusicInPlaylist(playlist.getMusicArray().get(0), playlist);
            }
        });
        return playMenuItem;
    }

    /**
     * меню в контекстном меню для удаления песни в плейлисте
     *
     * @param music    текущая песня
     * @param playlist текущий плейлист
     */
    private MenuItem getDeleteMusicMenuItem(Music music, Playlist playlist, ViewController viewController) {
        MenuItem deleteMusicMenuItem = new MenuItem("Delete music");
        deleteMusicMenuItem.setOnAction(actionEvent -> {
            playlist.delete(music);
            viewController.removeSongFromPlaylist(music);
        });
        return deleteMusicMenuItem;
    }

    /**
     * меню в контекстном меню для проигрывания музыки в плейлисте
     *
     * @param music    текущая песня
     * @param playlist текущий плейлист
     */
    private MenuItem getPlayMusicInPlaylistMenuItem(Music music, Playlist playlist) {
        MenuItem playMusicMenuItem = new MenuItem("Play music");
        playMusicMenuItem.setOnAction(actionEvent -> mediaController.playMusicInPlaylist(music, playlistManager.getPlaylistByName(playlist.getName())));
        return playMusicMenuItem;
    }

    /**
     * меню в контекстном меню для удаления плейлиста
     *
     * @param playlist текущий плейлист
     * @param button нажатая кнопка
     */
    private MenuItem getDeletePlaylistMenuItem(Playlist playlist, Button button, ViewController viewController) {
        MenuItem deletePlaylistMenuItem = new MenuItem("Delete playlist");
        deletePlaylistMenuItem.setOnAction(event -> {
            playlistManager.delete(playlist);
            viewController.removePlaylist(playlist, button);
        });
        return deletePlaylistMenuItem;
    }

    /**
     * меню в контекстном меню для переименовывания плейлиста
     *
     * @param button нажатая кнопка
     */
    private MenuItem getRenameMenuItem(Button button, ViewController viewController) {
        MenuItem renamePlaylistMenuItem = new MenuItem("Rename playlist");
        renamePlaylistMenuItem.setOnAction(actionEvent -> viewController.renamePlaylistButton(button));
        return renamePlaylistMenuItem;
    }
}
