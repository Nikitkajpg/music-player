package com.td.player;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class Controller {
    @FXML
    private VBox musicListVBox, fileListVBox;

    private FileManager fileManager;
    private ArrayList<Music> musicArrayList;

    @FXML
    private void initialize() {
        fileManager = new FileManager();

        musicListVBox.getChildren().add(new Label("File name"));
        musicArrayList = fileManager.getMusicList();

        ArrayList<String> pathArray = fileManager.getPathArray();
        for (String path : pathArray) {
            fileListVBox.getChildren().add(new Label(path));
        }
        for (Music music : musicArrayList) {
//            music.getMediaPlayer().setOnReady(() -> {
//                ObservableMap<String, Object> metadata = music.getMedia().getMetadata();
//                for (Map.Entry<String, Object> item : metadata.entrySet()) {
//                    if (item.getKey().equals("artist")) {
//                        music.setArtist(item.getValue().toString());
//                    } else if (item.getKey().equals("title")) {
//                        music.setTitle(item.getValue().toString());
//                    }
//                }
//
//                if (music.getTitle() == null) {
//                    music.setTitle(music.getFileName().replace(".mp3", ""));
//                }
//                if (music.getArtist() == null) {
//                    music.setArtist("(No data)");
//                }
//
//                Label artistLabel = new Label(music.getArtist());
//                Label titleLabel = new Label(music.getTitle());
//
//                HBox hBox = new HBox(titleLabel, artistLabel);
//                hBox.setSpacing(5);
//                musicListVBox.getChildren().add(hBox);
//            });
            musicListVBox.getChildren().add(new Label(music.getFileName().replace(".mp3", "")));
        }
    }

    @FXML
    private void onSelectButtonClick() {
        fileManager.selectDirectory();
    }
}