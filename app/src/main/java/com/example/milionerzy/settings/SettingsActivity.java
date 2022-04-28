package com.example.milionerzy.settings;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.milionerzy.R;
import com.example.milionerzy.validator.PasswordService;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RequiresApi(api = Build.VERSION_CODES.O)
public class SettingsActivity extends AppCompatActivity {
    public static final String SETTING_GAME_MODE = "GameMode";
    public static final int CLASSIC_MODE = 100;
    public static final int PARTY_MODE = 101;
    Button saveNewPasswordButton;
    Button saveSettingsButton;
    EditText newPasswordEditText;
    private RadioGroup chooseModeRadioGroup;
    private TextView wrongPasswordTextView, passwordChangedCorrectlyTextView, passwordsAreTheSameTextView, wrongOldPasswordTextView;
    private PasswordService passwordService;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        chooseModeRadioGroup = findViewById(R.id.chooseModeRadioGroup);
        wrongPasswordTextView = findViewById(R.id.wrongTypeOfNewPasswordTextView);
        setAllViews();
        passwordService = new PasswordService();
    }

    private void setAllViews() {
        passwordChangedCorrectlyTextView = findViewById(R.id.passwordChangedCorrectlyTextView);
        Button setNewPasswordButton = findViewById(R.id.saveNewPasswordButton);
        setNewPasswordButton.setOnClickListener(b -> openPasswordRequestDialog());
    }

    private void openPasswordRequestDialog() {
        LayoutInflater li = LayoutInflater.from(this);
        View passwordRequestView = li.inflate(R.layout.change_password_dialog, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        wrongPasswordTextView = passwordRequestView.findViewById(R.id.wrongTypeOfNewPasswordTextView);
        passwordsAreTheSameTextView = passwordRequestView.findViewById(R.id.passwordsAreTehSameTextView);
        wrongOldPasswordTextView = passwordRequestView.findViewById(R.id.wrongOldPasswordTextView);
        final EditText oldPasswordInput = passwordRequestView.findViewById(R.id.oldPasswordInChangeDialogEditText);
        final EditText newPasswordInput = passwordRequestView.findViewById(R.id.newPasswordInChangeDialogEditText);
        alertDialogBuilder.setView(passwordRequestView);
        alertDialogBuilder
                .setPositiveButton("Check", null)
                .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());

        final AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(b -> showWrongPasswordMessageOrSavePassword(oldPasswordInput, newPasswordInput, dialog));
        newPasswordEditText = findViewById(R.id.newPasswordEditText);
        saveNewPasswordButton = findViewById(R.id.saveNewPasswordButton);
        saveNewPasswordButton.setOnClickListener(b -> {
            try {
                saveNewPassword();
            } catch (GeneralSecurityException | IOException e) {
                e.printStackTrace();
            }
        });
        saveSettingsButton = findViewById(R.id.saveSettingsButton);
        saveSettingsButton.setOnClickListener(view -> setGameModeFromRadioButtons());
    }

    private void setGameModeFromRadioButtons() {
            if (chooseModeRadioGroup.getCheckedRadioButtonId() == R.id.partyRadioButton){
                setPreferenceGameMode(PARTY_MODE);
                Log.i("SettingsActivity", "Gamemode set to party mode");
            }
            else
            {
                setPreferenceGameMode(CLASSIC_MODE);
                Log.i("SettingsActivity", "Gamemode set to classic mode");
            }


        Toast.makeText(this, "Settings saved", Toast.LENGTH_SHORT).show();
        }

    private void showWrongPasswordMessageOrSavePassword(EditText oldPasswordInput, EditText newPasswordInput, AlertDialog dialog) {
        if (!passwordService.isCorrectFormat(oldPasswordInput)) {
            passwordsAreTheSameTextView.setVisibility(View.GONE);
            wrongPasswordTextView.setVisibility(View.VISIBLE);
            wrongOldPasswordTextView.setVisibility(View.GONE);
            return;
        }
        if (!passwordService.isCorrectFormat(newPasswordInput)) {
            passwordsAreTheSameTextView.setVisibility(View.GONE);
            wrongPasswordTextView.setVisibility(View.VISIBLE);
            wrongOldPasswordTextView.setVisibility(View.GONE);
            return;
        }
        if (!passwordService.passwordIsCorrect(this, oldPasswordInput)) {
            passwordsAreTheSameTextView.setVisibility(View.GONE);
            wrongPasswordTextView.setVisibility(View.GONE);
            wrongOldPasswordTextView.setVisibility(View.VISIBLE);
            return;
        }
        if (passwordService.twoPasswordMatches(oldPasswordInput, newPasswordInput)) {
            wrongPasswordTextView.setVisibility(View.GONE);
            passwordsAreTheSameTextView.setVisibility(View.VISIBLE);
            wrongOldPasswordTextView.setVisibility(View.GONE);
        } else {
            passwordService.saveNewPasswordToSP(newPasswordInput, this);
            dialog.dismiss();
            passwordChangedCorrectlyTextView.setVisibility(View.VISIBLE);
        }
    }

    private boolean correctLengthOfPassword(String password) {
        return password.length() > 6 && password.length() < 12;
    }


    public void setPreferenceGameMode(int thePreference)
    {
        SharedPreferences.Editor editor = getSharedPreferences(SETTING_GAME_MODE,0).edit();
        editor.putInt("GameMode", thePreference);
        editor.apply();
    }
}
