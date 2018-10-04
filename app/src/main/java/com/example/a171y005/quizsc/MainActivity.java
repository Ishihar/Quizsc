package com.example.a171y005.quizsc;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

    // カテゴリName（TableName)
    private String DB_TableName;
    // 問題の単語,答え
    private String Title,Anser;
    // カテゴリ別全件数,問題カウント,選択肢変数1~3,出題id変数
    private int Totalcount,cntQuestion = 0,SelectAns1,SelectAns2,SelectAns3,SelectQuestion;
    // 正解カウント変数
    private int c_cnt = 0;
    // 出題単語を取得するためのid配列
    private ArrayList<Integer> TitleSelection = new ArrayList<Integer>();
    // 選択肢を取得する為のid配列
    private ArrayList<Integer> ChoiceSelect = new ArrayList<Integer>();
    // ResultActivityへ送信するデータリスト
    private ArrayList<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 縦画面固定
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // DB呼び出し
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c;

        // TitleActivityからカテゴリ別テーブル名を取得
        DB_TableName = getIntent().getStringExtra("Table_NAME");

        // カテゴリ別タイトルの設定
        setTitle("英単語学習(" + set_Title(DB_TableName) + ")");

        // DBから全単語の数を取得
        c = db.rawQuery("select count(*) from " + DB_TableName, null);
        c.moveToFirst();

        // データの全件数をTotalcountに代入する
        Totalcount = Integer.parseInt(c.getString(c.getColumnIndex("count(*)")));
        Log.d("Main_Status",set_Title(DB_TableName) + "カテゴリの単語数は" + Totalcount + "です。");

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
        // 単語一覧から単語がDeleteされた際に出来る歯抜けidの抽出(2以上の歯抜けのみ）
        // DBに歯抜けが無いかチェック
        while(database_check());
        // 1が抜けていた場合の処理
        database_check1();
        // 学習開始
        setQuestion();
    }

    private void setQuestion(){
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c;

        // 出題用id配列から出題するidを抽出
        SelectQuestion = TitleSelection.get(cntQuestion);
        Log.d("Main_Status","出題IDは " + SelectQuestion + " です。");

        // cntQuestionが問題数を超えた場合タイトル画面へ戻る
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

        // 同じ選択肢が2つ発生しないように答え以外の3つの選択肢番号と出題番号が同じでないかチェック
        // 同じだった場合、4つ目のChoiceSelectデータを取得
        if(SelectQuestion == ChoiceSelect.get(0))
            SelectAns1 = ChoiceSelect.get(3);
        else if(SelectQuestion == ChoiceSelect.get(1))
            SelectAns2 = ChoiceSelect.get(3);
        else if(SelectQuestion == ChoiceSelect.get(2))
            SelectAns3 = ChoiceSelect.get(3);
        Log.d("Main_Status","3つの選択肢IDは " + SelectAns1 + "," + SelectAns2 + "," + SelectAns3 + " です。");

        // DBからTitle,Ansを取得し結果表示用配列listに代入
        Title = c.getString(c.getColumnIndex("Title"));
        Anser = c.getString(c.getColumnIndex("Ans"));
        list.add(Title);
        list.add(Anser);
        Log.d("Main_Status","出題される単語は " + Title + " です。その意味は " + Anser + " です。");
        Log.d("Main_Status"," ");

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

    // 引数の選択肢idからデータを抽出し返す関数
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

    // 選択肢（4つ）のいずれかを押されたとき
    public void onClick(final View v) {
        // 選択したボタンの取得
        final Button bt = (Button) v;

        // Enableする為のボタン取得
        final Button bt1 = (Button)(findViewById(R.id.button1));
        final Button bt2 = (Button)(findViewById(R.id.button2));
        final Button bt3 = (Button)(findViewById(R.id.button3));
        final Button bt4 = (Button)(findViewById(R.id.button4));

        // どれかボタンが押された場合1000ミリ秒Enableする（結果表示の為）
        bt1.setEnabled(false);bt2.setEnabled(false);bt3.setEnabled(false);bt4.setEnabled(false);

        // 選択したボタンの枠線を赤色表示
        bt.setBackgroundResource(R.drawable.change);

        // 〇×表示
        final ImageView iv = new ImageView(this);
        // XYを指定して表示
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(500,500);
        lp.leftMargin = 145;
        lp.topMargin = 3;

        Handler handle = new Handler();

        //  押されたボタンのテキストが答えと一致していた場合
        if (bt.getText().equals(Anser)) {
            bt.setBackgroundResource(R.drawable.change);
            list.add("〇");
            // 正解カウント
            c_cnt++;
            iv.setImageResource(R.drawable.outline_radio_button_unchecked_24);
            addContentView(iv,lp);
        }
        else{
            // 枠線を青色表示
            bt.setBackgroundResource(R.drawable.change2);
            list.add("×");
            iv.setImageResource(R.drawable.baseline_close_24);

            // 正解ボタンを赤色表示
            if(bt1.getText().equals(Anser)){
                bt1.setBackgroundResource(R.drawable.change);
            }
            else if(bt2.getText().equals(Anser)) {
                bt2.setBackgroundResource(R.drawable.change);
            }
            else if(bt3.getText().equals(Anser)){
                bt3.setBackgroundResource(R.drawable.change);
            }
            else if(bt4.getText().equals(Anser)){
                bt4.setBackgroundResource(R.drawable.change);
            }
            addContentView(iv,lp);
        }
        cntQuestion++;

        // 10問目が解き終わったとき
        if(cntQuestion == 10){

            // TableName（カテゴリName）を送信リストに追加
            list.add(DB_TableName);

            // 正解数を送信リストに追加
            list.add(String.valueOf(c_cnt));

            // 結果画面へ遷移
            Log.d("Main_Status","10問目が終了したので結果画面へ遷移します。");
            Intent intent = new Intent(MainActivity.this,ResultActivity.class);
            intent.putExtra("LIST",list);
            startActivity(intent);
        }

        else {
            // HandlerクラスからpostDelayedを呼び出し（1000ミリ秒スリープ後の処理)
            handle.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // 枠線表示を元に戻す
                    bt.setBackgroundResource(R.drawable.shape);
                    bt1.setBackgroundResource(R.drawable.shape);
                    bt2.setBackgroundResource(R.drawable.shape);
                    bt3.setBackgroundResource(R.drawable.shape);
                    bt4.setBackgroundResource(R.drawable.shape);
                    iv.setImageBitmap(null);
                    setQuestion();
                    bt1.setEnabled(true);bt2.setEnabled(true);bt3.setEnabled(true);bt4.setEnabled(true);
                }
            },1000);
        }
    }

    // バックキーが押された時の処理
    public boolean onKeyDown(int KeyCode, KeyEvent event) {
        // 確認ダイアログを表示
        new AlertDialog.Builder(MainActivity.this).setMessage("中断しますか？\n中断すると学習状況は記録されません。").setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("Main_Status","中断が選択されました。");
                Intent intent = new Intent(MainActivity.this, TitleActivity.class);
                startActivity(intent);
            }
        }).show();
        return false;
    }

    // 1の歯抜けの抽出関数
    public void database_check1(){
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c;
        int one_id;

        // 1の歯抜けの抽出
        c = db.rawQuery("Select min(_id) from " + DB_TableName + ";",null);
        c.moveToFirst();
        one_id = Integer.parseInt(c.getString(c.getColumnIndex("min(_id)")));

        // idの最小値が2だった場合(1がない場合)
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
        // 最小値id,最大値id
        int n_id,max_id;


        // 2以上の歯抜け番号の抽出
        c = db.rawQuery("select min(_id + 1) as id from " + DB_TableName + " where (_id + 1) not in (select _id from " + DB_TableName + ");",null);
        c.moveToFirst();
        n_id = Integer.parseInt(c.getString(c.getColumnIndex("id")));
        Log.d("Main_Status","歯抜けIDが抽出されました。IDは " + n_id + " です。");

        // id最大値の抽出
        c = db.rawQuery("select max(_id) as max_id from " + DB_TableName + ";",null);
        c.moveToFirst();
        max_id = Integer.parseInt(c.getString(c.getColumnIndex("max_id")));

        // 歯抜けが無かった場合falseを返す(繰り返し終了)
        if (n_id == max_id + 1){
            Log.d("Main_Status","歯抜けID処理終了");
            return false;
        }

        // id最大値を歯抜け番号にUpdateしtrueを返す
        c = db.rawQuery("update " + DB_TableName + " set _id = " + n_id + " where _id = "+ max_id + ";",null);
        c.moveToFirst();
        Log.d("Main_Status","最大値のID " + (max_id - 1) + " を " + n_id + " に置き換えます。");

        db.close();
        c.close();
        return true;
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
