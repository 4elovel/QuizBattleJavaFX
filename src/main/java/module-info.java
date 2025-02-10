module com.example.quizbattle {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;

    opens com.example.quizbattle to javafx.fxml;
    exports com.example.quizbattle;
    exports com.example.quizbattle.controllers;
    opens com.example.quizbattle.controllers to javafx.fxml;
}