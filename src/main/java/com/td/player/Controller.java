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
// 1. запись всех данных в файл с сериализацией
// 2. работа с плейлистами
// 3. вывод ошибок на экран
// 4. уникальный id для песен
// 5. генерация уровня песен на основе предпочтений
// 6. при нажатии на кнопку play проигрывается активный плейлист. если активных
// плейлистов нет, то проигрывается плейлист по умолчанию
// 7. проигрывание песни по двойному клику

public class Controller {
    @FXML
    private VBox musicListVBox, dirsListVBox, playlistVBox;

    private FileManager fileManager;
    private MusicController musicController;

    private ArrayList<String> pathToDirectoriesArray;

    @FXML
    private void initialize() {
        // инициализация
        musicController = new MusicController();
        fileManager = new FileManager(musicController); // создание папочной структуры
        pathToDirectoriesArray = fileManager.getPathToDirectoriesArray(); // получение списка папок с музыкой
        fileManager.fillMusicArray(); // первоначальное заполнение списка музыки
        updateAllInf(); // вывод списков на экран
    }

    private void updateAllInf() {
        updateDirs();
        updateTracks();
//        showPlaylists(); //todo playlist
    }

    private void updateDirs() {
        dirsListVBox.getChildren().clear();
        for (String path : pathToDirectoriesArray) {
            dirsListVBox.getChildren().add(getDeleteLabel(path));
        }
    }

    private void updateTracks() {
        musicListVBox.getChildren().clear();
        for (Music music : musicController.getMusicArray()) {
            musicListVBox.getChildren().add(new Label(music.getFile().getName()));
        }
    }

    @FXML
    private void onSelectButtonClick() {
        fileManager.selectDirectory();
        updateAllInf();
    }

    public Label getDeleteLabel(String path) {
        Label label = new Label(path);
        label.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                contextMenu(label, mouseEvent);
            }
        });
        return label;
    }

    private void contextMenu(Label label, MouseEvent mouseEvent) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem menuItem = new MenuItem("Delete");
        menuItem.setOnAction(actionEvent -> {
            dirsListVBox.getChildren().remove(label);
            pathToDirectoriesArray.remove(label.getText());
            musicController.deleteAll(label.getText());
            updateTracks();
        });
        contextMenu.getItems().add(menuItem);
        contextMenu.show(label, mouseEvent.getScreenX(), mouseEvent.getScreenY());
    }

    private final EventHandler<WindowEvent> closeEventHandler = windowEvent -> fileManager.writeAllInf();

    public EventHandler<WindowEvent> getCloseEventHandler() {
        return closeEventHandler;
    }

            /*music.getMediaPlayer().setOnReady(() -> {
                ObservableMap<String, Object> metadata = music.getMedia().getMetadata();
                for (Map.Entry<String, Object> item : metadata.entrySet()) {
                    if (item.getKey().equals("artist")) {
                        music.setArtist(item.getValue().toString());
                    } else if (item.getKey().equals("title")) {
                        music.setTitle(item.getValue().toString());
                    }
                }

                if (music.getTitle() == null) {
                    music.setTitle(music.getFileName().replace(".mp3", ""));
                }
                if (music.getArtist() == null) {
                    music.setArtist("(No data)");
                }

                Label artistLabel = new Label(music.getArtist());
                Label titleLabel = new Label(music.getTitle());

                HBox hBox = new HBox(titleLabel, artistLabel);
                hBox.setSpacing(5);
                musicListVBox.getChildren().add(hBox);
            });*/

//        createPlaylist(playlistLineArray);
}