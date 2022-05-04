package com.example.milionerzy.validator;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.EditText;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.example.milionerzy.exceptions.NoPasswordException;

public class PasswordService {
    private SharedPreferences sharedPreferences;

    public PasswordService() {
    }

    public boolean isCorrectFormat(EditText passwordFromInput) {
        String password = passwordFromInput.getText().toString();
        if (password.length() <= 6) {
            return false;
        }
        if (password.length() >= 13) {
            return false;
        }
        return !password.contains(" ");
    }

    public boolean passwordIsCorrect(Context context, String passwordFromEditText) {
        String passwordFromSP = null;
        try {
            passwordFromSP = getPasswordFromSharedPreferences(context);
        } catch (NoPasswordException e) {
            Log.i("PasswordValidator", "Exception in getting password from SP");
        }
        return passwordFromEditText.equals(passwordFromSP);
    }

    public boolean passwordIsCorrect(Context context, EditText passwordFromEditText) {
        String passwordFromSP = null;
        try {
            passwordFromSP = getPasswordFromSharedPreferences(context);
        } catch (NoPasswordException e) {
            Log.i("PasswordValidator", "Exception in getting password from SP");
        }
        return passwordFromEditText.getText().toString().equals(passwordFromSP);
    }

    public boolean noSavedPassword(Context context) {
        try {
            getPasswordFromSharedPreferences(context);
        } catch (NoPasswordException e) {
            return true;
        }
        return false;
    }

    private String getPasswordFromSharedPreferences(Context context) throws NoPasswordException {
        setSharedPreferences(context);
        String password = sharedPreferences.getString("encryptedPassword", null);
//        Log.i("Settings", password);
        if (password != null) {
            return password;
        } else {
            throw new NoPasswordException();
        }
    }

    private void setSharedPreferences(Context context) {
        try {
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            sharedPreferences = EncryptedSharedPreferences.create(
                    "secret_shared_prefs",
                    masterKeyAlias,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (Exception e) {
            Log.i("PasswordValidator", "Exception on initialize Shared Preferences");
        }
    }

    public boolean twoPasswordMatches(EditText oldPasswordInput, EditText newPasswordInput) {
        return oldPasswordInput.getText().toString().equals(newPasswordInput.getText().toString());
    }

    public void saveNewPasswordToSP(EditText passwordInput, Context context) {
        setSharedPreferences(context);
        sharedPreferences.edit().putString("encryptedPassword", passwordInput.getText().toString()).apply();
    }

    public boolean userHasPassword(Context context) {
        try {
            getPasswordFromSharedPreferences(context);
        } catch (NoPasswordException e) {
            return false;
        }
        return true;
    }
}
