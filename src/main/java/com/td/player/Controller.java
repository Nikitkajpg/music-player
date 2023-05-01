package com.td.player;

import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.ArrayList;

public class Controller {
    @FXML
    private VBox listVBox;

    private Media media;
    private MediaPlayer mediaPlayer;

    private FileManager fileManager;

    @FXML
    private void initialize() {
        fileManager = new FileManager();



//        media = new Media("file:///D:/другая%20ересь/музон/alt-j-taro.mp3");
//        mediaPlayer = new MediaPlayer(media);
    }

    @FXML
    protected void onPlayButtonClick() {
        mediaPlayer.play();
        ObservableMap<String, Object> map = media.getMetadata();

        for (String key : map.keySet()) {
            System.out.println(key + ", " + map.get(key));
        }
    }

    @FXML
    private void onSelectButtonClick() {
        fileManager.selectDirectory();
    }
}