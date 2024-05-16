package com.td.player.controllers.view;

import com.td.player.controllers.Controller;
import com.td.player.elements.Playlist;
import com.td.player.elements.Track;
import com.td.player.util.Util;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

/**
 * Класс, содержащий методы для управления {@link ContextMenu}
 */

@SuppressWarnings("FieldMayBeFinal")
public class ContextMenuController {
    private Controller controller;

    private ContextMenu currentContextMenu = new ContextMenu();

    public ContextMenuController(Controller controller) {
        this.controller = controller;
    }

    /**
     * Контекстное меню для каждой песни в списке музыки
     */
    public void showTrackCM(TrackView trackView, Track track, MouseEvent mouseEvent) {
        currentContextMenu.hide();
        ContextMenu contextMenu = new ContextMenu();
        MenuItem showMenuItem = new MenuItem("Show in explorer");
        showMenuItem.setOnAction(actionEvent -> Util.openFile(track));

        contextMenu.getItems().add(showMenuItem);
        contextMenu.show(trackView, mouseEvent.getScreenX(), mouseEvent.getScreenY());
        currentContextMenu = contextMenu;
    }

    public void showAddToPlaylistCM(int id, TrackView trackView, MouseEvent mouseEvent) {
        currentContextMenu.hide();
        ContextMenu contextMenu = new ContextMenu();
        for (Playlist playlist : controller.getPlaylistManager().getPlaylists()) {
            MenuItem playlistMenuItem = new MenuItem(playlist.getName());
            playlistMenuItem.setOnAction(actionEvent -> addToPlaylist(id, playlist));
            contextMenu.getItems().add(playlistMenuItem);
        }
        contextMenu.show(trackView, mouseEvent.getScreenX(), mouseEvent.getScreenY());
        currentContextMenu = contextMenu;
    }

    private void addToPlaylist(int id, Playlist playlist) {
        Track track = controller.getDirectoryManager().getTrackById(id);
        playlist.addTrack(track);

        VBox tracksVBox = (VBox) controller.playlistsVBox.getChildren().stream().filter(node -> node.getClass().equals(VBox.class)).findAny().orElse(null);
        tracksVBox.setSpacing(10);

        TrackView trackView = new TrackView(id, track.getArtist() + "\n" + track.getTitle(), track.getTime(), playlist, controller);
        trackView.setOnMouseClicked(mouseEvent -> controller.getViewController().actionWithTrackView(playlist, mouseEvent, trackView, track));
        tracksVBox.getChildren().add(trackView);
    }
}
