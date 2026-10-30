package com.example.quizproject.model;
import java.util.List;

public class MultipleChoice extends Question {
    private List<String> choices;
    private String choicesOrder;
    private String correctAnswer;

    public MultipleChoice(
            String questionId,
            String title,
            int timeLimit,
            boolean required,
            List<String> choices,
            String choicesOrder,
            String correctAnswer) {

        super(questionId, title, timeLimit, required);

        this.choices = choices;
        this.choicesOrder = choicesOrder;
        this.correctAnswer = correctAnswer;
    }

    public List<String> getChoices() {
        return choices;
    }

    public String getChoicesOrder() {
        return choicesOrder;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
}

