package com.example.milionerzy;

import android.app.AlertDialog;
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
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.example.milionerzy.admin.AdminActivity;
import com.example.milionerzy.game.GameActivity;
import com.example.milionerzy.settings.SettingsActivity;

import java.io.IOException;
import java.security.GeneralSecurityException;


public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setLayoutComponents();
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
        logAsAdminButton.setOnClickListener(v -> {
            String masterKeyAlias = null;
            try {
                masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            } catch (GeneralSecurityException | IOException e) {
                e.printStackTrace();
            }
            try {
                sharedPreferences = EncryptedSharedPreferences.create(
                        "secret_shared_prefs",
                        masterKeyAlias,
                        this,
                        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                );
            } catch (GeneralSecurityException | IOException e) {
                e.printStackTrace();
            }
            if (sharedPreferences.getString("encryptedPassword", null) != null) {
                showPasswordRequest();
            } else {
                showAlertThatUserHaveNotPasswordSet();
            }
        });

        ImageButton openSettingsButton = findViewById(R.id.openSettingsButton);
        openSettingsButton.setOnClickListener(b -> {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        });
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
        View promptsView = li.inflate(R.layout.password_request, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        final EditText userInput = promptsView.findViewById(R.id.editTextDialogUserInput);
        TextView wrongPasswordTextView = promptsView.findViewById(R.id.wrongPasswordMessage);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder
                .setPositiveButton("Check", null)
                .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());

        final AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(b -> {
            if (passwordIsCorrect(userInput)) {
                Intent intent = new Intent(this, AdminActivity.class);
                startActivity(intent);
                dialog.dismiss();
            } else {
                wrongPasswordTextView.setVisibility(View.VISIBLE);
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean passwordIsCorrect(EditText password) {
        String encryptedPassword = sharedPreferences.getString("encryptedPassword", "");
        String passwordFromEditText = password.getText().toString();

        return passwordFromEditText.equals(encryptedPassword);
    }
}