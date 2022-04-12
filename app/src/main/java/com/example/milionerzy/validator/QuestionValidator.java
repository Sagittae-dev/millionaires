package com.example.milionerzy.validator;

import android.util.Log;

import com.example.milionerzy.model.Question;

import java.util.ArrayList;
import java.util.List;

public class QuestionValidator {

    public void validate(Question question) throws EmptyFieldException {
        List<String> fieldList = new ArrayList<>();
        fieldList.add(question.getContentOfQuestion());
        fieldList.add(question.getAnswerA());
        fieldList.add(question.getAnswerB());
        fieldList.add(question.getAnswerC());
        fieldList.add(question.getAnswerD());
        fieldList.add(question.getCorrectAnswer());
        for (String field : fieldList){
            if(field == null || field.isEmpty()){
                Log.i("QuestionValidator", "QuestionValidator exception");
                throw new EmptyFieldException();
            }
        }
    }
}
