package com.example.milionerzy.admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.milionerzy.DaggerQuestionServiceComponent;
import com.example.milionerzy.QuestionServiceComponent;
import com.example.milionerzy.R;
import com.example.milionerzy.database.DatabaseException;
import com.example.milionerzy.model.Question;
import com.example.milionerzy.services.QuestionService;
import com.example.milionerzy.validator.EmptyFieldException;

public class AdminActivity extends AppCompatActivity {

    private RadioGroup radioGroup;
    private EditText contentOfQuestionEditText;
    private EditText answerAEditText;
    private EditText answerBEditText;
    private EditText answerCEditText;
    private EditText answerDEditText;
    private QuestionService questionService;
    private Button saveQuestionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        QuestionServiceComponent questionServiceComponent = DaggerQuestionServiceComponent.create();
        questionService = questionServiceComponent.getQuestionService();
        questionService.setDatabaseContext(this);
        findAllViews();
        Log.i("AdminActivity", "Oncreate invoked");
        fillEditTextsIfEditMode();
    }

    private void fillEditTextsIfEditMode() {
        try {
            Intent intent = getIntent();
            int id = intent.getIntExtra("questionId", -1);
            Log.i("AdminActivity", "Id = " + id);
            if (id != -1) {
                Question question = questionService.getQuestion(id);
                Log.i("AdminActiviy", question.toString());
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
            case "B":
                radioGroup.check(R.id.radioButton_B_isCorrect);
            case "C":
                radioGroup.check(R.id.radioButton_C_isCorrect);
            case "D":
                radioGroup.check(R.id.radioButton_D_isCorrect);
        }
    }

    private void findAllViews() {
        contentOfQuestionEditText = findViewById(R.id.contentOfQuestionEditText);
        answerAEditText = findViewById(R.id.answerAeditText);
        answerBEditText = findViewById(R.id.answerBeditText);
        answerCEditText = findViewById(R.id.answerCeditText);
        answerDEditText = findViewById(R.id.answerDeditText);
        radioGroup = findViewById(R.id.radioButton_group);
        saveQuestionButton = findViewById(R.id.saveQuestionButton);
        Button goToDatabaseActivityButton = findViewById(R.id.goToQuestionDatabaseActivity);
        goToDatabaseActivityButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, QuestionsDatabaseActivity.class);
            startActivity(intent);
        });
        saveQuestionButton.setOnClickListener(v -> saveQuestion());
    }

    private void saveQuestion() {
        try {
            Question question = createQuestionFromEditTexts();
            questionService.addQuestion(question);
            clearAllEditableFieldsAndRadioButtons();
            Toast.makeText(this, "Question saved succesfully", Toast.LENGTH_SHORT).show();
        } catch (EmptyFieldException e) {
            Toast.makeText(this, "Fill all required fields and check correct answer", Toast.LENGTH_SHORT).show();
        }
    }

    private void editQuestion(int id) {
        try {
            Question question = createQuestionFromEditTexts();
            questionService.editQuestion(id, question);
            Intent intent = new Intent(this, QuestionsDatabaseActivity.class);
            startActivity(intent);
            finish(); // TODO learn more about request code
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
            Log.i("Radiobutton", "Radiobuttons exception");
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