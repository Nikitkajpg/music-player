package com.td.player.controllers.view;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.util.Objects;

@SuppressWarnings("FieldMayBeFinal")
public class TrackView extends HBox {
    private Label idLabel;
    private Label singleLabel;
    private Label timeLabel;
    private Button addToPlaylistButton;
    private Button deleteButton;

    public TrackView(int id, String single, String time) {
        idLabel = new Label(String.valueOf(id));
        singleLabel = new Label(single);
        timeLabel = new Label(time);
        addToPlaylistButton = new Button();
        deleteButton = new Button();
        addToPlaylistButton.setGraphic(new ImageView(Objects.requireNonNull(getClass().getResource("/com/td/player/img/add.png")).toExternalForm()));
        deleteButton.setGraphic(new ImageView(Objects.requireNonNull(getClass().getResource("/com/td/player/img/delete.png")).toExternalForm()));
        this.getChildren().addAll(idLabel, singleLabel, timeLabel, addToPlaylistButton, deleteButton);
    }
}
