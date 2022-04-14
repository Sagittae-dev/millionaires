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
    QuestionService questionService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        findAllViews();
        QuestionServiceComponent questionServiceComponent = DaggerQuestionServiceComponent.create();
        questionService = questionServiceComponent.getQuestionService();
        questionService.setDatabaseContext(this);
    }

    private void findAllViews() {
        contentOfQuestionEditText = findViewById(R.id.contentOfQuestionEditText);
        answerAEditText = findViewById(R.id.answerAeditText);
        answerBEditText = findViewById(R.id.answerBeditText);
        answerCEditText = findViewById(R.id.answerCeditText);
        answerDEditText = findViewById(R.id.answerDeditText);
        radioGroup = findViewById(R.id.radioButton_group);
        Button saveQuestionButton = findViewById(R.id.saveQuestionButton);
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
            Toast.makeText(this, "Question saved succesfully", Toast.LENGTH_SHORT).show();
        } catch (EmptyFieldException e) {
            Toast.makeText(this, "Fill all required fields and check correct answer", Toast.LENGTH_SHORT).show();
        }
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

    private Question createQuestionFromEditTexts() throws EmptyFieldException {
        Question question = new Question();
        question.setContentOfQuestion(contentOfQuestionEditText.getText().toString());
        question.setAnswerA(answerAEditText.getText().toString().trim());
        question.setAnswerB(answerBEditText.getText().toString().trim());
        question.setAnswerC(answerCEditText.getText().toString().trim());
        question.setAnswerD(answerDEditText.getText().toString().trim());
        question.setCorrectAnswer(setCorrectAnswerFromRadioButtons());
        return question;
    }

}