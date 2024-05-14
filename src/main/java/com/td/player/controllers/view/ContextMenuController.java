package com.td.player.controllers.view;

import com.td.player.controllers.Controller;
import com.td.player.elements.Track;
import com.td.player.util.Util;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.io.IOException;

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
}
