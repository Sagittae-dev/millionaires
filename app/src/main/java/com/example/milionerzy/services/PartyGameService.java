package com.example.milionerzy.services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.milionerzy.DaggerQuestionServiceComponent;
import com.example.milionerzy.QuestionServiceComponent;
import com.example.milionerzy.exceptions.DatabaseException;
import com.example.milionerzy.exceptions.PartyGameRepositoryException;
import com.example.milionerzy.exceptions.PartyGameServiceException;
import com.example.milionerzy.model.PartyGame;
import com.example.milionerzy.model.PartyGameDataToDisplay;
import com.example.milionerzy.model.PartyGameEntity;
import com.example.milionerzy.model.Question;
import com.example.milionerzy.model.Team;
import com.example.milionerzy.repositories.PartyGameRepository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.stream.Collectors;

//  TODO Make a findWinner method

public class PartyGameService {
    private TeamsListService teamsListService;
    private PartyGame partyGame;
    private final Context context;


    @RequiresApi(api = Build.VERSION_CODES.N)
    public PartyGameService(Context context) {
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void createPartyGame() throws PartyGameServiceException, DatabaseException {
        TeamsListService teamsListService = new TeamsListService(context);
        setTeamsListService(teamsListService);
        PartyGame partyGame = new PartyGame();

        List<Team> teamsList = getTeamsList();
        partyGame.setTeamList(teamsList);
        List<Question> questionList = createQuestionList();
        partyGame.setQuestionList(questionList);
        partyGame.setFinished(false);
        partyGame.setDate(new Date());
        setPartyGame(partyGame);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setTeamsListService(TeamsListService teamsListService) {
        this.teamsListService = teamsListService;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<Team> getTeamsList() {
        return teamsListService.getTeams();
    }

    public PartyGame getPartyGame() {
        return partyGame;
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

    public void setPartyGame(PartyGame partyGame) {
        this.partyGame = partyGame;
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
        String correctAnswer = getCorrectAnswer();
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
        return getPartyGame().getQuestionList().get(partyGame.getNumberOfCurrentQuestion());
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void finishPartyGame() {
        partyGame.setFinished(true);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private String findWinners() {
        List<Team> teamList = partyGame.getTeamList();
        int maxPointTeam = teamList.stream().mapToInt(Team::getScore).max().orElseThrow(NoSuchElementException::new);
        List<String> winnersList = teamList.stream().filter(t -> t.getScore() == maxPointTeam).map(Team::getTeamName).collect(Collectors.toList());
        return convertWinnersListToString(winnersList, maxPointTeam);
    }

    private String convertWinnersListToString(List<String> winnersList, int score) {
        StringBuilder result = new StringBuilder();
        for (String name : winnersList) {
            result.append(name).append(", ");
        }
        result.append(" with score: ").append(score);
        return result.toString();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private PartyGameEntity createPartyGameEntity() {
        PartyGameEntity partyGameEntity = new PartyGameEntity();
        String winner = findWinners();
        partyGameEntity.setWinner(winner);
        String date = getTimeAndDate();
        partyGameEntity.setDate(date);
        return partyGameEntity;
    }

    private String getTimeAndDate() {
        String pattern = "MM/dd/yyyy HH:mm:ss";
        DateFormat dateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
        Date date = Calendar.getInstance().getTime();
        return dateFormat.format(date);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void savePartyGameResultInDatabase() {
        PartyGameRepository partyGameRepository = new PartyGameRepository(context);
        PartyGameEntity partyGameEntity = createPartyGameEntity();
        try {
            partyGameRepository.savePartyGame(partyGameEntity);
        } catch (PartyGameRepositoryException e) {
            Toast.makeText(context, "Fatal exception: Result was not saved in database", Toast.LENGTH_SHORT).show();
        }
    }

}
