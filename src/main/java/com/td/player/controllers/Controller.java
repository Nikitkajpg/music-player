package com.td.player.controllers;

import com.td.player.Player;
import com.td.player.managers.DirectoryManager;
import com.td.player.managers.TrackManager;
import com.td.player.managers.PlaylistManager;
import com.td.player.util.Mode;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;

public class Controller {
    @FXML
    public VBox trackListVBox, directoriesListVBox, playlistNamesVBox, playlistTrackVBox;

    @FXML
    public HBox addPlaylistHBox;

    @FXML
    public BorderPane topMenuBorderPane, controlsBorderPane;

    @FXML
    public Button playButton, addDirectoryButton, addPlaylistButton;

    @FXML
    public ScrollPane directoryScrollPane, trackScrollPane, playlistNamesScrollPane, playlistTrackScrollPane;

    @FXML
    private SplitPane splitPane;

    @FXML
    public Slider timeSlider, volumeSlider;

    @FXML
    public Label titleLabel, currentTimeLabel, endTimeLabel, playlistsLabel;

    @FXML
    public ToggleButton preferenceToggleButton, randomToggleButton;

    private DirectoryManager directoryManager;
    private TrackManager trackManager;
    private PlaylistManager playlistManager;
    private FileController fileController;
    private MediaController mediaController;
    private ViewController viewController;
    public Mode mode = Mode.DEFAULT;

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private void initialize() {
        init();
        setWidthProperties();
    }

    private void init() {
        trackManager = new TrackManager();
        directoryManager = new DirectoryManager();
        playlistManager = new PlaylistManager(this);
        fileController = new FileController(this);
        mediaController = new MediaController(this);
        viewController = new ViewController(this);
    }

    private void setWidthProperties() {
        widthPropertyForLists(directoriesListVBox, directoryScrollPane);
        widthPropertyForLists(trackListVBox, trackScrollPane);
        widthPropertyForLists(playlistNamesVBox, playlistNamesScrollPane);
    }

    private void widthPropertyForLists(VBox vBox, ScrollPane scrollPane) {
        for (int i = 0; i < vBox.getChildren().size(); i++) {
            Button button = (Button) vBox.getChildren().get(i);
            button.prefWidthProperty().bind(scrollPane.widthProperty().subtract(17));
        }
    }

    @FXML
    private void onAddDirectoryButtonClick() {
        fileController.selectDirectory();
        playlistManager.updateDefaultPlaylist();
        viewController.showLists();
    }

    @FXML
    private void onAddPlaylistButtonClick() {
        addPlaylistHBox.getChildren().remove(addPlaylistHBox.getChildren().size() - 1);
        TextField textField = new TextField();
        textField.setPromptText("Playlist name...");
        textField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                String playlistName = textField.getText();
                if (playlistName != null && !playlistName.equals("") && playlistManager.isUnique(playlistName)) {
                    viewController.createPlaylistNameButton(playlistName);
                    addPlaylistHBox.getChildren().remove(textField);
                    addPlaylistHBox.getChildren().add(playlistsLabel);
                }
            }
        });
        addPlaylistHBox.getChildren().add(textField);
    }

    @FXML
    private void onPlayButtonClick() {
        if (mediaController.isPaused()) {
            mediaController.playInMode();
            playButton.setGraphic(new ImageView(new Image(Objects.requireNonNull(Player.class.getResource("img/pause.png")).toExternalForm())));
        } else {
            mediaController.pause();
            playButton.setGraphic(new ImageView(new Image(Objects.requireNonNull(Player.class.getResource("img/play.png")).toExternalForm())));
        }
    }

    @FXML
    private void onPreviousButtonClick() {
        mediaController.switchTrack(false);
    }

    @FXML
    private void onNextButtonClick() {
        mediaController.switchTrack(true);
    }

    @FXML
    private void onExitButtonClick() {
        fileController.writeAllInf();
        Player.stage.close();
    }

    @FXML
    private void onMinimizeButtonClick() {
        Player.stage.setIconified(true);
    }

    @FXML
    private void onMaximizeButtonClick() {
        Player.stage.setMaximized(!Player.stage.isMaximized());
    }

    @FXML
    private void onPreferenceToggleButtonClick() {
        if (preferenceToggleButton.isSelected()) {
            enableMode(true, Mode.PREFERENCE);
        } else {
            enableMode(false, Mode.DEFAULT);
        }
    }

    @FXML
    private void onRandomToggleButtonClick() {
        if (randomToggleButton.isSelected()) {
            enableMode(true, Mode.RANDOM);
        } else {
            enableMode(false, Mode.DEFAULT);
        }
    }

    private void enableMode(boolean disable, Mode newMode) {
        splitPane.setDisable(disable);
        mode = newMode;
        mediaController.playInMode();
    }

    public void dragStage(Stage stage) {
        topMenuBorderPane.setOnMousePressed(mouseEvent -> {
            xOffset = mouseEvent.getSceneX();
            yOffset = mouseEvent.getSceneY();
        });
        topMenuBorderPane.setOnMouseDragged(mouseEvent -> {
            stage.setX(mouseEvent.getScreenX() - xOffset);
            stage.setY(mouseEvent.getScreenY() - yOffset);
        });
    }

    public DirectoryManager getDirectoryManager() {
        return directoryManager;
    }

    public TrackManager getTrackManager() {
        return trackManager;
    }

    public PlaylistManager getPlaylistManager() {
        return playlistManager;
    }

    public MediaController getMediaController() {
        return mediaController;
    }

    public ViewController getViewController() {
        return viewController;
    }
}