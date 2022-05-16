package com.example.milionerzy.game.party;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
import com.example.milionerzy.activities.PartyGameActivity;
import com.example.milionerzy.exceptions.PartyGameServiceException;
import com.example.milionerzy.model.PartyGameDataToDisplay;
import com.example.milionerzy.services.PartyGameService;

import java.util.Objects;

public class GameFragment extends Fragment {

    private Button buttonA, buttonB, buttonC, buttonD, buttonNextQuestion;
    private TextView contentOfQuestionTextView, currentGroupTextView, currentGroupScoreTextView;
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
        setAllViews();
        setPartyGameService();
        setPartyDataToDisplay();
        showQuestionContentAndAnswers();
        showCurrentTeam();
        showCurrentTeamScore();
        setButtonsClickable(true);
        return mainView;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setAllViews() {
        contentOfQuestionTextView = mainView.findViewById(R.id.textViewQuestionContentPartyMode);
        currentGroupTextView = mainView.findViewById(R.id.textViewCurrentGroup);
        currentGroupScoreTextView = mainView.findViewById(R.id.textViewCurrentGroupScore);
        setAnswerButtons(mainView);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setActionAfterClickButtonNextQuestion() {
        try {
            partyGameService.goToNextQuestion();
        } catch (PartyGameServiceException e) {
            showFinishGameAlertDialog();
            clearAllFields();
            return;
        }
        setPartyDataToDisplay();
        showQuestionContentAndAnswers();
        showCurrentTeam();
        clearButtonsBackgrounds();
        showCurrentTeamScore();
        setButtonsClickable(true);
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private void setPartyGameService() {
        partyGameService = ((PartyGameActivity) Objects.requireNonNull(getActivity())).getPartyGameService();
    }

    private void setPartyDataToDisplay() {
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
        buttonNextQuestion = view.findViewById(R.id.buttonNextQuestionPartyMode);
        buttonNextQuestion.setOnClickListener(b -> setActionAfterClickButtonNextQuestion());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setActionAfterCheckAnswer(String tag) {
        Button checkedButton = mainView.findViewWithTag(tag);
        if (!partyGameService.answerIsCorrect(tag)) {
            actionIfAnswerIsIncorrect(checkedButton);
        } else {
            checkedButton.setBackgroundResource(R.drawable.correctanswer);
            actualizeCurrentTeamScoreAfterCorrectAnswer();
        }
        notifyScoreFragment();
        setButtonsClickable(false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void actionIfAnswerIsIncorrect(Button checkedButton) {
        checkedButton.setBackgroundResource(R.drawable.wronganswer);
        String correctAnswerTag = partyGameService.getCorrectAnswer();
        Button correctButton = mainView.findViewWithTag(correctAnswerTag);
        correctButton.setBackgroundResource(R.drawable.correctanswer);
    }


    @SuppressLint("UseRequireInsteadOfGet")
    private void notifyScoreFragment() {
        ((PartyGameActivity) Objects.requireNonNull(getActivity())).refreshScore();
    }

    private void showQuestionContentAndAnswers() {
        contentOfQuestionTextView.setText(partyGameDataToDisplay.getContentOfCurrentQuestion());
        buttonA.setText(partyGameDataToDisplay.getCurrentAnswerA());
        buttonB.setText(partyGameDataToDisplay.getCurrentAnswerB());
        buttonC.setText(partyGameDataToDisplay.getCurrentAnswerC());
        buttonD.setText(partyGameDataToDisplay.getCurrentAnswerD());
    }

    private void showCurrentTeam() {
        String currentTeamToDisplay = partyGameDataToDisplay.getCurrentTeamName();
        currentGroupTextView.setText(currentTeamToDisplay);
    }

    private void showCurrentTeamScore() {
        int currentTeamScore = partyGameDataToDisplay.getCurrentTeamScore();
        currentGroupScoreTextView.setText(String.valueOf(currentTeamScore));
    }

    private void actualizeCurrentTeamScoreAfterCorrectAnswer() {
        int currentTeamScore = partyGameDataToDisplay.getCurrentTeamScore();
        currentGroupScoreTextView.setText(String.valueOf(currentTeamScore + 1));
    }

    private void setButtonsClickable(boolean clickable) {
        buttonA.setClickable(clickable);
        buttonB.setClickable(clickable);
        buttonC.setClickable(clickable);
        buttonD.setClickable(clickable);
        buttonNextQuestion.setClickable(!clickable);
    }

    private void clearButtonsBackgrounds() {
        buttonA.setBackgroundResource(R.drawable.default_button_background);
        buttonB.setBackgroundResource(R.drawable.default_button_background);
        buttonC.setBackgroundResource(R.drawable.default_button_background);
        buttonD.setBackgroundResource(R.drawable.default_button_background);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showFinishGameAlertDialog() {
        new AlertDialog.Builder(getActivity())
                .setTitle("Game over")
                .setCancelable(false)
                .setPositiveButton("Finish Game", (dialog, id) -> {
                    partyGameService.savePartyGameResultInDatabase();
                    partyGameService.finishPartyGame();
                    requireActivity().finish();
                    dialog.dismiss();
                })
                .create()
                .show();
    }

    private void clearAllFields() {
        contentOfQuestionTextView.setText("");
        buttonA.setText("");
        buttonB.setText("");
        buttonC.setText("");
        buttonD.setText("");
        clearButtonsBackgrounds();
        currentGroupScoreTextView.setText("");
        currentGroupTextView.setText("");
    }
}