package com.example.milionerzy.game;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.milionerzy.R;
import com.example.milionerzy.model.Question;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity {
    private int wynik;
    private int numberOfQuestion = 0;
    private TextView contentOfQuestionTextView;
    private Button answerAButton;
    private Button answerBButton;
    private Button answerCButton;
    private Button answerDButton;
    private List<Question> questionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        createListWithQuestions();
        findAllViewsAndListeners();
        getQuestionFromList();
    }

    private void getQuestionFromList() {
    contentOfQuestionTextView.setText(questionList.get(numberOfQuestion).getContentOfQuestion());
    answerAButton.setText(questionList.get(numberOfQuestion).getAnswerA());
    answerBButton.setText(questionList.get(numberOfQuestion).getAnswerB());
    answerCButton.setText(questionList.get(numberOfQuestion).getAnswerC());
    answerDButton.setText(questionList.get(numberOfQuestion).getAnswerD());
    }

    private void findAllViewsAndListeners() {
         contentOfQuestionTextView = findViewById(R.id.textViewQuestion);
         answerAButton = findViewById(R.id.buttonAnswerA);
         answerAButton.setOnClickListener(v ->{
             checkAnswer(v.getTag().toString(), questionList.get(numberOfQuestion).getCorrectAnswer());
         });
         answerBButton = findViewById(R.id.buttonAnswerB);
        answerBButton.setOnClickListener(v ->{
            checkAnswer(v.getTag().toString(), questionList.get(numberOfQuestion).getCorrectAnswer());
        });
         answerCButton = findViewById(R.id.buttonAnswerC);
        answerCButton.setOnClickListener(v ->{
            checkAnswer(v.getTag().toString(), questionList.get(numberOfQuestion).getCorrectAnswer());
        });
         answerDButton = findViewById(R.id.buttonAnswerD);
        answerDButton.setOnClickListener(v ->{
            checkAnswer(v.getTag().toString(), questionList.get(numberOfQuestion).getCorrectAnswer());
        });
    }

    private void checkAnswer(String tag,String correctAnswer) {
        if(tag.equals(correctAnswer))
        {
            Log.i("GameActivity" ,"prawidlowa odp");
            numberOfQuestion++;
            getQuestionFromList();
        }
    }

    private void createListWithQuestions()
    {
        Question a = new Question(0, "content AAAAA","odpowiedza ","b", "c ", "d", "D");
        Question b = new Question(1, "content BBB","odpowiedza ","b", "c ", "d", "C");
        Question c = new Question(2, "content CCC ","odpowiedza ","b", "c ", "d", "A");
        questionList = new ArrayList<>();
        questionList.add(a);
        questionList.add(b);
        questionList.add(c);

    }
}