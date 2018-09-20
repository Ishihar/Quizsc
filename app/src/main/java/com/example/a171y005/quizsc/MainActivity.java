package com.example.a171y005.quizsc;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private String DB_TableName;
    private String Title,Anser;                                  // 問題の単語
    private int Totalcount,cntQuestion = 0,SelectAns1,SelectAns2,SelectAns3,SelectQuestion,AnserNo;
    private ArrayList<Integer> TitleSelection = new ArrayList<Integer>();
    private ArrayList<Integer> ChoiceSelect = new ArrayList<Integer>();
    private ArrayList<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DB_TableName = getIntent().getStringExtra("DB_NAME");
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c;

        // DBから全単語の数を取得
        c = db.rawQuery("select count(*) from " + DB_TableName, null);
        c.moveToFirst();

        // データの全件数をTotalcountに代入する
        Totalcount = Integer.parseInt(c.getString(c.getColumnIndex("count(*)")));

        // 選択肢id配列の作成（1~全件数）
        for(int i = 1; i <= Totalcount; i++){
            ChoiceSelect.add(i);
        }
        // 出題用id配列の作成
        for(int i = 1; i <= Totalcount; i++){
            TitleSelection.add(i);
        }

        c.close();

        // 出題用id配列のシャッフル
        Collections.shuffle(TitleSelection);
    }

    @Override
    protected void onResume(){             // Activityが表示された際に行う処理
        super.onResume();

        // DBに歯抜けが無いかチェック
        while(database_check());
        database_check1();
        setQuestion();
    }

    private void setQuestion(){
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c;

        // 出題用id配列から出題するidを抽出
        SelectQuestion = TitleSelection.get(cntQuestion);

        // cntQuestionが問題数を超えた場合
        if(Totalcount <= cntQuestion + 1){
            Toast.makeText(this,"問題終了",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this,TitleActivity.class);
            startActivity(intent);
        }
        // SelectQuestionに該当する問題とその答えを出力
        c = db.rawQuery("select * from " + DB_TableName + " where _id = " + SelectQuestion + ";" , null);
        c.moveToFirst();

        // 選択肢用id配列のシャッフル
        Collections.shuffle(ChoiceSelect);

        // 3つの選択肢番号(id)の抽出
        SelectAns1 = ChoiceSelect.get(0);
        SelectAns2 = ChoiceSelect.get(1);
        SelectAns3 = ChoiceSelect.get(2);

        // 3つの選択肢番号と出題番号が同じでないかチェック
        if(SelectQuestion == ChoiceSelect.get(0))
            SelectAns1 = ChoiceSelect.get(3);
        else if(SelectQuestion == ChoiceSelect.get(1))
            SelectAns2 = ChoiceSelect.get(3);
        else if(SelectQuestion == ChoiceSelect.get(2))
            SelectAns3 = ChoiceSelect.get(3);

        // DBからTitle,Ansを取得し結果表示用配列listに代入
        Title = c.getString(c.getColumnIndex("Title"));
        Anser = c.getString(c.getColumnIndex("Ans"));
        list.add(Title);
        list.add(Anser);

        // ボタン表示配列の作成
        ArrayList<String> RandomChoice = new ArrayList<String>();
        RandomChoice.add(c.getString(c.getColumnIndex("Ans")));
        RandomChoice.add(getChoice(SelectAns1));
        RandomChoice.add(getChoice(SelectAns2));
        RandomChoice.add(getChoice(SelectAns3));
        Collections.shuffle(RandomChoice);

        // ボタンに選択肢をセット
        ((TextView)findViewById(R.id.textQuestion_Res)).setText(cntQuestion + 1 + " / 10");
        ((TextView)findViewById(R.id.textQuestion)).setText(Title);
        ((Button)findViewById(R.id.button1)).setText(RandomChoice.get(0));
        ((Button)findViewById(R.id.button2)).setText(RandomChoice.get(1));
        ((Button)findViewById(R.id.button3)).setText(RandomChoice.get(2));
        ((Button)findViewById(R.id.button4)).setText(RandomChoice.get(3));

        c.close();
        db.close();
    }

    // 引数の選択肢idからデータを抽出する
    private String getChoice(int SelectAns) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c;

        c = db.rawQuery("select Ans from " + DB_TableName + " where _id =" + SelectAns, null);
        c.moveToFirst();

        String get_Choice = c.getString(c.getColumnIndex("Ans"));

        c.close();
        db.close();

        return get_Choice;
    }


    public void onClick(final View v) {
        // 選択したボタンの取得
        final Button bt = (Button) v;

        // Enableする為のボタン取得
        final Button bt1 = (Button)(findViewById(R.id.button1));
        final Button bt2 = (Button)(findViewById(R.id.button2));
        final Button bt3 = (Button)(findViewById(R.id.button3));
        final Button bt4 = (Button)(findViewById(R.id.button4));

        // どれかボタンが押された場合1000ミリ秒Enableする
        bt1.setEnabled(false);bt2.setEnabled(false);bt3.setEnabled(false);bt4.setEnabled(false);

        // 選択したボタンの枠線を赤色表示
        bt.setBackgroundResource(R.drawable.change);

        // 〇×表示
        final ImageView iv = new ImageView(this);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(600,600);
        lp.leftMargin = 300;
        lp.topMargin = 120;

        Handler handle = new Handler();
        if (bt.getText().equals(Anser)) {     //  押されたボタンのテキストが答えと一致していた場合
            bt.setBackgroundResource(R.drawable.change);
            list.add("〇");
            iv.setImageResource(R.drawable.outline_radio_button_unchecked_24);
            addContentView(iv,lp);
        }
        else{
            // 枠線を青色表示
            bt.setBackgroundResource(R.drawable.change2);
            list.add("×");
            iv.setImageResource(R.drawable.baseline_close_24);
            addContentView(iv,lp);
        }
        cntQuestion++;
        if(cntQuestion == 10){
            // 結果画面へ遷移
            Intent intent = new Intent(MainActivity.this,ResultActivity.class);
            intent.putExtra("LIST",list);
            startActivity(intent);
        }

        else {
            // HandlerクラスからpostDelayedを呼び出し
            handle.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // 枠線表示を元に戻す
                    bt.setBackgroundResource(R.drawable.shape);
                    iv.setImageBitmap(null);
                    setQuestion();
                    bt1.setEnabled(true);bt2.setEnabled(true);bt3.setEnabled(true);bt4.setEnabled(true);
                }
            },1000);
        }
    }

    // バックキーが押された時の処理
    public boolean onKeyDown(int KeyCode, KeyEvent event) {
        new AlertDialog.Builder(MainActivity.this).setMessage("中断しますか？").setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MainActivity.this, TitleActivity.class);
                startActivity(intent);
            }
        }).show();
        return false;
    }

    public void database_check1(){
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c;
        int one_id;

        // 1の歯抜けの抽出
        c = db.rawQuery("Select min(_id) from " + DB_TableName + ";",null);
        c.moveToFirst();
        one_id = Integer.parseInt(c.getString(c.getColumnIndex("min(_id)")));
        if(one_id == 2){
            c = db.rawQuery("Select max(_id) from " + DB_TableName + ";",null);
            c.moveToFirst();
            int o_max = Integer.parseInt(c.getString(c.getColumnIndex("max(_id)")));
            c = db.rawQuery("update " + DB_TableName + " set _id = 1 where _id = " + o_max + ";",null);
            c.moveToFirst();
        }
        else {
            return;
        }
    }

    public boolean database_check(){
       DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c;
        int one_id,n_id,max_id;


        // 2以上の歯抜け番号の抽出
        c = db.rawQuery("select min(_id + 1) as id from " + DB_TableName + " where (_id + 1) not in (select _id from " + DB_TableName + ");",null);
        c.moveToFirst();
        n_id = Integer.parseInt(c.getString(c.getColumnIndex("id")));

        // id最大値の抽出
        c = db.rawQuery("select max(_id) as max_id from " + DB_TableName + ";",null);
        c.moveToFirst();
        max_id = Integer.parseInt(c.getString(c.getColumnIndex("max_id")));

        // 歯抜けが無かった場合falseを返す
        if (n_id == max_id + 1){
            return false;
        }

        // id最大値を歯抜け番号にUpdate
        c = db.rawQuery("update " + DB_TableName + " set _id = " + n_id + " where _id = "+ max_id + ";",null);
        c.moveToFirst();

        /*c = db.rawQuery("Select _id from " + DB_TableName + ";",null);
        c.moveToFirst();
        int id = 0;
        while(id >= 0){
            id = Integer.parseInt(c.getString(c.getColumnIndex("_id")));
            Log.d("FILE", String.valueOf(id));
            c.moveToNext();
        }*/
        db.close();
        c.close();


        return true;
    }
}
