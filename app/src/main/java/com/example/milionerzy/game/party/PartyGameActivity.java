package com.example.milionerzy.game.party;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.milionerzy.R;
import com.example.milionerzy.services.TeamsListService;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class PartyGameActivity extends AppCompatActivity {
    private TeamsListService teamsListService;
    private Button buttonA, buttonB,buttonC, buttonD, buttonNextQuestion;
    private TextView textViewContentOfQuestion, textViewCurrentGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_game);

        teamsListService = new TeamsListService(this);
        findAllViews();
        setFragments();

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
            if(position == 0) {
                tab.setText("GAME");
            }
            if (position == 1){
                tab.setText("SCORE");
            }
        }).attach();
    }
    private void findAllViews() {
        buttonA = findViewById(R.id.buttonAAnswerPartyMode);
        buttonB = findViewById(R.id.buttonBAnswerPartyMode);
        buttonC = findViewById(R.id.buttonCAnswerPartyMode);
        buttonD = findViewById(R.id.buttonDAnswerPartyMode);
        textViewContentOfQuestion = findViewById(R.id.textViewQuestionContentPartyMode);
        textViewCurrentGroup = findViewById(R.id.textViewCurrentGroup);
        buttonNextQuestion = findViewById(R.id.buttonNextQuestionPartyMode);
    }
}