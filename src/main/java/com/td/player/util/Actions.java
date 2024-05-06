package com.td.player.util;

import com.td.player.controllers.Controller;
import com.td.player.elements.Directory;
import com.td.player.elements.Playlist;
import com.td.player.managers.TrackManager;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.*;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Утилитарный класс, содержащий методы из лямбда-выражений
 */
public class Actions {
    /**
     * Метод открывает папку в проводнике
     */
    public static void openDirectory(Button pressedButtonWithDirectoryPath) {
        File directory = new File(pressedButtonWithDirectoryPath.getText());
        try {
            Desktop.getDesktop().open(directory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод открывает музыкальный файл в проводнике
     */
    public static void openFile(TrackManager trackManager, Button pressedButtonWithTrackFilename) {
        File directory = new File(trackManager.get(pressedButtonWithTrackFilename.getText()).getAbsolutePath());
        try {
            boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
            if (isWindows) {
                ProcessBuilder pb = new ProcessBuilder("explorer.exe", "/select," + directory.toURI());
                pb.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteDirectory(Controller controller, Directory directory, Button button) {
        controller.getDirectoryManager().delete(directory);
        controller.getTrackManager().deleteByPath(button.getText());
        controller.getPlaylistManager().deleteByPath(button.getText());
        controller.getViewController().showLists();
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

    public static void onDragOver(DragEvent dragEvent, VBox playlistTrackVBox, Playlist playlist) {
        // data is dragged over the target
        // accept it only if it is  not dragged from the same node and if it has a string data
        if (dragEvent.getGestureSource() != playlistTrackVBox && dragEvent.getDragboard().hasString() &&
                !playlist.getName().equals("All tracks")) {
            // allow for both copying and moving, whatever user chooses
            dragEvent.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        dragEvent.consume();
    }

    public static void onDragEntered(DragEvent dragEvent, VBox playlistTrackVBox, Playlist playlist) {
        // the drag-and-drop gesture entered the target
        // show to the user that it is an actual gesture target
        if (dragEvent.getGestureSource() != playlistTrackVBox && dragEvent.getDragboard().hasString() &&
                !playlist.getName().equals("All tracks")) {
            playlistTrackVBox.setStyle("-fx-background-color: #ffc600");
        }
        dragEvent.consume();
    }

    public static void onDragExited(DragEvent dragEvent, VBox vBox, Playlist playlist) {
        // mouse moved away, remove the graphical cues
        if (!playlist.getName().equals("All tracks")) {
            vBox.setStyle("-fx-background-color: #222222");
        }
        dragEvent.consume();
    }

    public static void onDragDropped(Controller controller, DragEvent dragEvent, Playlist playlist,
                                     VBox playlistTrackVBox, ScrollPane scrollPane) {
        // data dropped
        // if there is a string data on dragBoard, read it and use it
        Dragboard dragboard = dragEvent.getDragboard();
        boolean success = false;
        if (dragboard.hasString() && controller.getPlaylistManager().isUniqueInPlaylist(playlist, controller.getTrackManager().getTitleByFileName(dragboard.getString())) &&
                !playlist.getName().equals("All tracks")) {
            Button button = controller.getViewController().getNameButton(controller.getTrackManager().getTrackByFileName(dragboard.getString()), playlist);
            button.prefWidthProperty().bind(scrollPane.widthProperty().subtract(17));
            playlistTrackVBox.getChildren().add(playlistTrackVBox.getChildren().size() - 1, button);
            playlist.addTrackByFilename(dragboard.getString(), controller.getTrackManager());
            success = true;
        }
        // let the source know whether the string was successfully transferred and used
        dragEvent.setDropCompleted(success);
        dragEvent.consume();
    }
}
