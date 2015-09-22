package com.summation;

/**
 * Created by Iwk0 on 22/09/2015.
 */
public class Constants {

    public static final String DATABASE_NAME = "Summation";
    public static final String CREATE_TABLE_QUERY = "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s INTEGER);";
    public static final String SELECT_TOP_TWENTY = "SELECT * FROM %s ORDER BY %s DESC, %s ASC LIMIT 20;";
    public static final int DATABASE_VERSION = 1;

    /*Column names*/
    public static final String TABLE_NAME = "Score";
    public static final String ID = "id";
    public static final String TIME = "time";
    public static final String NAME = "name";
    public static final String SUCCESSFUL_SUMMATION = "successful_summation";

    public static final String DIALOG = "dialog";
}