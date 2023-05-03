package com.td.player;

import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Map;

public class Controller {
    @FXML
    private VBox listVBox;

    private FileManager fileManager;
    private ArrayList<Music> musicArrayList;
    private final Object object = new Object();

    @FXML
    private void initialize() {
        fileManager = new FileManager();

        musicArrayList = fileManager.getMusicList();
        for (Music music : musicArrayList) {
            music.getMediaPlayer().setOnReady(() -> {
                ObservableMap<String, Object> metadata = music.getMedia().getMetadata();
                for (Map.Entry<String, Object> item : metadata.entrySet()) {
                    if (item.getKey().equals("artist")) {
                        music.setArtist(item.getValue().toString());
                    } else if (item.getKey().equals("title")) {
                        music.setTitle(item.getValue().toString());
                    }
                }

                if (music.getTitle() == null) {
                    String[] strings = music.getMedia().getSource().split("/");
                    String name = strings[strings.length - 1].
                            replace("%20", " ").
                            replace(".mp3", "");
                    music.setTitle(name);
                }
                if (music.getArtist() == null) {
                    music.setArtist("(No data)");
                }

                Label artistLabel = new Label(music.getArtist());
                Label titleLabel = new Label(music.getTitle());

                HBox hBox = new HBox(titleLabel, artistLabel);
                hBox.setSpacing(5);
                hBox.setStyle("-fx-border-color: #555555; -fx-border-width: 2");
                listVBox.getChildren().add(hBox);
            });
        }
    }

    @FXML
    private void onSelectButtonClick() {
        fileManager.selectDirectory();
    }
}