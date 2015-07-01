package com.summation;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by imishev on 1.7.2015 г..
 */
public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Summation";
    private static final int DATABASE_VERSION = 1;

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    /*@Override
    public void onCreate(SQLiteDatabase database) {
        String createTableLoans = String.format(Constants.TABLE_LOAN,
                Constants.TABLE_NAME_LOAN,
                Constants.ID,
                Constants.CONTENT,
                Constants.TYPE,
                Constants.CREATED_AT);

        String createTableSettings = String.format(Constants.TABLE_SETTINGS,
                Constants.TABLE_NAME_SETTINGS,
                Constants.ID,
                Constants.URL,
                Constants.USER_ID,
                Constants.USERNAME);

        database.execSQL(createTableLoans);
        database.execSQL(createTableSettings);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME_LOAN);
        database.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME_SETTINGS);
        onCreate(database);
    }

    public void insertLoan(String loan, int type) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Constants.CONTENT, loan);
        values.put(Constants.TYPE, type);
        values.put(Constants.CREATED_AT, new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault()).format(new Date()));

        db.delete(Constants.TABLE_NAME_LOAN, Constants.TYPE + "=" + type, null);
        db.insert(Constants.TABLE_NAME_LOAN, null, values);
        db.close();
    }*/
}