package com.td.player.controllers;

import com.td.player.Player;
import com.td.player.managers.DirectoryManager;
import com.td.player.managers.MusicManager;
import com.td.player.managers.PlaylistManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Controller {
    @FXML
    private VBox musicListVBox, dirsListVBox, playlistVBox;

    @FXML
    private BorderPane topMenuBorderPane;

    @FXML
    private Button playButton, addDirButton, addPlaylistButton, renamePlaylistButton;

    @FXML
    private Accordion accordion;

    @FXML
    private TextField textField;

    @FXML
    private ScrollPane dirScrollPane, playlistScrollPane, musicScrollPane;

    @FXML
    private Slider timeSlider, volumeSlider;

    @FXML
    private Label titleLabel, artistLabel;

    private FileController fileController;
    private DirectoryManager directoryManager;
    private MusicManager musicManager;
    private PlaylistManager playlistManager;
    private MediaController mediaController;
    private ViewController viewController;

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private void initialize() {
        musicManager = new MusicManager();
        directoryManager = new DirectoryManager();
        playlistManager = new PlaylistManager();
        mediaController = new MediaController(this);
        fileController = new FileController(this);
        viewController = new ViewController(this);
        widthProperties();
    }

    private void widthProperties() {
        addDirButton.prefWidthProperty().bind(dirScrollPane.widthProperty());
        musicListVBox.prefWidthProperty().bind(musicScrollPane.widthProperty().subtract(23));
        playlistVBox.prefWidthProperty().bind(playlistScrollPane.widthProperty());
        textField.prefWidthProperty().bind(playlistScrollPane.widthProperty());
        dirsListVBox.prefWidthProperty().bind(dirScrollPane.widthProperty().subtract(20));
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

    @FXML
    private void onAddDirButtonClick() {
        fileController.selectDirectory();
        playlistManager.updateDefaultPlaylist(musicManager);
        viewController.update();
    }

    @FXML
    private void onAddPlaylistButtonClick() {
        String playlistName = textField.getText();
        if (playlistName != null && !playlistName.equals("") && playlistManager.isUnique(playlistName)) {
            viewController.createTitledPane(playlistName);
            textField.clear();
        }
    }

    @FXML
    private void onTextFieldKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            if (!addPlaylistButton.isDisable()) {
                onAddPlaylistButtonClick();
            } else {
                onRenamePlaylistButtonClick();
            }
        }
    }

    @FXML
    private void onRenamePlaylistButtonClick() {
        String playlistName = textField.getText();
        if (playlistManager.isUnique(playlistName)) {
            if (playlistName != null && !playlistName.equals("")) {
                viewController.renameTitledPane(playlistName);
                textField.clear();
            } else {
                addPlaylistButton.setDisable(false);
                renamePlaylistButton.setDisable(true);
            }
        }
    }

    @FXML
    private void onPlayButtonClick() {
        if (playButton.getText().equals("Pause")) {
            mediaController.pause();
        } else {
            mediaController.play();
        }
    }

    @FXML
    private void onPreviousButtonClick() {
        mediaController.switchMusic(false);
    }

    @FXML
    private void onNextButtonClick() {
        mediaController.switchMusic(true);
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

    public DirectoryManager getDirectoryManager() {
        return directoryManager;
    }

    public MusicManager getMusicManager() {
        return musicManager;
    }

    public PlaylistManager getPlaylistManager() {
        return playlistManager;
    }

    public Button getPlayButton() {
        return playButton;
    }

    public VBox getDirsListVBox() {
        return dirsListVBox;
    }

    public VBox getMusicListVBox() {
        return musicListVBox;
    }

    public Accordion getAccordion() {
        return accordion;
    }

    public MediaController getMediaController() {
        return mediaController;
    }

    public Button getAddPlaylistButton() {
        return addPlaylistButton;
    }

    public TextField getTextField() {
        return textField;
    }

    public Button getRenamePlaylistButton() {
        return renamePlaylistButton;
    }

    public Slider getTimeSlider() {
        return timeSlider;
    }

    public Slider getVolumeSlider() {
        return volumeSlider;
    }

    public ViewController getViewController() {
        return viewController;
    }

    public Label getTitleLabel() {
        return titleLabel;
    }

    public Label getArtistLabel() {
        return artistLabel;
    }
}