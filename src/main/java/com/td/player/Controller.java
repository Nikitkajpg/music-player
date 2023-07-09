package com.td.player;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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

    private FileManager fileManager;
    private MusicController musicController;
    private MediaController mediaController;

    private ArrayList<String> pathToDirectoriesArray;

    @FXML
    private void initialize() {                                             // инициализация
        musicController = new MusicController();
        mediaController = new MediaController(musicController, playButton);
        fileManager = new FileManager(musicController);                     // создание папочной структуры
        pathToDirectoriesArray = fileManager.getPathToDirectoriesArray();   // получение списка папок с музыкой
        fileManager.fillMusicArray();                                       // первоначальное заполнение списка музыки
        updateAllInf();                                                     // вывод обновленных списков на экран
    }

    private void updateAllInf() {
        updateDirs();   // вывод списка папок
        updateMusic();  // вывод списка музыки
//        showPlaylists(); //todo playlist
    }

    private void updateDirs() {
        dirsListVBox.getChildren().clear();
        for (String path : pathToDirectoriesArray) {
            dirsListVBox.getChildren().add(getDirLabel(path));
        }
    }

    private void updateMusic() {
        musicListVBox.getChildren().clear();
        for (Music music : musicController.getMusicArray()) {
            musicListVBox.getChildren().add(getTrackLabel(music.getFile().getName()));
        }
    }

    public Label getDirLabel(String path) {
        Label label = new Label(path);
        label.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                contextMenuForDir(label, mouseEvent);
            }
        });
        return label;
    }

    private void contextMenuForDir(Label label, MouseEvent mouseEvent) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem menuItem = new MenuItem("Delete");
        menuItem.setOnAction(actionEvent -> {
            dirsListVBox.getChildren().remove(label);       // удаление метки из списка меток путей
            pathToDirectoriesArray.remove(label.getText()); // удаление пути из списка путей
            musicController.deleteByPath(label.getText());  // удаление музыки из списка музыки
            updateMusic();                                  // вывод обновленного списка музыки на экран
        });
        contextMenu.getItems().add(menuItem);
        contextMenu.show(label, mouseEvent.getScreenX(), mouseEvent.getScreenY());
    }

    private Label getTrackLabel(String name) {
        Label label = new Label(name);
        label.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                if (mouseEvent.getClickCount() == 2) {
                    if (mediaController.getCurrentMediaPlayer() != null) {
                        mediaController.getCurrentMediaPlayer().stop();
                    }
                    mediaController.playByName(name);
                }
            }
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
//                contextMenuForTrack();
            }
        });
        return label;
    }

    // при закрытии окна вся информация записывается в файлы
    private final EventHandler<WindowEvent> closeEventHandler = windowEvent -> fileManager.writeAllInf();

    public EventHandler<WindowEvent> getCloseEventHandler() {
        return closeEventHandler;
    }

    @FXML
    private void onSelectButtonClick() {
        fileManager.selectDirectory();
        updateAllInf(); // вывод обновленных списков на экран
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