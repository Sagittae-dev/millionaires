package com.example.milionerzy.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.milionerzy.database.DatabaseHelper;
import com.example.milionerzy.exceptions.DatabaseException;
import com.example.milionerzy.exceptions.PartyGameRepositoryException;
import com.example.milionerzy.model.PartyGameEntity;

import java.util.ArrayList;
import java.util.List;

public class PartyGameRepository extends DatabaseHelper {
    private static final String TAG = "PartyGameRepository";

    public PartyGameRepository(Context context) {
        super(context);
    }

    public void savePartyGame(PartyGameEntity partyGameEntity) throws PartyGameRepositoryException {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(WINNER, partyGameEntity.getWinner());
        cv.put(PARTY_GAME_DATE, partyGameEntity.getDate());

        long insert = db.insert(PARTY_GAMES, null, cv);
        if (insert == -1) {
            Log.i(TAG, "Exception in saving to database (insert == -1)");
            throw new PartyGameRepositoryException();
        }
        Log.i(TAG, "Party Game Added to database correctly");

        db.close();
    }


    public List<String> getAllPartyGames() throws DatabaseException {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + PARTY_GAMES;
        Cursor cursor = db.rawQuery(query, null);

        List<String> gamesList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                String gameHistoryItem = createStringValueWithHistoryItem(cursor);
                gamesList.add(gameHistoryItem);

            } while (cursor.moveToNext());
        } else {
            Log.i("PartyGameRepository", "cursor: " + cursor.toString() + " list is empty");
            throw new DatabaseException();
        }

        cursor.close();
        db.close();
        return gamesList;
    }

    private String createStringValueWithHistoryItem(Cursor cursor) {
        return "Winner: " +
                cursor.getString(1) +
                "; Date: " +
                cursor.getString(2);
    }
}
