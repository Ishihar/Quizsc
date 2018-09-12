package com.example.a171y005.quizsc;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 171y005 on 2018/06/13.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context){
        super(context,"Eng.db",null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(
                "Create table quiz_table("
                + "_id integer primary key autoincrement not null, "
                + "Title text ,"
                + "Ans text,"
                + "Clear integer);" );

        db.execSQL(
                "Create table quiz_log("
                +   "_id integer primary key autoincrement not null,"
                +   "date text ,"
                +   "ans double);" );

        db.execSQL("insert into quiz_table(Title,Ans,Clear) values('suggestion','提案',0);" );
        db.execSQL("insert into quiz_table(Title,Ans,Clear) values('inspection','検査',0);" );
        db.execSQL("insert into quiz_table(Title,Ans,Clear) values('inflation','インフレーション',0);" );
        db.execSQL("insert into quiz_table(Title,Ans,Clear) values('privilege','特権、特典',0);" );
        db.execSQL("insert into quiz_table(Title,Ans,Clear) values('sponsor','広告主',0);" );
        db.execSQL("insert into quiz_table(Title,Ans,Clear) values('expense','経費、出費',0);" );
        db.execSQL("insert into quiz_table(Title,Ans,Clear) values('management','経営、経理',0);" );
        db.execSQL("insert into quiz_table(Title,Ans,Clear) values('enterprise','大規模な事業',0);" );
        db.execSQL("insert into quiz_table(Title,Ans,Clear) values('establishment','設立、創立',0);" );
        db.execSQL("insert into quiz_table(Title,Ans,Clear) values('export','輸出品',0);" );
        db.execSQL("insert into quiz_table(Title,Ans,Clear) values('import','輸入品',0);" );
        db.execSQL("insert into quiz_table(Title,Ans,Clear) values('inquiry','問い合わせ',0);" );
        db.execSQL("insert into quiz_table(Title,Ans,Clear) values('loan','貸付金',0);" );
        db.execSQL("insert into quiz_table(Title,Ans,Clear) values('recession','景気後退',0);" );
        db.execSQL("insert into quiz_table(Title,Ans,Clear) values('value','価値、価格',0);" );
        db.execSQL("insert into quiz_table(Title,Ans,Clear) values('wage','賃金、給料',0);" );
        db.execSQL("insert into quiz_table(Title,Ans,Clear) values('commercial','広告放送',0);" );
        db.execSQL("insert into quiz_table(Title,Ans,Clear) values('incentive','励み、動機。',0);" );
        db.execSQL("insert into quiz_table(Title,Ans,Clear) values('signature','署名、サイン',0);" );
        db.execSQL("insert into quiz_table(Title,Ans,Clear) values('employment','雇用',0);" );
        db.execSQL("insert into quiz_table(Title,Ans,Clear) values('reward','報い、報酬',0);" );
        db.execSQL("insert into quiz_table(Title,Ans,Clear) values('supply','供給（量）',0);" );
        db.execSQL("insert into quiz_table(Title,Ans,Clear) values('instruction','指図、指示',0);" );
        db.execSQL("insert into quiz_table(Title,Ans,Clear) values('load','重さ、圧力',0);" );
        db.execSQL("insert into quiz_table(Title,Ans,Clear) values('membership','会員、会員権',0);" );
        db.execSQL("insert into quiz_table(Title,Ans,Clear) values('payment','支払い、支払金',0);" );
        db.execSQL("insert into quiz_table(Title,Ans,Clear) values('permission','許可、認可',0);" );
        db.execSQL("insert into quiz_table(Title,Ans,Clear) values('recommendation','推薦、勧告',0);" );
        db.execSQL("insert into quiz_table(Title,Ans,Clear) values('survey','調査',0);" );
        db.execSQL("insert into quiz_table(Title,Ans,Clear) values('undertaking','事業、仕事',0);" );
        db.execSQL("insert into quiz_table(Title,Ans,Clear) values('cooperation','協力、連携',0);" );
        db.execSQL("insert into quiz_table(Title,Ans,Clear) values('demonstration','実演',0);" );
        db.execSQL("insert into quiz_table(Title,Ans,Clear) values('deposit','頭金',0);" );
        db.execSQL("insert into quiz_table(Title,Ans,Clear) values('developer','開発者',0);" );
        db.execSQL("insert into quiz_table(Title,Ans,Clear) values('discharge','解雇',0);" );
        db.execSQL("insert into quiz_table(Title,Ans,Clear) values('distribution','配分、配給',0);" );
        db.execSQL("insert into quiz_table(Title,Ans,Clear) values('headquarters','本社、本部',0);" );
        db.execSQL("insert into quiz_table(Title,Ans,Clear) values('promotion','昇進、促進',0);" );
        db.execSQL("insert into quiz_table(Title,Ans,Clear) values('property','性質',0);" );
        db.execSQL("insert into quiz_table(Title,Ans,Clear) values('retail','小売り',0);" );
        db.execSQL("insert into quiz_table(Title,Ans,Clear) values('schedule','予定、日程',0);" );
        db.execSQL("insert into quiz_table(Title,Ans,Clear) values('payday','給料日',0);" );
        db.execSQL("insert into quiz_table(Title,Ans,Clear) values('stockholder','株主',0);" );
        db.execSQL("insert into quiz_table(Title,Ans,Clear) values('validity','有効性、効力',0);" );
        db.execSQL("insert into quiz_table(Title,Ans,Clear) values('valuation','査定。査定額',0);" );
        db.execSQL("insert into quiz_table(Title,Ans,Clear) values('workflow','仕事の流れ',0);" );
        db.execSQL("insert into quiz_table(Title,Ans,Clear) values('workload','仕事量',0);" );
        db.execSQL("insert into quiz_table(Title,Ans,Clear) values('query','質問、疑問',0);" );
        db.execSQL("insert into quiz_table(Title,Ans,Clear) values('transit','輸送、運送',0);" );
        db.execSQL("insert into quiz_table(Title,Ans,Clear) values('startup','新興企業',0);" );
        db.execSQL("insert into quiz_table(Title,Ans,Clear) values('plumber','配管工',0);" );
        db.execSQL("insert into quiz_table(Title,Ans,Clear) values('tariff','関税',0);" );
        db.execSQL("insert into quiz_table(Title,Ans,Clear) values('blizzard','猛吹雪',0);" );
        db.execSQL("insert into quiz_table(Title,Ans,Clear) values('transplant','臓器移植',0);" );
        db.execSQL("insert into quiz_table(Title,Ans,Clear) values('exemption','免除、控除',0);" );
        db.execSQL("insert into quiz_table(Title,Ans,Clear) values('generator','発電機',0);" );
        db.execSQL("insert into quiz_table(Title,Ans,Clear) values('runway','滑走路',0);" );
        db.execSQL("insert into quiz_table(Title,Ans,Clear) values('briefing','報告会、説明会',0);" );
        db.execSQL("insert into quiz_table(Title,Ans,Clear) values('fad','流行',0);" );
        db.execSQL("insert into quiz_table(Title,Ans,Clear) values('intermission','休憩時間、幕間',0);" );
        db.execSQL("insert into quiz_table(Title,Ans,Clear) values('janitor','管理人、用務員',0);" );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }
}
