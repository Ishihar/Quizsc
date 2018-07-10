package com.example.a171y005.quizsc;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private final String DB_TableName = "quiz_table";
    private String Ans0,Ans1,Ans2,Ans3,Anser;           // Ans0~3...選択肢（4つ）Anser...問題の答え（判定用）
    private String Title;                                  // 問題の単語
    private int Totalcount,cntQuestion = 0,SelectAns1,SelectAns2,SelectAns3,SelectQuestion;                              // 問題表示の為の変数
    private ArrayList<Integer> TitleSelection = new ArrayList<Integer>();
    private ArrayList<Integer> ChoiceSelect = new ArrayList<Integer>();
    ArrayList<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c;
        c = db.rawQuery("select count(*) from " + DB_TableName, null);   // DBから全問題の数を取得
        c.moveToFirst();
        Totalcount = Integer.parseInt(c.getString(c.getColumnIndex("count(*)")));    // データの件数をcountに代入する
        Log.d("DataCount","clear0cnt=" + c.getString(c.getColumnIndex("count(*)")));

        for(int i = 1; i <= Totalcount; i++){
            ChoiceSelect.add(i);
        }
        c = db.rawQuery("Select count(*) from " + DB_TableName + " where Clear = 0",null);
        c.moveToFirst();
        int zeroclear = Integer.parseInt(c.getString(c.getColumnIndex("count(*)")));

        for(int i = 1; i <= zeroclear;i++) {
            TitleSelection.add(i);
        }
        Log.d("DataCount","clear0cnt=" + zeroclear);
        Collections.shuffle(TitleSelection);
    }

    @Override
    protected void onResume(){             // Activityが表示された際に行う処理
        super.onResume();
        setQuestion();
    }

    private void setQuestion(){
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c;

        SelectQuestion = TitleSelection.get(cntQuestion);
        if(Totalcount <= cntQuestion + 1){        // cntQuestionが問題数を超えた場合
            Toast.makeText(this,"問題終了",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this,TitleActivity.class);
            startActivity(intent);
        }
        c = db.rawQuery("select * from " + DB_TableName + " where _id = " + SelectQuestion + " AND Clear = 0;" , null);
        c.moveToFirst();

        Collections.shuffle(ChoiceSelect);
        SelectAns1 = ChoiceSelect.get(0);
        SelectAns2 = ChoiceSelect.get(1);
        SelectAns3 = ChoiceSelect.get(2);

        if(SelectQuestion == ChoiceSelect.get(0))
            SelectAns1 = ChoiceSelect.get(3);
        else if(SelectQuestion == ChoiceSelect.get(1))
            SelectAns2 = ChoiceSelect.get(3);
        else if(SelectQuestion == ChoiceSelect.get(2))
            SelectAns3 = ChoiceSelect.get(3);

        Title = c.getString(c.getColumnIndex("Title"));     //  DBからTitleを取得
        Anser = c.getString(c.getColumnIndex("Ans"));
        list.add(Title);
        list.add(Anser);

        ArrayList<String> RandomChoice = new ArrayList<String>();
        RandomChoice.add(c.getString(c.getColumnIndex("Ans")));
        RandomChoice.add(getChoice(SelectAns1));
        RandomChoice.add(getChoice(SelectAns2));
        RandomChoice.add(getChoice(SelectAns3));

        Collections.shuffle(RandomChoice);
        Ans0 = RandomChoice.get(0);
        Ans1 = RandomChoice.get(1);
        Ans2 = RandomChoice.get(2);
        Ans3 = RandomChoice.get(3);

        ((TextView)findViewById(R.id.textQuestion_Res)).setText(cntQuestion + 1 + " / 10");
        ((TextView)findViewById(R.id.textQuestion)).setText(Title);     // TextViewに取得したTitleを表示
        ((Button)findViewById(R.id.button1)).setText(Ans0);     //  Button1~4に取得した選択肢を表示
        ((Button)findViewById(R.id.button2)).setText(Ans1);
        ((Button)findViewById(R.id.button3)).setText(Ans2);
        ((Button)findViewById(R.id.button4)).setText(Ans3);


        c.close();
        db.close();
    }


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


    public void onClick(View v) {       //
        boolean j;
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
            Intent intent = new Intent(MainActivity.this,ResultActivity.class);
            intent.putExtra("LIST",list);

            startActivity(intent);
        }
        else
            setQuestion();

    }

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
