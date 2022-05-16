package com.example.milionerzy.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.milionerzy.R;
import com.example.milionerzy.enums.GameModes;
import com.example.milionerzy.validator.PasswordService;

import static com.example.milionerzy.enums.GameModes.CLASSIC_MODE;
import static com.example.milionerzy.enums.GameModes.PARTY_MODE;

@RequiresApi(api = Build.VERSION_CODES.O)
public class SettingsActivity extends AppCompatActivity {
    public static final String SETTING_GAME_MODE = "GameMode";
    private RadioGroup chooseModeRadioGroup;
    private TextView wrongPasswordTextView, passwordChangedCorrectlyTextView, passwordsAreTheSameTextView, wrongOldPasswordTextView;
    private PasswordService passwordService;
    private boolean userHasPassword;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        wrongPasswordTextView = findViewById(R.id.wrongTypeOfNewPasswordTextView);
        setAllViews();
        passwordService = new PasswordService();
        userHasPassword = passwordService.userHasPassword(this);
    }

    private void setAllViews() {

        chooseModeRadioGroup = findViewById(R.id.chooseModeRadioGroup);
        SharedPreferences sharedPreferences = getSharedPreferences(SETTING_GAME_MODE, MODE_PRIVATE);
        if (sharedPreferences.getString(SETTING_GAME_MODE, CLASSIC_MODE.toString()).equals(CLASSIC_MODE.toString())) {
            chooseModeRadioGroup.check(R.id.classicRadioButton);
        } else chooseModeRadioGroup.check(R.id.partyRadioButton);
        passwordChangedCorrectlyTextView = findViewById(R.id.passwordChangedCorrectlyTextView);
        Button setNewPasswordButton = findViewById(R.id.saveNewPasswordButton);
        setNewPasswordButton.setOnClickListener(b -> openPasswordRequestDialog());
        Button saveSettingsButton = findViewById(R.id.saveSettingsButton);
        saveSettingsButton.setOnClickListener(view -> setGameModeFromRadioButtons());
    }

    @SuppressLint("SetTextI18n")
    private void openPasswordRequestDialog() {
        LayoutInflater li = LayoutInflater.from(this);
        View passwordRequestView = li.inflate(R.layout.change_password_dialog, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        wrongPasswordTextView = passwordRequestView.findViewById(R.id.wrongTypeOfNewPasswordTextView);
        passwordsAreTheSameTextView = passwordRequestView.findViewById(R.id.passwordsAreTehSameTextView);
        wrongOldPasswordTextView = passwordRequestView.findViewById(R.id.wrongOldPasswordTextView);
        final TextView title = passwordRequestView.findViewById(R.id.changePasswordTitle);
        final EditText oldPasswordInput = passwordRequestView.findViewById(R.id.oldPasswordInChangeDialogEditText);
        final EditText newPasswordInput = passwordRequestView.findViewById(R.id.newPasswordInChangeDialogEditText);
        if (!userHasPassword) {
            title.setText("Create Your password");
            oldPasswordInput.setHint("New password");
            newPasswordInput.setHint("Repeat new password");
        }
        alertDialogBuilder.setView(passwordRequestView);
        alertDialogBuilder
                .setPositiveButton("Check", null)
                .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());

        final AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(b ->
                showWrongPasswordMessageOrSavePassword(oldPasswordInput, newPasswordInput, dialog));
    }

    @SuppressLint("SetTextI18n")
    private void showWrongPasswordMessageOrSavePassword(EditText oldPasswordInput, EditText newPasswordInput, AlertDialog dialog) {
        if (!validatePasswordFormat(oldPasswordInput)) {
            return;
        }
        if (!validatePasswordFormat(newPasswordInput)) {
            return;
        }

        if (!passwordService.passwordIsCorrect(this, oldPasswordInput) && userHasPassword) {
            passwordsAreTheSameTextView.setVisibility(View.GONE);
            wrongPasswordTextView.setVisibility(View.GONE);
            wrongOldPasswordTextView.setVisibility(View.VISIBLE);
            return;
        }
        if (passwordService.twoPasswordMatches(oldPasswordInput, newPasswordInput) && userHasPassword) {
            wrongPasswordTextView.setVisibility(View.GONE);
            passwordsAreTheSameTextView.setVisibility(View.VISIBLE);
            wrongOldPasswordTextView.setVisibility(View.GONE);
        }
        if (!passwordService.twoPasswordMatches(oldPasswordInput, newPasswordInput) && !userHasPassword) {
            wrongPasswordTextView.setVisibility(View.GONE);
            passwordsAreTheSameTextView.setText("Passwords no matches");
            passwordsAreTheSameTextView.setVisibility(View.VISIBLE);
            wrongOldPasswordTextView.setVisibility(View.GONE);
        } else {
            passwordService.saveNewPasswordToSP(newPasswordInput, this);
            dialog.dismiss();
            passwordChangedCorrectlyTextView.setVisibility(View.VISIBLE);
        }
    }

    private boolean validatePasswordFormat(EditText passwordInput) {
        if (!passwordService.isCorrectFormat(passwordInput)) {
            passwordsAreTheSameTextView.setVisibility(View.GONE);
            wrongPasswordTextView.setVisibility(View.VISIBLE);
            wrongOldPasswordTextView.setVisibility(View.GONE);
            return false;
        }
        return true;
    }

    public void setPreferenceGameMode(GameModes thePreference) {
        SharedPreferences.Editor editor = getSharedPreferences(SETTING_GAME_MODE, MODE_PRIVATE).edit();
        editor.putString(SETTING_GAME_MODE, thePreference.toString());
        editor.apply();
    }

    private void setGameModeFromRadioButtons() {
        if (chooseModeRadioGroup.getCheckedRadioButtonId() == R.id.partyRadioButton) {
            setPreferenceGameMode(PARTY_MODE);
            Log.i("SettingsActivity", "GameMode set to party mode");
        } else {
            setPreferenceGameMode(CLASSIC_MODE);
            Log.i("SettingsActivity", "GameMode set to classic mode");
        }
        Toast.makeText(this, "Settings saved", Toast.LENGTH_SHORT).show();
    }
}