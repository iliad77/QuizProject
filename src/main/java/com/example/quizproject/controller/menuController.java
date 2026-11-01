package com.example.quizproject.controller;

import com.example.quizproject.model.Quiz;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import java.io.File;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import java.util.Optional;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import com.example.quizproject.model.Question;
import com.example.quizproject.model.MultipleChoice;
import com.example.quizproject.model.TrueFalse;

import java.util.ArrayList;
import java.util.List;

public class menuController {
    @FXML
    private Button startButton;
    private Quiz loadedQuiz;

    @FXML
    private void initialize() {
        startButton.setDisable(true);
    }
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Input");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleLoadQuiz() {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Select Quiz File");

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "JSON Files", "*.json"
                )
        );


        Window window = startButton.getScene().getWindow();

        File selectedFile = fileChooser.showOpenDialog(window);

        if (selectedFile == null) {
            return;}
        try {
            ObjectMapper mapper = new ObjectMapper();

            JsonNode root = mapper.readTree(selectedFile);

            if (!root.hasNonNull("quizId")) {
                throw new IllegalArgumentException("Quiz ID is missing.");
            }

            if (!root.hasNonNull("title")) {
                throw new IllegalArgumentException("Quiz title is missing.");
            }

            if (!root.hasNonNull("pages")) {
                throw new IllegalArgumentException("Quiz pages are missing.");
            }

            if (root == null || !root.isObject()) {
                throw new IllegalArgumentException(
                        "The JSON root must be an object."
                );
            }

            String quizId = root.path("quizId").asText();
            String title = root.path("title").asText();
            String description = root.path("description").asText();
            JsonNode pages = root.path("pages");

            if (!pages.isArray()) {
                throw new IllegalArgumentException(
                        "The quiz must contain a pages array."
                );
            }

            if (pages.isEmpty()) {
                throw new IllegalArgumentException(
                        "The quiz must contain at least one page."
                );
            }



            Quiz quiz = new Quiz(quizId, title, description);

            int skippedQuestions = 0;

            for (JsonNode page : pages) {

                int timeLimit = page.path("timeLimit").asInt();

                JsonNode elements = page.path("elements");

                if (!page.hasNonNull("timeLimit")) {
                    throw new IllegalArgumentException("Question timeLimit is missing.");
                }

                if (!page.hasNonNull("elements")) {
                    throw new IllegalArgumentException("Question elements are missing.");
                }


                for (JsonNode element : elements) {

                    String type = element.path("type").asText();
                    String questionId = element.path("name").asText();
                    String questionTitle = element.path("title").asText();
                    boolean required = element.path("isRequired").asBoolean();

                    if (!element.hasNonNull("type")) {
                        throw new IllegalArgumentException("Question type is missing.");
                    }

                    if (!element.hasNonNull("name")) {
                        throw new IllegalArgumentException("Question ID is missing.");
                    }

                    if (!element.hasNonNull("title")) {
                        throw new IllegalArgumentException("Question title is missing.");
                    }

                    if (!element.hasNonNull("isRequired")) {
                        throw new IllegalArgumentException("Question isRequired is missing.");
                    }


                    Question question = null;

                    if (type.equals("radiogroup")) {

                        List<String> choices = new ArrayList<>();

                        for (JsonNode choice : element.path("choices")) {
                            choices.add(choice.asText());
                        }

                        String choicesOrder =
                                element.path("choicesOrder").asText();

                        String correctAnswer =
                                element.path("correctAnswer").asText();

                        question = new MultipleChoice(
                                questionId,
                                questionTitle,
                                timeLimit,
                                required,
                                choices,
                                choicesOrder,
                                correctAnswer
                        );

                    } else if (type.equals("boolean")) {

                        String labelTrue =
                                element.path("labelTrue").asText();

                        String labelFalse =
                                element.path("labelFalse").asText();

                        boolean correctAnswer =
                                element.path("correctAnswer").asBoolean();

                        question = new TrueFalse(
                                questionId,
                                questionTitle,
                                timeLimit,
                                required,
                                labelTrue,
                                labelFalse,
                                correctAnswer
                        );
                    }
                    else {

                        skippedQuestions++;
                        System.out.println("Unsupported question type: " + type);
                    }

                    if (question != null) {
                        quiz.addQuestion(question);
                    }
                }
            }

            if (skippedQuestions > 0) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Unsupported Questions");
                alert.setHeaderText(null);
                alert.setContentText(
                        skippedQuestions + " unsupported question(s) were skipped."
                );
                alert.showAndWait();
            }
        } catch (IOException | IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not read quiz file");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void handleStartQuiz() {

        TextInputDialog dialog = new TextInputDialog();

        dialog.setTitle("Start Quiz");
        dialog.setHeaderText("Welcome to the Quiz!");
        dialog.setContentText("Enter your name:");

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {

            String playerName = result.get().trim();

            if (playerName.isEmpty()) {
                showError("Please enter your name.");
                return;
            }

            System.out.println("Player name: " + playerName);
        }
    }
}
