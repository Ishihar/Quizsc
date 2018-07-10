package com.example.a171y005.quizsc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    ListView listtitle,listans,listres;
    ArrayList<String> list = new ArrayList<>();
    ArrayList<String> title = new ArrayList<>();
    ArrayList<String> ans = new ArrayList<>();
    ArrayList<String> res = new ArrayList<>();
    boolean open = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        list = getIntent().getStringArrayListExtra("LIST");
        Log.d("lt","list=" + list.size());

        for(int i = 0; i < list.size();i+=3){
            title.add(list.get(i));
            ans.add(list.get(i+1));
            res.add(list.get(i+2));
        }

        final ArrayAdapter t_adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,title);
        final ArrayAdapter a_adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,ans);
        final ArrayAdapter res_adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,res);

        final ListView listtitle = (ListView)findViewById(R.id.listtitle);
        final ListView listans = (ListView)findViewById(R.id.listans);
        final ListView listres = (ListView)findViewById(R.id.listres);
        listtitle.setAdapter(t_adapter);
        listans.setAdapter(a_adapter);
        listres.setAdapter(res_adapter);

        /*final Button button1 = (Button)findViewById(R.id.button);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!open) {
                    open = true;
                    setListViewHeightBasedOnChildren(listView);
                    button1.setText("表示を閉じる");
                }
                else {
                    open = false;
                    listView.removeHeaderView(v);
                    button1.setText("結果表示");
                }
            }
        });*/

        final Button bt_returnTitle = (Button) findViewById(R.id.bt_returnTitle);
        bt_returnTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this,TitleActivity.class);
                startActivity(intent);
            }
        });

        final Button bt_next = (Button) findViewById(R.id.bt_next);
        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        {

        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listview){

        ListAdapter listAdapter = listview.getAdapter();

        if(listAdapter == null){
            return;
        }

        int totalHeight = 0;

        for(int i = 0; i < listAdapter.getCount(); i++){
            View listItem = listAdapter.getView(i,null,listview);
            listItem.measure(0,0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listview.getLayoutParams();

        params.height = totalHeight + (listview.getDividerHeight() * (listAdapter.getCount() - 1 ));
        listview.setLayoutParams(params);
    }

}