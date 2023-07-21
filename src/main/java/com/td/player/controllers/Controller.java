package com.td.player.controllers;

import com.td.player.managers.DirectoryManager;
import com.td.player.managers.MusicManager;
import com.td.player.managers.PlaylistManager;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.WindowEvent;

// todo окошки с delete не убираются при повторном вызове

public class Controller {
    @FXML
    private VBox musicListVBox, dirsListVBox;

    @FXML
    private Button playButton;

    @FXML
    private Accordion accordion;

    @FXML
    private TextField textField;

    private FileController fileController;
    private DirectoryManager directoryManager;
    private MusicManager musicManager;
    private PlaylistManager playlistManager;
    private MediaController mediaController;
    private ViewController viewController;

    @FXML
    private void initialize() {
        musicManager = new MusicManager();
        directoryManager = new DirectoryManager();
        playlistManager = new PlaylistManager();
        mediaController = new MediaController(musicManager, playButton);
        // создание папочной структуры, первичное заполнение списков
        fileController = new FileController(directoryManager, musicManager, playlistManager);
        // вывод обновленных списков на экран
        viewController = new ViewController(directoryManager, musicManager, playlistManager, dirsListVBox, musicListVBox, accordion, mediaController);
    }

    // при закрытии окна вся информация записывается в файлы
    private final EventHandler<WindowEvent> closeEventHandler = windowEvent -> fileController.writeAllInf();

    public EventHandler<WindowEvent> getCloseEventHandler() {
        return closeEventHandler;
    }

    @FXML
    private void onSelectButtonClick() {
        fileController.selectDirectory();
        playlistManager.updateDefaultPlaylist(musicManager);
        viewController.update(); // вывод обновленных списков на экран
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
    private void onNewPlaylistButtonClick() {
        String playlistName = textField.getText();
        if (playlistName != null && !playlistName.equals("")) {
            viewController.createTitledPane(playlistName);
            textField.clear();
        }
    }

    @FXML
    private void onTextFieldKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            onNewPlaylistButtonClick();
        }
    }
}