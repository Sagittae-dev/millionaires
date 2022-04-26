package com.example.milionerzy.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.milionerzy.exceptions.DatabaseException;
import com.example.milionerzy.model.Question;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class DatabaseService extends SQLiteOpenHelper {

    private static final String QUESTIONS = "QUESTIONS";
    private static final String Q_CONTENT = "QCONTENT";
    private static final String ANSWER_A = "ANSWERA";
    private static final String ANSWER_B = "ANSWERB";
    private static final String ANSWER_C = "ANSWERC";
    private static final String ANSWER_D = "ANSWERD";
    private static final String CORRECT_ANSWER = "CORRECTANSWER";


    @Inject
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

    public void addQuestion(Question question) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(Q_CONTENT, question.getContentOfQuestion());
            cv.put(ANSWER_A, question.getAnswerA());
            cv.put(ANSWER_B, question.getAnswerB());
            cv.put(ANSWER_C, question.getAnswerC());
            cv.put(ANSWER_D, question.getAnswerD());
            cv.put(CORRECT_ANSWER, question.getCorrectAnswer());

            long insert = db.insert(QUESTIONS, null, cv);
            if (insert == -1) {
                Log.i("DatabaseException", "Exception in saving to database (-1)");
                throw new DatabaseException();
            }
            Log.i("Database", "Question Added to database");
        } catch (Exception | DatabaseException e) {
            Log.i("DatabaseException", "Exception in saving to database");
        }
    }

    public List<Question> getAllQuestions() throws DatabaseException {
        List<Question> listToReturn = new ArrayList<>();
        String query = "SELECT * FROM " + QUESTIONS;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Question question = createQuestionFromCursor(cursor);
                listToReturn.add(question);

            } while (cursor.moveToNext());
        } else throw new DatabaseException();
        cursor.close();
        db.close();
        return listToReturn;
    }

    public void deleteQuestion(int id) {
        try {
            String query = " DELETE FROM " + QUESTIONS + " WHERE ID = " + id;
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL(query);
            Log.i("DatabaseService", "query executed");
            db.close();
        } catch (Exception e) {
            Log.i("DatabaseService", "Problem with deleteQuestion method");
        }
    }

    public Question getQuestion(int id) throws DatabaseException {
        Question question;
        String query = " SELECT * FROM " + QUESTIONS + " WHERE ID = " + id;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            question = createQuestionFromCursor(cursor);
        } else {
            Log.i("DatabaseService", "Exception in getting question");
            throw new DatabaseException();
        }
        Log.i("DatabaseService", "Query Executed");
        cursor.close();
        db.close();

        return question;
    }

    private Question createQuestionFromCursor(Cursor cursor) {
        int id = cursor.getInt(0);
        String contentOfQuestion = cursor.getString(1);
        String answerA = cursor.getString(2);
        String answerB = cursor.getString(3);
        String answerC = cursor.getString(4);
        String answerD = cursor.getString(5);
        String correctAnswer = cursor.getString(6);

        return new Question(id, contentOfQuestion, answerA, answerB, answerC, answerD, correctAnswer);
    }

    public void editExistQuestion(int id, Question question) throws DatabaseException {
        try {
            String query = " UPDATE " + QUESTIONS + " SET "
                    + Q_CONTENT + " = '" + question.getContentOfQuestion() + "' , "
                    + ANSWER_A + " = '" + question.getAnswerA() + "' , "
                    + ANSWER_B + " = '" + question.getAnswerB() + "' , "
                    + ANSWER_C + " = '" + question.getAnswerC() + "' , "
                    + ANSWER_D + " = '" + question.getAnswerD() + "' , "
                    + CORRECT_ANSWER + " = '" + question.getCorrectAnswer() + "' "
                    + " WHERE ID = " + id;
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL(query);
            db.close();
        } catch (Exception e) {
            Log.i("DatabaseException", "Exception when editing in Database");
            throw new DatabaseException();
        }
    }
}
