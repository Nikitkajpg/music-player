package com.td.player.controllers.view;

import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.util.Objects;

@SuppressWarnings("FieldMayBeFinal")
public class ParentElementView extends HBox {
    //todo rename button
    // delete button
    public ParentElementView(int id, String name, boolean isPlaylist) {
        Label idLabel = new Label(String.valueOf(id));
        Label nameLabel = new Label(name);
        Button renameButton = new Button();
        Button deleteButton = new Button();
        nameLabel.setOnMouseEntered(mouseEvent -> setCursor(Cursor.HAND));
        renameButton.setGraphic(new ImageView(Objects.requireNonNull(getClass().getResource("/com/td/player/img/rename.png")).toExternalForm()));
        deleteButton.setGraphic(new ImageView(Objects.requireNonNull(getClass().getResource("/com/td/player/img/delete.png")).toExternalForm()));
        if (isPlaylist) {
            this.getChildren().addAll(idLabel, nameLabel, renameButton, deleteButton);
        } else {
            this.getChildren().addAll(idLabel, nameLabel, deleteButton);
        }

    }
}
