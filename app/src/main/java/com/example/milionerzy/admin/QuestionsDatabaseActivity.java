package com.example.milionerzy.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.milionerzy.DaggerQuestionServiceComponent;
import com.example.milionerzy.QuestionServiceComponent;
import com.example.milionerzy.R;
import com.example.milionerzy.adapters.AllQuestionsListAdapter;
import com.example.milionerzy.database.DatabaseException;
import com.example.milionerzy.model.Question;
import com.example.milionerzy.services.QuestionService;

import java.util.List;


public class QuestionsDatabaseActivity extends AppCompatActivity {

    QuestionService questionService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions_database);
        QuestionServiceComponent questionServiceComponent = DaggerQuestionServiceComponent.create();
        questionService = questionServiceComponent.getQuestionService();
        questionService.setDatabaseContext(this);
        setRecyclerView();
    }

    private void setRecyclerView() {
        try {
            List<Question> questionList = questionService.getAllQuestions();
            RecyclerView listOfQuestionsRecyclerView = findViewById(R.id.allQuestionsList_Database);
            AllQuestionsListAdapter adapter = new AllQuestionsListAdapter(this, questionList);
            listOfQuestionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            listOfQuestionsRecyclerView.setAdapter(adapter);
        }catch(DatabaseException de){
            Log.i("DatabaseException", "Exception in getting data from database");
        }
    }
}