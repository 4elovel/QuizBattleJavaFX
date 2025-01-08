module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;

    opens com.example.demo to javafx.fxml;
    exports com.example.demo;
    exports com.example.demo.controllers;
    opens com.example.demo.controllers to javafx.fxml;
}