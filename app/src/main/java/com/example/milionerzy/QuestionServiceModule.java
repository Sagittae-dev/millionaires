package com.example.milionerzy;

import android.content.Context;

import com.example.milionerzy.repositories.QuestionsRepository;
import com.example.milionerzy.validator.QuestionValidator;

import dagger.Module;
import dagger.Provides;

@Module
public class QuestionServiceModule {

    @Provides
    QuestionsRepository provideDatabaseService() {
        MainActivity mainActivity = new MainActivity();
        Context context = mainActivity.getBaseContext();
        return new QuestionsRepository(context);
    }

    @Provides
    QuestionValidator provideQuestionValidator() {
        return new QuestionValidator();
    }
}
