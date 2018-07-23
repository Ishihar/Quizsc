package com.example.a171y005.quizsc;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private final String DB_TableName = "quiz_table";
    private String Title,Anser;                                  // 問題の単語
    private int Totalcount,cntQuestion = 0,SelectAns1,SelectAns2,SelectAns3,SelectQuestion;                              // 問題表示の為の変数
    private ArrayList<Integer> TitleSelection = new ArrayList<Integer>();
    private ArrayList<Integer> ChoiceSelect = new ArrayList<Integer>();
    private ArrayList<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        c.close();

        // Clear=0のデータの件数抽出
        c = db.rawQuery("Select count(*) from " + DB_TableName + " where Clear = 0",null);
        c.moveToFirst();
        int zeroclear = Integer.parseInt(c.getString(c.getColumnIndex("count(*)")));

        // 出題用id配列の作成（1~zeroclear)
        for(int i = 1; i <= zeroclear;i++) {
            TitleSelection.add(i);
        }
        //Log.d("DataCount","clear0cnt=" + zeroclear);

        // 出題用id配列のシャッフル
        Collections.shuffle(TitleSelection);
    }

    @Override
    protected void onResume(){             // Activityが表示された際に行う処理
        super.onResume();

        // DBに歯抜けが無いかチェック
        while(database_check());

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
        c = db.rawQuery("select * from " + DB_TableName + " where _id = " + SelectQuestion + " AND Clear = 0;" , null);
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


    public void onClick(View v) {
        if (((Button) v).getText().equals(Anser)) {     //  押されたボタンのテキストが答えと一致していた場合
            Toast.makeText(this,"正解",Toast.LENGTH_SHORT).show();
            list.add("正解");
        }
        else{
            Toast.makeText(this,"不正解",Toast.LENGTH_SHORT).show();
            list.add("不正解");
        }
        cntQuestion++;

        if(cntQuestion == 10){
            // 結果画面へ遷移
            Intent intent = new Intent(MainActivity.this,ResultActivity.class);
            intent.putExtra("LIST",list);

            startActivity(intent);
        }
        else
            setQuestion();
    }

    // バックキーが押された時の処理
    public boolean onKeyDown(int KeyCode, KeyEvent event){
        if(KeyCode != KeyEvent.KEYCODE_BACK){
            return super.onKeyDown(KeyCode,event);
        }
        else{
            Intent intent = new Intent(MainActivity.this,TitleActivity.class);
            startActivity(intent);
            return false;
        }
    }

    public boolean database_check(){
       DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c;
        int n_id,max_id;

        // 歯抜け番号の抽出
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
        db.close();
        c.close();

        return true;
    }
  /*  private void Clear_update() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c;
        c = db.rawQuery("Select _id,Clear from " + DB_TableName + " where _id = " + SelectQuestion,null);
        c.moveToFirst();

        try {
            db.execSQL("Update " + DB_TableName + " set Clear = 1 where _id = " + SelectQuestion  + ";");
        }
        catch (Exception e){
            Toast.makeText(this,"update失敗",Toast.LENGTH_SHORT).show();
        }
        finally {
            c.close();
            db.close();
        }
    }*/
}
