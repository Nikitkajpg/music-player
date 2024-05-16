package com.td.player.controllers;

import com.td.player.Player;
import com.td.player.controllers.view.ParentElementView;
import com.td.player.controllers.view.ViewController;
import com.td.player.managers.DirectoryManager;
import com.td.player.managers.PlaylistManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

//TODO: add id to tracks, directories and playlists

public class Controller {
    @FXML
    public VBox directoriesVBox, playlistsVBox;

    @FXML
    public HBox addPlaylistHBox;

    @FXML
    public BorderPane topMenuBorderPane, controlsBorderPane;

    @FXML
    public Button playButton, addDirectoryButton, addPlaylistButton;

    @FXML
    public ScrollPane directoriesScrollPane, playlistsScrollPane;

    @FXML
    public SplitPane splitPane;

    @FXML
    public Slider timeSlider, volumeSlider;

    @FXML
    public Label titleLabel, currentTimeLabel, endTimeLabel, playlistsLabel;

    @FXML
    public ToggleButton preferenceToggleButton, randomToggleButton;

    private DirectoryManager directoryManager;
    private PlaylistManager playlistManager;
    private FileController fileController;
    private MediaController mediaController;
    private ViewController viewController;

    @FXML
    private void initialize() {
        init();
        setWidthProperties();
    }

    private void init() {
        directoryManager = new DirectoryManager();
        playlistManager = new PlaylistManager(this);
        fileController = new FileController(this);
        mediaController = new MediaController(this);
        viewController = new ViewController(this);
    }

    private void setWidthProperties() {
        widthPropertyForLists(directoriesVBox, directoriesScrollPane);
        widthPropertyForLists(playlistsVBox, playlistsScrollPane);
    }

    private void widthPropertyForLists(VBox vBox, ScrollPane scrollPane) {
        for (int i = 0; i < vBox.getChildren().size(); i++) {
            ParentElementView parentElementView = (ParentElementView) vBox.getChildren().get(i);
            parentElementView.prefWidthProperty().bind(scrollPane.widthProperty().subtract(17));
        }
    }

    @FXML
    private void onAddDirectoryButtonClick() {
        fileController.selectDirectory();
    }

    @FXML
    private void onAddPlaylistButtonClick() {
        viewController.addPlaylist();
    }

    @FXML
    private void onPlayButtonClick() {
        mediaController.play();
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
        fileController.writeToFile();
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
        viewController.clickingOnPreference();
    }

    @FXML
    private void onRandomToggleButtonClick() {
        viewController.clickingOnRandom();
    }

    public DirectoryManager getDirectoryManager() {
        return directoryManager;
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