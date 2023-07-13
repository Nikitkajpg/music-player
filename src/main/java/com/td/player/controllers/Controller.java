package com.td.player.controllers;

import com.td.player.elements.Music;
import com.td.player.managers.DirectoryManager;
import com.td.player.managers.MusicManager;
import com.td.player.managers.PlaylistManager;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.WindowEvent;

import java.util.ArrayList;

//todo Тестирование:
// 1. файлы не созданы
// 2. создана только папка
// 3. созданы папка и пустой directories
// 4. созданы папка и полный directories

//TODO:
// 1. работа с плейлистами
// 2. вывод ошибок на экран
// 3. уникальный id для песен
// 4. генерация уровня песен на основе предпочтений
// 5. при нажатии на кнопку play проигрывается активный плейлист. если активных
// плейлистов нет, то проигрывается плейлист по умолчанию
// 6. при нажатии на трек правой кнопкой мыши можно удалить трек из компьютера

public class Controller {
    @FXML
    private VBox musicListVBox, dirsListVBox, playlistVBox;

    @FXML
    private Button playButton;

    private FileController fileController;
    private DirectoryManager directoryManager;
    private MusicManager musicManager;
    private PlaylistManager playlistManager;
    private MediaController mediaController;
    private ViewController viewController;

    @FXML
    private void initialize() {                                             // инициализация
        musicManager = new MusicManager();
        directoryManager = new DirectoryManager();
        playlistManager = new PlaylistManager();
        mediaController = new MediaController(musicManager, playButton);
        viewController = new ViewController(directoryManager, musicManager, dirsListVBox, musicListVBox, mediaController);
        fileController = new FileController(directoryManager, musicManager);                     // создание папочной структуры
        viewController.update();                                                     // вывод обновленных списков на экран
    }

    // при закрытии окна вся информация записывается в файлы
    private final EventHandler<WindowEvent> closeEventHandler = windowEvent -> fileController.writeAllInf();

    public EventHandler<WindowEvent> getCloseEventHandler() {
        return closeEventHandler;
    }

    @FXML
    private void onSelectButtonClick() {
        fileController.selectDirectory();
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
}