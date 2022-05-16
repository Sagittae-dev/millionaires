package com.example.milionerzy.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.milionerzy.database.DatabaseHelper;
import com.example.milionerzy.exceptions.DatabaseException;
import com.example.milionerzy.model.Question;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class QuestionsRepository extends DatabaseHelper {
    private static final String TAG = "QuestionsRepository";

    @Inject
    public QuestionsRepository(Context context) {
        super(context);
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
                Log.i(TAG, "Exception in saving to database (-1)");
                throw new DatabaseException();
            }
            Log.i(TAG, "Question Added to database");
            db.close();
        } catch (Exception | DatabaseException e) {
            Log.i(TAG, "Exception in saving to database");
        }
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
            Log.i(TAG, "Exception when editing in Database");
            throw new DatabaseException();
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
            Log.i(TAG, "Exception in getting question");
            throw new DatabaseException();
        }
        Log.i(TAG, "Query Executed");
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

    public void deleteQuestion(int id) {
        try {
            String query = " DELETE FROM " + QUESTIONS + " WHERE ID = " + id;
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL(query);
            Log.i(TAG, "query executed");
            db.close();
        } catch (Exception e) {
            Log.i(TAG, "Problem with deleteQuestion method");
        }
    }

    public int getAmountOfQuestions() throws DatabaseException {
        String query = " SELECT COUNT(*) FROM " + QUESTIONS;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            int amount = cursor.getInt(0);
            cursor.close();
            db.close();
            return amount;
        } else {
            Log.i(TAG, "can't get amount of questions");
            throw new DatabaseException();
        }
    }
}
