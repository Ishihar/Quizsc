package com.example.a171y005.quizsc;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.a171y005.quizsc.R.id.word;

public class WordActivity extends AppCompatActivity {

    private ArrayList<HashMap<String,String>> list_data;
    private SimpleAdapter sim;
    private String title;
    private int pos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c;
        HashMap<String,String> hashMap = new HashMap<String, String>();
        final ListView list;

        c = db.rawQuery("Select Title,Ans from quiz_table order by Title;",null);
        c.moveToFirst();
        list_data = new ArrayList<HashMap<String, String>>();
        sim = new SimpleAdapter(getApplicationContext(),list_data,R.layout.lv_layout,new String[]{"main","right"},new int[]{word, R.id.mean});
        for(int i = 0; i < c.getCount(); i++) {
            hashMap.put("main",c.getString(c.getColumnIndex("Title")));
            hashMap.put("right",c.getString(c.getColumnIndex("Ans")));
            list_data.add(new HashMap<String, String>(hashMap));
            hashMap.clear();
            c.moveToNext();
        }

        list = (ListView)findViewById(R.id.listview);
        list.setAdapter(sim);
        registerForContextMenu(list);

        c.close();
        db.close();

        /*list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

                c.close();
                db.close();
            }
        });*/
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if(v.getId() == R.id.listview){
            menu.setHeaderTitle("選択");
            menu.add(0,0,0,"削除");
            menu.add(0,1,0,"修正");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        AlertDialog.Builder builder = new AlertDialog.Builder(WordActivity.this);
        int cnt = 0;
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c;
        title = sim.getItem(info.position).toString();
        for(int i = 0; i < title.length(); i++){
            String check = title.substring(i,i+1);
            if(check.equals(",")){
                break;
            }
            cnt++;
        }
        title = title.substring(6,cnt);
        pos = info.position;

        Log.d("test","=" + title);
        if(item.getItemId() == 0){
            builder.setTitle("単語削除");
            builder.setMessage(title + "を削除します。");

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Cursor c_delete;
                    DatabaseHelper dbHelper = new DatabaseHelper(WordActivity.this);
                    SQLiteDatabase db = dbHelper.getReadableDatabase();

                    c_delete = db.rawQuery("Delete from quiz_table where Title = '" + title + "';", null);
                    c_delete.moveToFirst();
                    list_data.remove(pos);
                    sim.notifyDataSetChanged();
                }
            }).setNegativeButton("キャンセル",null).show();

            return true;
        }
        else if(item.getItemId() == 1){
            Log.d("test","修正");
        }


        return super.onContextItemSelected(item);
    }
}
