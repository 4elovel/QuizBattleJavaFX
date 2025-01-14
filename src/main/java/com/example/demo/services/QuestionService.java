package com.example.demo.services;

import com.example.demo.entities.Question;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestionService {

    private static final String FILE_PATH = "data.txt";
    private final List<Question> questions;
    private int currentIndex = 0;

    public QuestionService(List<Question> questions) {
        this.questions = questions;
        Collections.shuffle(this.questions);
    }

    public static List<Question> loadQuestions() {
        List<Question> questions = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    questions.add(parseQuestion(line));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return questions;
    }


    private static Question parseQuestion(String line) {
        String[] parts = splitWithEscapedDelimiter(line, ";").toArray(new String[0]);
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid question format: " + line);
        }
        String questionText = unescape(parts[0].trim());

        List<String> answerOptions = new ArrayList<>();
        for (String option : splitWithEscapedDelimiter(parts[1], ",")) {
            answerOptions.add(unescape(option));
        }

        List<Integer> correctAnswerIndexes = new ArrayList<>();
        for (String index : parts[2].split(",")) {
            correctAnswerIndexes.add(Integer.parseInt(index.trim()));
        }

        return new Question(questionText, answerOptions, correctAnswerIndexes);
    }

    private static List<String> splitWithEscapedDelimiter(String text, String delimiter) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inEscape = false;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (inEscape) {
                current.append(c);
                inEscape = false;
            } else if (c == '\\') {
                inEscape = true;
            } else if (text.startsWith(delimiter, i)) {
                result.add(current.toString().trim());
                current.setLength(0);
                i += delimiter.length() - 1; // Пропускаємо довжину роздільника
            } else {
                current.append(c);
            }
        }

        result.add(current.toString().trim());
        return result;
    }

    private static String escape(String text) {
        return text.replace("\\", "\\\\")
                .replace(";", "\\;")
                .replace(",", "\\\\,");
    }

    private static String unescape(String text) {
        StringBuilder result = new StringBuilder();
        boolean inEscape = false;

        for (char c : text.toCharArray()) {
            if (inEscape) {
                result.append(c);
                inEscape = false;
            } else if (c == '\\') {
                inEscape = true;
            } else {
                result.append(c);
            }
        }

        return result.toString();
    }

    public static void saveQuestion(String questionText, List<String> answerOptions,
            List<Integer> correctAnswerIndexes) throws IOException {
        String escapedQuestion = escape(questionText);
        List<String> escapedOptions = new ArrayList<>();
        for (String option : answerOptions) {
            escapedOptions.add(escape(option));
        }

        StringBuilder line = new StringBuilder();
        line.append(escapedQuestion).append(";");
        line.append(String.join(",", escapedOptions)).append(";");
        line.append(String.join(",",
                correctAnswerIndexes.stream().map(String::valueOf).toArray(String[]::new)));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(line.toString());
            writer.newLine();
        }
    }

    public static void setDatabase(List<Question> questions) throws IOException {
        try (FileWriter writer = new FileWriter(FILE_PATH, false)) {
        }
        for (Question question : questions) {
            saveQuestion(question.getQuestionText(), question.getAnswerOptions(),
                    question.getCorrectAnswerIndexes());
        }
    }


    public Question getNextQuestion() {
        if (currentIndex < questions.size()) {
            return questions.get(currentIndex++);
        }
        return null;
    }
}
