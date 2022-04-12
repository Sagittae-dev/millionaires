package com.example.milionerzy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.milionerzy.admin.AdminActivity;
import com.example.milionerzy.model.Question;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
    public static int textLength;
    static List<Integer> listOfNumbersOfQuestions= new ArrayList<>();
    static List<String> listOfQuestionsAndAnswers = new ArrayList<>();
    static int numberOfQuestion =0;
    //static int numberOfQuestionInList = 0;
    private Question question;
    //public String TrescPoprawnejOdpowiedzi;
    public int iloscPoprawnychOdpowiedzi;
    public int iloscBlednychOdpowiedzi;
    private TextView numerPytania;
    private TextView trescPytania;
    private TextView answerA;
    private TextView answerB;
    private TextView answerC;
    private TextView answerD;
    private ConstraintLayout metalLayout;
    private Button logAsAdminButton;

    private void setLayoutComponents() {
        metalLayout = findViewById(R.id.metalLayout);
        numerPytania = findViewById(R.id.numberQuestion1);
        trescPytania = findViewById(R.id.textView);
        answerA = findViewById(R.id.textView6);
        answerB = findViewById(R.id.textView5);
        answerC = findViewById(R.id.textView4);
        answerD = findViewById(R.id.textView3);
        logAsAdminButton = findViewById(R.id.logAsAdminButton);
        logAsAdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setLayoutComponents();
        txtReader();

        addQuestionsToList();
        System.out.println(listOfNumbersOfQuestions.size());
    }


    @SuppressLint("SetTextI18n")
    public void play(View view)  {
        if(numberOfQuestion == listOfNumbersOfQuestions.size()) {
            metalLayout.setClickable(false);
            numerPytania.setVisibility(View.INVISIBLE);
            trescPytania.setText("To już koniec.\n Udało się "+iloscPoprawnychOdpowiedzi+" razy.\n "+iloscBlednychOdpowiedzi+" razy podano złą odpowiedź ");
        }
        else{
            metalLayout.setClickable(false);
            answerA.setClickable(true);
            answerB.setClickable(true);
            answerC.setClickable(true);
            answerD.setClickable(true);

            Log.i("klikniete", "button pressed");

            question.setContentOfQuestion(listOfQuestionsAndAnswers.get(listOfNumbersOfQuestions.get(numberOfQuestion)));

            question.setAnswerA(listOfQuestionsAndAnswers.get(listOfNumbersOfQuestions.get(numberOfQuestion)+1));

            answerA.setBackgroundResource(R.drawable.guzik1);
            question.setAnswerB(listOfQuestionsAndAnswers.get(listOfNumbersOfQuestions.get(numberOfQuestion) + 2));

            answerB.setBackgroundResource(R.drawable.guzik1);
            question.setAnswerC(listOfQuestionsAndAnswers.get(listOfNumbersOfQuestions.get(numberOfQuestion) + 3));

            answerC.setBackgroundResource(R.drawable.guzik1);
            question.setAnswerD(listOfQuestionsAndAnswers.get(listOfNumbersOfQuestions.get(numberOfQuestion) + 4));

            answerD.setBackgroundResource(R.drawable.guzik1);
            question.setCorrectAnswer(listOfQuestionsAndAnswers.get(listOfNumbersOfQuestions.get(numberOfQuestion) + 5));
            numberOfQuestion = numberOfQuestion + 1;

            fillQuestionTextViews(question);
        }

        for (Integer integer : listOfNumbersOfQuestions) {
            System.out.println(integer);
        }
        System.out.println(listOfQuestionsAndAnswers.size());
        System.out.println(listOfQuestionsAndAnswers.get(58));
    }

    private void fillQuestionTextViews(Question question) {
        trescPytania.setText(question.getContentOfQuestion());
        numerPytania.setText("Pytanie " + (numberOfQuestion + 1));
        answerA.setText(question.getAnswerA());
        answerB.setText(question.getAnswerB());
        answerC.setText(question.getAnswerC());
        answerD.setText(question.getAnswerD());
    }

    private void fillQuestion(){

    }

    public void txtReader(){
        BufferedReader reader;
        try{
            final InputStream file = getAssets().open("pyt.txt");
            reader = new BufferedReader(new InputStreamReader(file));
            String line = " ";
            do{
                Log.d("StackOverflow", line);
                line = reader.readLine();
                listOfQuestionsAndAnswers.add(line);
                textLength++;
            }while(line != null);
            file.close();
        } catch(IOException ioe){
            ioe.printStackTrace();
        }
    }


    public void addQuestionsToList() {
        while(listOfNumbersOfQuestions.size() <= (textLength/6)-1) {
            int tmp = losowanie(textLength);
            if (!listOfNumbersOfQuestions.contains(tmp))
                listOfNumbersOfQuestions.add(tmp);
        }
    }

    public int losowanie(int textLength)  {
        Random r = new Random();
        int numberOfQuestion = r.nextInt(textLength / 6);
        numberOfQuestion = 6 * numberOfQuestion;
        return numberOfQuestion;
    }

    public void checkCorrectAnswer(View view){
        TextView trescPytania = findViewById(R.id.textView);
        TextView answerA = findViewById(R.id.textView6);
        TextView answerB = findViewById(R.id.textView5);
        TextView answerC = findViewById(R.id.textView4);
        TextView answerD = findViewById(R.id.textView3);
        ConstraintLayout metalLayout = findViewById(R.id.metalLayout);
        metalLayout.setClickable(true);

        answerA.setClickable(false);
        answerB.setClickable(false);
        answerC.setClickable(false);
        answerD.setClickable(false);

//        if(view.getTag().toString().equals(poprawnaOdpowiedz)) {
//            trescPytania.setText("brawo poprawna odpowiedź.\n Kliknij aby odczytać następne pytanie");
//            iloscPoprawnychOdpowiedzi++;
//        }
//        else {
//            trescPytania.setText("Niestety błędna odpowiedź.\n Kliknij aby odczytać następne pytanie");
//            iloscBlednychOdpowiedzi++;
//        }
//
//        if(poprawnaOdpowiedz.equals("a"))
//            answerA.setBackgroundResource(R.drawable.correctanswer);
//        else answerA.setBackgroundResource(R.drawable.wronganswer);
//        if(poprawnaOdpowiedz.equals("b"))
//            answerB.setBackgroundResource(R.drawable.correctanswer);
//        else answerB.setBackgroundResource(R.drawable.wronganswer);
//        if(poprawnaOdpowiedz.equals("c"))
//            answerC.setBackgroundResource(R.drawable.correctanswer);
//        else answerC.setBackgroundResource(R.drawable.wronganswer);
//        if(poprawnaOdpowiedz.equals("d"))
//            answerD.setBackgroundResource(R.drawable.correctanswer);
//        else answerD.setBackgroundResource(R.drawable.wronganswer);

    }

    public void playAgain(){
        ConstraintLayout metalLayout = findViewById(R.id.metalLayout);
        TextView numerPytania = findViewById(R.id.numberQuestion1);

        numberOfQuestion =0;
        listOfQuestionsAndAnswers.clear();
        listOfNumbersOfQuestions.clear();
        metalLayout.setClickable(true);

        txtReader();

        addQuestionsToList();
        metalLayout.setClickable(false);
        numerPytania.setVisibility(View.INVISIBLE);

    }
}
