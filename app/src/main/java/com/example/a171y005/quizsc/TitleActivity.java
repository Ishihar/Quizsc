package com.example.a171y005.quizsc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class TitleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);

    }

    public void onClick(View v){
        if(v.getId() == R.id.bt_start){
                Intent intent = new Intent(TitleActivity.this,MainActivity.class);
                startActivity(intent);
        }
        else if(v.getId() == R.id.bt_add){
            Intent intent = new Intent(TitleActivity.this,AddwordActivity.class);
            startActivity(intent);
        }
        finish();
    }
}
