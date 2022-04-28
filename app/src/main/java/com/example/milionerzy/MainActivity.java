package com.example.milionerzy;

import android.app.AlertDialog;
import android.content.Intent;
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
import com.example.milionerzy.settings.SettingsActivity;
import com.example.milionerzy.validator.PasswordService;


public class MainActivity extends AppCompatActivity {

    private PasswordService passwordService;

    private Button openSetGroupsAndGameLengthActivityToTest; // only for testing

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setLayoutComponents();
        passwordService = new PasswordService();
        openSetGroupsAndGameLengthActivityToTest = findViewById(R.id.openSetGroupsAndGameLength_ForTEst);
        openSetGroupsAndGameLengthActivityToTest.setOnClickListener(b -> {
            Intent intent = new Intent(this, SetGroupsAndGameLengthActivity.class);
            startActivity(intent);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setLayoutComponents() {
        Button startGameButton;
        startGameButton = findViewById(R.id.startGameButton);
        startGameButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            startActivity(intent);

        });

        Button logAsAdminButton = findViewById(R.id.logAsAdminButton);
        logAsAdminButton.setOnClickListener(v -> onAdminButtonClickListener());

        ImageButton openSettingsButton = findViewById(R.id.openSettingsButton);
        openSettingsButton.setOnClickListener(b -> {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        });
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