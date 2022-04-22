package com.example.milionerzy.settings;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.example.milionerzy.R;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RequiresApi(api = Build.VERSION_CODES.O)
public class SettingsActivity extends AppCompatActivity {
    Button saveNewPasswordButton;
    EditText newPasswordEditText;
    TextView wrongPasswordTextView, passwordChangedCorrectlyTextView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        wrongPasswordTextView = findViewById(R.id.wrongTypeOfNewPasswordTextView);
        passwordChangedCorrectlyTextView = findViewById(R.id.passwordChangedCorrectlyTextView);
        newPasswordEditText = findViewById(R.id.newPasswordEditText);
        saveNewPasswordButton = findViewById(R.id.saveNewPasswordButton);
        saveNewPasswordButton.setOnClickListener(b -> {
            try {
                saveNewPassword();
            } catch (GeneralSecurityException | IOException e) {
                e.printStackTrace();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void saveNewPassword() throws GeneralSecurityException, IOException {
        String password = newPasswordEditText.getText().toString();
        if (!passwordIsValid(password)) {
            wrongPasswordTextView.setVisibility(View.VISIBLE);
            passwordChangedCorrectlyTextView.setVisibility(View.GONE);
            return;
        }
        String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
        SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                "secret_shared_prefs",
                masterKeyAlias,
                this,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );
        sharedPreferences.edit().putString("encryptedPassword", newPasswordEditText.getText().toString()).apply();
        wrongPasswordTextView.setVisibility(View.GONE);
        passwordChangedCorrectlyTextView.setVisibility(View.VISIBLE);
    }

    private boolean passwordIsValid(String password) {
        boolean lengthOfPasswordIsCorrect = correctLengthOfPassword(password);
        if (!lengthOfPasswordIsCorrect) {
            return false;
        }
        if (password.contains(" ")) {
            return false;
        }
        return password.trim().length() == password.length();
    }

    private boolean correctLengthOfPassword(String password) {
        return password.length() > 6 && password.length() < 12;
    }
}