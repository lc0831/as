package com.sqlHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.model.department;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by 刘畅 on 2018/3/25.
 */

public class departManager {
    private DBHelper dbHelper;

    public departManager(Context context){
        dbHelper=new DBHelper(context);
    }

    public int insert(department depart){
        //打开连接，写入数据
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(department.KEY_CODE,depart.cDepCode);
        values.put(department.KEY_NAME,depart.cDepName);
        //
        long depart_Id=db.insert(department.TABLE,null,values);
        db.close();
        return (int)depart_Id;
    }

    public void delete(String depCode){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        db.delete(department.TABLE,department.KEY_CODE+"=?", new String[]{depCode});
        db.close();
    }
    public void update(department depart){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();

        values.put(department.KEY_CODE,depart.cDepCode);
        values.put(department.KEY_NAME,depart.cDepName);

        db.update(department.TABLE,values,department.KEY_CODE+"=?" ,new String[]{depart.cDepCode});
    }

    public ArrayList<HashMap<String, String>> getdepartList(){
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String selectQuery="SELECT "+
                department.KEY_CODE+","+
                department.KEY_NAME+" FROM "+department.TABLE;
        ArrayList<HashMap<String,String>> departList=new ArrayList<HashMap<String, String>>();
        Cursor cursor=db.rawQuery(selectQuery,null);

        if(cursor.moveToFirst()){
            do{
                HashMap<String,String> depart=new HashMap<String,String>();
                depart.put("code",cursor.getString(cursor.getColumnIndex(department.KEY_CODE)));
                depart.put("name",cursor.getString(cursor.getColumnIndex(department.KEY_NAME)));
                departList.add(depart);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return departList;
    }

    public department getDepartById(String departCode){
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String selectQuery="SELECT "+
                department.KEY_CODE + "," +
                department.KEY_NAME + " FROM " + department.TABLE
                + " WHERE " +
                department.KEY_CODE + "=?";

        department depart=new department();
        Cursor cursor=db.rawQuery(selectQuery,new String[]{departCode});
        if(cursor.moveToFirst()){
            do{
                depart.cDepCode =cursor.getString(cursor.getColumnIndex(department.KEY_CODE));
                depart.cDepName =cursor.getString(cursor.getColumnIndex(department.KEY_NAME));

            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return depart;
    }
}
