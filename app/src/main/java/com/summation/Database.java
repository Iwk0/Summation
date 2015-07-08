package com.summation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Summation";
    private static final int DATABASE_VERSION = 8;

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableLoans = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s INTEGER);",
                "Score",
                "id",
                "time",
                "name",
                "successful_summation");
        sqLiteDatabase.execSQL(createTableLoans);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Score");
        onCreate(sqLiteDatabase);
    }

    public void insertScore(Score score) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("time", score.time);
        values.put("name", score.name);
        values.put("successful_summation", score.countSuccessfulSummation);

        db.insert("Score", null, values);
        db.close();
    }

    public List<Score> getTopTwenty() {
        Cursor cursor = getWritableDatabase().rawQuery(String.format("SELECT * FROM %s ORDER BY %s DESC, %s ASC LIMIT 20;",
                "Score", "successful_summation", "time"), new String[]{});

        List<Score> scores = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Score score = new Score();
                score.id = cursor.getInt(0);
                score.time = cursor.getString(1);
                score.name = cursor.getString(2);
                score.countSuccessfulSummation = cursor.getInt(3);
                scores.add(score);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return scores;
    }
}