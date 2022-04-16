package com.example.milionerzy.game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.milionerzy.DaggerQuestionServiceComponent;
import com.example.milionerzy.MainActivity;
import com.example.milionerzy.QuestionServiceComponent;
import com.example.milionerzy.R;
import com.example.milionerzy.database.DatabaseException;
import com.example.milionerzy.model.Question;
import com.example.milionerzy.services.QuestionService;

import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    private int score;
    private int numberOfQuestion;
    private TextView contentOfQuestionTextView;
    private Button finishGameButton;
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
        findAllViews();
        randomizeNumberOfQuestion();
        getQuestionFromList();
        setAllListeners();
    }

    private void setScore(int initialScore){
        if (initialScore<0) score=0;
    }

     private void randomizeNumberOfQuestion() {
        Random random = new Random();
        numberOfQuestion = random.nextInt(questionList.size());
    }

    private void getQuestionFromList() {
        contentOfQuestionTextView.setText(questionList.get(numberOfQuestion).getContentOfQuestion());
        answerAButton.setText(questionList.get(numberOfQuestion).getAnswerA());
        answerBButton.setText(questionList.get(numberOfQuestion).getAnswerB());
        answerCButton.setText(questionList.get(numberOfQuestion).getAnswerC());
        answerDButton.setText(questionList.get(numberOfQuestion).getAnswerD());
    }


    private void checkAnswer(String tag, String correctAnswer) {
        if (tag.equals(correctAnswer)) {
            Log.i("GameActivity", "prawidlowa odp");
            setScore(score+2);
        }
        else {
            Log.i("GameActivity","nieprawidlowa odp");
            setScore(score--);
        }
        getNextQuestion();

    }

    private void getNextQuestion() {
        removeQuestionFromList();
        if (!questionList.isEmpty()) {
            randomizeNumberOfQuestion();
            getQuestionFromList();
        }else
        {
            finishGame();
        }
    }

    private void finishGame() {
        Log.i("GameActivity", "ukonczyles gre z wynikiem " + score);
        setScore(0);
        Intent intent = new Intent(GameActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void createListWithQuestions() {
        QuestionServiceComponent questionServiceComponent = DaggerQuestionServiceComponent.create();
        QuestionService questionService = questionServiceComponent.getQuestionService();
        questionService.setDatabaseContext(this);
        try {
            questionList = questionService.getAllQuestions();
        }catch (DatabaseException de){
            Log.i("GameActivity", "Exception during getting questions");
        }


        Question a = new Question(0, "content AAAAA", "odpowiedza ", "b", "c ", "d", "D");
        questionList.add(a);
        //        Question b = new Question(1, "content BBB", "odpowiedza ", "b", "c ", "d", "C");
//        Question c = new Question(2, "content CCC ", "odpowiedza ", "b", "c ", "d", "A");
//        questionList = new ArrayList<>();
//        questionList.add(b);
//        questionList.add(c);

    }
    private void findAllViews() {
        contentOfQuestionTextView = findViewById(R.id.textViewQuestion);
        finishGameButton = findViewById(R.id.finishGameButton);
        answerAButton = findViewById(R.id.buttonAnswerA);
        answerBButton = findViewById(R.id.buttonAnswerB);
        answerCButton = findViewById(R.id.buttonAnswerC);
        answerDButton = findViewById(R.id.buttonAnswerD);
    }
    private void setAllListeners() {
        finishGameButton.setOnClickListener(v -> finishGame());
        answerAButton.setOnClickListener(v -> checkAnswer(v.getTag().toString(), questionList.get(numberOfQuestion).getCorrectAnswer()));
        answerBButton.setOnClickListener(v -> checkAnswer(v.getTag().toString(), questionList.get(numberOfQuestion).getCorrectAnswer()));
        answerCButton.setOnClickListener(v -> checkAnswer(v.getTag().toString(), questionList.get(numberOfQuestion).getCorrectAnswer()));
        answerDButton.setOnClickListener(v -> checkAnswer(v.getTag().toString(), questionList.get(numberOfQuestion).getCorrectAnswer()));
    }
    private void removeQuestionFromList() {
        questionList.remove(numberOfQuestion);
    }

}