package com.example.jambgame;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class DBConnection extends SQLiteOpenHelper {

    public DBConnection(Context context) {
        super(context, "jamb.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table result (id integer primary key autoincrement, score integer )");
      }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists result");
        onCreate(db);
    }
    public boolean addScore(int score){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("score",score);
        long res=db.insert("result",null,cv);
        if(res==-1){
            return false;
        }else{
            return true;
        }
    }
    public ArrayList<Integer> getScores(){
        ArrayList<Integer> scores=new ArrayList<Integer>();
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select score from result order by score",null);
        if(res.getCount()!=0){
            while(res.moveToNext()){
                scores.add(Integer.parseInt(res.getString(0)));
            }
        }
        return scores;
    }
}
