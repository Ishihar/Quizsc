package com.example.a171y005.quizsc;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.a171y005.quizsc.R.id.word;

public class WordActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate();
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c;
        ArrayList<HashMap<String,String>> list_data = new ArrayList<HashMap<String, String>>();
        HashMap<String,String> hashMap = new HashMap<String, String>();

        final AlertDialog.Builder builder = new AlertDialog.Builder(WordActivity.this);

        final ListView list;
        //ArrayAdapter listadapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);

        c = db.rawQuery("Select Title,Ans from quiz_table order by Title;",null);
        c.moveToFirst();
        for(int i = 0; i < c.getCount(); i++) {
            hashMap.put("main",c.getString(c.getColumnIndex("Title")));
            hashMap.put("right",c.getString(c.getColumnIndex("Ans")));
            list_data.add(new HashMap<String, String>(hashMap));
            hashMap.clear();
            //listadapter.add(c.getString(c.getColumnIndex("Title")));
            c.moveToNext();
        }
        SimpleAdapter sim = new SimpleAdapter(getApplicationContext(),list_data,R.layout.lv_layout,new String[]{"main","right"},new int[]{word, R.id.mean});
        list = (ListView)findViewById(R.id.listview);
        list.setAdapter(sim);

        c.close();

        db.close();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int cnt = 0;
                ListView listView = (ListView) parent;
                Object item = ((ListView) parent).getItemAtPosition(position);
                String word1 = item.toString();
                for(int i = 0; i < word1.length(); i++){
                    String check = word1.substring(i,i+1);
                    if(check.equals(",")){
                        break;
                    }
                    cnt++;
                }

                word1 = word1.substring(6,cnt);

                //((String) listView.getItemAtPosition(position));

                DatabaseHelper dbHelper = new DatabaseHelper(WordActivity.this);
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Cursor c;
                c = db.rawQuery("Select Title,Ans from quiz_table where Title = '" + word1 + "';",null);
                c.moveToFirst();

               Log.d("sql","word=" + word1);
                builder.setTitle("単語管理");
                builder.setPositiveButton("OK",null);
                builder.setMessage("内容: " + c.getString(c.getColumnIndex("Title")) + "\n意味: " + c.getString(c.getColumnIndex("Ans")));
                builder.show();

                c.close();
                db.close();
            }
        });
    }
}
