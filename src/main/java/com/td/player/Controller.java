package com.td.player;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class Controller {
    @FXML
    private VBox musicListVBox, fileListVBox, playlistVBox;

    private FileManager fileManager;

    @FXML
    private void initialize() {
        fileManager = new FileManager(musicListVBox, fileListVBox);
        showFileList();
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

        createPlaylist();
    }

    private void createPlaylist() {
        String allMusicPlaylist = "All music";
        Accordion accordion = new Accordion();
        playlistVBox.getChildren().add(accordion);
        accordion.getPanes().add(new TitledPane(allMusicPlaylist, new VBox()));

        ArrayList<String> playlistLinesArrayList = fileManager.getPlaylistArrayList();
        String[] playlistNames = playlistLinesArrayList.get(0).split("::");
        for (String playlistName : playlistNames) {
            accordion.getPanes().add(new TitledPane(playlistName, new VBox()));
        }

        TitledPane allMusicTitledPane = accordion.getPanes().get(0);
        for (int i = 1; i < playlistLinesArrayList.size(); i++) {
            String[] params = playlistLinesArrayList.get(i).split("::");
            VBox vBox = (VBox) allMusicTitledPane.getContent();

//            ContextMenu contextMenu = new ContextMenu();
//            MenuItem menuItem1 = new MenuItem("Menu 1");
//            contextMenu.getItems().add(menuItem1);

            Label label = new Label(params[1].replace(".mp3", ""));
//            label.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
//                if (mouseEvent.getButton() == MouseButton.SECONDARY) {
//                    contextMenu.show(label, mouseEvent.getScreenX(), mouseEvent.getScreenY());
//                }
//            });

            vBox.getChildren().add(label);
        }

        for (int i = 1; i < playlistLinesArrayList.size(); i++) {
            for (int j = 1; j < accordion.getPanes().size(); j++) {
                TitledPane titledPane = accordion.getPanes().get(j);
                if (playlistLinesArrayList.get(i).contains(titledPane.getText())) {
                    String[] params = playlistLinesArrayList.get(i).split("::");
                    VBox vBox = (VBox) titledPane.getContent();

//                    ContextMenu contextMenu = new ContextMenu();
//                    MenuItem menuItem1 = new MenuItem("Menu 1");
//                    contextMenu.getItems().add(menuItem1);

                    Label label = new Label(params[1].replace(".mp3", ""));
//                    label.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
//                        if (mouseEvent.getButton() == MouseButton.SECONDARY) {
//                            contextMenu.show(label, mouseEvent.getScreenX(), mouseEvent.getScreenY());
//                        }
//                    });

                    vBox.getChildren().add(label);
                }
            }
        }
    }

    private void showFileList() {
        ArrayList<String> pathArray = fileManager.getPathArray();
        for (String path : pathArray) {
            fileListVBox.getChildren().add(new Label(path));
        }
    }

    @FXML
    private void onSelectButtonClick() {
        fileManager.selectDirectory();
    }
}