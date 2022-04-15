package com.example.milionerzy.services;

import android.content.Context;
import android.util.Log;

import com.example.milionerzy.database.DatabaseException;
import com.example.milionerzy.database.DatabaseService;
import com.example.milionerzy.model.Question;
import com.example.milionerzy.validator.EmptyFieldException;
import com.example.milionerzy.validator.QuestionValidator;

import java.util.List;

import javax.inject.Inject;

public class QuestionService {
    private DatabaseService databaseService;
    private final QuestionValidator questionValidator;

    @Inject
    public QuestionService(QuestionValidator questionValidator, DatabaseService databaseService) {
        this.databaseService = databaseService;
        this.questionValidator = questionValidator;
    }

    public void setDatabaseContext(Context context) {
        databaseService = new DatabaseService(context);
    }

    public void addQuestion(Question question) throws EmptyFieldException {
        questionValidator.validate(question);
        databaseService.addQuestion(question);
    }

    public void editQuestion(int id, Question question) throws EmptyFieldException, DatabaseException {
        questionValidator.validate(question);
        databaseService.editExistQuestion(id, question);
    }

    public List<Question> getAllQuestions() throws DatabaseException {
        return databaseService.getAllQuestions();
    }

    public void deleteQuestion(int id) {
        databaseService.deleteQuestion(id);
        Log.i("QuestionService", "deleteQuestion invoked ! ");
    }

    public Question getQuestion(int id) throws DatabaseException {
        Log.i("QuestionService", "get question invoked !");
        return databaseService.getQuestion(id);
    }
}
