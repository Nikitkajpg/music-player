package com.td.player.controllers.view;

import com.td.player.controllers.Controller;
import com.td.player.elements.ParentElement;
import com.td.player.elements.Playlist;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Objects;

@SuppressWarnings("FieldMayBeFinal")
public class TrackView extends HBox {
    private Label idLabel;
    private Label singleLabel;
    private Label timeLabel;
    private Button addToPlaylistButton;
    private Button deleteButton;

    //todo add to playlist button
    // delete button
    public TrackView(int id, String single, String time, ParentElement parentElement, Controller controller) {
        init(id, single, time);
        addProperties(id, parentElement, controller);
        fillParent();
    }

    private void init(int id, String single, String time) {
        idLabel = new Label(String.valueOf(id));
        singleLabel = new Label(single);
        timeLabel = new Label(time);
        addToPlaylistButton = new Button();
        deleteButton = new Button();
    }

    private void addProperties(int id, ParentElement parentElement, Controller controller) {
        singleLabel.setOnMouseEntered(mouseEvent -> setCursor(Cursor.HAND));
        addToPlaylistButton.setGraphic(new ImageView(Objects.requireNonNull(getClass().getResource("/com/td/player/img/add.png")).toExternalForm()));
        deleteButton.setGraphic(new ImageView(Objects.requireNonNull(getClass().getResource("/com/td/player/img/delete.png")).toExternalForm()));
        deleteButton.setOnAction(actionEvent -> deleteAction(id, parentElement, controller));
    }

    private void deleteAction(int id, ParentElement parentElement, Controller controller) {
        VBox vBox = (VBox) this.getParent();
        vBox.getChildren().remove(this);

        if (parentElement instanceof Playlist) {
            controller.getPlaylistManager().deleteTrack(id, parentElement.getId());
        }
    }

    private void fillParent() {
        this.getChildren().addAll(idLabel, singleLabel, timeLabel, addToPlaylistButton, deleteButton);
    }
}
