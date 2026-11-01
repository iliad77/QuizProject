
package com.example.quizproject.model;

import java.time.Instant;

public class QuizResult {

    private String playerName;
    private int totalQuestions;
    private int correctQuestions;
    private String date;

    public QuizResult(String playerName, int totalQuestions, int correctQuestions) {
        this.playerName = playerName;
        this.totalQuestions = totalQuestions;
        this.correctQuestions = correctQuestions;
        this.date = Instant.now().toString();
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public int getCorrectQuestions() {
        return correctQuestions;
    }

    public String getDate() {
        return date;
    }
}

