package com.example.milionerzy.game.party;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.milionerzy.R;
import com.example.milionerzy.model.PartyGameDataToDisplay;
import com.example.milionerzy.services.PartyGameService;

public class GameFragment extends Fragment {

    private Button buttonA, buttonB, buttonC, buttonD, buttonNextQuestion;
    private TextView contentOfQuestionTextView, currentGroupTextView;
    private PartyGameService partyGameService;
    private PartyGameDataToDisplay partyGameDataToDisplay;
    private View mainView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_game, container, false);
        contentOfQuestionTextView = mainView.findViewById(R.id.textViewQuestionContentPartyMode);
        currentGroupTextView = mainView.findViewById(R.id.textViewCurrentGroup);
        setAnswerButtons(mainView);
        getPartyGameDataToDisplay();
        showQuestionContentAndAnswers();
        return mainView;
    }

    private void getPartyGameDataToDisplay() {
        partyGameService = ((PartyGameActivity) getActivity()).getPartyGameService();
        partyGameDataToDisplay = partyGameService.getPartyGameDataToDisplay();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setAnswerButtons(View view) {
        buttonA = view.findViewById(R.id.buttonAAnswerPartyMode);
        buttonA.setOnClickListener(b -> setActionAfterCheckAnswer(buttonA.getTag().toString()));
        buttonB = view.findViewById(R.id.buttonBAnswerPartyMode);
        buttonB.setOnClickListener(b -> setActionAfterCheckAnswer(buttonB.getTag().toString()));
        buttonC = view.findViewById(R.id.buttonCAnswerPartyMode);
        buttonC.setOnClickListener(b -> setActionAfterCheckAnswer(buttonC.getTag().toString()));
        buttonD = view.findViewById(R.id.buttonDAnswerPartyMode);
        buttonD.setOnClickListener(b -> setActionAfterCheckAnswer(buttonD.getTag().toString()));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setActionAfterCheckAnswer(String tag) {
        Button checkedButton = mainView.findViewWithTag(tag);
        if (!partyGameService.answerIsCorrect(tag)) {
            checkedButton.setBackgroundResource(R.drawable.wronganswer);
            String correctAnswerTag = partyGameService.getCorrectAnswer();
            Button correctButton = mainView.findViewWithTag(correctAnswerTag);
            correctButton.setBackgroundResource(R.drawable.correctanswer);

        }else{
            checkedButton.setBackgroundResource(R.drawable.correctanswer);
        }
        setButtonsClickable(false);
    }

    private void showQuestionContentAndAnswers(){
        contentOfQuestionTextView.setText(partyGameDataToDisplay.getContentOfCurrentQuestion());
        buttonA.setText(partyGameDataToDisplay.getCurrentAnswerA());
        buttonB.setText(partyGameDataToDisplay.getCurrentAnswerB());
        buttonC.setText(partyGameDataToDisplay.getCurrentAnswerC());
        buttonD.setText(partyGameDataToDisplay.getCurrentAnswerD());
    }

    private void setButtonsClickable(boolean clickable){
        buttonA.setClickable(clickable);
        buttonB.setClickable(clickable);
        buttonC.setClickable(clickable);
        buttonD.setClickable(clickable);
    }
}