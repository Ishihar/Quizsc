package com.example.a171y005.quizsc;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class Std extends AppCompatActivity {

    public class StdActivity extends AppCompatActivity {

        final String DB_Table_Name = "quiz_log";
        private String date;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor c;

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_std);
            TextView tv = (TextView) (findViewById(R.id.textView2));
            c = db.rawQuery("Select * from " + DB_Table_Name + ";", null);
            c.moveToFirst();
            date = c.getString(c.getColumnIndex("date"));
            tv.setText("date");
        }
    }
}