package com.td.player.controllers.view;

import com.td.player.controllers.Controller;
import com.td.player.elements.Directory;
import com.td.player.elements.ParentElement;
import com.td.player.util.Util;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.Objects;

@SuppressWarnings("FieldMayBeFinal")
public class ParentElementView extends HBox {
    private Label idLabel;
    private Label nameLabel;
    private Button renameButton;
    private Button deleteButton;

    private ParentElement parentElement;

    public ParentElementView(ParentElement parentElement, boolean isPlaylist, Controller controller) {
        this.parentElement = parentElement;
        init();
        addProperties(isPlaylist, controller);
        fillParent(isPlaylist);
        applyStyle();
    }

    private void init() {
        idLabel = new Label(String.valueOf(parentElement.getId()));
        nameLabel = new Label(parentElement.getName() + Util.getNumberOfTracks(parentElement));
        renameButton = new Button();
        deleteButton = new Button();
    }

    private void addProperties(boolean isPlaylist, Controller controller) {
        renameButton.setOnAction(actionEvent -> renameAction(controller));
        deleteButton.setOnAction(actionEvent -> deleteAction(isPlaylist, parentElement.getId(), controller));
    }

    private void renameAction(Controller controller) {
        TextField textField = new TextField();
        textField.setTooltip(new Tooltip("Press \"Enter\" to rename"));
        String[] names = nameLabel.getText().split(":");
        textField.setText(names[0]);
        int id = getChildren().indexOf(nameLabel);
        getChildren().remove(nameLabel);
        getChildren().add(id, textField);
        textField.requestFocus();

        textField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                controller.getPlaylistManager().renamePlaylist(idLabel.getText(), textField.getText());
                nameLabel.setText(textField.getText() + Util.getNumberOfTracks(idLabel.getText(), controller));
                getChildren().remove(textField);
                getChildren().add(id, nameLabel);
            }
        });
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
            if (nameLabel.getText().contains("All tracks")) {
                getChildren().addAll(idLabel, nameLabel);
            } else {
                getChildren().addAll(idLabel, nameLabel, renameButton, deleteButton);
            }
        } else {
            getChildren().addAll(idLabel, nameLabel, deleteButton);
        }
    }

    private void applyStyle() {
        setCursor(Cursor.HAND);
        setPadding(new Insets(0, 0, 0, 5));
        setSpacing(5);
        setAlignment(Pos.CENTER);
        setHgrow(nameLabel, Priority.ALWAYS);
        nameLabel.setMaxWidth(Double.MAX_VALUE);

        idLabel.setStyle("-fx-text-fill: #858585; -fx-font-size: 10; -fx-font-family: Verdana");
        nameLabel.setStyle("-fx-text-fill: #FFF2C2; -fx-font-size: 14; -fx-font-family: Verdana; -fx-font-weight: 500");

        nameLabel.setOnMouseEntered(mouseEvent -> {
            setStyle("-fx-background-color: #333333");
            renameButton.setStyle("-fx-background-color: #333333");
            deleteButton.setStyle("-fx-background-color: #333333");
        });
        nameLabel.setOnMouseExited(mouseEvent -> {
            setStyle("-fx-background-color: #222222");
            renameButton.setStyle("-fx-background-color: #222222");
            deleteButton.setStyle("-fx-background-color: #222222");
        });
        renameButton.setOnMouseEntered(mouseEvent -> renameButton.setStyle("-fx-background-color: #333333"));
        renameButton.setOnMouseExited(mouseEvent -> renameButton.setStyle("-fx-background-color: #222222"));
        deleteButton.setOnMouseEntered(mouseEvent -> deleteButton.setStyle("-fx-background-color: #333333"));
        deleteButton.setOnMouseExited(mouseEvent -> deleteButton.setStyle("-fx-background-color: #222222"));

        renameButton.setGraphic(new ImageView(Objects.requireNonNull(getClass()
                .getResource("/com/td/player/img/rename.png")).toExternalForm()));
        deleteButton.setGraphic(new ImageView(Objects.requireNonNull(getClass()
                .getResource("/com/td/player/img/delete.png")).toExternalForm()));

        if (parentElement instanceof Directory) {
            nameLabel.setTooltip(new Tooltip(((Directory) parentElement).getPath()));
        } else {
            nameLabel.setTooltip(new Tooltip(parentElement.getName()));
        }
        renameButton.setTooltip(new Tooltip("Rename playlist"));
        deleteButton.setTooltip(new Tooltip("Delete"));
    }
}
