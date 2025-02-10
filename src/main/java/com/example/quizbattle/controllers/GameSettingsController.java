package com.example.quizbattle.controllers;

import com.example.quizbattle.entities.Question;
import com.example.quizbattle.services.QuestionService;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import lombok.SneakyThrows;


public class GameSettingsController {

    private final List<Question> loadedQuestions = QuestionService.loadQuestions();
    @FXML
    private TableView<Question> questionsTable;
    @FXML
    private TableColumn<Question, String> questionColumn;
    @FXML
    private TableColumn<Question, String> answersColumn;
    @FXML
    private TableColumn<Question, String> correctAnswersColumn;
    private ObservableList<Question> questions;

    @FXML
    public void initialize() {
        questions = FXCollections.observableArrayList(loadedQuestions);

        questionsTable.setItems(questions);

        questionColumn.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getQuestionText()));

        answersColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                String.join(", ", cellData.getValue().getAnswerOptions())));

        correctAnswersColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getCorrectAnswers().stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(", "))
        ));

        questionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        questionColumn.setOnEditCommit(event -> {
            Question question = event.getRowValue();
            question.setQuestionText(event.getNewValue());
        });

        answersColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        answersColumn.setOnEditCommit(event -> {
            Question question = event.getRowValue();
            String newAnswersText = event.getNewValue();
            List<String> newAnswers = Arrays.asList(newAnswersText.split(","));
            question.setAnswerOptions(newAnswers);
        });

        correctAnswersColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        correctAnswersColumn.setOnEditCommit(event -> {
            Question question = event.getRowValue();
            String newCorrectAnswersText = event.getNewValue();
            List<Integer> newCorrectAnswers = Arrays.stream(newCorrectAnswersText.split(","))
                    .map(String::trim)
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            question.setCorrectAnswerIndexes(newCorrectAnswers);
        });

        questionsTable.widthProperty().addListener((observable, oldValue, newValue) -> {
            double tableWidth = newValue.doubleValue();
            double columnWidth = tableWidth / 3;

            questionColumn.setPrefWidth(columnWidth);
            answersColumn.setPrefWidth(columnWidth);
            correctAnswersColumn.setPrefWidth(columnWidth);
        });

        questionsTable.setEditable(true);
    }


    @FXML
    private void handleAddButtonAction() {
        Question newQuestion = new Question("Neue Frage", Arrays.asList("Option 1", "Option 2"),
                List.of(0));
        questions.add(newQuestion);
    }

    @FXML
    @SneakyThrows
    private void handleGoToMainMenuButtonAction() {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/quizbattle/main-menu.fxml"));
        Scene scene = new Scene(loader.load(), 320, 240);
        Stage stage = (Stage) questionsTable.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void handleDeleteButtonAction() {
        Question selectedQuestion = questionsTable.getSelectionModel().getSelectedItem();

        if (selectedQuestion != null) {
            questions.remove(selectedQuestion);
            try {
                QuestionService.setDatabase(questions);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Keine Frage ausgewählt.");
        }
    }

    @FXML
    private void handleSaveChangesButtonAction() throws IOException {
        QuestionService.setDatabase(questions);
    }
}
