package com.td.player.controllers.view;

import com.td.player.controllers.Controller;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Objects;

@SuppressWarnings("FieldMayBeFinal")
public class ParentElementView extends HBox {
    private Label idLabel;
    private Label nameLabel;
    private Button renameButton;
    private Button deleteButton;

    //todo rename button
    public ParentElementView(int id, String name, boolean isPlaylist, Controller controller) {
        init(id, name);
        addProperties(isPlaylist, id, controller);
        fillParent(isPlaylist);

    }

    private void init(int id, String name) {
        idLabel = new Label(String.valueOf(id));
        nameLabel = new Label(name);
        renameButton = new Button();
        deleteButton = new Button();
    }

    private void addProperties(boolean isPlaylist, int id, Controller controller) {
        nameLabel.setOnMouseEntered(mouseEvent -> setCursor(Cursor.HAND));
        renameButton.setGraphic(new ImageView(Objects.requireNonNull(getClass().getResource("/com/td/player/img/rename.png")).toExternalForm()));
        deleteButton.setGraphic(new ImageView(Objects.requireNonNull(getClass().getResource("/com/td/player/img/delete.png")).toExternalForm()));
        deleteButton.setOnAction(actionEvent -> deleteAction(isPlaylist, id, controller));
    }

    private void deleteAction(boolean isPlaylist, int id, Controller controller) {
        VBox vBox = (VBox) this.getParent();
        vBox.getChildren().remove(this);

        if (isPlaylist) {
            controller.getPlaylistManager().delete(id);
        } else {
            controller.getDirectoryManager().delete(id);
        }
    }

    private void fillParent(boolean isPlaylist) {
        if (isPlaylist) {
            this.getChildren().addAll(idLabel, nameLabel, renameButton, deleteButton);
        } else {
            this.getChildren().addAll(idLabel, nameLabel, deleteButton);
        }
    }
}
