package com.example.quizproject.controller;

import com.example.quizproject.GameManager;

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

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;



import java.util.ArrayList;
import java.util.List;

import com.example.quizproject.QuestionFactory;

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
            return;
        }

        loadedQuiz = null;
        startButton.setDisable(true);
        try {
            ObjectMapper mapper = new ObjectMapper();

            JsonNode root = mapper.readTree(selectedFile);

            if (root == null || !root.isObject()) {
                throw new IllegalArgumentException(
                        "The JSON root must be an object."
                );
            }

            if (!root.hasNonNull("quizId")) {
                throw new IllegalArgumentException("Quiz ID is missing.");
            }

            if (!root.hasNonNull("title")) {
                throw new IllegalArgumentException("Quiz title is missing.");
            }

            if (!root.hasNonNull("pages")) {
                throw new IllegalArgumentException("Quiz pages are missing.");
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


                    Question question = QuestionFactory.createQuestion(element, timeLimit);

                    if (question != null) {
                        quiz.addQuestion(question);
                    } else {
                        skippedQuestions++;
                        System.out.println("Unsupported question type: " + type);
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
            loadedQuiz = quiz;
            startButton.setDisable(false);
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

        if (loadedQuiz == null || loadedQuiz.getQuestions().isEmpty()) {
            return;
        }

        if (result.isPresent()) {

            String playerName = result.get().trim();

            if (playerName.isEmpty()) {
                showError("Please enter your name.");
                return;
            }



            try {

                GameManager gameManager = GameManager.getInstance();

                gameManager.startGame(loadedQuiz, playerName);


                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource(
                                "/com/example/quizproject/view/game-view.fxml"
                        )
                );

                Parent root = loader.load();


                Stage stage = (Stage) startButton.getScene().getWindow();

                Scene scene = new Scene(root, 600, 400);

                stage.setScene(scene);
                stage.setTitle("Quiz - " + loadedQuiz.getTitle());

            } catch (IOException e) {
                e.printStackTrace();

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Navigation Error");
                alert.setHeaderText("Could not open Game screen");
                alert.setContentText(e.toString());
                alert.showAndWait();
            }
        }
    }
}
