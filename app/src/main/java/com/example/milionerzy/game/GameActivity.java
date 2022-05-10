package com.example.milionerzy.game;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.milionerzy.DaggerQuestionServiceComponent;
import com.example.milionerzy.MainActivity;
import com.example.milionerzy.QuestionServiceComponent;
import com.example.milionerzy.R;
import com.example.milionerzy.exceptions.DatabaseException;
import com.example.milionerzy.model.Question;
import com.example.milionerzy.services.QuestionService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameActivity extends AppCompatActivity {
    private int score, numberOfQuestion;
    private TextView contentOfQuestionTextView, scoreNumberTextView;
    private Button finishGameButton, answerAButton, answerBButton, answerCButton, answerDButton;
    private List<Question> questionList;
    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_game);
        createListWithQuestions();
        findAllViews();
        randomizeNumberOfQuestion();
        getQuestionFromList();
        setAllListeners();
    }

    private void setScore(int newScore) {
        if (newScore >= 0) {
            score = newScore;
        } else score = 0;
    }

    private void randomizeNumberOfQuestion() {
        Random random = new Random();
        numberOfQuestion = random.nextInt(questionList.size());
    }

    @SuppressLint("SetTextI18n")
    private void getQuestionFromList() {
        scoreNumberTextView.setText(Integer.toString(score));
        contentOfQuestionTextView.setText(questionList.get(numberOfQuestion).getContentOfQuestion());
        answerAButton.setText(questionList.get(numberOfQuestion).getAnswerA());
        answerBButton.setText(questionList.get(numberOfQuestion).getAnswerB());
        answerCButton.setText(questionList.get(numberOfQuestion).getAnswerC());
        answerDButton.setText(questionList.get(numberOfQuestion).getAnswerD());
    }


    private void checkAnswer(int id, String correctAnswer) {
        if (findViewById(id).getTag().toString().equals(correctAnswer)) {
            Log.i("GameActivity", "prawidlowa odp");
            findViewById(id).setBackgroundResource(R.drawable.correctanswer);
            setScore(score + 2);
        } else {
            findViewById(id).setBackgroundResource(R.drawable.wronganswer);
            Log.i("GameActivity", "nieprawidlowa odp");
            setScore(--score);
        }

        scheduledExecutorService.schedule(() -> {
            findViewById(id).setBackgroundResource(R.drawable.default_button_background);
            getNextQuestion();
        }, 1, TimeUnit.SECONDS);
    }

    @SuppressLint("SetTextI18n")
    private void getNextQuestion() {
        removeQuestionFromList();
        scoreNumberTextView.setText(Integer.toString(score));
        if (!questionList.isEmpty()) {
            randomizeNumberOfQuestion();
            getQuestionFromList();
        } else {
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
        questionList = new ArrayList<>();
        try {
            questionList = questionService.getAllQuestions();
        } catch (DatabaseException de) {
            Log.i("GameActivity", "Exception during getting questions");
        }

        Question testQuestion = new Question(0, "content AAAAA", "odpowiedza ", "b", "c ", "d", "D");
        questionList.add(testQuestion);
    }

    private void findAllViews() {
        findViewById(R.id.textViewScoreText);
        scoreNumberTextView = findViewById(R.id.textViewScoreNumber);
        contentOfQuestionTextView = findViewById(R.id.textViewQuestion);
        finishGameButton = findViewById(R.id.finishGameButton);
        answerAButton = findViewById(R.id.buttonAnswerA);
        answerBButton = findViewById(R.id.buttonAnswerB);
        answerCButton = findViewById(R.id.buttonAnswerC);
        answerDButton = findViewById(R.id.buttonAnswerD);
    }

    private void setAllListeners() {
        finishGameButton.setOnClickListener(v -> finishGame());
        answerAButton.setOnClickListener(v -> checkAnswer(v.getId(), questionList.get(numberOfQuestion).getCorrectAnswer()));
        answerBButton.setOnClickListener(v -> checkAnswer(v.getId(), questionList.get(numberOfQuestion).getCorrectAnswer()));
        answerCButton.setOnClickListener(v -> checkAnswer(v.getId(), questionList.get(numberOfQuestion).getCorrectAnswer()));
        answerDButton.setOnClickListener(v -> checkAnswer(v.getId(), questionList.get(numberOfQuestion).getCorrectAnswer()));
    }

    private void removeQuestionFromList() {
        questionList.remove(numberOfQuestion);
    }

}