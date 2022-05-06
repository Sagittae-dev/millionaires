package com.example.milionerzy.game.party;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.milionerzy.R;
import com.example.milionerzy.model.PartyGameDataToDisplay;
import com.example.milionerzy.services.PartyGameService;

public class GameFragment extends Fragment {

    private Button buttonA, buttonB, buttonC, buttonD, buttonNextQuestion;
    private TextView contentOfQuestionTextView, currentGroupTextView;
    private PartyGameDataToDisplay partyGameDataToDisplay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        contentOfQuestionTextView = view.findViewById(R.id.textViewQuestionContentPartyMode);
        currentGroupTextView = view.findViewById(R.id.textViewCurrentGroup);
        setAnswerButtons(view);
        getPartyGameDataToDisplay();

        return view;
    }

    private void getPartyGameDataToDisplay() {
        PartyGameService partyGameService = ((PartyGameActivity) getActivity()).getPartyGameService();
        partyGameDataToDisplay = partyGameService.getPartyGameDataToDisplay();
    }

    private void setAnswerButtons(View view) {
        buttonA = view.findViewById(R.id.buttonAAnswerPartyMode);
        buttonA.setOnClickListener(b -> checkAnswer(buttonA.getTag().toString()));
        buttonB = view.findViewById(R.id.buttonBAnswerPartyMode);
        buttonB.setOnClickListener(b -> checkAnswer(buttonB.getTag().toString()));
        buttonC = view.findViewById(R.id.buttonCAnswerPartyMode);
        buttonC.setOnClickListener(b -> checkAnswer(buttonC.getTag().toString()));
        buttonD = view.findViewById(R.id.buttonDAnswerPartyMode);
        buttonD.setOnClickListener(b -> checkAnswer(buttonD.getTag().toString()));
    }

    private void checkAnswer(String tag) {

    }
}