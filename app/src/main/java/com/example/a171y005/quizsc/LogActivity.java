package com.example.a171y005.quizsc;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.TextView;

import java.util.ArrayList;

import static java.lang.String.format;

public class LogActivity extends AppCompatActivity {

    private Double min,max,total;
    private String min_name,max_name;
    private ArrayList<Double> avg = new ArrayList<Double>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTitle("学習状況");
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c;

        TextView tv_1 = (TextView) findViewById(R.id.tv_1);
        TextView tv_2 = (TextView) findViewById(R.id.tv_2);
        TextView tv_3 = (TextView) findViewById(R.id.tv_3);

        try {
            c = db.rawQuery("Select avg(cnt) from quiz_log where Category = 'ビジネス';", null);
            c.moveToFirst();
            avg.add(Double.valueOf(c.getString(c.getColumnIndex("avg(cnt)"))));
            c = db.rawQuery("Select avg(cnt) from quiz_log where Category = '生活';", null);
            c.moveToFirst();
            avg.add(Double.parseDouble(c.getString(c.getColumnIndex("avg(cnt)"))));
            c = db.rawQuery("Select avg(cnt) from quiz_log where Category = '動物';", null);
            c.moveToFirst();
            avg.add(Double.parseDouble(c.getString(c.getColumnIndex("avg(cnt)"))));
            c = db.rawQuery("Select avg(cnt) from quiz_log where Category = '宇宙';", null);
            c.moveToFirst();
            avg.add(Double.parseDouble(c.getString(c.getColumnIndex("avg(cnt)"))));
            c = db.rawQuery("Select avg(cnt) from quiz_log where Category = '食べ物';", null);
            c.moveToFirst();
            avg.add(Double.parseDouble(c.getString(c.getColumnIndex("avg(cnt)"))));
            c = db.rawQuery("Select avg(cnt) from quiz_log;", null);
            c.moveToFirst();
            total = c.getDouble(c.getColumnIndex("avg(cnt)"));
            db.close();
            c.close();
        } catch (Exception e) {
            tv_1.setText("学習状況を取得できません。");
        }

        try {
            min = avg.get(0);
            max = avg.get(0);

            for (int i = 0; i < avg.size(); ++i) {
                min = Math.min(min, avg.get(i));
                max = Math.max(max, avg.get(i));
            }
            min_name = set_Title(min);
            max_name = set_Title(max);

            tv_1.setText("全体の平均正解数\n" + format("%.2f", total));
            tv_2.setText("最高平均正答数\n  " + max_name + ": " + format("%.2f", max));
            tv_3.setText("最低平均正解数\n  " + min_name + ": " + format("%.2f", min));
        }
        catch (Exception e){
            tv_2.setText("学習状況を取得できません。");
        }
    }

    // バックキーが押された時の処理（タイトルへ戻る）
    public boolean onKeyDown(int KeyCode, KeyEvent event){
        if(KeyCode != KeyEvent.KEYCODE_BACK){
            return super.onKeyDown(KeyCode,event);
        }
        else{
            Intent intent = new Intent(LogActivity.this,TitleActivity.class);
            startActivity(intent);
            return false;
        }
    }

    public String set_Title(Double args){
        String key;
        if(args.equals(avg.get(0))){
            key = "ビジネス";
        }
        else if(args.equals(avg.get(1))){
            key = "生活";
        }
        else if(args.equals(avg.get(2))){
            key = "動物";
        }
        else if(args.equals(avg.get(3))){
            key = "宇宙";
        }
        else {
            key = "食べ物";
        }

        return key;
    }
}