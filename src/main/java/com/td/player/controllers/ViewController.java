package com.td.player.controllers;

import com.td.player.elements.Directory;
import com.td.player.elements.Music;
import com.td.player.elements.Playlist;
import com.td.player.managers.MusicManager;
import com.td.player.managers.PlaylistManager;
import com.td.player.util.Actions;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;

/**
 * Класс для управления отображаемыми объектами
 */
@SuppressWarnings("FieldMayBeFinal")
public class ViewController {
    private Button currentPlaylistButton = new Button();
    private VBox playlistNamesVBox, playlistMusicVBox;

    private Controller controller;
    private ContextMenuController contextMenuController;
    private MediaController mediaController;

    private MusicManager musicManager;
    private PlaylistManager playlistManager;

    /**
     * Конструктор отображает все списки {@link Directory}, {@link Music} и {@link Playlist}.
     */
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

    /**
     * Метод отображает пути к папкам в виде {@link Button}
     */
    public void showDirectories() {
        controller.dirsListVBox.getChildren().clear();
        for (Directory directory : controller.getDirectoryManager().getDirectoryArray()) {
            controller.dirsListVBox.getChildren().add(getDirButton(directory));
        }
    }

    /**
     * Метод отображает названия файлов музыки в виде {@link Button}
     */
    public void showMusic() {
        controller.musicListVBox.getChildren().clear();
        for (Music music : musicManager.getMusicArray()) {
            controller.musicListVBox.getChildren().add(getMusicButton(music));
        }
    }

