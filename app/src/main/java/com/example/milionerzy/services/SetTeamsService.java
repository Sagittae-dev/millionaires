package com.example.milionerzy.services;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.milionerzy.exceptions.SetTeamsServiceException;
import com.example.milionerzy.interfaces.BaseService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class SetTeamsService implements BaseService<List<String>> {
    public static final String TEAMS_FROM_SP = "TeamsList";
    private final Context context;
    private final List<String> teamsList = new ArrayList<>();

    public SetTeamsService(Context context) {
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<String> addNewGroupFromEditTextToList(EditText newGroupEditText) throws SetTeamsServiceException {
        String newGroupName = newGroupEditText.getText().toString();
        groupNameIsValid(newGroupName);

        if (teamsList.size() >= 8) {
            showToastWithMessage("You can set max 8 groups", Toast.LENGTH_SHORT);
            throw new SetTeamsServiceException();
        }
        if (checkListContainsGroupWithTheSameName(newGroupName)) {
            showToastWithMessage("Group with name: " + newGroupName + " already exist.", Toast.LENGTH_SHORT);
            throw new SetTeamsServiceException();
        } else {
            teamsList.add(newGroupName);
            saveResultInSharedPreferences(teamsList);
        }
        return teamsList;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private boolean checkListContainsGroupWithTheSameName(String newGroupName) {
        return teamsList.stream().anyMatch(groupName -> groupName.equals(newGroupName));
    }

    private void groupNameIsValid(String newGroupName) throws SetTeamsServiceException {
        if (newGroupName == null || newGroupName.equals("")) {
            showToastWithMessage("Group name could not be empty", Toast.LENGTH_SHORT);
            throw new SetTeamsServiceException();
        }
        if (newGroupName.trim().length() != newGroupName.length()) {
            showToastWithMessage("Group name could not have spaces on begin and end", Toast.LENGTH_SHORT);
            throw new SetTeamsServiceException();
        }
        if (newGroupName.length() < 3 || newGroupName.length() > 14) {
            showToastWithMessage("Group name should have min 3 max 14 length", Toast.LENGTH_SHORT);
            throw new SetTeamsServiceException();
        }
    }

    public List<String> removeGroupFromList(int position) {
        teamsList.remove(position);
        saveResultInSharedPreferences(teamsList);
        return teamsList;
    }

    public int getGroupsListSize() {
        return teamsList.size();
    }

    public List<String> getTeamsList() {
        return teamsList;
    }

    @Override
    public void saveResultInSharedPreferences(List<String> toSave) {
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor sharedPreferencesEditor = context.getSharedPreferences(TEAMS_FROM_SP, Context.MODE_PRIVATE).edit();
        Log.i("SetTeamsService", teamsList.toString());
        sharedPreferencesEditor.putStringSet(TEAMS_FROM_SP, new HashSet<>(teamsList));
        sharedPreferencesEditor.apply();
    }

    @Override
    public void showToastWithMessage(String message, int lengthOfToast) {
        Toast.makeText(context, message, lengthOfToast).show();
    }
}
