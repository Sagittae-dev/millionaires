package com.example.milionerzy.validator;

import android.util.Log;

import com.example.milionerzy.model.Question;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class QuestionValidator {

    @Inject
    public QuestionValidator() {
    }

    public void validate(Question question) throws EmptyFieldException {
        List<String> fieldList = new ArrayList<>();
        fieldList.add(question.getContentOfQuestion());
        fieldList.add(question.getAnswerA());
        fieldList.add(question.getAnswerB());
        fieldList.add(question.getAnswerC());
        fieldList.add(question.getAnswerD());
        fieldList.add(question.getCorrectAnswer());
        for (String field : fieldList){
            if(field == null){
                Log.i("QuestionValidator", "QuestionValidator exception");
                throw new EmptyFieldException();
            }
            else{
                validateParticularField(field);
            }
        }
    }

    private void validateParticularField(String field) throws EmptyFieldException {
        if(field.isEmpty() || field.trim().length() == 0){
            Log.i("QuestionValidator", "QuestionValidator exception. Field can't be empty");
            throw new EmptyFieldException();
        }
    }
}
