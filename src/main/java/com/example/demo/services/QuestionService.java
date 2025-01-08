package com.example.demo.services;

import com.example.demo.entities.Question;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestionService {

    private final List<Question> questions;
    private int currentIndex = 0;

    public QuestionService(List<Question> questions) {
        this.questions = questions;
        Collections.shuffle(this.questions);
    }

    public static List<Question> loadQuestions() {
        String filePath = "data.txt";
        List<Question> questions = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
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
        String questionText = parts[0].trim();
        List<String> answerOptions = splitWithEscapedDelimiter(parts[1], ",");
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

        for (char c : text.toCharArray()) {
            if (inEscape) {
                if (c == delimiter.charAt(0) || c == '\\') {
                    current.append(c);
                } else {
                    current.append('\\').append(c);
                }
                inEscape = false;
            } else if (c == '\\') {
                inEscape = true;
            } else if (c == delimiter.charAt(0)) {
                result.add(current.toString().trim());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }

        result.add(current.toString().trim());
        return result;
    }

    public Question getNextQuestion() {
        if (currentIndex < questions.size()) {
            return questions.get(currentIndex++);
        }
        return null;
    }
}
