package com.example.demo.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class GameMenuController {

    @FXML
    TextField player1Name;
    @FXML
    TextField player2Name;


    @FXML
    protected void startGame(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/demo/game.fxml"));

            Parent root = loader.load();

            GameController gameController = loader.getController();
            gameController.setPlayersNames(player1Name.getText(), player2Name.getText());

            Stage stage = (Stage) player1Name.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Game");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
