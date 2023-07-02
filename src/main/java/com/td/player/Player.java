package com.td.player;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Player extends Application {
    public static Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Player.class.getResource("view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("EMP");
        stage.setScene(scene);
        stage.setHeight(600);
        stage.setWidth(800);
        stage.setResizable(false);
        stage.getIcons().add(new Image(Objects.requireNonNull(Player.class.getResourceAsStream("EMP128.png"))));
        stage.show();

        Player.stage = stage;
    }

    public static void main(String[] args) {
        launch();
    }
}