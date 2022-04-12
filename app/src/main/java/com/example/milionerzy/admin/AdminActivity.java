package com.example.milionerzy.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.milionerzy.R;
import com.example.milionerzy.database.DatabaseService;
import com.example.milionerzy.model.Question;
import com.example.milionerzy.validator.EmptyFieldException;
import com.example.milionerzy.validator.QuestionValidator;

public class AdminActivity extends AppCompatActivity {

    private RadioGroup radioGroup;
    private EditText contentOfQuestionEditText;
    private EditText answerAEditText;
    private EditText answerBEditText;
    private EditText answerCEditText;
    private EditText answerDEditText;
    private Button saveQuestionButton;
    private RadioButton radioButtonWithCorrectAnswer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        findAllViews();
    }

    private void findAllViews() {
        contentOfQuestionEditText = findViewById(R.id.contentOfQuestionEditText);
        answerAEditText = findViewById(R.id.answerAeditText);
        answerBEditText = findViewById(R.id.answerBeditText);
        answerCEditText = findViewById(R.id.answerCeditText);
        answerDEditText = findViewById(R.id.answerDeditText);
        radioGroup = findViewById(R.id.radioButton_group);
        saveQuestionButton = findViewById(R.id.saveQuestionButton);
        saveQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveQuestion();
            }
        });
    }

    private void saveQuestion(){
        try {
            Question question = createQuestionFromEditTexts();
            question.setCorrectAnswer(setCorrectAnswerFromRadioButtons());
            QuestionValidator questionValidator = new QuestionValidator();
            questionValidator.validate(question);
            DatabaseService databaseService = new DatabaseService(AdminActivity.this);
            databaseService.addQuestion(question);
            Toast.makeText(this,"Question saved succesfully",Toast.LENGTH_SHORT).show();
        }catch(Exception | EmptyFieldException e){
            Toast.makeText(this,"Fill all required fields and check correct answer",Toast.LENGTH_SHORT).show();
        }
    }

    private String setCorrectAnswerFromRadioButtons() throws EmptyFieldException {
        radioButtonWithCorrectAnswer = findViewById(radioGroup.getCheckedRadioButtonId());
        if (radioButtonWithCorrectAnswer != null) {
            return radioButtonWithCorrectAnswer.getTag().toString();
        }else {
            Log.i("Radiobutton", "Radiobuttons exception");
            throw new EmptyFieldException();
        }
    }

    private Question createQuestionFromEditTexts() {
        Question question = new Question();
        question.setContentOfQuestion(contentOfQuestionEditText.getText().toString());
        question.setAnswerA(answerAEditText.getText().toString());
        question.setAnswerB(answerBEditText.getText().toString());
        question.setAnswerC(answerCEditText.getText().toString());
        question.setAnswerD(answerDEditText.getText().toString());
        return question;
    }

}