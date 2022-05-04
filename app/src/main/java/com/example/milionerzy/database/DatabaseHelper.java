package com.example.milionerzy.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import javax.inject.Inject;

public class DatabaseHelper extends SQLiteOpenHelper {

    protected static final String QUESTIONS = "QUESTIONS";
    protected static final String Q_CONTENT = "QCONTENT";
    protected static final String ANSWER_A = "ANSWERA";
    protected static final String ANSWER_B = "ANSWERB";
    protected static final String ANSWER_C = "ANSWERC";
    protected static final String ANSWER_D = "ANSWERD";
    protected static final String CORRECT_ANSWER = "CORRECTANSWER";

    protected static final String TEAMS = "TEAMS";
    protected static final String TEAM_NAME = "TEAM_NAME";
    protected static final String TEAM_SCORE = "TEAM_SCORE";

    @Inject
    public DatabaseHelper(@Nullable Context context) {
        super(context, "MillionairesDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createQuestionsTable(db);
        createTeamsTable(db);
    }

    private void createTeamsTable(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + TEAMS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                TEAM_NAME + " TEXT NOT NULL," +
                TEAM_SCORE + " INTEGER NOT NULL)";
        db.execSQL(createTableStatement);
    }

    private void createQuestionsTable(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + QUESTIONS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                Q_CONTENT + " TEXT NOT NULL," +
                ANSWER_A + " TEXT NOT NULL," +
                ANSWER_B + " TEXT NOT NULL," +
                ANSWER_C + " TEXT NOT NULL," +
                ANSWER_D + " TEXT NOT NULL," +
                CORRECT_ANSWER + " TEXT NOT NULL)";
        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
