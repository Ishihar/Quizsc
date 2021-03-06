package com.example.a171y005.quizsc;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;


public class TitleActivity extends AppCompatActivity {

    private GetCategoryName mGetCategoryName = new GetCategoryName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_title);

        // 各カテゴリのデータをCSVファイルからInsert
        Insert_data("B");
        Insert_data("L");
        Insert_data("A");
        Insert_data("C");
        Insert_data("F");
        Insert_data("IT");
    }

   /* @Override
    protected void onDestroy() {
        super.onDestroy();
        int mNotificationId = 001;

        Calendar cal = Calendar.getInstance();
        //cal.setTimeInMillis(86400000);
        cal.setTimeInMillis(20000);
        cal.set(Calendar.MILLISECOND, 0);
        Intent intent = new Intent(this, TitleActivity.class);
        intent.putExtra("KEY", value);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        long init_alarm = cal.getTimeInMillis();

        PendingIntent contentIntent = PendingIntent.getBroadcast(this, mNotificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, init_alarm, contentIntent);
    }*/



    public void onClick(View v) {
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        String cate = spinner.getSelectedItem().toString();

        // STARTボタンが押されたとき選択カテゴリからTable名を取得
        if (v.getId() == R.id.bt_start) {
            if(cate.equals("問題カテゴリを選択してください")){
                Toast.makeText(TitleActivity.this, "カテゴリを選択してください。", Toast.LENGTH_SHORT).show();
                return;
            }
            cate = mGetCategoryName.getTable(cate);
            // MainActivityに取得したTableNameをput
            Intent intent = new Intent(TitleActivity.this, MainActivity.class);
            intent.putExtra("Table_NAME", cate);
            startActivity(intent);
        }
        // 単語一覧Activityへ
        else if (v.getId() == R.id.bt_add) {
            Intent intent = new Intent(TitleActivity.this, WordActivity.class);
            startActivity(intent);
        }
        // 学習履歴Activityへ
        else if (v.getId() == R.id.bt_gostd) {
            Intent intent = new Intent(TitleActivity.this, LogActivity.class);
            startActivity(intent);
        }
        finish();
    }

    // 各カテゴリのデータをInsert
    public void Insert_data(String catekey) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c;


        c = db.rawQuery("Select * from quiz_table_" + catekey + ";", null);
        c.moveToFirst();

        // データ件数が0件だった場合Insert
        int count = c.getCount();
        if (count == 0) {
            try {
                // /main/assets/worddata.csvを取得
                InputStream is = getResources().getAssets().open("worddata-" + catekey + ".csv");
                InputStreamReader inputStreamReader = new InputStreamReader(is);
                BufferedReader bf = new BufferedReader(inputStreamReader);
                String line;
                try {
                    while ((line = bf.readLine()) != null) {
                        StringTokenizer stringTokenizer = new StringTokenizer(line, ",");
                        db.execSQL("insert into quiz_table_" + catekey + "(Title,Ans) values('" + stringTokenizer.nextToken() + "','" + stringTokenizer.nextToken() + "');");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            c.close();
        }
    }
}