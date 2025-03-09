module com.example.lab08v1 {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires gs.core;
    requires gs.ui.javafx;
    requires javafx.swing;

    opens com.example.lab08v1 to javafx.fxml;
    exports com.example.lab08v1;
}