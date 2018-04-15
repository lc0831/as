package com.sqlHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.model.department;
import com.model.deviceInfo;

import java.util.ArrayList;
import java.util.List;

public class CostDB {
    private SQLiteDatabase db;

    private CostDB(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
    }
    private static CostDB costDB;
public synchronized static CostDB getInstance(Context context){
        if(costDB==null)
            costDB=new CostDB(context);
        return  costDB;

}
    /**
     * 新增部门
     * @param depart 部门
     */
    public void insertDepart(department depart) {
        if (depart != null) {
            ContentValues values = new ContentValues();
            values.put(department.KEY_CODE, depart.cDepCode);
            values.put(department.KEY_NAME, depart.cDepName);
            db.insert(department.TABLE, null, values);
            db.close();
        }
    }
    public void insertDevice(deviceInfo device){
        if(device!=null){
            ContentValues values= new ContentValues();
            values.put(deviceInfo.KEY_CODE,device.getDevID());
            values.put(deviceInfo.KEY_NAME,device.getDevname());
            db.insert(deviceInfo.TABLE,null,values);
            db.close();
        }
    }
    public List<department> getDepartList(){

        String selectQuery="SELECT "+
                department.KEY_CODE+","+
                department.KEY_NAME+" FROM "+department.TABLE;
        List<department> departList=new ArrayList<>();
        Cursor cursor=db.rawQuery(selectQuery,null);

        if(cursor.moveToFirst()){
            do{
                department depart=new department();
                depart.setcDepCode(cursor.getString(cursor.getColumnIndex(department.KEY_CODE)));
                depart.setcDepName(cursor.getString(cursor.getColumnIndex(department.KEY_NAME)));
                departList.add(depart);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return departList;
    }

    public department getDepartById(String departCode){

        String selectQuery="SELECT "+
                department.KEY_CODE + "," +
                department.KEY_NAME + " FROM " + department.TABLE
                + " WHERE " +
                department.KEY_CODE + "=?";

        department depart=new department();
        Cursor cursor=db.rawQuery(selectQuery,new String[]{departCode});
        if(cursor.moveToFirst()){
            do{
                depart.setcDepCode(cursor.getString(cursor.getColumnIndex(department.KEY_CODE)));
                depart.setcDepName(cursor.getString(cursor.getColumnIndex(department.KEY_NAME)));

            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return depart;
    } public List<deviceInfo> getDeviceList(){

        String selectQuery="SELECT "+
                deviceInfo.KEY_CODE+","+
                deviceInfo.KEY_NAME+" FROM "+deviceInfo.TABLE;
        List<deviceInfo> deviceList=new ArrayList<>();
        Cursor cursor=db.rawQuery(selectQuery,null);

        if(cursor.moveToFirst()){
            do{
                deviceInfo device=new deviceInfo();
                device.setDevID(cursor.getString(cursor.getColumnIndex(deviceInfo.KEY_CODE)));
                device.setDevname(cursor.getString(cursor.getColumnIndex(deviceInfo.KEY_NAME)));
                deviceList.add(device);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return deviceList;
    }

    public deviceInfo getDeviceById(String id){

        String selectQuery="SELECT "+
                deviceInfo.KEY_CODE + "," +
                deviceInfo.KEY_NAME + " FROM " + deviceInfo.TABLE
                + " WHERE " +
                deviceInfo.KEY_CODE + "=?";

        deviceInfo device=new deviceInfo();
        Cursor cursor=db.rawQuery(selectQuery,new String[]{id});
        if(cursor.moveToFirst()){
            do{
                device.setDevID(cursor.getString(cursor.getColumnIndex(deviceInfo.KEY_CODE)));
                device.setDevname(cursor.getString(cursor.getColumnIndex(deviceInfo.KEY_NAME)));

            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return device;
    }

}
