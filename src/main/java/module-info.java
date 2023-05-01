module com.td.player {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    opens com.td.player to javafx.fxml;
    exports com.td.player;
}