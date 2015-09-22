package com.summation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {

    public Database(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableLoans =
                String.format(Constants.CREATE_TABLE_QUERY,
                        Constants.TABLE_NAME,
                        Constants.ID,
                        Constants.TIME,
                        Constants.NAME,
                        Constants.SUCCESSFUL_SUMMATION);
        sqLiteDatabase.execSQL(createTableLoans);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Constants.DATABASE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void insertScore(Score score) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Constants.TIME, score.time);
        values.put(Constants.NAME, score.name);
        values.put(Constants.SUCCESSFUL_SUMMATION, score.countSuccessfulSummation);

        db.insert(Constants.TABLE_NAME, null, values);
        db.close();
    }

    public List<Score> getTopTwenty() {
        Cursor cursor =
                getWritableDatabase().rawQuery(
                        String.format(Constants.SELECT_TOP_TWENTY,
                                Constants.TABLE_NAME,
                                Constants.SUCCESSFUL_SUMMATION,
                                Constants.TIME), new String[] {});

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