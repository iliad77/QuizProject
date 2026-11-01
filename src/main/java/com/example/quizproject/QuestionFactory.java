
package com.example.quizproject;

import com.example.quizproject.model.*;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

public class QuestionFactory {

    public static Question createQuestion(JsonNode element, int timeLimit) {

        String type = element.path("type").asText();
        String questionId = element.path("name").asText();
        String title = element.path("title").asText();
        boolean required = element.path("isRequired").asBoolean();

        if (type.equals("radiogroup")) {

            List<String> choices = new ArrayList<>();

            for (JsonNode choice : element.path("choices")) {
                choices.add(choice.asText());
            }

            String choicesOrder =
                    element.path("choicesOrder").asText();

            String correctAnswer =
                    element.path("correctAnswer").asText();

            return new MultipleChoice(
                    questionId,
                    title,
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

            return new TrueFalse(
                    questionId,
                    title,
                    timeLimit,
                    required,
                    labelTrue,
                    labelFalse,
                    correctAnswer
            );
        }

        // Unknown question type
        return null;
    }
}
