package com.td.player.controllers.view;

import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.util.Objects;

@SuppressWarnings("FieldMayBeFinal")
public class TrackView extends HBox {
    //todo add to playlist button
    // delete button
    public TrackView(int id, String single, String time, ViewController viewController) {
        Label idLabel = new Label(String.valueOf(id));
        Label singleLabel = new Label(single);
        Label timeLabel = new Label(time);
        Button addToPlaylistButton = new Button();
        Button deleteButton = new Button();
        singleLabel.setOnMouseEntered(mouseEvent -> setCursor(Cursor.HAND));
        addToPlaylistButton.setGraphic(new ImageView(Objects.requireNonNull(getClass().getResource("/com/td/player/img/add.png")).toExternalForm()));
        deleteButton.setGraphic(new ImageView(Objects.requireNonNull(getClass().getResource("/com/td/player/img/delete.png")).toExternalForm()));
        this.getChildren().addAll(idLabel, singleLabel, timeLabel, addToPlaylistButton, deleteButton);
    }
}
