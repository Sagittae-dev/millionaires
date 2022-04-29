package com.example.milionerzy;



import static com.example.milionerzy.enums.GameModes.CLASSIC_MODE;
import static com.example.milionerzy.enums.GameModes.PARTY_MODE;
import static com.example.milionerzy.settings.SettingsActivity.SETTING_GAME_MODE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.milionerzy.admin.AdminActivity;
import com.example.milionerzy.game.GameActivity;
import com.example.milionerzy.game.SetGroupsAndGameLengthActivity;
import com.example.milionerzy.game.party.PartyGameActivity;
import com.example.milionerzy.settings.SettingsActivity;
import com.example.milionerzy.validator.PasswordService;


public class MainActivity extends AppCompatActivity {

    private PasswordService passwordService;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setLayoutComponents();
        passwordService = new PasswordService();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("InlinedApi")
    private void setLayoutComponents() {
        Button startGameButton;
        startGameButton = findViewById(R.id.startGameButton);
        startGameButton.setOnClickListener(view -> checkMode());

        Button logAsAdminButton = findViewById(R.id.logAsAdminButton);
        logAsAdminButton.setOnClickListener(v -> onAdminButtonClickListener());

        Button testButton = findViewById(R.id.testnewActivityButton);
        testButton.setOnClickListener(b -> {
            Intent intent = new Intent(this, PartyGameActivity.class);
            startActivity(intent);
        });

        ImageButton openSettingsButton = findViewById(R.id.openSettingsButton);
        openSettingsButton.setOnClickListener(b -> {
            @SuppressLint({"NewApi", "LocalSuppress"}) Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("InlinedApi")
    private void checkMode() {
        SharedPreferences sharedPreferences = getSharedPreferences(SETTING_GAME_MODE, Context.MODE_PRIVATE);
        String mode = sharedPreferences.getString(SETTING_GAME_MODE, CLASSIC_MODE.toString());
            if (mode.equals(CLASSIC_MODE.toString())) {
               startGame(GameActivity.class);
            } else if (mode.equals(PARTY_MODE.toString())) {
                startGame(SetGroupsAndGameLengthActivity.class);
            }
        }

        private void startGame(Class<?> activity){
            Intent intent = new Intent(this, activity);
            startActivity(intent);
        }

    private void onAdminButtonClickListener() {
        if (passwordService.noSavedPassword(this)) {
            showAlertThatUserHaveNotPasswordSet();
        } else {
            showPasswordRequest();
        }
    }

    private void showAlertThatUserHaveNotPasswordSet() {
        new AlertDialog.Builder(this)
                .setTitle("You have not password")
                .setCancelable(false)
                .setPositiveButton("ok", (dialog, id) -> dialog.dismiss())
                .create()
                .show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showPasswordRequest() {
        LayoutInflater li = LayoutInflater.from(this);
        View passwordRequestView = li.inflate(R.layout.password_request, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        final EditText userInput = passwordRequestView.findViewById(R.id.editTextDialogUserInput);
        TextView wrongPasswordTextView = passwordRequestView.findViewById(R.id.wrongPasswordMessage);
        alertDialogBuilder.setView(passwordRequestView);
        alertDialogBuilder
                .setPositiveButton("Check", null)
                .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());

        final AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(b -> {
            String password = userInput.getText().toString();

            if (passwordService.passwordIsCorrect(this, password)) {
                Intent intent = new Intent(this, AdminActivity.class);
                startActivity(intent);
                dialog.dismiss();
            } else {
                wrongPasswordTextView.setVisibility(View.VISIBLE);
            }
        });
    }
}