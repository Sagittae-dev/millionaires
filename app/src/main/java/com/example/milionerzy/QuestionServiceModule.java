package com.example.milionerzy;

import android.content.Context;

import com.example.milionerzy.database.DatabaseService;
import com.example.milionerzy.validator.QuestionValidator;

import dagger.BindsInstance;
import dagger.Module;
import dagger.Provides;

@Module
public class QuestionServiceModule {

    @Provides
    DatabaseService provideDatabaseService(){
        MainActivity mainActivity = new MainActivity();
        Context context = mainActivity.getBaseContext();
        return new DatabaseService(context);
    }

    @Provides
    QuestionValidator provideQuestionValidator(){
        return new QuestionValidator();
    }
}
