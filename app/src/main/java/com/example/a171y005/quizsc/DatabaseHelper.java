package com.example.a171y005.quizsc;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 171y005 on 2018/06/13.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context){
        super(context,"Eng.db",null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
                "Create table quiz_table_B("
                        + "_id integer primary key autoincrement not null, "
                        + "Title text ,"
                        + "Ans text"
                        + ");");

        db.execSQL(
                "Create table quiz_table_L("
                        + "_id integer primary key autoincrement not null, "
                        + "Title text ,"
                        + "Ans text"
                        + ");");

        db.execSQL(
                "Create table quiz_table_A("
                        + "_id integer primary key autoincrement not null, "
                        + "Title text ,"
                        + "Ans text"
                        + ");");

        db.execSQL(
                "Create table quiz_table_C("
                        + "_id integer primary key autoincrement not null, "
                        + "Title text ,"
                        + "Ans text"
                        + ");");

        db.execSQL(
                "Create table quiz_table_F("
                        + "_id integer primary key autoincrement not null, "
                        + "Title text ,"
                        + "Ans text"
                        + ");");


        db.execSQL(
                "Create table quiz_log("
                        + "_id integer primary key autoincrement not null,"
                        + "date text,"
                        + "ans double);");



    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }
}
