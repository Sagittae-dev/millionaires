package com.example.milionerzy.game;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.milionerzy.R;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity {
    private int wynik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        createListWithQuestions();
    }

    private void createListWithQuestions()
    {
        ArrayList Listapytań;

    }
}