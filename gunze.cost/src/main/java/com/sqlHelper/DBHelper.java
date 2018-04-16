package com.sqlHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.model.department;
import com.model.deviceInfo;

/**
 * Created by 刘畅 on 2018/3/25.
 */

public class DBHelper extends SQLiteOpenHelper {
    //数据库版本号
    private static final int DATABASE_VERSION = 3;
    //数据库名称
    private static final String DATABASE_NAME = "cost.db";
    /**
     * departments表创建
     */
    public static final String CREATE_DEPARTMENT = "CREATE TABLE " + department.TABLE + "("
            + department.KEY_CODE + " TEXT PRIMARY KEY ,"
            + department.KEY_NAME + " TEXT)";
    /**
     * devices表创建
     */
    public static final String CREATE_DEVICE = "CREATE TABLE " + deviceInfo.TABLE + "("
            + deviceInfo.KEY_ID + " INTEGER PRIMARY KEY autoincrement,"
            + deviceInfo.KEY_CODE + " TEXT  ,"
            + deviceInfo.KEY_NAME + " TEXT)";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建数据表

        db.execSQL(CREATE_DEPARTMENT);
        db.execSQL(CREATE_DEVICE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //如果旧表存在，删除，所以数据将会消失
        db.execSQL("DROP TABLE IF EXISTS " + department.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + deviceInfo.TABLE);
        //再次创建表
        onCreate(db);
    }
}