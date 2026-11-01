
package com.example.quizproject;

import com.example.quizproject.model.Quiz;
import com.example.quizproject.model.Question;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class GameManager {

    private static final GameManager instance = new GameManager();

    private Quiz quiz;
    private String playerName;
    private int currentQuestionIndex;
    private final IntegerProperty score = new SimpleIntegerProperty(0);

    private GameManager() {
    }

    public static GameManager getInstance() {
        return instance;
    }

    public void startGame(Quiz quiz, String playerName) {
        this.quiz = quiz;
        this.playerName = playerName;
        this.currentQuestionIndex = 0;
        score.set(0);
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getScore() {
        return score.get();
    }

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public Question getCurrentQuestion() {
        if (quiz == null
                || currentQuestionIndex >= quiz.getQuestions().size()) {
            return null;
        }

        return quiz.getQuestions().get(currentQuestionIndex);
    }

    public void incrementScore() {
        score.set(score.get() + 1);
    }

    public IntegerProperty scoreProperty() {
        return score;
    }

    public void nextQuestion() {
        currentQuestionIndex++;
    }

    public boolean isFinished() {
        return quiz != null
                && currentQuestionIndex >= quiz.getQuestions().size();
    }
}
