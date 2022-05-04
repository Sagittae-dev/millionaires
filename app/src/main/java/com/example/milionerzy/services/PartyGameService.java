package com.example.milionerzy.services;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.milionerzy.DaggerQuestionServiceComponent;
import com.example.milionerzy.QuestionServiceComponent;
import com.example.milionerzy.exceptions.PartyGameServiceException;
import com.example.milionerzy.model.PartyGame;
import com.example.milionerzy.model.Question;

import java.util.List;

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
    public void createPartyGame() throws PartyGameServiceException {
        partyGame = new PartyGame();
        partyGame.setTeamList(teamsListService.getTeams());

        List<Question> questionList = createQuestionList(getListSize());
        partyGame.setQuestionList(questionList);
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

    private List<Question> createQuestionList(int listSize) {
//        QuestionServiceComponent questionServiceComponent = DaggerQuestionServiceComponent.create();
//        QuestionService questionService = questionServiceComponent.getQuestionService();
//        for( ; ; )
//        {
//
//        }
//
        return null;
    }


}
