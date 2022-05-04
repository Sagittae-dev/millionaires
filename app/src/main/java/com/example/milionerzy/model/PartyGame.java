package com.example.milionerzy.model;

import java.util.List;

public class PartyGame {
    private int id;
    private boolean isFinished;

    private int numberOfCurrentQuestion;
    private List<Question> questionList;

    private byte numberOfCurrentTeam;
    private Team winner;
    private List<Team> teamList;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        this.isFinished = finished;
    }

    public int getNumberOfCurrentQuestion() {
        return numberOfCurrentQuestion;
    }

    public void setNumberOfCurrentQuestion(int numberOfCurrentQuestion) {
        this.numberOfCurrentQuestion = numberOfCurrentQuestion;
    }

    public List<Question> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
    }

    public byte getNumberOfCurrentTeam() {
        return numberOfCurrentTeam;
    }

    public void setNumberOfCurrentTeam(byte numberOfCurrentTeam) {
        this.numberOfCurrentTeam = numberOfCurrentTeam;
    }

    public Team getWinner() {
        return winner;
    }

    public void setWinner(Team winner) {
        this.winner = winner;
    }

    public List<Team> getTeamList() {
        return teamList;
    }

    public void setTeamList(List<Team> teamList) {
        this.teamList = teamList;
    }
}
