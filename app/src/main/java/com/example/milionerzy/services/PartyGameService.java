package com.example.milionerzy.services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.milionerzy.DaggerQuestionServiceComponent;
import com.example.milionerzy.QuestionServiceComponent;
import com.example.milionerzy.exceptions.DatabaseException;
import com.example.milionerzy.exceptions.PartyGameServiceException;
import com.example.milionerzy.model.PartyGame;
import com.example.milionerzy.model.PartyGameDataToDisplay;
import com.example.milionerzy.model.Question;
import com.example.milionerzy.model.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//  TODO Make a findWinner method

public class PartyGameService {
    private final TeamsListService teamsListService;
    private final PartyGame partyGame;
    private final Context context;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public PartyGameService(Context context) throws DatabaseException, PartyGameServiceException {
        this.context = context;
        this.teamsListService = new TeamsListService(context);
        this.partyGame = createPartyGame();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private PartyGame createPartyGame() throws PartyGameServiceException, DatabaseException {
        PartyGame partyGame = new PartyGame();
        List<Team> teamsList = getTeamsList();
        partyGame.setTeamList(teamsList);
        List<Question> questionList = createQuestionList();
        partyGame.setQuestionList(questionList);
        partyGame.setFinished(false);
        return partyGame;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<Team> getTeamsList() {
        return teamsListService.getTeams();
    }

    public TeamsListService getTeamsListService() {
        return teamsListService;
    }

    private int getGameLength() throws PartyGameServiceException {
        Intent intent = ((Activity) context).getIntent();
        int gameLength = intent.getIntExtra("gameLength", 0);
        if (gameLength == 0) {
            Log.i("PartyGameService", "gameLength is 0");
            throw new PartyGameServiceException();
        }
        return gameLength;
    }

    private List<Question> createQuestionList() throws DatabaseException, PartyGameServiceException {
        QuestionServiceComponent questionServiceComponent = DaggerQuestionServiceComponent.create();
        QuestionService questionService = questionServiceComponent.getQuestionService();
        questionService.setDatabaseContext(context);
        List<Question> finalQuestionList = new ArrayList<>();
        List<Question> listToDrawQuestionsFrom = questionService.getAllQuestions();

        int listSize = getGameLength();
        Random random = new Random();
        int databaseSize = listToDrawQuestionsFrom.size();
        for (int i = 0; i < listSize; i++) {
            int randomInt = random.nextInt(databaseSize);
            finalQuestionList.add(listToDrawQuestionsFrom.get(randomInt));
            listToDrawQuestionsFrom.remove(randomInt);
            databaseSize--;
        }

        return finalQuestionList;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public boolean answerIsCorrect(String answer) {
        String correctAnswer = getCurrentQuestion().getCorrectAnswer();
        boolean answerIsCorrect = answer.equals(correctAnswer);
        if (answerIsCorrect) {
            teamsListService.addPointToTeam(getCurrentTeam());
        }
        return answerIsCorrect;
    }

    public String getCorrectAnswer() {
        return getCurrentQuestion().getCorrectAnswer();
    }

    public PartyGameDataToDisplay getPartyGameDataToDisplay() {
        PartyGameDataToDisplay partyGameDataToDisplay = new PartyGameDataToDisplay();
        Question question = getCurrentQuestion();
        partyGameDataToDisplay.setContentOfCurrentQuestion(question.getContentOfQuestion());
        partyGameDataToDisplay.setCurrentAnswerA(question.getAnswerA());
        partyGameDataToDisplay.setCurrentAnswerB(question.getAnswerB());
        partyGameDataToDisplay.setCurrentAnswerC(question.getAnswerC());
        partyGameDataToDisplay.setCurrentAnswerD(question.getAnswerD());

        Team team = getCurrentTeam();
        partyGameDataToDisplay.setCurrentTeamName(team.getTeamName());
        partyGameDataToDisplay.setCurrentTeamScore(team.getScore());

        return partyGameDataToDisplay;
    }

    public Question getCurrentQuestion() {
        return partyGame.getQuestionList().get(partyGame.getNumberOfCurrentQuestion());
    }

    private Team getCurrentTeam() {
        byte numberOfCurrentTeam = partyGame.getNumberOfCurrentTeam();
        return partyGame.getTeamList().get(numberOfCurrentTeam);
    }

    public void setNextTeam() {
        int numberOfCurrentTeam = partyGame.getNumberOfCurrentTeam();

        if (numberOfCurrentTeam < partyGame.getTeamList().size() - 1) {
            partyGame.setNumberOfCurrentTeam((byte) (numberOfCurrentTeam + 1));
        } else {
            partyGame.setNumberOfCurrentTeam((byte) 0);
        }
    }

    public void setNextQuestion() throws PartyGameServiceException {
        int listSize = partyGame.getQuestionList().size();
        int nrOfCurrentQuestion = partyGame.getNumberOfCurrentQuestion();
        if (nrOfCurrentQuestion < listSize - 1) {
            partyGame.setNumberOfCurrentQuestion(partyGame.getNumberOfCurrentQuestion() + 1);
        } else throw new PartyGameServiceException();
    }

    public void goToNextQuestion() throws PartyGameServiceException {
        setNextTeam();
        setNextQuestion();
    }

    public void finishPartyGame() {
        partyGame.setFinished(true);
    }
}
