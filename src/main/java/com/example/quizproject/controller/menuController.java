package com.example.quizproject.controller;


import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import java.util.Optional;

public class menuController {
    @FXML
    private Button startButton;

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
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Load Quiz");
        alert.setHeaderText(null);
        alert.setContentText("JSON loading will be implemented in Week 4.");
        alert.showAndWait();

        // Temporary for Week 3:
        // Allow testing navigation without loading JSON.
        startButton.setDisable(false);
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
