package com.example.quizproject.model;

public class TrueFalse extends Question{
    private String labelTrue;
    private String labelFalse;
    private boolean correctAnswer;

    public TrueFalse(
            String questionId,
            String title,
            int timeLimit,
            boolean required,
            String labelTrue,
            String labelFalse,
            boolean correctAnswer) {

        super(questionId, title, timeLimit, required);

        this.labelTrue = labelTrue;
        this.labelFalse = labelFalse;
        this.correctAnswer = correctAnswer;
    }

    public String getLabelTrue() {
        return labelTrue;
    }

    public String getLabelFalse() {
        return labelFalse;
    }

    public boolean isCorrectAnswer() {
        return correctAnswer;
    }
}

