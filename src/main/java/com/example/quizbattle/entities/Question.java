package com.example.quizbattle.entities;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Question {

    @Getter
    @Setter
    private String questionText;
    @Getter
    private List<String> answerOptions;
    private List<Integer> correctAnswerIndexes;


    public List<Integer> getCorrectAnswers() {
        return correctAnswerIndexes;
    }

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
