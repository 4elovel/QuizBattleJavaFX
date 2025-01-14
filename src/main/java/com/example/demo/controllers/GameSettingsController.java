package com.example.demo.controllers;

import com.example.demo.entities.Question;
import com.example.demo.services.QuestionService;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;


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

        // Встановлення даних для таблиці
        questionsTable.setItems(questions);

        // Прив'язка першого стовпця до питання
        questionColumn.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getQuestionText()));

        // Прив'язка другого стовпця до варіантів відповідей (об'єднання їх в один рядок)
        answersColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                String.join(", ", cellData.getValue().getAnswerOptions())));

        correctAnswersColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getCorrectAnswers().stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(", "))
        ));

        // Дозволяємо редагувати стовпець питання
        questionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        questionColumn.setOnEditCommit(event -> {
            Question question = event.getRowValue();
            question.setQuestionText(event.getNewValue()); // Оновлення питання
        });

        // Дозволяємо редагувати стовпець з варіантами відповідей
        answersColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        answersColumn.setOnEditCommit(event -> {
            Question question = event.getRowValue();
            String newAnswersText = event.getNewValue();
            List<String> newAnswers = Arrays.asList(newAnswersText.split(","));
            question.setAnswerOptions(newAnswers); // Оновлення варіантів відповідей
        });

        // Дозволяємо редагувати стовпець з правильними відповідями
        correctAnswersColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        correctAnswersColumn.setOnEditCommit(event -> {
            Question question = event.getRowValue();
            String newCorrectAnswersText = event.getNewValue();
            List<Integer> newCorrectAnswers = Arrays.stream(newCorrectAnswersText.split(","))
                    .map(String::trim)
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            question.setCorrectAnswerIndexes(newCorrectAnswers); // Оновлення правильних відповідей
        });

        // Активуємо редагування таблиці при натисканні на клітинки
        questionsTable.setEditable(true);
    }


    // Метод для додавання нового питання
    @FXML
    private void handleAddButtonAction() {
        Question newQuestion = new Question("New Question", Arrays.asList("Option 1", "Option 2"),
                List.of(0));
        questions.add(newQuestion);
    }

    @FXML
    private void handleDeleteButtonAction() {
        // Отримуємо вибране питання з таблиці
        Question selectedQuestion = questionsTable.getSelectionModel().getSelectedItem();

        // Перевірка, чи є вибране питання
        if (selectedQuestion != null) {
            // Видалення питання з ObservableList
            questions.remove(selectedQuestion);

            // Можна також оновити базу даних, якщо потрібно
            try {
                QuestionService.setDatabase(questions);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No question selected.");
        }
    }

    @FXML
    private void handleSaveChangesButtonAction() throws IOException {
        QuestionService.setDatabase(questions);
    }
}
