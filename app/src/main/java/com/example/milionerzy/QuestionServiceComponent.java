package com.example.milionerzy;

import com.example.milionerzy.activities.AdminActivity;
import com.example.milionerzy.services.QuestionService;

import dagger.Component;

@Component(modules = QuestionServiceModule.class)
public interface QuestionServiceComponent {

    QuestionService getQuestionService();

    void inject(AdminActivity adminActivity);
}
