package com.example.milionerzy;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.milionerzy.admin.AdminActivity;
import com.example.milionerzy.game.GameActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setLayoutComponents();
    }

    private void setLayoutComponents() {
        Button startGameButton;
        startGameButton = findViewById(R.id.startGameButton);
        startGameButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            startActivity(intent);

        });

        Button logAsAdminButton;
        logAsAdminButton = findViewById(R.id.logAsAdminButton);
        logAsAdminButton.setOnClickListener(v -> showPasswordRequest());
    }

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
            if (passwordIsCorrect(userInput.getText().toString())) {
                Intent intent = new Intent(this, AdminActivity.class);
                startActivity(intent);
                dialog.dismiss();
            } else {
                wrongPasswordTextView.setVisibility(View.VISIBLE);
            }
        });

    }

    private boolean passwordIsCorrect(String password) {
        //TODO encryption for password
        return password.equals("admin");
    }
}