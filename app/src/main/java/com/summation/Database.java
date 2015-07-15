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
    private static final int DATABASE_VERSION = 1;

    /*Column names*/
    private static final String TABLE_NAME = "Score";
    private static final String ID = "id";
    private static final String TIME = "time";
    private static final String NAME = "name";
    private static final String SUCCESSFUL_SUMMATION = "successful_summation";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableLoans =
                String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s INTEGER);",
                TABLE_NAME,
                ID,
                TIME,
                NAME,
                SUCCESSFUL_SUMMATION);
        sqLiteDatabase.execSQL(createTableLoans);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void insertScore(Score score) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TIME, score.time);
        values.put(NAME, score.name);
        values.put(SUCCESSFUL_SUMMATION, score.countSuccessfulSummation);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public List<Score> getTopTwenty() {
        Cursor cursor =
                getWritableDatabase().rawQuery(String.format("SELECT * FROM %s ORDER BY %s DESC, %s ASC LIMIT 20;",
                TABLE_NAME, SUCCESSFUL_SUMMATION, TIME), new String[]{});

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