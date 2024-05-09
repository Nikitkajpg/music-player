package com.td.player.controllers.view;

import com.td.player.controllers.Controller;
import com.td.player.elements.Directory;
import com.td.player.elements.Playlist;
import com.td.player.elements.Track;
import com.td.player.util.Actions;
import com.td.player.util.Mode;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Класс для управления отображаемыми объектами
 */
@SuppressWarnings("FieldMayBeFinal")
public class ViewControllerOld {
    private Button currentPlaylistButton = new Button();

    private Controller controller;
    private ContextMenuControllerOld contextMenuControllerOld;

    private double xOffset = 0;
    private double yOffset = 0;

    /**
     * Конструктор отображает все списки {@link Directory}, {@link Track} и {@link Playlist}.
     */
    public ViewControllerOld(Controller controller) {
        this.controller = controller;
        contextMenuControllerOld = new ContextMenuControllerOld(controller);
        showLists();
    }

    public void showLists() {
        showDirectories();
        showTrack();
        showPlaylists();
    }

    public void showDirectories() {
        controller.directoriesVBox.getChildren().clear();
        for (Directory directory : controller.getDirectoryManager().getDirectoryArray()) {
            controller.directoriesVBox.getChildren().add(getDirButton(directory));
        }
    }

    public void showTrack() {
        controller.trackListVBox.getChildren().clear();
        for (Track track : controller.getTrackManager().getTrackArray()) {
            controller.trackListVBox.getChildren().add(getTrackButton(track));
        }
    }