    /**
     * Метод отображает плейлисты и их содержимое в виде {@link Button}.
     * <p>Перед отображением очищает {@link #playlistMusicVBox} и {@link #playlistNamesVBox}.
     */
    public void showPlaylists() {
        playlistNamesVBox.getChildren().clear();
        playlistMusicVBox.getChildren().clear();

        for (Playlist playlist : playlistManager.getPlaylistArray()) {
            Button button = new Button(playlist.getName());
            button.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                    contextMenuController.showPlaylistButtonCM(playlist, button, mouseEvent, this);
                }
            });
            button.setOnAction(actionEvent -> actionForShowingPlaylist(button, playlist));
            playlistNamesVBox.getChildren().add(button);
        }
    }

    /**
     * Метод создает кнопку для отображения {@link Directory}.
     *
     * @param directory текущая папка
     * @return {@link Button}
     */
    private Button getDirButton(Directory directory) {
        Button button = new Button(directory.getPath());
        button.setOnAction(actionEvent -> Actions.openDirectory(button));
        button.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                contextMenuController.showDirCM(directory, button, mouseEvent, this);
            }
        });
        return button;
    }

    /**
     * Метод создает кнопку для отображения {@link Music}.
     * <p>Содержит метод, обнаруживающий {@link DragEvent}
     *
     * @param music объект {@link Music}
     * @return {@link Button}
     */
    private Button getMusicButton(Music music) {
        Button button = new Button(music.getTitle());
        button.setOnAction(actionEvent -> mediaController.playMusicInPlaylist(music, playlistManager.getDefaultPlaylist()));
        button.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                contextMenuController.showMusicCM(button, mouseEvent, music);
            }
        });
        button.setOnDragDetected(mouseEvent -> Actions.onDragDetected(mouseEvent, button));
        return button;
    }

    /**
     * Метод создает кнопку для отображения названия {@link Music}.
     *
     * @param music    объект {@link Music}
     * @param playlist объект {@link Playlist}
     * @return {@link Button}
     */
    public Button getPlaylistMusicTitleButton(Music music, Playlist playlist) {
        Button titleButton = new Button(music.getTitle());
        titleButton.setOnAction(actionEvent -> mediaController.playMusicInPlaylist(music, playlist));
        titleButton.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                contextMenuController.showPlaylistMusicButtonCM(playlist, titleButton, music, mouseEvent, this);
            }
        });
        return titleButton;
    }

    /**
     * Метод создает кнопку для отображения исполнителя {@link Music}.
     *
     * @param playlist объект {@link Playlist}
     * @param music    объект {@link Music}
     * @return {@link Button}
     */
    public Button getPlaylistMusicArtistButton(Playlist playlist, Music music) {
        Button artistButton = new Button(music.getArtist());
        artistButton.setOnAction(actionEvent -> mediaController.playMusicInPlaylist(music, playlist));
        artistButton.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                contextMenuController.showPlaylistMusicButtonCM(playlist, artistButton, music, mouseEvent, this);
            }
        });
        return artistButton;
    }

    /**
     * Метод при нажатии на плейлист отображает его список музыки.
     * <p> Содержит метод для {@link DragEvent}. Список музыки {@link VBox} очищается при каждом вызове.
     *
     * @param button   нажатая кнопка плейлиста
     * @param playlist текущий плейлист
     */
    private void actionForShowingPlaylist(Button button, Playlist playlist) {
        currentPlaylistButton = button;
        dragAndDrop(playlist);
        playlistMusicVBox.getChildren().clear();
        for (Music music : playlist.getMusicArray()) {
            VBox vBox = new VBox();
            Button titleButton = getPlaylistMusicTitleButton(music, playlist);
            Button artistButton = getPlaylistMusicArtistButton(playlist, music);
            vBox.getChildren().addAll(titleButton, artistButton);
            playlistMusicVBox.getChildren().add(vBox);
            titleButton.prefWidthProperty().bind(controller.playlistMusicScrollPane.widthProperty().subtract(17));
            artistButton.prefWidthProperty().bind(controller.playlistMusicScrollPane.widthProperty().subtract(22));
        }
        if (!playlist.getName().equals("All music")) {
            playlistMusicVBox.getChildren().add(new Label("Put song here..."));
        }
    }

    public void removeSongFromPlaylist(Music music) {
        for (int i = 0; i < playlistMusicVBox.getChildren().size() - 1; i++) {
            VBox vBox = (VBox) playlistMusicVBox.getChildren().get(i);
            Button musicLabel = (Button) vBox.getChildren().get(0);
            if (musicLabel.getText().equals(music.getTitle())) {
                playlistMusicVBox.getChildren().remove(vBox);
            }
        }
    }

    /**
     * Метод для удаления плейлиста. Если отображаются песни этого плейлиста, они будут очищены.
     *
     * @param playlist плейлист для удаления
     * @param button   нажатая кнопка плейлиста
     */
    public void removePlaylist(Playlist playlist, Button button) {
        for (int i = 0; i < playlistNamesVBox.getChildren().size(); i++) {
            Button buttonToRemove = (Button) playlistNamesVBox.getChildren().get(i);
            if (buttonToRemove.getText().equals(playlist.getName())) {
                playlistNamesVBox.getChildren().remove(buttonToRemove);
                if (button == currentPlaylistButton) {
                    playlistMusicVBox.getChildren().clear();
                }
            }
        }
    }

    /**
     * Метод для создания кнопки плейлиста.
     *
     * @param playlistName название плейлиста
     */
    public void createPlaylistNameButton(String playlistName) {
        Button button = new Button(playlistName);
        Playlist playlist = playlistManager.createAndGetPlaylist(playlistName);
        button.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                contextMenuController.showPlaylistButtonCM(playlist, button, mouseEvent, this);
            }
        });
        actionForShowingPlaylist(button, playlist);
        playlistNamesVBox.getChildren().add(button);
    }

    /**
     * Метод для присвоения нового названия плейлиста при переименовании
     * (начало переименовывания - {@link #startRename(boolean, boolean, Button)})
     *
     * @param playlistName название плейлиста
     */
    public void renamePlaylist(String playlistName) {
        playlistManager.renamePlaylist(currentPlaylistButton.getText(), playlistName);
        currentPlaylistButton.setText(playlistName);
        controller.renamePlaylistButton.setDisable(true);
        controller.addPlaylistButton.setDisable(false);
    }

    private void dragAndDrop(Playlist playlist) {
        playlistMusicVBox.setOnDragOver(dragEvent -> Actions.onDragOver(dragEvent, playlistMusicVBox, playlist));
        playlistMusicVBox.setOnDragEntered(dragEvent -> Actions.onDragEntered(dragEvent, playlistMusicVBox, playlist));
        playlistMusicVBox.setOnDragExited(dragEvent -> Actions.onDragExited(dragEvent, playlistMusicVBox, playlist));
        playlistMusicVBox.setOnDragDropped(dragEvent -> Actions.onDragDropped(dragEvent, playlistManager, playlist, musicManager, playlistMusicVBox, this));
    }

    /**
     * Метод для начала переименования плейлиста
     *
     * @param renameDisable флаг для кнопки {@link Controller#renamePlaylistButton}
     * @param addDisable    флаг для кнопки {@link Controller#addPlaylistButton}
     * @param button        кнопка для переименования
     */
    public void startRename(boolean renameDisable, boolean addDisable, Button button) {
        controller.renamePlaylistButton.setDisable(renameDisable);
        controller.addPlaylistButton.setDisable(addDisable);
        controller.textField.requestFocus();
        currentPlaylistButton = button;
    }
}
