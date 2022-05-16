package com.example.milionerzy.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.milionerzy.DaggerQuestionServiceComponent;
import com.example.milionerzy.QuestionServiceComponent;
import com.example.milionerzy.R;
import com.example.milionerzy.exceptions.DatabaseException;
import com.example.milionerzy.exceptions.EmptyFieldException;
import com.example.milionerzy.model.Question;
import com.example.milionerzy.services.QuestionService;

public class AdminActivity extends AppCompatActivity {

    private RadioGroup radioGroup;
    private EditText contentOfQuestionEditText;
    private EditText answerAEditText;
    private EditText answerBEditText;
    private EditText answerCEditText;
    private EditText answerDEditText;
    private QuestionService questionService;
    private Button saveQuestionButton;

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isAcceptingText()) {
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(),
                    0
            );
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        QuestionServiceComponent questionServiceComponent = DaggerQuestionServiceComponent.create();
        questionService = questionServiceComponent.getQuestionService();
        questionService.setDatabaseContext(this);
        findAllViews(this);
        fillEditTextsIfEditMode();
    }

    private void fillEditTextsIfEditMode() {
        try {
            Intent intent = getIntent();
            int id = intent.getIntExtra("questionId", -1);
            Log.i("AdminActivity", "Id = " + id);
            if (id != -1) {
                Question question = questionService.getQuestion(id);
                Log.i("AdminActivity", question.toString());
                fillEditTextsWithRequestedQuestion_EditMode(question);
                saveQuestionButton.setOnClickListener(b -> editQuestion(id));
            }
        } catch (DatabaseException de) {
            Log.i("AdminActivity", "Exception during getting question");
        }
    }


    private void fillEditTextsWithRequestedQuestion_EditMode(Question question) {
        contentOfQuestionEditText.setText(question.getContentOfQuestion());
        answerAEditText.setText(question.getAnswerA());
        answerBEditText.setText(question.getAnswerB());
        answerCEditText.setText(question.getAnswerC());
        answerDEditText.setText(question.getAnswerD());
        switch (question.getCorrectAnswer()) {
            case "A":
                radioGroup.check(R.id.radioButton_A_isCorrect);
                break;
            case "B":
                radioGroup.check(R.id.radioButton_B_isCorrect);
                break;
            case "C":
                radioGroup.check(R.id.radioButton_C_isCorrect);
                break;
            case "D":
                radioGroup.check(R.id.radioButton_D_isCorrect);
                break;
        }
    }

    private void findAllViews(Activity activity) {
        ConstraintLayout layout = findViewById(R.id.adminLayout);
        layout.setOnClickListener(l -> hideSoftKeyboard(activity));
        contentOfQuestionEditText = findViewById(R.id.contentOfQuestionEditText);
        answerAEditText = findViewById(R.id.answerAeditText);
        answerBEditText = findViewById(R.id.answerBeditText);
        answerCEditText = findViewById(R.id.answerCeditText);
        answerDEditText = findViewById(R.id.answerDeditText);
        radioGroup = findViewById(R.id.radioButton_group);
        saveQuestionButton = findViewById(R.id.saveQuestionButton);
        Button goToDatabaseActivityButton = findViewById(R.id.goToQuestionDatabaseActivity);
        goToDatabaseActivityButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AdminModeDatabaseActivity.class);
            startActivity(intent);
        });
        saveQuestionButton.setOnClickListener(v -> saveQuestion());
    }

    private void saveQuestion() {
        try {
            Question question = createQuestionFromEditTexts();
            questionService.addQuestion(question);
            clearAllEditableFieldsAndRadioButtons();
            Toast.makeText(this, "Question saved successfully", Toast.LENGTH_SHORT).show();
        } catch (EmptyFieldException e) {
            Toast.makeText(this, "Fill all required fields and check correct answer", Toast.LENGTH_SHORT).show();
        }
    }

    private void editQuestion(int id) {
        try {
            Question question = createQuestionFromEditTexts();
            questionService.editQuestion(id, question);
            Intent intent = new Intent(this, AdminModeDatabaseActivity.class);
            startActivity(intent);
            finish();
        } catch (EmptyFieldException | DatabaseException e) {
            Log.i("AdminActivity", "Empty field Exception");
        }
    }

    private void clearAllEditableFieldsAndRadioButtons() {
        contentOfQuestionEditText.getText().clear();
        answerAEditText.getText().clear();
        answerBEditText.getText().clear();
        answerCEditText.getText().clear();
        answerDEditText.getText().clear();
        radioGroup.clearCheck();
    }

    private String setCorrectAnswerFromRadioButtons() throws EmptyFieldException {
        RadioButton radioButtonWithCorrectAnswer = findViewById(radioGroup.getCheckedRadioButtonId());
        if (radioButtonWithCorrectAnswer != null) {
            return radioButtonWithCorrectAnswer.getTag().toString();
        } else {
            Log.i("Radiobutton", "Radio buttons exception");
            throw new EmptyFieldException();
        }
    }

    private Question createQuestionFromEditTexts() {
        Question question = new Question();
        try {
            question.setContentOfQuestion(contentOfQuestionEditText.getText().toString());
            question.setAnswerA(answerAEditText.getText().toString().trim());
            question.setAnswerB(answerBEditText.getText().toString().trim());
            question.setAnswerC(answerCEditText.getText().toString().trim());
            question.setAnswerD(answerDEditText.getText().toString().trim());
            question.setCorrectAnswer(setCorrectAnswerFromRadioButtons());
            return question;
        } catch (Exception | EmptyFieldException e) {
            Toast.makeText(this, "Fill all fields an check correct answer", Toast.LENGTH_LONG).show();
        }
        return question;
    }

}