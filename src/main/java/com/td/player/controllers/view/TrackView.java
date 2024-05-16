package com.td.player.controllers.view;

import com.td.player.controllers.Controller;
import com.td.player.elements.Directory;
import com.td.player.elements.ParentElement;
import com.td.player.elements.Playlist;
import com.td.player.util.Util;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.Objects;

@SuppressWarnings("FieldMayBeFinal")
public class TrackView extends HBox {
    private Label idLabel;
    private Label singleLabel;
    private Label timeLabel;
    private Button addToPlaylistButton;
    private Button deleteButton;

    public TrackView(int id, String single, String time, ParentElement parentElement, Controller controller) {
        init(id, single, time);
        addProperties(id, parentElement, controller);
        fillParent(parentElement);
        applyStyle();
    }

    private void init(int id, String single, String time) {
        idLabel = new Label(String.valueOf(id));
        singleLabel = new Label(Util.getSingleText(single));
        timeLabel = new Label(time);
        addToPlaylistButton = new Button();
        deleteButton = new Button();
    }

    private void addProperties(int id, ParentElement parentElement, Controller controller) {
        singleLabel.setOnMouseEntered(mouseEvent -> setCursor(Cursor.HAND));
        addToPlaylistButton.setGraphic(new ImageView(Objects.requireNonNull(getClass().getResource("/com/td/player/img/add.png")).toExternalForm()));
        addToPlaylistButton.setOnMouseClicked(mouseEvent -> addToPlaylistAction(id, mouseEvent, controller));
        deleteButton.setGraphic(new ImageView(Objects.requireNonNull(getClass().getResource("/com/td/player/img/delete.png")).toExternalForm()));
        deleteButton.setOnAction(actionEvent -> deleteAction(id, parentElement, controller));
    }

    private void addToPlaylistAction(int id, MouseEvent mouseEvent, Controller controller) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            controller.getViewController().contextMenuController.showAddToPlaylistCM(id, this, mouseEvent);
        }
    }

    private void deleteAction(int id, ParentElement parentElement, Controller controller) {
        VBox vBox = (VBox) this.getParent();
        vBox.getChildren().remove(this);

        if (parentElement instanceof Playlist) {
            controller.getPlaylistManager().deleteTrack(id, parentElement.getId());
        }
    }

    private void fillParent(ParentElement parentElement) {
        if (parentElement instanceof Directory) {
            this.getChildren().addAll(idLabel, singleLabel, timeLabel, addToPlaylistButton, deleteButton);
        } else {
            this.getChildren().addAll(idLabel, singleLabel, timeLabel, deleteButton);
        }
    }

    private void applyStyle() {
        setPadding(new Insets(0, 0, 0, 5));
        setSpacing(5);
        setAlignment(Pos.CENTER_LEFT);
        setHgrow(singleLabel, Priority.ALWAYS);
        singleLabel.setMaxWidth(Double.MAX_VALUE);
        idLabel.setMinWidth(20);

        idLabel.setStyle("-fx-text-fill: #858585; -fx-font-size: 10; -fx-font-family: Verdana");
        singleLabel.setStyle("-fx-text-fill: #FFF2C2; -fx-font-size: 12; -fx-font-family: Verdana; -fx-font-weight: 500");
        timeLabel.setStyle("-fx-text-fill: #FFF2C2; -fx-font-size: 10; -fx-font-family: Verdana; -fx-font-weight: 500");
    }
}
