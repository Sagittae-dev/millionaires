package com.example.milionerzy.services;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.milionerzy.database.DatabaseService;
import com.example.milionerzy.exceptions.DatabaseException;
import com.example.milionerzy.exceptions.SetGameLengthServiceException;
import com.example.milionerzy.interfaces.BaseService;

public class SetGameLengthService implements BaseService {
    private static final String TAG = "SetGameLengthService";

    private final Context context;
    private final int amountOfAvailableQuestions;
    private int currentAmountOfCycles;

    public SetGameLengthService(Context context) {
        this.context = context;
        amountOfAvailableQuestions = getAmountOfQuestionsFromDatabase();
    }

    public int addNewCycleOfQuestions(int currentAmountOfGroups) throws SetGameLengthServiceException {
        if ((currentAmountOfGroups * currentAmountOfCycles) + currentAmountOfGroups > amountOfAvailableQuestions) {
            showToastWithMessage("Not enough questions in database", Toast.LENGTH_SHORT);
            Log.i(TAG, "Not enough questions in database");
            throw new SetGameLengthServiceException();
        }
        if (currentAmountOfCycles > 50) {
            showToastWithMessage("You can set max 50 cycles", Toast.LENGTH_SHORT);
            Log.i(TAG, "Too much cycles");
            throw new SetGameLengthServiceException();
        } else {
            currentAmountOfCycles = currentAmountOfCycles + 1;
            Log.i(TAG, "Incremented amount of cycles" + currentAmountOfCycles);
            return currentAmountOfCycles;
        }
    }

    private int getAmountOfQuestionsFromDatabase() {
        DatabaseService databaseService = new DatabaseService(context);
        try {
            int amount = databaseService.getAmountOfQuestions();
            Log.i(TAG, "Amount questions from database: " + amount);
            return amount;
        } catch (DatabaseException e) {
            Log.i(TAG, "Problem with getting amount of questions and amount is set as 0");
            return 0;
        }
    }

    public int removeOneCycleOfQuestions() throws SetGameLengthServiceException {
        if (currentAmountOfCycles > 1) {
            currentAmountOfCycles = currentAmountOfCycles - 1;
            return currentAmountOfCycles;
        } else {
            throw new SetGameLengthServiceException();
        }
    }

    public int getAmountOfAvailableQuestions() {
        return amountOfAvailableQuestions;
    }

    public int getCurrentAmountOfCycles() {
        return currentAmountOfCycles;
    }

    @Override
    public void showToastWithMessage(String message, int lengthOfToast) {
        Toast.makeText(context, message, lengthOfToast).show();
    }
}
