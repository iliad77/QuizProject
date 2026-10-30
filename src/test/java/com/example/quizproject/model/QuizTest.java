
package com.example.quizproject.model;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QuizTest {

    @Test
    void testQuizWithDifferentQuestionTypes() {

        Quiz quiz = new Quiz(
                "quiz001",
                "Java Fundamentals Quiz",
                "Testing Java knowledge"
        );

        MultipleChoice q1 = new MultipleChoice(
                "q1",
                "Which company developed Java?",
                20,
                true,
                List.of("Microsoft", "Sun Microsystems", "Google"),
                "random",
                "Sun Microsystems"
        );

        TrueFalse q2 = new TrueFalse(
                "q2",
                "Java is statically typed.",
                15,
                true,
                "True",
                "False",
                true
        );

        quiz.addQuestion(q1);
        quiz.addQuestion(q2);

        assertEquals("quiz001", quiz.getQuizId());
        assertEquals(2, quiz.getQuestions().size());
        assertInstanceOf(MultipleChoice.class, quiz.getQuestions().get(0));
        assertInstanceOf(TrueFalse.class, quiz.getQuestions().get(1));
    }
}

