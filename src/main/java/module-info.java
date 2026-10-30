module com.example.quizproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.quizproject to javafx.fxml;
    exports com.example.quizproject;
    opens com.example.quizproject.model;
    opens com.example.quizproject.controller to javafx.fxml;
}