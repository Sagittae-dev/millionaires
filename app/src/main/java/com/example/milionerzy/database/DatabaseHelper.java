package com.example.milionerzy.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import javax.inject.Inject;

public class DatabaseHelper extends SQLiteOpenHelper {

    protected static final String PARTY_GAMES = "PARTY_GAMES";
    protected static final String QUESTIONS = "QUESTIONS";
    protected static final String Q_CONTENT = "QCONTENT";
    protected static final String ANSWER_A = "ANSWERA";
    protected static final String ANSWER_B = "ANSWERB";
    protected static final String ANSWER_C = "ANSWERC";
    protected static final String ANSWER_D = "ANSWERD";
    protected static final String CORRECT_ANSWER = "CORRECTANSWER";
    protected static final String WINNER = "WINNER";
    protected static final String PARTY_GAME_DATE = "PARTY_GAME_DATE";
    private static final int DATABASE_VERSION = 3;


    @Inject
    public DatabaseHelper(@Nullable Context context) {
        super(context, "MillionairesDB", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createQuestionsTable(db);
        createPartyGamesTable(db);
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
        db.close();
    }

    private void createPartyGamesTable(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE IF NOT EXISTS " + PARTY_GAMES + " (ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                WINNER + " TEXT NOT NULL, " +
                PARTY_GAME_DATE + " TEXT NOT NULL)";
        db.execSQL(createTableStatement);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("DatabaseHelper", "OnUpgrade invoked...");
    }
}
