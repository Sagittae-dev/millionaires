package com.example.milionerzy.game.party;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.milionerzy.R;
import com.example.milionerzy.model.PartyGame;

public class GameFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button buttonA, buttonB, buttonC, buttonD, buttonNextQuestion;
    PartyGame partyGame = new PartyGame();
    public GameFragment() {
    }

    public static GameFragment newInstance(String param1, String param2) {
        GameFragment fragment = new GameFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);

        buttonA = view.findViewById(R.id.buttonAAnswerPartyMode);
        buttonB = view.findViewById(R.id.buttonBAnswerPartyMode);
        buttonC = view.findViewById(R.id.buttonCAnswerPartyMode);
        buttonD = view.findViewById(R.id.buttonDAnswerPartyMode);

        return view;
    }
}