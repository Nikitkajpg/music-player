package com.td.player.controllers.view;

import com.td.player.controllers.Controller;
import com.td.player.elements.Directory;
import com.td.player.elements.ParentElement;
import com.td.player.elements.Playlist;
import com.td.player.elements.Track;
import com.td.player.util.Util;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
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

    private boolean isHighlighted = false;
    private Track track;

    public TrackView(int id, Track track, ParentElement parentElement, Controller controller) {
        this.track = track;
        init(id, parentElement);
        addProperties(id, parentElement, controller);
        fillParent(parentElement);
        applyStyle(parentElement);
    }

    private void init(int id, ParentElement parentElement) {
        idLabel = new Label(String.valueOf(id));
        if (parentElement instanceof Playlist) {
            singleLabel = new Label(Util.getSingleText(track.getArtist() + "\n" + track.getTitle()));
        } else {
            singleLabel = new Label(Util.getTrackFilename(track.getFileName()));
        }
        timeLabel = new Label(track.getTime());
        addToPlaylistButton = new Button();
        deleteButton = new Button();
    }

    private void addProperties(int id, ParentElement parentElement, Controller controller) {
        addToPlaylistButton.setOnMouseClicked(mouseEvent -> addToPlaylistAction(id, mouseEvent, controller));
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

    private void applyStyle(ParentElement parentElement) {
        setCursor(Cursor.HAND);
        setPadding(new Insets(0, 0, 0, 5));
        setSpacing(5);
        setAlignment(Pos.CENTER_LEFT);
        setHgrow(singleLabel, Priority.ALWAYS);
        singleLabel.setMaxWidth(Double.MAX_VALUE);
        idLabel.setMinWidth(20);

        idLabel.setStyle("-fx-text-fill: #858585; -fx-font-size: 10; -fx-font-family: Verdana");
        singleLabel.setStyle("-fx-text-fill: #FFF2C2; -fx-font-size: 12; -fx-font-family: Verdana; -fx-font-weight: 500");
        timeLabel.setStyle("-fx-text-fill: #FFF2C2; -fx-font-size: 10; -fx-font-family: Verdana; -fx-font-weight: 500");

        singleLabel.setOnMouseEntered(mouseEvent -> {
            if (!isHighlighted) {
                setColor("#333333");
            }
        });
        singleLabel.setOnMouseExited(mouseEvent -> {
            if (!isHighlighted) {
                setColor("#222222");
            }
        });
        addToPlaylistButton.setOnMouseEntered(mouseEvent -> addToPlaylistButton.setStyle("-fx-background-color: #333333"));
        addToPlaylistButton.setOnMouseExited(mouseEvent -> addToPlaylistButton.setStyle("-fx-background-color: #222222"));
        deleteButton.setOnMouseEntered(mouseEvent -> deleteButton.setStyle("-fx-background-color: #333333"));
        deleteButton.setOnMouseExited(mouseEvent -> deleteButton.setStyle("-fx-background-color: #222222"));

        addToPlaylistButton.setGraphic(new ImageView(Objects.requireNonNull(getClass()
                .getResource("/com/td/player/img/add.png")).toExternalForm()));
        deleteButton.setGraphic(new ImageView(Objects.requireNonNull(getClass()
                .getResource("/com/td/player/img/delete.png")).toExternalForm()));

        if (parentElement instanceof Directory) {
            singleLabel.setTooltip(new Tooltip(track.getFileName()));
        } else {
            singleLabel.setTooltip(new Tooltip(track.getArtist() + " - " + track.getTitle()));
        }
        addToPlaylistButton.setTooltip(new Tooltip("Add track to playlist"));
        deleteButton.setTooltip(new Tooltip("Delete track"));
    }

    public void setHighlighted(boolean highlighted) {
        isHighlighted = highlighted;
        if (highlighted) {
            setColor("#7A3100");
        } else {
            setColor("#222222");
        }
    }

    private void setColor(String color) {
        setStyle("-fx-background-color: " + color);
        addToPlaylistButton.setStyle("-fx-background-color: " + color);
        deleteButton.setStyle("-fx-background-color: " + color);
    }
}
