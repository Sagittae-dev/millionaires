package com.example.milionerzy.activities;

import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.milionerzy.R;
import com.example.milionerzy.exceptions.DatabaseException;
import com.example.milionerzy.repositories.PartyGameRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GamesHistoryActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_history);
        setGamesHistoryList();
        Button backButton = findViewById(R.id.backToMainActivity_Button);
        backButton.setOnClickListener(b -> finish());
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void setGamesHistoryList() {
        List<String> gamesList = getGamesHistoryList();
        Collections.reverse(gamesList);
        ListView gamesHistoryListView = findViewById(R.id.gamesHistoryList_ListView);
        gamesHistoryListView.setAdapter(new ArrayAdapter<>(this, R.layout.games_history_list_item, gamesList));
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private List<String> getGamesHistoryList() {
        PartyGameRepository partyGameRepository = new PartyGameRepository(this);
        try {
            return partyGameRepository.getAllPartyGames();
        } catch (DatabaseException e) {
            Toast.makeText(this, "History List is empty. Play Your first game and result will be saved here.", Toast.LENGTH_LONG).show();
        }
        return new ArrayList<>();
    }
}