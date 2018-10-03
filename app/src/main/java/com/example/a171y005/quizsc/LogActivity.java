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
import java.util.Random;

import static java.lang.String.format;

public class LogActivity extends AppCompatActivity {

    private Double min,max,total,goalset;
    private String min_name,max_name;
    private int cnt_b,cnt_l,cnt_a,cnt_c,cnt_f;
    private ArrayList<Double> avg = new ArrayList<Double>();
    private ArrayList<String> GOAL_LIST = new ArrayList<String>();


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
            c = db.rawQuery("Select count(*) from quiz_log where Category = 'ビジネス';", null);
            c.moveToFirst();
            cnt_b = Integer.parseInt(c.getString(c.getColumnIndex("count(*)")));
            c = db.rawQuery("Select count(*) from quiz_log where Category = '生活';", null);
            c.moveToFirst();
            cnt_l = Integer.parseInt(c.getString(c.getColumnIndex("count(*)")));
            c = db.rawQuery("Select count(*) from quiz_log where Category = '動物';", null);
            c.moveToFirst();
            cnt_a = Integer.parseInt(c.getString(c.getColumnIndex("count(*)")));
            c = db.rawQuery("Select count(*) from quiz_log where Category = '宇宙';", null);
            c.moveToFirst();
            cnt_c = Integer.parseInt(c.getString(c.getColumnIndex("count(*)")));
            c = db.rawQuery("Select count(*) from quiz_log where Category = '食べ物';", null);
            c.moveToFirst();
            cnt_f = Integer.parseInt(c.getString(c.getColumnIndex("count(*)")));
        }catch (Exception e){
            return;
        }

            if(cnt_b > 0 && cnt_l > 0 && cnt_a > 0 && cnt_c > 0 && cnt_f > 0) {
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
                    tv_2.setText("最高平均正解数\n  " + max_name + ": " + format("%.2f", max));
                    tv_3.setText("最低平均正解数\n  " + min_name + ": " + format("%.2f", min));
                } catch (Exception e) {
                    tv_2.setText("学習状況を取得できません。");
                }

                GOAL_LIST.clear();
                set_Goal(total,max,min);
            }
            else {
                set_Goal(false);
                tv_1.setText("学習履歴がありません。");
                tv_2.setText("学習履歴がありません。");

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

    public void set_Goal(boolean goalset_INIT){
        TextView tv_GOAL = (TextView) findViewById(R.id.tv_g1);

        if(!goalset_INIT) {
            GOAL_LIST.add("各カテゴリを1回ずつ学習して学習状況を取得しよう");
            tv_GOAL.setText(GOAL_LIST.get(0));
        }
    }
    public void set_Goal(double totalavg, double highavg, double lowavg){
        TextView tv_GOAL = (TextView) findViewById(R.id.tv_g1);
        double hantei = 0;
        String str  = "";

        Random random = new Random();
        int select = random.nextInt(3);

        switch (select) {
            case 0:
                hantei = totalavg;
                str = "全体";
                break;
            case 1:
                hantei = highavg;
                str = "最高";
                select = 1;
                break;
            case 2:
                hantei = lowavg;
                str = "最低";
                select = 2;
                break;
        }

        if (hantei < 5) {
            tv_GOAL.setText(str + "平均正解数5.0以上を目指そう");
            goalset = 5.0;
        } else if (hantei < 7) {
            tv_GOAL.setText(str + "平均正解数7.0以上を目指そう");
            goalset = 7.0;
        } else if (hantei < 9) {
            tv_GOAL.setText(str + "平均正解数9.0以上を目指そう");
            goalset = 9.0;
        }
    }
}