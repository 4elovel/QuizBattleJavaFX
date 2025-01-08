package com.example.demo.entities;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Question {

    private String questionText;
    private List<String> answerOptions;
    private List<Integer> correctAnswerIndexes;

    public boolean isCorrectAnswer(String Answer) {
        return correctAnswerIndexes.contains(answerOptions.indexOf(Answer));
    }

    public void printDebug() {
        System.out.println(questionText);
        for (String option : answerOptions) {
            System.out.print(option + " @ ");
        }
        System.out.println();
        for (Integer index : correctAnswerIndexes) {
            System.out.print(index + "=>" + answerOptions.get(index) + " ");
        }
        System.out.println();
        System.out.println();
    }
}
