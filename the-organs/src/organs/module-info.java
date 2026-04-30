module organs {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens organs to javafx.fxml;
    exports organs;
    exports organs.audio;
    exports organs.instruments;
}