package com.example.a171y005.quizsc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class ResultActivity extends AppCompatActivity {

    private ArrayList<HashMap<String,String>> list_data;
    private SimpleAdapter sim;


    ArrayList<String> list = new ArrayList<>();
    ArrayList<String> title = new ArrayList<>();
    ArrayList<String> ans = new ArrayList<>();
    ArrayList<String> res = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        setTitle("学習結果");
        list = getIntent().getStringArrayListExtra("LIST");
        int cnt = 0;
        final ListView listT;

        HashMap<String, String> hashMap = new HashMap<String, String>();

        for (int i = 0; i < list.size(); i += 3) {
            title.add(list.get(i));
            ans.add(list.get(i + 1));
            res.add(list.get(i + 2));
            cnt++;
        }

        list_data = new ArrayList<HashMap<String, String>>();
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

        final Button bt_returnTitle = (Button) findViewById(R.id.bt_returnTitle);
        bt_returnTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this, TitleActivity.class);
                startActivity(intent);
            }
        });

        final Button bt_next = (Button) findViewById(R.id.bt_next);
        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

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

}