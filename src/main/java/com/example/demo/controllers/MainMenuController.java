package com.example.demo.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

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
        btnSettings.setOnAction(event -> System.out.println("Settings button clicked!"));
        btnExit.setOnAction(event -> System.exit(0));
    }

    private void startGameMenu(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/demo/game-menu.fxml"));

            Parent root = loader.load();

            Stage stage = (Stage) btnStart.getScene().getWindow();
            stage.setScene(new Scene(root, 320, 240));
            stage.setTitle("Game");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
