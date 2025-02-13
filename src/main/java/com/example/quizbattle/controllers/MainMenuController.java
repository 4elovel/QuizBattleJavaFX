package com.example.quizbattle.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import lombok.SneakyThrows;

public class MainMenuController {

    @FXML
    private Button btnStart;

    @FXML
    private Button btnSettings;

    @FXML
    private Button btnExit;


    @FXML
    private void initialize() {
        btnStart.setOnAction(this::startGameMenu);
        btnExit.setOnAction(event -> System.exit(0));
        btnSettings.setOnAction(this::startSettings);
    }

    @SneakyThrows
    private void startGameMenu(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/quizbattle/game-menu.fxml"));

        Parent root = loader.load();

        Stage stage = (Stage) btnStart.getScene().getWindow();
        stage.setScene(new Scene(root, 320, 300));

    }

    @SneakyThrows
    private void startSettings(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/quizbattle/game-settings.fxml"));

        Parent root = loader.load();

        Stage stage = (Stage) btnStart.getScene().getWindow();
        stage.setScene(new Scene(root, 640, 600));

    }
}
