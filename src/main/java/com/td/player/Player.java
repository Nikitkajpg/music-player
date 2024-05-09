package com.td.player;

import com.td.player.controllers.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class Player extends Application {
    public static Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Player.class.getResource("views/view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setScene(scene);
        stage.setHeight(600);
        stage.setWidth(800);
        stage.getIcons().add(new Image(Objects.requireNonNull(Player.class.getResource("img/EMP6.png")).toExternalForm()));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();

        Controller controller = fxmlLoader.getController();
        controller.getViewController().dragStage(stage);

        Player.stage = stage;
    }

    public static void main(String[] args) {
        launch();
    }
}