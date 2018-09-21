package com.example.a171y005.quizsc;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

public class LogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c;

        ListView lv_data = (ListView)findViewById(R.id.lv_data);
        ArrayList arrayList = new ArrayList<String>();

        c = db.rawQuery("Select * from quiz_log",null);
        c.moveToFirst();
        //arrayList.set(c.getString(c.getColumnIndex("date")));
        //lv_data.
        //c.close();
        //db.close();

    }
}
