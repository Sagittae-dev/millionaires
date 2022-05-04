package com.example.milionerzy.model;

public class Team {
    private byte numberInQueue;
    private String teamName;
    private int score;

    public Team(byte numberInQueue, String teamName, int score) {
        this.numberInQueue = numberInQueue;
        this.teamName = teamName;
        this.score = score;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public short getNumberInQueue() {
        return numberInQueue;
    }

    public void setNumberInQueue(byte numberInQueue) {
        this.numberInQueue = numberInQueue;
    }

    @Override
    public String toString() {
        return "Team{" +
                "numberInQueue=" + numberInQueue +
                ", teamName='" + teamName + '\'' +
                ", score=" + score +
                '}';
    }
}
