module com.td.player {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.desktop;

    opens com.td.player to javafx.fxml;
    exports com.td.player;
    exports com.td.player.elements;
    opens com.td.player.elements to javafx.fxml;
    exports com.td.player.managers;
    opens com.td.player.managers to javafx.fxml;
    exports com.td.player.controllers;
    opens com.td.player.controllers to javafx.fxml;
    exports com.td.player.util;
}