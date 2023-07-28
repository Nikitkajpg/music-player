package com.td.player.controllers;

import com.td.player.Player;
import com.td.player.managers.DirectoryManager;
import com.td.player.managers.MusicManager;
import com.td.player.managers.PlaylistManager;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Controller {
    @FXML
    private VBox musicListVBox, dirsListVBox, playlistVBox, dirVBox;

    @FXML
    private BorderPane topMenuBorderPane;

    @FXML
    private Button playButton, addDirButton, addPlaylistButton, renamePlaylistButton;

    @FXML
    private Accordion accordion;

    @FXML
    private TextField textField;

    @FXML
    private ScrollPane dirScrollPane, playlistScrollPane, musicScrollPane;

    private FileController fileController;
    private DirectoryManager directoryManager;
    private MusicManager musicManager;
    private PlaylistManager playlistManager;
    private MediaController mediaController;
    private ViewController viewController;

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private void initialize() {
        musicManager = new MusicManager();
        directoryManager = new DirectoryManager();
        playlistManager = new PlaylistManager();
        mediaController = new MediaController(musicManager, playButton);
        // создание папочной структуры, первичное заполнение списков
        fileController = new FileController(directoryManager, musicManager, playlistManager);
        // вывод обновленных списков на экран
        viewController = new ViewController(directoryManager, musicManager, playlistManager, dirsListVBox,
                musicListVBox, accordion, mediaController, addPlaylistButton, textField, renamePlaylistButton);

        widthProperties();
    }

    private void widthProperties() {
        addDirButton.prefWidthProperty().bind(dirScrollPane.widthProperty());
        dirVBox.prefWidthProperty().bind(dirScrollPane.widthProperty());
        musicListVBox.prefWidthProperty().bind(musicScrollPane.widthProperty());
        playlistVBox.prefWidthProperty().bind(playlistScrollPane.widthProperty());
        textField.prefWidthProperty().bind(playlistScrollPane.widthProperty());
    }

    public void dragStage(Stage stage) {
        topMenuBorderPane.setOnMousePressed(mouseEvent -> {
            xOffset = mouseEvent.getSceneX();
            yOffset = mouseEvent.getSceneY();
        });
        topMenuBorderPane.setOnMouseDragged(mouseEvent -> {
            stage.setX(mouseEvent.getScreenX() - xOffset);
            stage.setY(mouseEvent.getScreenY() - yOffset);
        });
    }

    @FXML
    private void onAddDirButtonClick() {
        fileController.selectDirectory();
        playlistManager.updateDefaultPlaylist(musicManager);
        viewController.update(); // вывод обновленных списков на экран
    }

    @FXML
    private void onAddPlaylistButtonClick() {
        String playlistName = textField.getText();
        if (playlistName != null && !playlistName.equals("")) {
            viewController.createTitledPane(playlistName);
            textField.clear();
        }
    }

    @FXML
    private void onTextFieldKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            if (!addPlaylistButton.isDisable()) {
                onAddPlaylistButtonClick();
            } else {
                onRenamePlaylistButtonClick();
            }
        }
    }

    @FXML
    private void onRenamePlaylistButtonClick() {
        String playlistName = textField.getText();
        if (playlistName != null && !playlistName.equals("")) {
            viewController.renameTitledPane(playlistName);
            textField.clear();
        } else {
            addPlaylistButton.setDisable(false);
            renamePlaylistButton.setDisable(true);
        }
    }

    @FXML
    private void onPlayButtonClick() {
        if (playButton.getText().equals("Pause")) {
            mediaController.pause();
        } else {
            mediaController.play();
        }
    }

    @FXML
    private void onStopButtonClick() {
        mediaController.stop();
    }

    @FXML
    private void onExitButtonClick() {
        fileController.writeAllInf();
        Player.stage.close();
    }

    @FXML
    private void onMinimizeButtonClick() {
        Player.stage.setIconified(true);
    }
}