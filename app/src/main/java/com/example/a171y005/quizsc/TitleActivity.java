package com.example.a171y005.quizsc;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;


public class TitleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);

            Insert_data_b();
            Insert_data_l();
            Insert_data_a();
            Insert_data_c();
    }

    public void onClick(View v){
        Spinner spinner = (Spinner)findViewById(R.id.spinner);
        String cate = spinner.getSelectedItem().toString();

        if(v.getId() == R.id.bt_start){
            switch (cate){
                case "問題カテゴリ選択":
                    Toast.makeText(TitleActivity.this,"カテゴリを選択してください。",Toast.LENGTH_SHORT).show();
                    return;
                case "ビジネス":
                    cate = "quiz_table_B";
                    break;
                case "生活":
                    cate = "quiz_table_L";
                    break;
                case "動物":
                    cate = "quiz_table_A";
                    break;
                case "宇宙":
                    cate = "quiz_table_C";
                    break;
            }
            Intent intent = new Intent(TitleActivity.this,MainActivity.class);
            Log.d("FILE",cate);
            intent.putExtra("DB_NAME",cate);
            startActivity(intent);
        }
        else if(v.getId() == R.id.bt_add){
            Intent intent = new Intent(TitleActivity.this,WordActivity.class);
            startActivity(intent);
        }
        else if(v.getId() == R.id.bt_gostd){
            Intent intent = new Intent(TitleActivity.this,Std.class);
            startActivity(intent);
        }
        finish();
    }

    public void Insert_data_b() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c;


        c = db.rawQuery("Select count(*) from quiz_table_B;",null);
        c.moveToFirst();


        int count = Integer.parseInt(c.getString(c.getColumnIndex("count(*)")));
        if(count == 0) {
            try {
                InputStream is = getResources().getAssets().open("worddata-B.csv");
                InputStreamReader inputStreamReader = new InputStreamReader(is);
                BufferedReader bf = new BufferedReader(inputStreamReader);
                String line = "";
                try {
                    while ((line = bf.readLine()) != null) {
                        StringTokenizer stringTokenizer = new StringTokenizer(line, ",");
                        db.execSQL("insert into quiz_table_B(Title,Ans) values('" + stringTokenizer.nextToken() + "','" + stringTokenizer.nextToken() + "');");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            return;
        }
    }
    public void Insert_data_l() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c;

        c = db.rawQuery("Select count(*) from quiz_table_L;",null);
        c.moveToFirst();
        int count = Integer.parseInt(c.getString(c.getColumnIndex("count(*)")));
        if(count == 0) {
            try {
                InputStream is = getResources().getAssets().open("worddata-L.csv");
                InputStreamReader inputStreamReader = new InputStreamReader(is);
                BufferedReader bf = new BufferedReader(inputStreamReader);
                String line = "";
                try {
                    while ((line = bf.readLine()) != null) {
                        StringTokenizer stringTokenizer = new StringTokenizer(line, ",");
                        db.execSQL("insert into quiz_table_L(Title,Ans) values('" + stringTokenizer.nextToken() + "','" + stringTokenizer.nextToken() + "');");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            return;
        }
        db.close();
        c.close();
    }
    public void Insert_data_a() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c;

        c = db.rawQuery("Select count(*) from quiz_table_A;",null);
        c.moveToFirst();
        int count = Integer.parseInt(c.getString(c.getColumnIndex("count(*)")));
        if(count == 0) {
            try {
                InputStream is = getResources().getAssets().open("worddata-A.csv");
                InputStreamReader inputStreamReader = new InputStreamReader(is);
                BufferedReader bf = new BufferedReader(inputStreamReader);
                String line = "";
                try {
                    while ((line = bf.readLine()) != null) {
                        StringTokenizer stringTokenizer = new StringTokenizer(line, ",");
                        db.execSQL("insert into quiz_table_A(Title,Ans) values('" + stringTokenizer.nextToken() + "','" + stringTokenizer.nextToken() + "');");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            return;
        }
        db.close();
        c.close();
    }
    public void Insert_data_c() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c;

        c = db.rawQuery("Select count(*) from quiz_table_C;",null);
        c.moveToFirst();
        int count = Integer.parseInt(c.getString(c.getColumnIndex("count(*)")));
        if(count == 0) {
            try {
                InputStream is = getResources().getAssets().open("worddata-C.csv");
                InputStreamReader inputStreamReader = new InputStreamReader(is);
                BufferedReader bf = new BufferedReader(inputStreamReader);
                String line = "";
                try {
                    while ((line = bf.readLine()) != null) {
                        StringTokenizer stringTokenizer = new StringTokenizer(line, ",");
                        db.execSQL("insert into quiz_table_C(Title,Ans) values('" + stringTokenizer.nextToken() + "','" + stringTokenizer.nextToken() + "');");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            return;
        }
        db.close();
        c.close();
    }
}