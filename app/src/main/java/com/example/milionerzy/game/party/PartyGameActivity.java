package com.example.milionerzy.game.party;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.milionerzy.DaggerQuestionServiceComponent;
import com.example.milionerzy.QuestionServiceComponent;
import com.example.milionerzy.R;
import com.example.milionerzy.exceptions.DatabaseException;
import com.example.milionerzy.model.PartyGame;
import com.example.milionerzy.model.Question;
import com.example.milionerzy.repositories.QuestionsRepository;
import com.example.milionerzy.services.PartyGameService;
import com.example.milionerzy.services.QuestionService;
import com.example.milionerzy.services.TeamsListService;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Random;

public class PartyGameActivity extends AppCompatActivity {
    //private TeamsListService teamsListService;
    private PartyGame partyGame;
    private QuestionService questionService;
    private PartyGameService partyGameService;


    private TextView textViewContentOfQuestion, textViewCurrentGroup;
//    private Button buttonA = new Button(this);
//    private Button buttonB = new Button(this);
//    private Button buttonC = new Button(this);
//    private Button buttonD = new Button(this);

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_game);
        partyGameService = new PartyGameService(this);
        setFragments();

        textViewContentOfQuestion = findViewById(R.id.textViewQuestionContentPartyMode);
        textViewCurrentGroup = findViewById(R.id.textViewCurrentGroup);
        setAllQuestions();
    }

    private void setAllQuestions() {
        try {
            partyGame.setQuestionList(questionService.getAllQuestions());
        }catch (DatabaseException e){
            Log.i("PartyGameActivity", "Exception in method setAllQuestions");
        }
    }


    private void setFragments() {
        TabLayout gameScoreTabLayout = findViewById(R.id.gameScoreTabLayout);
        GameScoreFragmentsAdapter gameScoreFragmentsAdapter = new GameScoreFragmentsAdapter(this);
        ViewPager2 gameScoreViewPager2 = findViewById(R.id.gameScoreFragmentsViewPager2);
        gameScoreViewPager2.setAdapter(gameScoreFragmentsAdapter);
        setTabLayoutMediator(gameScoreTabLayout, gameScoreViewPager2);
    }

    private void setTabLayoutMediator(TabLayout gameScoreTabLayout, ViewPager2 gameScoreViewPager2) {
        new TabLayoutMediator(gameScoreTabLayout, gameScoreViewPager2, (tab, position) -> {
            if (position == 0) {
                tab.setText("GAME");
            }
            if (position == 1) {
                tab.setText("SCORE");
            }
        }).attach();
    }
    private void findAllViews() {

    }
}