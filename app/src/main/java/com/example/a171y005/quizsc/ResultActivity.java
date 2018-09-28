package com.example.a171y005.quizsc;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class ResultActivity extends AppCompatActivity {

    private ArrayList<HashMap<String,String>> list_data;
    private SimpleAdapter sim;


    ArrayList<String> list = new ArrayList<>();     // MainActivityの学習結果を保存するlist
    // list.get(0) ～ list.get(29)... 英単語・意味・正解不正解の3つ1セットのデータが10個
    // list.get(30)...カテゴリ名（テーブル名）
    // list.get(31)...問題正解数
    ArrayList<String> title = new ArrayList<>();    // listから英単語データのみを取得するlist
    ArrayList<String> ans = new ArrayList<>();      // listから意味データのみを取得するlist
    ArrayList<String> res = new ArrayList<>();      // listから正解か否かを取得するlist

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTitle("学習結果");

        // MainActivityからの送信リストを受け取り
        list = getIntent().getStringArrayListExtra("LIST");
        int cnt = 0;
        final ListView listT;

        // カテゴリ名表示
        TextView tv_cate = (TextView)findViewById(R.id.tv_cate);
        tv_cate.setText("カテゴリ: " + set_Title(list.get(30)));

        // 正解数の表示
        TextView tv_ccnt = (TextView)findViewById(R.id.tv_ccnt);
        if(Integer.parseInt(list.get(31)) == 0) {
            tv_ccnt.setText("全問不正解");
        }
        else if(Integer.parseInt(list.get(31)) == 10) {
            tv_ccnt.setText("全問正解！");
        }
        else
            tv_ccnt.setText("１０問中 " + list.get(31) + " 問正解！");
        HashMap<String, String> hashMap = new HashMap<String, String>();

        // list0+i番目...単語データ 1+i番目...意味データ 2+i番目...〇or× をそれぞれ追加
        for (int i = 0; i < 29; i += 3) {
            title.add(list.get(i));
            ans.add(list.get(i + 1));
            res.add(list.get(i + 2));
            cnt++;
        }

        list_data = new ArrayList<HashMap<String, String>>();


        // ListViewに表示するためのAdapterにセット
        sim = new SimpleAdapter(getApplicationContext(), list_data, R.layout.result, new String[]{"left", "center", "right"}, new int[]{R.id.textTitle, R.id.textAns, R.id.textRes});

        for (int i = 0; i < cnt; i++) {
            hashMap.put("left", title.get(i));
            hashMap.put("center", ans.get(i));
            hashMap.put("right", res.get(i));
            list_data.add(new HashMap<String, String>(hashMap));
            hashMap.clear();
        }
        listT = (ListView) findViewById(R.id.listtitle);
        listT.setAdapter(sim);

        // タイトル画面へ戻るボタン
        final Button bt_returnTitle = (Button) findViewById(R.id.bt_returnTitle);
        bt_returnTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this, TitleActivity.class);
                startActivity(intent);
            }
        });

        // 次の問題へボタン
        final Button bt_next = (Button) findViewById(R.id.bt_next);
        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                // list30番目...TableNameデータを再びMainActivityへ送信
                intent.putExtra("Table_NAME",list.get(30));
                startActivity(intent);
            }
        });

        // LogActivityへ学習結果を送信
        Insert_data(list.get(31),set_Title(list.get(30)));

    }

    private void Insert_data(String int_cnt,String catename) {
        double c_cnt = Double.parseDouble(int_cnt);
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.execSQL("Insert into quiz_log(Date,cnt,Category) values((Select current_timestamp)," + c_cnt + ",'" + catename + "');");
        db.close();
    }

    public boolean onKeyDown(int KeyCode, KeyEvent event){
        if(KeyCode != KeyEvent.KEYCODE_BACK){
            return super.onKeyDown(KeyCode,event);
        }
        else{
            Intent intent = new Intent(ResultActivity.this,TitleActivity.class);
            startActivity(intent);
            return false;
        }
    }

    // タイトルをカテゴリ別に設定
    private String set_Title(String db_tableName) {
        switch (db_tableName){
            case "quiz_table_B":
                return "ビジネス";
            case "quiz_table_L":
                return "生活";
            case "quiz_table_A":
                return "動物";
            case "quiz_table_C":
                return "宇宙";
            case "quiz_table_F":
                return "食べ物";
        }
        return "選択なし";
    }

}