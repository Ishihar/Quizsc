package com.example.a171y005.quizsc;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddwordActivity extends AppCompatActivity {

    private Button button,button1;
    private EditText word;
    private EditText mean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addword);

        word = (EditText) findViewById(R.id.Word);
        mean = (EditText) findViewById(R.id.Mean);
    }

        public void onClick(View v) {
                DatabaseHelper dbHelper = new DatabaseHelper(AddwordActivity.this);
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Cursor c;
                AlertDialog.Builder builder = new AlertDialog.Builder(AddwordActivity.this);
                String message;

                String w = word.getText().toString();
                String m = mean.getText().toString();
                c = db.rawQuery("Select count(*),Ans from quiz_table where Title = '" + w + "';", null);
                c.moveToFirst();
                String check,anser = c.getString(c.getColumnIndex("Ans"));
                int count = Integer.parseInt(c.getString(c.getColumnIndex("count(*)")));

                if (v.getId() == R.id.btadd) {
                    if(w.isEmpty() || m.isEmpty()){
                        Toast.makeText(this,"入力欄が空欄です。",Toast.LENGTH_LONG).show();
                        return;
                    }

                    for(int i = 0; i < w.length(); i++) {
                        check = w.substring(i, i + 1);
                        if (check.matches("[^a-zA-z]")) {
                            Toast.makeText(this, "アルファベット以外の文字が入力されています。", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }


                    if (count >= 1) {
                        message = w + "は既に登録されています。" + "\n" + "登録内容:" + anser;
                        builder.setMessage(message);
                    } else {
                        db.execSQL("Insert into quiz_table(Title,Ans,Clear) values('" + w + "','" + m + "',0);");
                        builder.setMessage(w + "\nデータベースに登録しました。");
                        word.setText("");
                        mean.setText("");
                    }
                    builder.setTitle("単語登録");
                    builder.setPositiveButton("OK", null);
                    builder.show();
                    c.close();
                    db.close();
                }

                if(v.getId() == R.id.bt_back){
                    Intent intent = new Intent(AddwordActivity.this,TitleActivity.class);
                    startActivity(intent);
                }

                if(v.getId() == R.id.bt_Sr){
                    Intent intent = new Intent(AddwordActivity.this,WordActivity.class);
                    startActivity(intent);
                }
        }

        public boolean onKeyDown(int KeyCode, KeyEvent event){
            if(KeyCode != KeyEvent.KEYCODE_BACK){
                return super.onKeyDown(KeyCode,event);
            }
            else{
                Intent intent = new Intent(AddwordActivity.this,TitleActivity.class);
                startActivity(intent);
                return false;
            }
      }
}
