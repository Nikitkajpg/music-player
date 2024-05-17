package com.td.player.controllers.view;

import com.td.player.controllers.Controller;
import com.td.player.elements.Directory;
import com.td.player.elements.Playlist;
import com.td.player.elements.Track;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;

/**
 * Класс для управления отображаемыми объектами
 */
@SuppressWarnings("FieldMayBeFinal")
public class ViewControllerOld {
    private Button currentPlaylistButton = new Button();

    private Controller controller;

    /**
     * Конструктор отображает все списки {@link Directory}, {@link Track} и {@link Playlist}.
     */
    public ViewControllerOld(Controller controller) {
        this.controller = controller;
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
