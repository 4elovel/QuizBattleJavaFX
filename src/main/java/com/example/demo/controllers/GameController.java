package com.example.demo.controllers;

import com.example.demo.entities.Player;
import com.example.demo.entities.Question;
import com.example.demo.services.QuestionService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import lombok.Getter;

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
    GridPane answerGrid;
    @FXML
    Text question;
    QuestionService questionService = new QuestionService(QuestionService.loadQuestions());
    Question currentQuestion;
    @Getter
    Player player1 = new Player();
    @Getter
    Player player2 = new Player();
    boolean isPlayer1Turn = true;

/*    public static void delay(long millis, Runnable continuation) {
        Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Thread.sleep(millis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        sleeper.setOnSucceeded(event -> continuation.run());
        new Thread(sleeper).start();
    }*/

    @FXML
    public void initialize() {
        rerenderScene();
        answerGrid.setHgap(10);
        answerGrid.setVgap(10);
    }

    private void handleAnswerSelection(Button button, String selectedAnswer) {
        boolean isCorrect = currentQuestion.isCorrectAnswer(selectedAnswer);

        if (isCorrect) {
            button.setStyle("-fx-background-color: #32CD32;");
            if (isPlayer1Turn) {
                player1.incrementScore();
            } else {

                player2.incrementScore();
            }
        } else {
            button.setStyle(
                    "-fx-background-color: #FF6347;");
        }

        isPlayer1Turn = !isPlayer1Turn;

        //delay(5000, this::rerenderScene);
        rerenderScene();
    }

    private void rerenderScene() {
        currentQuestion = questionService.getNextQuestion();
        var answers = currentQuestion.getAnswerOptions();
        int size = answers.size();
        question.setText(currentQuestion.getQuestionText());

        int columns = (int) Math.ceil(Math.sqrt(size));
        int rows = (int) Math.ceil((double) size / columns);

        if (currentQuestion == null) {
            var winner = getWinner();
            question.setText("TIE");
            if (winner != null) {
                question.setText(winner.getName() + " WINS WITH SCORE - " + winner.getScore());
            }
            return;
        }

        setupAnswerButtons(currentQuestion, columns);
        setupGridConstraints(columns, rows);

    }

    private void setupAnswerButtons(Question currentQuestion, int columns) {
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

    private void setupGridConstraints(int columns, int rows) {
        answerGrid.getColumnConstraints().clear();
        answerGrid.getRowConstraints().clear();

        for (int i = 0; i < columns; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(100.0 / columns);
            answerGrid.getColumnConstraints().add(col);
        }

        for (int i = 0; i < rows; i++) {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(100.0 / rows);
            answerGrid.getRowConstraints().add(row);
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
}
