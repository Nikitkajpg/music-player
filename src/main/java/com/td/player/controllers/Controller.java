package com.td.player.controllers;

import com.td.player.Player;
import com.td.player.controllers.view.ViewControllerOld;
import com.td.player.managers.DirectoryManager;
import com.td.player.managers.TrackManager;
import com.td.player.managers.PlaylistManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Controller {
    //todo delete trackListVBox, playlistTrackVBox
    @FXML
    public VBox trackListVBox, directoriesVBox, playlistsVBox, playlistTrackVBox;

    @FXML
    public HBox addPlaylistHBox;

    @FXML
    public BorderPane topMenuBorderPane, controlsBorderPane;

    @FXML
    public Button playButton, addDirectoryButton, addPlaylistButton;

    //todo delete trackScrollPane, playlistTrackScrollPane
    @FXML
    public ScrollPane directoriesScrollPane, trackScrollPane, playlistsScrollPane, playlistTrackScrollPane;

    @FXML
    public SplitPane splitPane;

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
    private ViewControllerOld viewControllerOld;

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
        viewControllerOld = new ViewControllerOld(this);
    }

    private void setWidthProperties() {
        widthPropertyForLists(directoriesVBox, directoriesScrollPane);
        widthPropertyForLists(trackListVBox, trackScrollPane);
        widthPropertyForLists(playlistsVBox, playlistsScrollPane);
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
    }

    @FXML
    private void onAddPlaylistButtonClick() {
        viewControllerOld.addPlaylist();
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
        viewControllerOld.clickingOnPreference();
    }

    @FXML
    private void onRandomToggleButtonClick() {
        viewControllerOld.clickingOnRandom();
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

    public ViewControllerOld getViewController() {
        return viewControllerOld;
    }
}