package com.example.milionerzy.game;

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
import com.example.milionerzy.exceptions.SetGroupsServiceException;
import com.example.milionerzy.services.SaveGroupsAndGameLengthService;
import com.example.milionerzy.services.SetGameLengthService;
import com.example.milionerzy.services.SetGroupsService;

import java.util.List;

public class SetGroupsAndGameLengthActivity extends AppCompatActivity {
    private static final String TAG = "SetGroupsAndGameLength";

    private EditText addNewGroupEditText;
    private Button addNewGroupButton, addCycleButton, removeCycleButton, saveAndPlayButton;
    private TextView amountOfCyclesTextView;
    private SetGameLengthService setGameLengthService;
    private SetGroupsService setGroupsService;
    private ListView listOfGroups_ListView;
    private SaveGroupsAndGameLengthService saveGroupsAndGameLengthService;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_groups_and_game_length);
        setGameLengthService = new SetGameLengthService(this);
        setGroupsService = new SetGroupsService(this);
        setAllViews();
        setMethodForAddAndRemoveCycleButtons();
        setListOfGroups();
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

    private void onSaveButtonClickListenerMethod() {
        saveGroupsAndGameLengthService = new SaveGroupsAndGameLengthService(setGroupsService, setGameLengthService, this);
        try {
            saveGroupsAndGameLengthService.actionAfterTapOnSaveGroupsAndPlayButton();
        } catch (SetGroupsServiceException | SetGameLengthServiceException e) {
            Log.i(TAG, "Exception on saving and going to Party Game");
        }
    }

    private void setListOfGroups() {
        listOfGroups_ListView.setOnItemLongClickListener((adapterView, view, position, l) -> {
            List<String> listOfGroups = setGroupsService.removeGroupFromList(position);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listOfGroups);
            listOfGroups_ListView.setAdapter(arrayAdapter);
            return true;
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addNewGroupFromEditTextToList(EditText newGroupEditText) {
        try {
            List<String> listOfGroups = setGroupsService.addNewGroupFromEditTextToList(newGroupEditText);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listOfGroups);
            listOfGroups_ListView.setAdapter(arrayAdapter);
            newGroupEditText.getText().clear();
            Log.i(TAG, "group added succesfully" + listOfGroups.toString());
        } catch (SetGroupsServiceException e) {
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
            int amountOfGroups = setGroupsService.getGroupsListSize();
            int amountOfCycles = setGameLengthService.addNewCycleOfQuestions(amountOfGroups);
            setAmountOfCycleTextView(amountOfCycles);

        } catch (SetGameLengthServiceException e) {
            Log.i(TAG, "Not enough questions in database");
        }
    }


}