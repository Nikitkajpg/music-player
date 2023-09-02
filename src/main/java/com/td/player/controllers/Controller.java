package com.td.player.controllers;

import com.td.player.Player;
import com.td.player.managers.DirectoryManager;
import com.td.player.managers.MusicManager;
import com.td.player.managers.PlaylistManager;
import com.td.player.util.Mode;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Controller {
    @FXML
    public VBox musicListVBox, dirsListVBox, playlistVBox, playlistNamesVBox, playlistMusicVBox;

    @FXML
    public BorderPane topMenuBorderPane, borderPane;

    @FXML
    public Button playButton, addDirButton, addPlaylistButton, renamePlaylistButton;

    @FXML
    public TextField textField;

    @FXML
    public ScrollPane dirScrollPane, musicScrollPane, playlistNamesScrollPane, playlistMusicScrollPane;

    @FXML
    private SplitPane splitPane;

    @FXML
    public Slider timeSlider, volumeSlider;

    @FXML
    public Label titleLabel, artistLabel, currentTimeLabel, endTimeLabel;

    @FXML
    public ToggleButton preferenceToggleButton, randomToggleButton;

    private FileController fileController;
    private DirectoryManager directoryManager;
    private MusicManager musicManager;
    private PlaylistManager playlistManager;
    private MediaController mediaController;
    private ViewController viewController;
    public Mode mode = Mode.DEFAULT;

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private void initialize() {
        musicManager = new MusicManager();
        directoryManager = new DirectoryManager();
        playlistManager = new PlaylistManager();
        fileController = new FileController(this);
        mediaController = new MediaController(this);
        viewController = new ViewController(this);
        volumeSlider.setValue(volumeSlider.getMax());
        widthProperties();
    }

    private void widthProperties() {
        addDirButton.prefWidthProperty().bind(dirScrollPane.widthProperty());
        widthPropertyForLists(dirsListVBox, dirScrollPane);
        widthPropertyForLists(musicListVBox, musicScrollPane);
        widthPropertyForLists(playlistNamesVBox, playlistNamesScrollPane);
        textField.prefWidthProperty().bind(playlistVBox.widthProperty());
    }

    private void widthPropertyForLists(VBox vBox, ScrollPane scrollPane) {
        for (int i = 0; i < vBox.getChildren().size(); i++) {
            Button button = (Button) vBox.getChildren().get(i);
            button.prefWidthProperty().bind(scrollPane.widthProperty().subtract(17));
        }
    }

    /**
     * Метод для перетаскивания окна
     */
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
        viewController.showLists();
    }

    @FXML
    private void onAddPlaylistButtonClick() {
        String playlistName = textField.getText();
        if (playlistName != null && !playlistName.equals("") && playlistManager.isUnique(playlistName)) {
            viewController.createPlaylistNameButton(playlistName);
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
        String newPlaylistName = textField.getText();
        if (playlistManager.isUnique(newPlaylistName)) {
            if (newPlaylistName != null && !newPlaylistName.equals("")) {
                viewController.renamePlaylist(newPlaylistName);
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
        mediaController.play();
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

    public MediaController getMediaController() {
        return mediaController;
    }
}