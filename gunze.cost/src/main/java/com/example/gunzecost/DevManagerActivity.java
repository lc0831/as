package com.example.gunzecost;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.Receivers.BroadcastReceiver;
import com.listAdapter.DepartAdapter;
import com.listAdapter.MyAdapter;
import com.model.department;
import com.sqlHelper.DBHelper;
import com.sqlHelper.departManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import model.DeviceInfo;


public class DevManagerActivity extends AppCompatActivity {

    private EditText txtDepart, txtDate;
    private MyAdapter<DeviceInfo> devAdapter = null;
    private List<DeviceInfo> devData = null;
    private List departList = new ArrayList<department>();
    BroadcastReceiver mbcr = new BroadcastReceiver();
    private ListView listview;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_manager);
        //注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.ACTION_DECODE_DATA");
        registerReceiver(mbcr, filter);
        // 注册控件
        txtDepart=findViewById(R.id.txtDepart);
        //注册数据库连接对象
        dbHelper=new DBHelper(this);


        //选择部门
        findViewById(R.id.txtDepart).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //创建弹窗list
                    AlertDialog.Builder builder = new AlertDialog.Builder(DevManagerActivity.this);
                    View view = View.inflate(DevManagerActivity.this, R.layout.dialog_department, null);
                    ListView departListView = view.findViewById(R.id.list_view);
                    //填充弹窗数据
                    createProductList();
                    dbHelper.getWritableDatabase();

                    DepartAdapter myadapter = new DepartAdapter(DevManagerActivity.this, R.layout.list_view, departList);
                    departListView.setAdapter(myadapter);
                    //构建弹窗显示
                    builder.setView(view);
                    final AlertDialog dialog = builder.show();


                    departListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            View curr = parent.getChildAt((int) id);
                            TextView c = curr.findViewById(R.id.item_Name);
                            txtDepart.setText(c.getText());
                            dialog.dismiss();
                        }
                    });
                }
                return true;
            }

        });
        //选择日期
        findViewById(R.id.txtDate).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //创建弹窗list
                AlertDialog.Builder builder = new AlertDialog.Builder(DevManagerActivity.this);
                View view = View.inflate(DevManagerActivity.this, R.layout.datapicker, null);
                final DatePicker datePicker = view.findViewById(R.id.date_picker);
                //构建弹窗显示
                builder.setView(view);

                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(System.currentTimeMillis());
                datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null);

                final AlertDialog dialog = builder.show();
                dialog.show();
                return true;
            }
        });
        //数据初始化
        devData = new ArrayList<DeviceInfo>();

        //填充适配器
        devAdapter = new MyAdapter<DeviceInfo>((ArrayList)devData,R.layout.list_view) {
            @Override
            public void bindView(ViewHolder holder, DeviceInfo obj) {
                holder.setText(R.id.item_code,obj.getDevID());
                holder.setText(R.id.item_Name,obj.getDevname());
            }
        };
        //绑定数据
        listview = findViewById(R.id.lv_dev);
        listview.setAdapter(devAdapter);
        mbcr.setMessage(new BroadcastReceiver.Message() {
            @Override
            public void getMsg(String str) {
                //Adapter初始化
                DeviceInfo devInfo=new DeviceInfo();
                devInfo.setDevID(str);
                devInfo.setDevname(str);
                devData.add(devInfo);
                devAdapter.notifyDataSetChanged();
            }
        });

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mbcr);     //注销广播接收器
    }

    private void createProductList() {
        departList.clear();
        for (int i = 0; i < 3; i++) {
            department depart = new department();
            depart.depName = "缝制" + i + 1 + " A";
            depart.depCode = "0407003" + i;


            departList.add(i, depart);

        }
    }


}
