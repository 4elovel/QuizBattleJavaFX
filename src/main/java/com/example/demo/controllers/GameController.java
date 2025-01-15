package com.example.demo.controllers;

import com.example.demo.entities.Player;
import com.example.demo.entities.Question;
import com.example.demo.services.QuestionService;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.Getter;
import lombok.SneakyThrows;

public class GameController {

    private static final Color[] BUTTON_COLORS = {
            Color.web("#8FBC8F"), // Dark Sea Green
            Color.web("#DCDCDC"), // Gainsboro
            Color.web("#B0C4DE"), // Light Steel Blue
            Color.web("#F4A460"), // Sandy Brown
            Color.web("#D8BFD8"), // Thistle
            Color.web("#98FB98"), // Pale Green
            Color.web("#ADD8E6"), // Light Blue
            Color.web("#FF6347"), // Tomato
            Color.web("#FFD700"), // Gold
            Color.web("#C71585")  // Medium Violet Red
    };
    @FXML
    Button backToMenuButton;
    @FXML
    GridPane answerGrid;
    @FXML
    TextFlow questionText;
    @FXML
    GridPane playersInfoGrid;
    @FXML
    Text question;
    @FXML
    Label player1Name;
    @FXML
    Label player2Name;
    @FXML
    Label player1Score;
    @FXML
    Label player2Score;

    QuestionService questionService = new QuestionService(QuestionService.loadQuestions());
    Question currentQuestion;
    @Getter
    Player player1 = new Player();
    @Getter
    Player player2 = new Player();
    boolean isPlayer1Turn = true;


    @FXML
    public void initialize() {
        rerenderScene();
    }

    private void rerenderScene() {
        currentQuestion = questionService.getNextQuestion();
        if (currentQuestion == null || player1.getScore() >= 1000 || player2.getScore() >= 1000) {
            var winner = getWinner();
            question.setText("TIE");
            if (winner != null) {
                question.setText(winner.getName() + " wins with score - " + winner.getScore());
            }
            backToMenuButton.setVisible(true);
            return;
        }
        var answers = currentQuestion.getAnswerOptions();
        int size = answers.size();
        question.setText(currentQuestion.getQuestionText());

        int columns = (int) Math.ceil(Math.sqrt(size));
        int rows = (int) Math.ceil((double) size / columns);

        setupAnswerButtons(currentQuestion, columns);
        setupGridConstraints(answerGrid, columns, rows);
        setupGridConstraints(playersInfoGrid, columns, rows);
        updatePlayerHighlight();
        setupPlayersLabel();

    }

    private void setupPlayersLabel() {
        while (playersInfoGrid.getColumnCount() != answerGrid.getColumnCount()) {
            if (playersInfoGrid.getColumnCount() < answerGrid.getColumnCount()) {
                playersInfoGrid.getColumnConstraints().add(1, new ColumnConstraints());
                continue;
            }
            if (playersInfoGrid.getColumnConstraints().size() == 1) {
                break;
            }
            if (playersInfoGrid.getColumnCount() > 2) {
                playersInfoGrid.getColumnConstraints().remove(1);
                continue;
            }
            break;
        }
    }

    private void handleAnswerSelection(Button button, String selectedAnswer) {
        boolean isCorrect = currentQuestion.isCorrectAnswer(selectedAnswer);

        if (isCorrect) {
            button.setStyle("-fx-background-color: #32CD32;");
            if (isPlayer1Turn) {
                player1.incrementScore();
                player1Score.setText("score: " + player1.getScore());
            } else {

                player2.incrementScore();
                player2Score.setText("score: " + player2.getScore());
            }
        } else {
            button.setStyle(
                    "-fx-background-color: #FF6347;");
        }

        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        answerGrid.setDisable(true);
        pause.setOnFinished(event -> {
            answerGrid.setDisable(false);
            isPlayer1Turn = !isPlayer1Turn;
            rerenderScene();
        });

        pause.play();
    }


    private void setupAnswerButtons(Question currentQuestion, int columns) {
        answerGrid.getChildren().clear();
        var answers = currentQuestion.getAnswerOptions();
        int size = answers.size();
        int colorIndex = 0;

        for (int i = 0; i < size; i++) {
            Button button = new Button(answers.get(i));
            button.setMaxWidth(Double.MAX_VALUE);
            button.setMaxHeight(Double.MAX_VALUE);
            button.setOnAction(event -> handleAnswerSelection(button, button.getText()));

            button.setStyle("-fx-background-color: " + toHex(BUTTON_COLORS[colorIndex]) + ";"
                    + "-fx-text-fill: white;"
                    + "-fx-font-size: calc(1vw + 12px);"
                    + "-fx-font-weight: bold;"
                    + "-fx-padding: 10px;"
                    + "-fx-border-radius: 8px;"
                    + "-fx-cursor: hand;"
                    + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 5, 0, 2, 2);");

            button.setStyle("-fx-background-color: " + toHex(BUTTON_COLORS[colorIndex]) + ";");

            colorIndex = (colorIndex + 1) % BUTTON_COLORS.length;

            int row = i / columns;
            int col = i % columns;
            if (i == size - 1) {
                answerGrid.add(button, col, row, columns - col, 1);
            } else {
                answerGrid.add(button, col, row);
            }
        }
    }

    private String toHex(Color color) {
        return String.format("#%02X%02X%02X", (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255), (int) (color.getBlue() * 255));
    }

    private void setupGridConstraints(GridPane grid, int columns, int rows) {
        grid.getColumnConstraints().clear();
        grid.getRowConstraints().clear();

        for (int i = 0; i < columns; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(100.0 / columns);
            grid.getColumnConstraints().add(col);
        }

        for (int i = 0; i < rows; i++) {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(100.0 / rows);
            grid.getRowConstraints().add(row);
        }
    }

    private Player getWinner() {
        if (player1.getScore() > player2.getScore()) {
            return player1;
        } else if (player2.getScore() > player1.getScore()) {
            return player2;
        } else {
            return null; // tie
        }
    }

    public void setPlayersNames(String name1, String name2) {
        player1.setName(name1);
        player2.setName(name2);
        player1Name.setText(name1);
        player2Name.setText(name2);
    }


    private void updatePlayerHighlight() {
        if (isPlayer1Turn) {
            player1Name.setStyle(
                    "-fx-border-color: #FFD700;-fx-background-color: #FFD700; -fx-border-width: 3px; -fx-border-radius: 5px;;-fx-background-radius: 3px;");
            player2Name.setStyle(
                    "-fx-border-color: transparent;-fx-background-color: transparent;");
        } else {
            player2Name.setStyle(
                    "-fx-border-color: #FFD700;-fx-background-color: #FFD700; -fx-border-width: 3px; -fx-border-radius: 5px;-fx-background-radius: 3px;");
            player1Name.setStyle(
                    "-fx-border-color: transparent;-fx-background-color: transparent;");
        }
    }

    @FXML
    @SneakyThrows
    private void handleBackToMenu() {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/demo/main-menu.fxml"));
        Scene scene = new Scene(loader.load(), 320, 240);
        Stage stage = (Stage) backToMenuButton.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
