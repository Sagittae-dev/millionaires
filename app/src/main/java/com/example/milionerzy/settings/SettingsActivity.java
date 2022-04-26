package com.example.milionerzy.settings;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.example.milionerzy.R;

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
    TextView wrongPasswordTextView, passwordChangedCorrectlyTextView;
    private RadioGroup chooseModeRadioGroup;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        chooseModeRadioGroup = findViewById(R.id.chooseModeRadioGroup);
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

//    public int getPreferenceValueGameMode()
//    {
//        SharedPreferences sharedPreferences = getSharedPreferences(SETTING_GAME_MODE, 0);
//        //basic preference is classic gamemode
//        return sharedPreferences.getInt("GameMode",CLASSIC_MODE);
//    }

    public void setPreferenceGameMode(int thePreference)
    {
        SharedPreferences.Editor editor = getSharedPreferences(SETTING_GAME_MODE,0).edit();
        editor.putInt("GameMode", thePreference);
        editor.apply();
    }
}