    public void showPlaylists() {
        controller.playlistsVBox.getChildren().clear();
        controller.playlistTrackVBox.getChildren().clear();

        for (Playlist playlist : controller.getPlaylistManager().getPlaylistArray()) {
            Button button = new Button(playlist.getName());
            button.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                    contextMenuControllerOld.showPlaylistButtonCM(playlist, button, mouseEvent);
                }
            });
            button.setOnAction(actionEvent -> actionForShowingPlaylist(button, playlist));
            controller.playlistsVBox.getChildren().add(button);
        }
    }

    /**
     * Метод создает кнопку для отображения {@link Directory}.
     */
    private Button getDirButton(Directory currentDirectory) {
        Button button = new Button(currentDirectory.getPath());
        button.setOnAction(actionEvent -> Actions.openDirectory(button));
        button.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                contextMenuControllerOld.showDirCM(currentDirectory, button, mouseEvent);
            }
        });
        return button;
    }

    /**
     * Метод создает кнопку для отображения {@link Track}.
     * <p>Содержит метод, обнаруживающий {@link DragEvent}
     */
    private Button getTrackButton(Track track) {
        Button button = new Button(track.getFileName());
        button.setOnAction(actionEvent ->
                controller.getMediaController().playTrackInPlaylist(track, controller.getPlaylistManager().getDefaultPlaylist()));
        button.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                contextMenuControllerOld.showTrackCM(button, mouseEvent, track);
            }
        });
        button.setOnDragDetected(mouseEvent -> Actions.onDragDetected(mouseEvent, button));
        return button;
    }

    public void addPlaylist() {
        controller.addPlaylistHBox.getChildren().remove(controller.addPlaylistHBox.getChildren().size() - 1);
        TextField textField = new TextField();
        textField.setPromptText("Playlist name...");
        textField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                String playlistName = textField.getText();
                if (playlistName != null && !playlistName.equals("") && controller.getPlaylistManager().isUnique(playlistName)) {
                    controller.getViewController().createPlaylistNameButton(playlistName);
                    controller.addPlaylistHBox.getChildren().remove(textField);
                    controller.addPlaylistHBox.getChildren().add(controller.playlistsLabel);
                }
            }
        });
        controller.addPlaylistHBox.getChildren().add(textField);
    }

    public void clickingOnPreference() {
        if (controller.preferenceToggleButton.isSelected()) {
            enableMode(true, Mode.PREFERENCE);
        } else {
            enableMode(false, Mode.DEFAULT);
        }
    }

    public void clickingOnRandom() {
        if (controller.randomToggleButton.isSelected()) {
            enableMode(true, Mode.RANDOM);
        } else {
            enableMode(false, Mode.DEFAULT);
        }
    }

    private void enableMode(boolean disable, Mode newMode) {
        controller.splitPane.setDisable(disable);
        controller.getMediaController().setCurrentMode(newMode);
        controller.getMediaController().playInMode();
    }

    public void dragStage(Stage stage) {
        controller.topMenuBorderPane.setOnMousePressed(mouseEvent -> {
            xOffset = mouseEvent.getSceneX();
            yOffset = mouseEvent.getSceneY();
        });
        controller.topMenuBorderPane.setOnMouseDragged(mouseEvent -> {
            stage.setX(mouseEvent.getScreenX() - xOffset);
            stage.setY(mouseEvent.getScreenY() - yOffset);
        });
    }

    /**
     * Метод при нажатии на плейлист отображает его список музыки.
     * <p> Содержит метод для {@link DragEvent}. Список музыки {@link VBox} очищается при каждом вызове.
     */
    private void actionForShowingPlaylist(Button pressedPlaylistButton, Playlist currentPlaylist) {
        currentPlaylistButton = pressedPlaylistButton;
        dragAndDrop(currentPlaylist);
        controller.playlistTrackVBox.getChildren().clear();
        for (Track track : currentPlaylist.getTrackArray()) {
            Button nameButton = getNameButton(track, currentPlaylist);
            controller.playlistTrackVBox.getChildren().add(nameButton);
            nameButton.prefWidthProperty().bind(controller.playlistTrackScrollPane.widthProperty().subtract(17));
        }
        if (!currentPlaylist.getName().equals("All tracks")) {
            controller.playlistTrackVBox.getChildren().add(new Label("Put song here..."));
        }
    }

    /**
     * Метод создает кнопку для отображения названия и исполнителя песни {@link Track}
     */
    public Button getNameButton(Track track, Playlist playlist) {
        Button button = new Button(track.getTitle() + "\n" + track.getArtist());
        button.setOnAction(actionEvent -> controller.getMediaController().playTrackInPlaylist(track, playlist));
        button.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                contextMenuControllerOld.showPlaylistTrackButtonCM(playlist, button, track, mouseEvent);
            }
        });
        return button;
    }

    public void removeTrackFromPlaylist(Track track) {
        for (int i = 0; i < controller.playlistTrackVBox.getChildren().size() - 1; i++) {
            VBox vBox = (VBox) controller.playlistTrackVBox.getChildren().get(i);
            Button trackLabel = (Button) vBox.getChildren().get(0);
            if (trackLabel.getText().equals(track.getTitle())) {
                controller.playlistTrackVBox.getChildren().remove(vBox);
            }
        }
    }

    /**
     * Метод для удаления плейлиста. Если отображаются песни этого плейлиста, они будут очищены.
     */
    public void removePlaylist(Playlist playlistToDelete, Button pressedPlaylistButton) {
        for (int i = 0; i < controller.playlistsVBox.getChildren().size(); i++) {
            Button buttonToRemove = (Button) controller.playlistsVBox.getChildren().get(i);
            if (buttonToRemove.getText().equals(playlistToDelete.getName())) {
                controller.playlistsVBox.getChildren().remove(buttonToRemove);
                if (pressedPlaylistButton == currentPlaylistButton) {
                    controller.playlistTrackVBox.getChildren().clear();
                }
            }
        }
    }

    /**
     * Метод для создания кнопки плейлиста.
     */
    public void createPlaylistNameButton(String playlistName) {
        Button button = new Button(playlistName);
        Playlist playlist = controller.getPlaylistManager().createAndGetPlaylist(playlistName);
        button.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                contextMenuControllerOld.showPlaylistButtonCM(playlist, button, mouseEvent);
            }
        });
        actionForShowingPlaylist(button, playlist);
        button.prefWidthProperty().bind(controller.playlistsScrollPane.widthProperty().subtract(17));
        controller.playlistsVBox.getChildren().add(button);
    }

    /**
     * Метод для присвоения нового названия плейлиста при переименовании
     */
    public void renamePlaylist(String playlistName) {
        controller.getPlaylistManager().renamePlaylist(currentPlaylistButton.getText(), playlistName);
        currentPlaylistButton.setText(playlistName);
        controller.addPlaylistButton.setDisable(false);
    }

    private void dragAndDrop(Playlist playlist) {
        controller.playlistTrackVBox.setOnDragOver(dragEvent ->
                Actions.onDragOver(dragEvent, controller.playlistTrackVBox, playlist));
        controller.playlistTrackVBox.setOnDragEntered(dragEvent ->
                Actions.onDragEntered(dragEvent, controller.playlistTrackVBox, playlist));
        controller.playlistTrackVBox.setOnDragExited(dragEvent ->
                Actions.onDragExited(dragEvent, controller.playlistTrackVBox, playlist));
        controller.playlistTrackVBox.setOnDragDropped(dragEvent ->
                Actions.onDragDropped(controller, dragEvent, playlist, controller.playlistTrackVBox, controller.playlistTrackScrollPane));
    }

    /**
     * Метод для начала переименования плейлиста
     * <p> Кнопка меняется на текстовое поле.
     * После нажатия "Enter" текстовое поле меняется на кнопку в прежнее состояние.
     */
    public void renamePlaylistButton(Button buttonToRename) {
        TextField textField = new TextField();
        textField.setTooltip(new Tooltip("Press \"Enter\" to rename"));
        textField.setText(buttonToRename.getText());
        int index = controller.playlistsVBox.getChildren().indexOf(buttonToRename);
        controller.playlistsVBox.getChildren().remove(buttonToRename);
        controller.playlistsVBox.getChildren().add(index, textField);
        textField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                String newPlaylistName = textField.getText();
                if (newPlaylistName != null && !newPlaylistName.equals("") && controller.getPlaylistManager().isUnique(newPlaylistName)) {
                    renamePlaylist(newPlaylistName);
                    buttonToRename.setText(newPlaylistName);
                    controller.playlistsVBox.getChildren().remove(textField);
                    controller.playlistsVBox.getChildren().add(index, buttonToRename);
                }
            }
        });
    }
}
