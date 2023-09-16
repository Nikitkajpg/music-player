package com.td.player.controllers;

import com.td.player.elements.Directory;
import com.td.player.elements.Music;
import com.td.player.elements.Playlist;
import com.td.player.managers.MusicManager;
import com.td.player.managers.PlaylistManager;
import com.td.player.util.Actions;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyCode;
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
        Button button = new Button(music.getFileName());
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
            Button nameButton = getNameButton(music, playlist);
            playlistMusicVBox.getChildren().add(nameButton);
            nameButton.prefWidthProperty().bind(controller.playlistMusicScrollPane.widthProperty().subtract(17));
        }
        if (!playlist.getName().equals("All music")) {
            playlistMusicVBox.getChildren().add(new Label("Put song here..."));
        }
    }

    /**
     * Метод создает кнопку для отображения названия и исполнителя песни {@link Music}
     *
     * @param music    объект Music
     * @param playlist объект Playlist
     * @return {@link Button}
     */
    public Button getNameButton(Music music, Playlist playlist) {
        Button button = new Button(music.getTitle() + "\n" + music.getArtist());
        button.setOnAction(actionEvent -> mediaController.playMusicInPlaylist(music, playlist));
        button.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                contextMenuController.showPlaylistMusicButtonCM(playlist, button, music, mouseEvent, this);
            }
        });
        return button;
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
        button.prefWidthProperty().bind(controller.playlistNamesScrollPane.widthProperty().subtract(17));
        playlistNamesVBox.getChildren().add(button);
    }

    /**
     * Метод для присвоения нового названия плейлиста при переименовании
     *
     * @param playlistName название плейлиста
     */
    public void renamePlaylist(String playlistName) {
        playlistManager.renamePlaylist(currentPlaylistButton.getText(), playlistName);
        currentPlaylistButton.setText(playlistName);
        controller.addPlaylistButton.setDisable(false);
    }

    private void dragAndDrop(Playlist playlist) {
        playlistMusicVBox.setOnDragOver(dragEvent -> Actions.onDragOver(dragEvent, playlistMusicVBox, playlist));
        playlistMusicVBox.setOnDragEntered(dragEvent -> Actions.onDragEntered(dragEvent, playlistMusicVBox, playlist));
        playlistMusicVBox.setOnDragExited(dragEvent -> Actions.onDragExited(dragEvent, playlistMusicVBox, playlist));
        playlistMusicVBox.setOnDragDropped(dragEvent -> Actions.onDragDropped(dragEvent, playlistManager, playlist, musicManager, playlistMusicVBox, this, controller.playlistMusicScrollPane));
    }

    /**
     * Метод для начала переименования плейлиста
     * <p> Кнопка меняется на текстовое поле.
     * После нажатия "Enter" текстовое поле меняется на кнопку в прежнее состояние.
     *
     * @param button кнопка для переименования
     */
    public void renamePlaylistButton(Button button) {
        TextField textField = new TextField();
        textField.setTooltip(new Tooltip("Press \"Enter\" to rename"));
        textField.setText(button.getText());
        int index = playlistNamesVBox.getChildren().indexOf(button);
        playlistNamesVBox.getChildren().remove(button);
        playlistNamesVBox.getChildren().add(index, textField);
        textField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                String newPlaylistName = textField.getText();
                if (newPlaylistName != null && !newPlaylistName.equals("") && playlistManager.isUnique(newPlaylistName)) {
                    renamePlaylist(newPlaylistName);
                    button.setText(newPlaylistName);
                    playlistNamesVBox.getChildren().remove(textField);
                    playlistNamesVBox.getChildren().add(index, button);
                }
            }
        });
    }
}
