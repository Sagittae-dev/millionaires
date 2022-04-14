package com.example.milionerzy.model;

import java.util.Objects;

public class Question {
    private int id;
    private String contentOfQuestion;
    private String answerA;
    private String answerB;
    private String answerC;
    private String answerD;
    private String correctAnswer;

    public Question() {
    }

    public Question(String contentOfQuestion, String answerA, String answerB, String answerC, String answerD, String correctAnswer) {
        this.contentOfQuestion = contentOfQuestion;
        this.answerA = answerA;
        this.answerB = answerB;
        this.answerC = answerC;
        this.answerD = answerD;
        this.correctAnswer = correctAnswer;
    }

    public Question(int id, String contentOfQuestion, String answerA, String answerB, String answerC, String answerD, String correctAnswer) {
        this.id = id;
        this.contentOfQuestion = contentOfQuestion;
        this.answerA = answerA;
        this.answerB = answerB;
        this.answerC = answerC;
        this.answerD = answerD;
        this.correctAnswer = correctAnswer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContentOfQuestion() {
        return contentOfQuestion;
    }

    public void setContentOfQuestion(String contentOfQuestion) {
        this.contentOfQuestion = contentOfQuestion;
    }

    public String getAnswerA() {
        return answerA;
    }

    public void setAnswerA(String answerA) {
        this.answerA = answerA;
    }

    public String getAnswerB() {
        return answerB;
    }

    public void setAnswerB(String answerB) {
        this.answerB = answerB;
    }

    public String getAnswerC() {
        return answerC;
    }

    public void setAnswerC(String answerC) {
        this.answerC = answerC;
    }

    public String getAnswerD() {
        return answerD;
    }

    public void setAnswerD(String answerD) {
        this.answerD = answerD;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return id == question.id &&
                Objects.equals(contentOfQuestion, question.contentOfQuestion) &&
                Objects.equals(answerA, question.answerA) &&
                Objects.equals(answerB, question.answerB) &&
                Objects.equals(answerC, question.answerC) &&
                Objects.equals(answerD, question.answerD) &&
                Objects.equals(correctAnswer, question.correctAnswer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contentOfQuestion, answerA, answerB, answerC, answerD, correctAnswer);
    }
}
