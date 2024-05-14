package com.td.player.util;

import com.td.player.controllers.Controller;
import com.td.player.elements.Directory;
import com.td.player.elements.Playlist;
import com.td.player.elements.Track;
import javafx.scene.control.Button;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class DragEventUtil {
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

    public static void onDragEntered(DragEvent dragEvent, VBox trackVBox, Playlist playlist) {
        // the drag-and-drop gesture entered the target
        // show to the user that it is an actual gesture target
        if (dragEvent.getGestureSource() != trackVBox && dragEvent.getDragboard().hasString() &&
                !playlist.getName().equals("All tracks")) {
            trackVBox.getChildren().get(0).setStyle("-fx-background-color: #ffc600");
        }
        dragEvent.consume();
    }

    public static void onDragExited(DragEvent dragEvent, VBox vBox, Playlist playlist) {
        // mouse moved away, remove the graphical cues
        if (!playlist.getName().equals("All tracks")) {
            vBox.getChildren().get(0).setStyle("-fx-background-color: #222222");
        }
        dragEvent.consume();
    }

    public static void onDragDropped(Controller controller, DragEvent dragEvent, Playlist playlist,
                                     VBox trackVBox) {
        // data dropped
        // if there is a string data on dragBoard, read it and use it
        Dragboard dragboard = dragEvent.getDragboard();
        boolean success = false;
        Track track = DragEventUtil.getTrack(controller, dragboard);


        if (dragboard.hasString() && controller.getPlaylistManager().isUniqueInPlaylist(playlist, track)
                && !playlist.getName().equals("All tracks")) {
            HBox trackHBox = DragEventUtil.getTrackHBox(controller, track, playlist);
            System.out.println(trackVBox.getChildren().size());
            trackVBox.getChildren().add(trackVBox.getChildren().size() - 1, trackHBox);
            System.out.println(trackVBox.getChildren().size());
            playlist.addTrack(track);
            success = true;
        }
        // let the source know whether the string was successfully transferred and used
        dragEvent.setDropCompleted(success);
        dragEvent.consume();
    }

    private static Track getTrack(Controller controller, Dragboard dragboard) {
        Track track;
        for (Directory directory: controller.getDirectoryManager().getDirectories()) {
            track = directory.getTrackByProperties(dragboard.getString());
            if (track != null) {
                return track;
            }
        }
        return null;
    }

    private static HBox getTrackHBox(Controller controller, Track track, Playlist playlist) {
        HBox trackHBox = new HBox();
        Button trackButton = new Button(track.getArtist() + " - " + track.getTitle());
        trackButton.setOnMouseClicked(mouseEvent ->
                controller.getViewController().actionWithTrackButton(playlist, mouseEvent, trackButton, track));
        return trackHBox;
    }
}
