package com.example.milionerzy.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.milionerzy.model.Question;

public class DatabaseService extends SQLiteOpenHelper {

    private static final String QUESTIONS = "QUESTIONS";
    private static final String Q_CONTENT = "QCONTENT";
    private static final String ANSWER_A = "ANSWERA";
    private static final String ANSWER_B = "ANSWERB";
    private static final String ANSWER_C = "ANSWERC";
    private static final String ANSWER_D = "ANSWERD";
    private static final String CORRECT_ANSWER = "CORRECTANSWER";

    public DatabaseService(@Nullable Context context) {
        super(context, "QuestionsDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
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

    public void addQuestion(Question question){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(Q_CONTENT, question.getContentOfQuestion());
            cv.put(ANSWER_A, question.getAnswerA());
            cv.put(ANSWER_B, question.getAnswerB());
            cv.put(ANSWER_C, question.getAnswerC());
            cv.put(ANSWER_D, question.getAnswerD());
            cv.put(CORRECT_ANSWER, question.getCorrectAnswer());

            long insert = db.insert(QUESTIONS, null, cv);
            if (insert == -1){
                Log.i("DatabaseException","Exception in saving to database (-1)");
                throw new DatabaseException();
            }
            Log.i("Database", "Question Added to database");
        } catch (Exception | DatabaseException e){
            Log.i("DatabaseException","Exception in saving to database");
        }
    }
}
