package com.td.player.controllers;

import com.td.player.elements.Directory;
import com.td.player.elements.Music;
import com.td.player.managers.DirectoryManager;
import com.td.player.managers.MusicManager;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

@SuppressWarnings("FieldMayBeFinal")
public class ViewController {
    private VBox dirsListVBox;
    private VBox musicListVBox;

    private DirectoryManager directoryManager;
    private MusicManager musicManager;

    private MediaController mediaController;

    public ViewController(DirectoryManager directoryManager, MusicManager musicManager,
                          VBox dirsListVBox, VBox musicListVBox, MediaController mediaController) {
        this.dirsListVBox = dirsListVBox;
        this.musicListVBox = musicListVBox;
        this.directoryManager = directoryManager;
        this.musicManager = musicManager;
        this.mediaController = mediaController;
    }

    public void update() {
        updateDirs();
        updateMusic();
//        showPlaylists(); //todo playlist
    }

    private void updateDirs() {
        dirsListVBox.getChildren().clear();
        for (Directory directory : directoryManager.getDirectoryArray()) {
            dirsListVBox.getChildren().add(getDirLabel(directory.getPath()));
        }
    }

    private Label getDirLabel(String path) {
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
            directoryManager.delete(label.getText()); // удаление пути из списка путей
            musicManager.delete(label.getText());  // удаление музыки из списка музыки
            updateMusic();                                  // вывод обновленного списка музыки на экран
        });
        contextMenu.getItems().add(menuItem);
        contextMenu.show(label, mouseEvent.getScreenX(), mouseEvent.getScreenY());
    }

    private void updateMusic() {
        musicListVBox.getChildren().clear();
        for (Music music : musicManager.getMusicArray()) {
            musicListVBox.getChildren().add(getTrackLabel(music.getFileName()));
        }
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
                // todo delete
            }
        });
        return label;
    }
}
