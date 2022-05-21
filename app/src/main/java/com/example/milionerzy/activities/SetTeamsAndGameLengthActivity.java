package com.example.milionerzy.activities;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.milionerzy.R;
import com.example.milionerzy.exceptions.SetGameLengthServiceException;
import com.example.milionerzy.exceptions.SetTeamsServiceException;
import com.example.milionerzy.services.SaveTeamsAndGameLengthService;
import com.example.milionerzy.services.SetGameLengthService;
import com.example.milionerzy.services.SetTeamsService;

import java.util.List;

public class SetTeamsAndGameLengthActivity extends AppCompatActivity {
    private static final String TAG = "SetGroupsAndGameLength";

    private EditText addNewGroupEditText;
    private Button addNewGroupButton, addCycleButton, removeCycleButton, saveAndPlayButton;
    private TextView amountOfCyclesTextView;
    private SetGameLengthService setGameLengthService;
    private SetTeamsService setTeamsService;
    private ListView listOfGroups_ListView;
    private ArrayAdapter<String> arrayAdapter;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_groups_and_game_length);
        setGameLengthService = new SetGameLengthService(this);
        setTeamsService = new SetTeamsService(this);
        setAllViews();
        setMethodForAddAndRemoveCycleButtons();
        setListOfGroups();
        setAdapterForListView();
        setMethodForAddNewGroupButton();
        setMethodForSaveAndPlayButton();
    }

    private void setAllViews() {
        addNewGroupEditText = findViewById(R.id.setGroupsAndGameLength_AddNewGroup_EditText);
        addNewGroupButton = findViewById(R.id.setGroupsAndGameLength_AddNewGroup_Button);
        addCycleButton = findViewById(R.id.setGroupsAndGameLength_AddCycle_Button);
        removeCycleButton = findViewById(R.id.setGroupsAndGameLength_RemoveCycle_Button);
        amountOfCyclesTextView = findViewById(R.id.setGroupsAndGameLength_CyclesAmount_TextView);
        listOfGroups_ListView = findViewById(R.id.setGroupsAndGameLength_GroupsList_ListView);
        saveAndPlayButton = findViewById(R.id.setGroupsAndGameLength_SaveGroupsAndPlay_Button);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setMethodForAddNewGroupButton() {
        addNewGroupButton.setOnClickListener(b -> addNewGroupFromEditTextToList(addNewGroupEditText));
    }

    private void setMethodForAddAndRemoveCycleButtons() {
        addCycleButton.setOnClickListener(b -> addCycle());
        removeCycleButton.setOnClickListener(b -> removeCycle());
    }

    private void setMethodForSaveAndPlayButton() {
        saveAndPlayButton.setOnClickListener(b -> onSaveButtonClickListenerMethod());
    }

    private void setAdapterForListView() {
        List<String> teamsAsStrings = setTeamsService.getTeamsList();
        arrayAdapter = new ArrayAdapter<>(this, R.layout.milionaires_list_item, teamsAsStrings);
    }

    private void onSaveButtonClickListenerMethod() {
        SaveTeamsAndGameLengthService saveTeamsAndGameLengthService = new SaveTeamsAndGameLengthService(setTeamsService, setGameLengthService, this);
        try {
            saveTeamsAndGameLengthService.actionAfterTapOnSaveGroupsAndPlayButton();
        } catch (SetTeamsServiceException | SetGameLengthServiceException e) {
            Log.i(TAG, "Exception on saving and going to Party Game");
        }
    }

    private void setListOfGroups() {
        listOfGroups_ListView.setOnItemLongClickListener((adapterView, view, position, l) -> {
            List<String> listOfGroups = setTeamsService.removeGroupFromList(position);
            arrayAdapter = new ArrayAdapter<>(this, R.layout.milionaires_list_item, listOfGroups);
            listOfGroups_ListView.setAdapter(arrayAdapter);
            return true;
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addNewGroupFromEditTextToList(EditText newGroupEditText) {
        try {
            List<String> listOfGroups = setTeamsService.addNewGroupFromEditTextToList(newGroupEditText);
            arrayAdapter = new ArrayAdapter<>(this, R.layout.milionaires_list_item, listOfGroups);
            listOfGroups_ListView.setAdapter(arrayAdapter);
            newGroupEditText.getText().clear();
            Log.i(TAG, "group added successfully" + listOfGroups.toString());
        } catch (SetTeamsServiceException e) {
            Log.i(TAG, "Exception in adding new group");
        }
    }

    private void setAmountOfCycleTextView(int amountOfCycles) {
        amountOfCyclesTextView.setText(String.valueOf(amountOfCycles));
    }

    private void removeCycle() {
        try {
            int amountOfCycles = setGameLengthService.removeOneCycleOfQuestions();
            setAmountOfCycleTextView(amountOfCycles);
        } catch (SetGameLengthServiceException e) {
            Log.i(TAG, "You cannot have less than 1 cycle");
        }
    }

    private void addCycle() {
        try {
            int amountOfGroups = setTeamsService.getGroupsListSize();
            int amountOfCycles = setGameLengthService.addNewCycleOfQuestions(amountOfGroups);
            setAmountOfCycleTextView(amountOfCycles);

        } catch (SetGameLengthServiceException e) {
            Log.i(TAG, "Not enough questions in database");
        }
    }


}