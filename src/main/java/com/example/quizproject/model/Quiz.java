package com.example.quizproject.model;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

public class Quiz {

    // Attributes
        //I use string for ids despite they are int in my model cuz of json id format
    private String quizId;
    private String title;
    private String description;
    private List<Question> questions;

    // Constructor
    public Quiz(String quizId, String title, String description) {
        this.quizId = Objects.requireNonNull(quizId, "Quiz ID is required");
        this.title = Objects.requireNonNull(title, "Quiz title is required");
        this.description = description;
        this.questions = new ArrayList<>();
    }

    // Getters
    public String getQuizId() {
        return quizId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    // Add a question to the quiz
    public void addQuestion(Question question) {
        questions.add(question);
    }
}
