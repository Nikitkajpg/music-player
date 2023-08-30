package com.td.player.util;

import com.td.player.controllers.ViewController;
import com.td.player.elements.Playlist;
import com.td.player.managers.DirectoryManager;
import com.td.player.managers.MusicManager;
import com.td.player.managers.PlaylistManager;
import javafx.scene.control.Button;
import javafx.scene.input.*;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Actions {
    public static void openDirectory(Button button) {
        File directory = new File(button.getText());
        try {
            Desktop.getDesktop().open(directory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openFile(MusicManager musicManager, Button button) {
        File directory = new File(musicManager.get(button.getText()).getAbsolutePath());
        try {
            // FIXME: 28.07.2023 it works only on windows
            Runtime.getRuntime().exec("explorer /select, " + directory.getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteDirectory(DirectoryManager directoryManager, MusicManager musicManager,
                                       ViewController viewController, Button button, PlaylistManager playlistManager) {
        directoryManager.deleteByPath(button.getText());
        musicManager.deleteByPath(button.getText());
        playlistManager.deleteByPath(button.getText());
        viewController.showLists();
    }

    public static void onDragDetected(MouseEvent mouseEvent, Button button) {
        // drag was detected, start drag-and-drop gesture
        // allow any transfer mode
        Dragboard dragboard = button.startDragAndDrop(TransferMode.ANY);
        // put a string on dragBoard
        ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putString(button.getText());
        dragboard.setContent(clipboardContent);
        mouseEvent.consume();
    }

    public static void onDragOver(DragEvent dragEvent, VBox playlistMusicVBox, Playlist playlist) {
        // data is dragged over the target
        // accept it only if it is  not dragged from the same node and if it has a string data
        if (dragEvent.getGestureSource() != playlistMusicVBox && dragEvent.getDragboard().hasString() &&
                !playlist.getName().equals("All music")) {
            // allow for both copying and moving, whatever user chooses
            dragEvent.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        dragEvent.consume();
    }

    public static void onDragEntered(DragEvent dragEvent, VBox playlistMusicVBox, Playlist playlist) {
        // the drag-and-drop gesture entered the target
        // show to the user that it is an actual gesture target
        if (dragEvent.getGestureSource() != playlistMusicVBox && dragEvent.getDragboard().hasString() &&
                !playlist.getName().equals("All music")) {
            playlistMusicVBox.setStyle("-fx-background-color: green");
        }
        dragEvent.consume();
    }

    public static void onDragExited(DragEvent dragEvent, VBox vBox, Playlist playlist) {
        // mouse moved away, remove the graphical cues
        if (!playlist.getName().equals("All music")) {
            vBox.setStyle("-fx-background-color: #333333");
        }
        dragEvent.consume();
    }

    public static void onDragDropped(DragEvent dragEvent, PlaylistManager playlistManager, Playlist playlist,
                                     MusicManager musicManager, VBox playlistMusicVBox, Button button, ViewController viewController) {
        // data dropped
        // if there is a string data on dragBoard, read it and use it
        Dragboard dragboard = dragEvent.getDragboard();
        boolean success = false;
        if (dragboard.hasString() && playlistManager.isUniqueInPlaylist(playlist, musicManager.getTitleByFileName(dragboard.getString())) &&
                !playlist.getName().equals("All music")) {
            VBox labelsVBox = new VBox();
            labelsVBox.getChildren().addAll(
                    viewController.getPlaylistMusicButton(musicManager.getMusicByFileName(dragboard.getString()), playlist, button),
                    new Button(musicManager.get(dragboard.getString()).getArtist()));
            playlistMusicVBox.getChildren().add(playlistMusicVBox.getChildren().size(), labelsVBox);
            playlist.addByName(dragboard.getString(), musicManager);
            success = true;
        }
        // let the source know whether the string was successfully transferred and used
        dragEvent.setDropCompleted(success);
        dragEvent.consume();
    }
}
