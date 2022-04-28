package com.example.milionerzy.services;

import android.content.Context;
import android.os.Build;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.milionerzy.exceptions.SetGroupsServiceException;
import com.example.milionerzy.interfaces.BaseService;

import java.util.ArrayList;
import java.util.List;

public class SetGroupsService implements BaseService {

    private final Context context;
    private final List<String> groupsList = new ArrayList<>();

    public SetGroupsService(Context context) {
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<String> addNewGroupFromEditTextToList(EditText newGroupEditText) throws SetGroupsServiceException {
        String newGroupName = newGroupEditText.getText().toString();

        groupNameIsValid(newGroupName);

        if (groupsList.size() >= 8) {
            showToastWithMessage("You can set max 8 groups", Toast.LENGTH_SHORT);
            throw new SetGroupsServiceException();
        }
        if (checkListContainsGroupWithTheSameName(newGroupName)) {
            showToastWithMessage("Group with name: " + newGroupName + " already exist.", Toast.LENGTH_SHORT);
            throw new SetGroupsServiceException();
        } else {
            groupsList.add(newGroupName);
        }
        return groupsList;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private boolean checkListContainsGroupWithTheSameName(String newGroupName) {
        return groupsList.stream().anyMatch(groupName -> groupName.equals(newGroupName));
    }

    private void groupNameIsValid(String newGroupName) throws SetGroupsServiceException {
        if (newGroupName == null || newGroupName.equals("")) {
            showToastWithMessage("Group name could not be empty", Toast.LENGTH_SHORT);
            throw new SetGroupsServiceException();
        }
        if (newGroupName.trim().length() != newGroupName.length()) {
            showToastWithMessage("Group name could not have spaces on begin and end", Toast.LENGTH_SHORT);
            throw new SetGroupsServiceException();
        }
        if (newGroupName.length() < 3 || newGroupName.length() > 14) {
            showToastWithMessage("Group name should have min 3 max 14 length", Toast.LENGTH_SHORT);
            throw new SetGroupsServiceException();
        }
    }

    public List<String> removeGroupFromList(int position) {
        groupsList.remove(position);
        return groupsList;
    }

    public int getGroupsListSize() {
        return groupsList.size();
    }

    @Override
    public void showToastWithMessage(String message, int lengthOfToast) {
        Toast.makeText(context, message, lengthOfToast).show();
    }
}
