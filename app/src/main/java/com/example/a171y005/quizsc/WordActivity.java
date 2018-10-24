package com.example.a171y005.quizsc;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.a171y005.quizsc.R.id.Show_mean;
import static com.example.a171y005.quizsc.R.id.Show_word;

public class WordActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private ArrayList<HashMap<String,String>> list_data;
    private SimpleAdapter sim;
    private String title,search = "";
    private String DB_Table_Name = "quiz_table_B";
    private int pos;
    private HashMap<String,String> hashMap = new HashMap<String, String>();
    private GetCategoryName mGetCategoryName = new GetCategoryName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTitle("単語一覧(ビジネス)");

        // 単語リストの表示関数（引数はあいまい検索の為のキーワード)
        ShowListView(search);
    }

    // カテゴリ(tablename)を選択した際のリスト再表示関数
    private void ShowListView(String sea_str,String tablename) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        final ListView list;
        Cursor c;

        // Tableから単語とその意味をアルファベット順であいまい検索
        c = db.rawQuery("select Title,Ans from " + tablename + " where Title like '" + sea_str +"%' order by Title;",null);
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

    // 初期表示(ビジネス)用関数
    private void ShowListView(String sea_str) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        final ListView list;
        Cursor c;

        c = db.rawQuery("select Title,Ans from " + DB_Table_Name + " where Title like '" + sea_str +"%' order by Title;",null);
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
    // ListViewのメニュー作成
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if(v.getId() == R.id.listview){
            menu.add(0,0,0,"削除");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        // 削除ボタンが押されたとき
        // リストに登録されている「単語,意味」のセットから単語と意味の切り出し
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        AlertDialog.Builder builder = new AlertDialog.Builder(WordActivity.this);
        int cnt = 0;
        title = sim.getItem(info.position).toString();
        for (int i = 0; i < title.length(); i++) {
            String check = title.substring(i, i + 1);
            if (check.equals(",")) {
                break;
            }
            cnt++;
        }
        title = title.substring(6, cnt);
        pos = info.position;

        if (item.getItemId() == 0) {
            builder.setTitle("単語削除");
            builder.setMessage(title + "を削除します。");

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Cursor c_delete;
                    DatabaseHelper dbHelper = new DatabaseHelper(WordActivity.this);
                    SQLiteDatabase db = dbHelper.getReadableDatabase();

                    c_delete = db.rawQuery("Delete from " + DB_Table_Name + " where Title = '" + title + "';", null);
                    c_delete.moveToFirst();
                    list_data.remove(pos);
                    sim.notifyDataSetChanged();
                }
            }).setNegativeButton("キャンセル", null).show();

            return true;
        }
        return super.onContextItemSelected(item);
    }

    // Toolbarにmenufileを設定
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main,menu);
        getMenuInflater().inflate(R.menu.menulist,menu);

        // searchViewを取得
        MenuItem searchItem = menu.findItem(R.id.search_menu_search_view);
        final SearchView searchv = (SearchView) searchItem.getActionView();
        searchv.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //Toolbarのメニューが押されたとき
        switch (item.getItemId()) {
            // addボタンが押されたとき
            case R.id.action_settings:
                LayoutInflater factory = LayoutInflater.from(this);
                final View edit_view = factory.inflate(R.layout.addlayout, null);
                AlertDialog.Builder dialog = new AlertDialog.Builder(WordActivity.this);
                dialog.setView(edit_view);
                dialog.setTitle("単語追加");

                // 単語追加ダイアログ内の設定
                dialog.setPositiveButton("追加", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // カテゴリ選択用のSpinner
                        Spinner spinner = (Spinner) edit_view.findViewById(R.id.spinner2);
                        String catename = spinner.getSelectedItem().toString();

                        // 単語とその意味の入力欄
                        EditText edit_Text = (EditText) edit_view.findViewById(R.id.EditWord);
                        EditText edit_mean = (EditText) edit_view.findViewById(R.id.EditMean);
                        String editw = edit_Text.getText().toString();
                        String editm = edit_mean.getText().toString();

                        // 3つのデータを引き渡し
                        add_mod(editw, editm,catename);
                    }
                });
                dialog.show();

                // Spinnerからビジネスを選択
            case R.id.item2:
                setTitle("単語一覧(ビジネス)");
                // テーブル名の設定
                DB_Table_Name = "quiz_table_B";
                // カテゴリを指定してリストを再表示
                ShowListView(search,DB_Table_Name);
                break;

            // Spinnerから生活を選択
            case R.id.item3:
                setTitle("単語一覧(生活)");
                DB_Table_Name = "quiz_table_L";
                ShowListView(search,DB_Table_Name);
                break;
            // Spinnerから動物を選択
            case R.id.item4:
                setTitle("単語一覧(動物)");
                DB_Table_Name = "quiz_table_A";
                ShowListView(search,DB_Table_Name);
                break;
            // Spinnerから宇宙を選択
            case R.id.item5:
                setTitle("単語一覧(宇宙)");
                DB_Table_Name = "quiz_table_C";
                ShowListView(search,DB_Table_Name);
                break;
            // Spinnerから食べ物を選択
            case R.id.item6:
                setTitle("単語一覧(食べ物)");
                DB_Table_Name = "quiz_table_F";
                ShowListView(search,DB_Table_Name);
                break;
            // SpinnerからITを選択
            case R.id.item7:
                setTitle("IT");
                DB_Table_Name = "quiz_table_IT";
                ShowListView(search,DB_Table_Name);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    // 検索欄にテキストが入力されたとき
    public boolean onQueryTextChange(String newText) {
        // 入力されたテキストを検索ワードとしてリスト表示
        ShowListView(newText,DB_Table_Name);
        return true;
    }

    // 単語追加用関数(引数は単語,意味,カテゴリ)
    public void add_mod(String edit_w,String edit_m,String catename){
        DatabaseHelper dbHelper = new DatabaseHelper(WordActivity.this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c;
        AlertDialog.Builder builder = new AlertDialog.Builder(WordActivity.this);
        String message,tablename = "";

        // 入力欄が空欄だった場合
        if(edit_w.isEmpty() || edit_m.isEmpty()){
            Toast.makeText(WordActivity.this,"入力欄が空欄です。",Toast.LENGTH_LONG).show();
            return;
        }

        // 選択されたカテゴリからテーブル名を取得
        tablename = mGetCategoryName.getTable(catename);

        // 追加される単語が選択されたカテゴリに既に登録されているかチェック
        c = db.rawQuery("Select count(*),Ans from " + tablename + " where Title = '" + edit_w + "';", null);
        c.moveToFirst();
        String check,anser = c.getString(c.getColumnIndex("Ans"));
        int count = Integer.parseInt(c.getString(c.getColumnIndex("count(*)")));

        // 単語にアルファベット以外の文字が入力されている場合
        // i=0番目から単語の長さまで1文字ずつ検査
        for(int i = 0; i < edit_w.length(); i++) {
            check = edit_w.substring(i, i + 1);

            // アルファベット以外の文字が検出された場合
            if (check.matches("[^a-zA-z]")) {
                Toast.makeText(WordActivity.this, "アルファベット以外の文字が入力されています。", Toast.LENGTH_LONG).show();
                return;
            }
        }

        // 追加される単語をDBで検索をかけデータ件数が1件以上出力された場合
        if (count >= 1) {
            message = edit_w + "は既に登録されています。" + "\n" + "登録内容:" + anser;
            builder.setMessage(message);
        }
        else {
            db.execSQL("Insert into " + tablename + " (Title,Ans) values('" + edit_w + "','" + edit_m + "');");
            builder.setMessage(edit_w + "\n" + catename + "カテゴリに登録しました。");
        }
        builder.setTitle("単語登録");
        builder.setPositiveButton("OK", null);
        builder.show();
        c.close();
        db.close();
    }

    // バックキーが押された時の処理（タイトルへ戻る）
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
