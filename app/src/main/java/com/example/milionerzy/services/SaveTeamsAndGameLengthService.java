package com.example.milionerzy.services;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.milionerzy.exceptions.SetGameLengthServiceException;
import com.example.milionerzy.exceptions.SetTeamsServiceException;
import com.example.milionerzy.game.SetTeamsAndGameLengthActivity;
import com.example.milionerzy.game.party.PartyGameActivity;
import com.example.milionerzy.interfaces.BaseService;

public class SaveTeamsAndGameLengthService implements BaseService<Object> {

    private final SetTeamsService setTeamsService;
    private final SetGameLengthService setGameLengthService;
    private final Context context;

    public SaveTeamsAndGameLengthService(SetTeamsService setTeamsService, SetGameLengthService setGameLengthService, Context context) {
        this.setTeamsService = setTeamsService;
        this.setGameLengthService = setGameLengthService;
        this.context = context;
    }

    public void actionAfterTapOnSaveGroupsAndPlayButton() throws SetTeamsServiceException, SetGameLengthServiceException {
        final int groupsListSize = setTeamsService.getGroupsListSize();
        final int currentAmountOfCycles = setGameLengthService.getCurrentAmountOfCycles();
        final int gameLength = getGameLength(groupsListSize, currentAmountOfCycles);

        final int amountOfAvailableQuestionsInDB = setGameLengthService.getAmountOfAvailableQuestions();
        if (currentAmountOfCycles < 1) {
            showToastWithMessage("You can set one or more cycles", Toast.LENGTH_SHORT);
        }
        if (groupsListSize < 2) {
            showToastWithMessage("You must set at least two groups to go next", Toast.LENGTH_SHORT);
            throw new SetTeamsServiceException();
        }
        if ((gameLength) > amountOfAvailableQuestionsInDB) {
            showToastWithMessage("You set too much groups or cycles because have only: " + amountOfAvailableQuestionsInDB + " available questions in database.", Toast.LENGTH_LONG);
            throw new SetGameLengthServiceException();
        }
        moveToPartyGame(gameLength);
    }

    private int getGameLength(int groupsListSize, int currentAmountOfCycle){
        return groupsListSize * currentAmountOfCycle;
    }

    private void moveToPartyGame(int gameLength) {
        Intent intent = new Intent(context, PartyGameActivity.class);
        intent.putExtra( "gameLength" , gameLength);
        context.startActivity(intent);
        ((SetTeamsAndGameLengthActivity) context).finish();
    }

    @Override
    public void saveResultInSharedPreferences(Object toSave) {

    }

    @Override
    public void showToastWithMessage(String message, int lengthOfToast) {
        Toast.makeText(context, message, lengthOfToast).show();
    }
}
