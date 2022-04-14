package com.example.milionerzy.services;

import android.content.Context;

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

    public void addQuestion(Question question) throws EmptyFieldException {
        questionValidator.validate(question);
        databaseService.addQuestion(question);
    }

    public List<Question> getAllQuestions() throws DatabaseException {
        return databaseService.getAllQuestions();
    }

    public void setDatabaseContext(Context context) {
        databaseService = new DatabaseService(context);
    }

}
