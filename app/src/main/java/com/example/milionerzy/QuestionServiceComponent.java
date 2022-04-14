package com.example.milionerzy;

import android.content.Context;

import com.example.milionerzy.admin.AdminActivity;
import com.example.milionerzy.services.QuestionService;

import dagger.Component;

@Component(modules = QuestionServiceModule.class)
public interface QuestionServiceComponent {
    QuestionService getQuestionService();

    void inject(AdminActivity adminActivity);
}
