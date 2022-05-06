package com.example.milionerzy.game.party;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.milionerzy.R;
import com.example.milionerzy.exceptions.DatabaseException;
import com.example.milionerzy.exceptions.PartyGameServiceException;
import com.example.milionerzy.services.PartyGameService;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class PartyGameActivity extends AppCompatActivity {
    private PartyGameService partyGameService;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_game);

        createPartyGame();
        setFragments();

//      Uncomment this line when need fragment
//      @SuppressLint("ResourceType") GameFragment fragment = (GameFragment) getSupportFragmentManager().findFragmentById(fragment_game);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void createPartyGame() {
        try {
            partyGameService = new PartyGameService(this);
        } catch (PartyGameServiceException | DatabaseException de) {
            Log.i("PartyGameActivity", "Unexpected problem with PartyGameService");
            finishActivity(0);
        }
    }

    public PartyGameService getPartyGameService() {
        return partyGameService;
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
}