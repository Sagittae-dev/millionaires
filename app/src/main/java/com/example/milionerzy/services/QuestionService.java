package com.example.milionerzy.services;

import android.content.Context;
import android.util.Log;

import com.example.milionerzy.exceptions.DatabaseException;
import com.example.milionerzy.exceptions.EmptyFieldException;
import com.example.milionerzy.model.Question;
import com.example.milionerzy.repositories.QuestionsRepository;
import com.example.milionerzy.validator.QuestionValidator;

import java.util.List;

import javax.inject.Inject;

public class QuestionService {
    private QuestionsRepository questionsRepository;
    private final QuestionValidator questionValidator;

    @Inject
    public QuestionService(QuestionValidator questionValidator, QuestionsRepository questionsRepository) {
        this.questionValidator = questionValidator;
        this.questionsRepository = questionsRepository;
    }

    public void setDatabaseContext(Context context) {
        questionsRepository = new QuestionsRepository(context);
    }

    public void addQuestion(Question question) throws EmptyFieldException {
        questionValidator.validate(question);
        questionsRepository.addQuestion(question);
    }

    public void editQuestion(int id, Question question) throws EmptyFieldException, DatabaseException {
        questionValidator.validate(question);
        questionsRepository.editExistQuestion(id, question);
    }

    public List<Question> getAllQuestions() throws DatabaseException {
        return questionsRepository.getAllQuestions();
    }

    public void deleteQuestion(int id) {
        questionsRepository.deleteQuestion(id);
        Log.i("QuestionService", "deleteQuestion invoked ! ");
    }

    public Question getQuestion(int id) throws DatabaseException {
        Log.i("QuestionService", "get question invoked !");
        return questionsRepository.getQuestion(id);
    }
}
