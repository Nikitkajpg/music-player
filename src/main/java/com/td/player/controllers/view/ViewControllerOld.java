package com.td.player.controllers.view;

import com.td.player.controllers.Controller;
import com.td.player.elements.Directory;
import com.td.player.elements.Playlist;
import com.td.player.elements.Track;
import com.td.player.util.Mode;
import javafx.scene.control.Button;
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
    private ContextMenuController contextMenuControllerOld;


    /**
     * Конструктор отображает все списки {@link Directory}, {@link Track} и {@link Playlist}.
     */
    public ViewControllerOld(Controller controller) {
        this.controller = controller;
//        contextMenuControllerOld = new ContextMenuControllerOld(controller);
        showLists();
    }

    public void showLists() {
        showDirectories();
        showTrack();
        showPlaylists();
    }

    public void showDirectories() {

    }

    public void showTrack() {
//
    }

    public void showPlaylists() {

    }

//    public void addPlaylist() {
//        controller.addPlaylistHBox.getChildren().remove(controller.addPlaylistHBox.getChildren().size() - 1);
//        TextField textField = new TextField();
//        textField.setPromptText("Playlist name...");
//        textField.setOnKeyPressed(keyEvent -> {
//            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
//                String playlistName = textField.getText();
//                if (playlistName != null && !playlistName.equals("") && controller.getPlaylistManager().isUnique(playlistName)) {
////                    controller.getViewController().createPlaylistNameButton(playlistName);
//                    controller.addPlaylistHBox.getChildren().remove(textField);
//                    controller.addPlaylistHBox.getChildren().add(controller.playlistsLabel);
//                }
//            }
//        });
//        controller.addPlaylistHBox.getChildren().add(textField);
//    }

    public void removeTrackFromPlaylist(Track track) {
//        for (int i = 0; i < controller.playlistTrackVBox.getChildren().size() - 1; i++) {
//            VBox vBox = (VBox) controller.playlistTrackVBox.getChildren().get(i);
//            Button trackLabel = (Button) vBox.getChildren().get(0);
//            if (trackLabel.getText().equals(track.getTitle())) {
//                controller.playlistTrackVBox.getChildren().remove(vBox);
//            }
//        }
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
//                    controller.playlistTrackVBox.getChildren().clear();
                }
            }
        }
    }

    /**
     * Метод для присвоения нового названия плейлиста при переименовании
     */
    public void renamePlaylist(String playlistName) {
        controller.getPlaylistManager().renamePlaylist(currentPlaylistButton.getText(), playlistName);
        currentPlaylistButton.setText(playlistName);
        controller.addPlaylistButton.setDisable(false);
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
