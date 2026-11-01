
package com.example.quizproject.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

import com.example.quizproject.GameManager;

public class ResultsController {

    @FXML
    private Label playerLabel;

    @FXML
    private Label scoreLabel;

    @FXML
    private void initialize() {

        GameManager gameManager = GameManager.getInstance();

        playerLabel.setText(
                "Player: " + gameManager.getPlayerName()
        );

        scoreLabel.setText(
                "Score: " + gameManager.getScore()
                        + "/" + gameManager.getQuiz().getQuestions().size()
        );
    }

    @FXML
    private void handleBackToMenu() {

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/com/example/quizproject/view/menu-view.fxml"
                    )
            );

            Scene scene = new Scene(loader.load(), 600, 400);

            Stage stage = (Stage) scoreLabel.getScene().getWindow();

            stage.setScene(scene);
            stage.setTitle("Java Fundamentals Quiz");

        } catch (IOException e) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Navigation Error");
            alert.setHeaderText("Could not return to Menu");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}
