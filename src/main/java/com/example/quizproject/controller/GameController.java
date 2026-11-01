
package com.example.quizproject.controller;
import com.example.quizproject.GameManager;

import com.example.quizproject.model.*;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import com.example.quizproject.model.QuizResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.nio.file.Files;
import java.nio.file.Path;
import com.fasterxml.jackson.databind.JsonNode;

import javafx.beans.binding.Bindings;


public class GameController {

    @FXML
    private Label playerLabel;

    @FXML
    private Label questionLabel;

    @FXML
    private VBox answersBox;

    @FXML
    private Button nextButton;

    private final GameManager gameManager = GameManager.getInstance();

    private ToggleGroup answerGroup;

    @FXML
    private Label scoreLabel;


    @FXML
    private void initialize() {
        playerLabel.setText("Player: " + gameManager.getPlayerName());

        scoreLabel.textProperty().bind(
                Bindings.concat("Score: ", gameManager.scoreProperty())
        );

        showQuestion();
    }

    private void showQuestion() {

        if (gameManager.getQuiz() == null
                || gameManager.getCurrentQuestion() == null) {

            questionLabel.setText("No questions available.");
            answersBox.getChildren().clear();
            nextButton.setDisable(true);
            return;
        }

        Question question = gameManager.getCurrentQuestion();

        questionLabel.setText(question.getTitle());

        // Remove answers from the previous question
        answersBox.getChildren().clear();


        answerGroup = new ToggleGroup();

        nextButton.setDisable(true);

        if (question instanceof MultipleChoice mcQuestion) {

            for (String choice : mcQuestion.getChoices()) {

                RadioButton radioButton = new RadioButton(choice);
                radioButton.setToggleGroup(answerGroup);

                answersBox.getChildren().add(radioButton);
            }

        } else if (question instanceof TrueFalse booleanQuestion) {

            RadioButton trueButton =
                    new RadioButton(booleanQuestion.getLabelTrue());

            RadioButton falseButton =
                    new RadioButton(booleanQuestion.getLabelFalse());

            trueButton.setToggleGroup(answerGroup);
            falseButton.setToggleGroup(answerGroup);

            answersBox.getChildren().addAll(trueButton, falseButton);
        }
        answerGroup.selectedToggleProperty().addListener(
                (observable, oldValue, newValue) -> {
                    nextButton.setDisable(newValue == null);
                }
        );
    }

    @FXML
    private void handleNextQuestion() {

        RadioButton selectedButton =
                (RadioButton) answerGroup.getSelectedToggle();

        if (selectedButton == null) {
            return;
        }

        Question currentQuestion = gameManager.getCurrentQuestion();

        boolean isCorrect = false;

        if (currentQuestion instanceof MultipleChoice mcQuestion) {

            isCorrect = selectedButton.getText()
                    .equals(mcQuestion.getCorrectAnswer());

        } else if (currentQuestion instanceof TrueFalse booleanQuestion) {

            boolean selectedAnswer =
                    selectedButton.getText().equals(booleanQuestion.getLabelTrue());

            isCorrect = selectedAnswer == booleanQuestion.isCorrectAnswer();
        }

        if (isCorrect) {
            gameManager.incrementScore();
        }

        gameManager.nextQuestion();

        if (gameManager.isFinished()) {
            showResults();
        } else {
            showQuestion();
        }
    }

    private void showResults() {

        try {
            saveResult();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Save Error");
            alert.setHeaderText("Could not save quiz result");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/com/example/quizproject/view/results-view.fxml"
                    )
            );

            Scene scene = new Scene(loader.load(), 600, 400);

            Stage stage = (Stage) questionLabel.getScene().getWindow();

            stage.setScene(scene);
            stage.setTitle("Quiz Results");

        } catch (IOException e) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Navigation Error");
            alert.setHeaderText("Could not open Results screen");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void saveResult() throws IOException {

        QuizResult result = new QuizResult(
                gameManager.getPlayerName(),
                gameManager.getQuiz().getQuestions().size(),
                gameManager.getScore()
        );

        ObjectMapper mapper = new ObjectMapper();

        Path filePath = Path.of(
                gameManager.getQuiz().getQuizId()
                        + "-results.json"
        );

        ObjectNode root;
        ArrayNode results;

        if (Files.exists(filePath)) {

            // Read the existing results file
            JsonNode existingData = mapper.readTree(filePath.toFile());

            if (existingData == null || !existingData.isObject()
                    || !existingData.path("results").isArray()) {
                throw new IOException("Existing results file is invalid.");
            }

            root = (ObjectNode) existingData;
            results = (ArrayNode) root.get("results");

        } else {

            // Create a new results file structure
            root = mapper.createObjectNode();
            root.put("quizId", gameManager.getQuiz().getQuizId()
            );
            root.put("name", gameManager.getQuiz().getTitle());

            results = mapper.createArrayNode();
            root.set("results", results);
        }

        // Append the new player result
        results.add(mapper.valueToTree(result));

        // Save the updated JSON
        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(filePath.toFile(), root);

        System.out.println("Result saved to: "
                + filePath.toAbsolutePath());
    }


}


