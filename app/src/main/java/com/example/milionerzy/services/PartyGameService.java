package com.example.milionerzy.services;

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
import com.example.milionerzy.model.Question;
import com.example.milionerzy.model.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//  TODO Make a findWinner method

public class PartyGameService {
    private TeamsListService teamsListService;
    private Context context;
    private PartyGame partyGame;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public PartyGameService(Context context){
        this.context = context;
        this.teamsListService = new TeamsListService(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void createPartyGame() throws PartyGameServiceException, DatabaseException {
        partyGame = new PartyGame();
        partyGame.setTeamList(teamsListService.getTeams());
        partyGame.setQuestionList(createQuestionList(getListSize()));
        partyGame.setFinished(false);
    }

    public void finishPartyGame(){
        partyGame.setFinished(true);
    }

    private int getListSize() throws PartyGameServiceException {
        Intent intent = new Intent();
        int listSize = intent.getIntExtra("gameLength", 0);
        if (listSize == 0){
            Log.i("PartyGameService" , "listSize is 0");
            throw new PartyGameServiceException();
        }
        return listSize;
    }

    private List<Question> createQuestionList(int listSize) throws DatabaseException {
        QuestionServiceComponent questionServiceComponent = DaggerQuestionServiceComponent.create();
        QuestionService questionService = questionServiceComponent.getQuestionService();

        List<Question> finalQuestionList = new ArrayList<>();
        List<Question> listToDrawQuestionsFrom = questionService.getAllQuestions();

        Random random = new Random();
        int databaseSize = listToDrawQuestionsFrom.size();
        for(int i = 0; i<listSize; i++ )
        {
            int randomInt = random.nextInt(databaseSize);
            finalQuestionList.add(listToDrawQuestionsFrom.get(randomInt));
            listToDrawQuestionsFrom.remove(randomInt);
            databaseSize--;
        }

        return finalQuestionList;
    }

    public Question getCurrentQuestion(){
        return partyGame.getQuestionList().get(partyGame.getNumberOfCurrentQuestion());
    }

    public void setNextQuestion(){
        partyGame.setNumberOfCurrentQuestion(partyGame.getNumberOfCurrentQuestion()+1);
    }

    public Team getCurrentTeam(){
        return partyGame.getTeamList().get(partyGame.getNumberOfCurrentTeam());
    }

    public void setNextTeam(){
        if(partyGame.getNumberOfCurrentTeam() < partyGame.getTeamList().size()-1) {
            partyGame.setNumberOfCurrentTeam((byte) (partyGame.getNumberOfCurrentTeam() + 1));
        }
        else{
            partyGame.setNumberOfCurrentTeam( (byte) 0);
        }
    }

    public boolean checkAnswer(char buttonTag, char correctAnswer){
        return buttonTag == correctAnswer;
    }
}
