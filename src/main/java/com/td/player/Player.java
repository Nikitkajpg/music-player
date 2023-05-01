package com.td.player;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Player extends Application {
    public static Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Player.class.getResource("view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Player");
        stage.setScene(scene);
        stage.setHeight(600);
        stage.setWidth(800);
        stage.setResizable(false);
        stage.show();

        Player.stage = stage;
    }

    public static void main(String[] args) {
        launch();
    }
}