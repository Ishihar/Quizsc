package com.example.a171y005.quizsc;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class WordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);

        final ListView list;
        ArrayAdapter listadapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c;

        c = db.rawQuery("Select Title,Ans from quiz_table order by Title;",null);
        c.moveToFirst();
        for(int i = 0; i < c.getCount(); i++) {
            listadapter.add(c.getString(c.getColumnIndex("Title")));
            c.moveToNext();
        }
        list = (ListView)findViewById(R.id.listview1);
        list.setAdapter(listadapter);

        /*list.setOnClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView listView = (ListView)parent;
                String item = (String) listView.getItemAtPosition(position);
                Toast.makeText(WordActivity.this,item,Toast.LENGTH_LONG).show();
            }
        });*/
    }
}
