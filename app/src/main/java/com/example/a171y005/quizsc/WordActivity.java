package com.example.a171y005.quizsc;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.a171y005.quizsc.R.id.Show_mean;
import static com.example.a171y005.quizsc.R.id.Show_word;

public class WordActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private ArrayList<HashMap<String,String>> list_data;
    private SimpleAdapter sim;
    private String title,search = "";
    private int pos;
    private HashMap<String,String> hashMap = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);
        ShowListView(search);
    }

    private void ShowListView(String sea_str) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        final ListView list;
        Cursor c;

        c = db.rawQuery("select Title,Ans from quiz_table where Title like '" + sea_str +"%' order by Title;",null);
        c.moveToFirst();
        list_data = new ArrayList<HashMap<String, String>>();
        sim = new SimpleAdapter(getApplicationContext(),list_data,R.layout.lv_layout,new String[]{"main","right"},new int[]{Show_word, Show_mean});
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
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if(v.getId() == R.id.listview){
            menu.setHeaderTitle("選択");
            menu.add(0,0,0,"削除");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        AlertDialog.Builder builder = new AlertDialog.Builder(WordActivity.this);
        int cnt = 0;
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
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main,menu);

        // searchViewを取得
        MenuItem searchItem = menu.findItem(R.id.search_menu_search_view);
        final SearchView searchv = (SearchView) searchItem.getActionView();
        searchv.setOnQueryTextListener(this);
        return true;
    }

  @Override
    public boolean onOptionsItemSelected(MenuItem item){

        LayoutInflater factory = LayoutInflater.from(this);
        final View edit_view = factory.inflate(R.layout.addlayout,null);
        AlertDialog.Builder dialog = new AlertDialog.Builder(WordActivity.this);
        dialog.setView(edit_view);
        dialog.setTitle("単語追加");


        dialog.setPositiveButton("追加", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText edit_Text = (EditText) edit_view.findViewById(R.id.EditWord);
                EditText edit_mean = (EditText) edit_view.findViewById(R.id.EditMean);
                String editw = edit_Text.getText().toString();
                String editm = edit_mean.getText().toString();
                add_mod(editw,editm);
                ShowListView("");
            }
        });
        dialog.show();


        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        ShowListView(newText);
        return true;
    }

    public void add_mod(String edit_w,String edit_m){
        DatabaseHelper dbHelper = new DatabaseHelper(WordActivity.this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c;
        AlertDialog.Builder builder = new AlertDialog.Builder(WordActivity.this);
        String message;

        c = db.rawQuery("Select count(*),Ans from quiz_table where Title = '" + edit_w + "';", null);
        c.moveToFirst();
        String check,anser = c.getString(c.getColumnIndex("Ans"));
        int count = Integer.parseInt(c.getString(c.getColumnIndex("count(*)")));

        if(edit_w.isEmpty() || edit_m.isEmpty()){
            Toast.makeText(WordActivity.this,"入力欄が空欄です。",Toast.LENGTH_LONG).show();
            return;
        }

        for(int i = 0; i < edit_w.length(); i++) {
            check = edit_w.substring(i, i + 1);
            if (check.matches("[^a-zA-z]")) {
                Toast.makeText(WordActivity.this, "アルファベット以外の文字が入力されています。", Toast.LENGTH_LONG).show();
                return;
            }
        }
        if (count >= 1) {
            message = edit_w + "は既に登録されています。" + "\n" + "登録内容:" + anser;
            builder.setMessage(message);
        } else {
            db.execSQL("Insert into quiz_table(Title,Ans,Clear) values('" + edit_w + "','" + edit_m + "',0);");
            builder.setMessage(edit_w + "\nデータベースに登録しました。");
        }
        builder.setTitle("単語登録");
        builder.setPositiveButton("OK", null);
        builder.show();
        c.close();
        db.close();
    }

    public boolean onKeyDown(int KeyCode, KeyEvent event){
        if(KeyCode != KeyEvent.KEYCODE_BACK){
            return super.onKeyDown(KeyCode,event);
        }
        else{
            Intent intent = new Intent(WordActivity.this,TitleActivity.class);
            startActivity(intent);
            return false;
        }
    }
}
