
package com.example.quizproject.model;

public abstract class Question {

    private String name;
    private String title;
    private int timeLimit;
    private boolean required;

    public Question(String name, String title, int timeLimit, boolean required) {
        this.name = name;
        this.title = title;
        this.timeLimit = timeLimit;
        this.required = required;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public boolean isRequired() {
        return required;
    }
}
