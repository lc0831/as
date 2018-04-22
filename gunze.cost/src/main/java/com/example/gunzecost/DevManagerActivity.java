package com.example.gunzecost;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.Receivers.BroadcastReceiver;
import com.common.commonHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.listAdapter.MyAdapter;
import com.model.department;
import com.model.deviceInfo;
import com.model.personRecord;
import com.sqlHelper.CostDB;
import com.sqlHelper.DBHelper;
import com.sqlHelper.HttpUtil;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class DevManagerActivity extends AppCompatActivity {

    private EditText txtDepart, txtDate;
    private ProgressBar progressBar;
    private MyAdapter<deviceInfo> devAdapter = null;
    private ArrayList<deviceInfo> devData = new ArrayList<>();
    private ArrayList<department> departData = new ArrayList<>();
    BroadcastReceiver barcodeReceiver = new BroadcastReceiver();
    private int mYear, mMonth, mDay;
    //private CostDB costDB;
    private deviceInfo deviceInfo;
    private department department;
    private Date dateToday;
    private int duplicateIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_manager);
        //注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.ACTION_DECODE_DATA");
        registerReceiver(barcodeReceiver, filter);
        // 注册控件
        txtDepart = findViewById(R.id.txtDepart);
        txtDate = findViewById(R.id.txtDate);
        progressBar = findViewById(R.id.progressBar);
        ListView listDev = findViewById(R.id.lv_dev);
        //注册DBHelper
        //costDB = CostDB.getInstance(this);

        LitePal.getDatabase();
        //获取日期
        Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);
        dateToday = commonHelper.string2Date(commonHelper.date2String(new Date()));

        //初始化
        InitData();
        //填充适配器
        //设备
        devAdapter = new MyAdapter<deviceInfo>(devData, R.layout.listview_device) {
            @Override
            public void bindView(ViewHolder holder, deviceInfo obj) {
                holder.setText(R.id.item_code, obj.getDevID());
                holder.setText(R.id.item_Name, obj.getDevname());
            }
        };
        listDev.setAdapter(devAdapter);


        //选择部门
        findViewById(R.id.txtDepart).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //填充弹窗数据
                    ShowDepartment();
                }
                return true;
            }
        });
        //选择日期
        findViewById(R.id.txtDate).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    new DatePickerDialog(DevManagerActivity.this, onDateSetListener, mYear, mMonth, mDay).show();
                }
                return true;
            }
        });
        //接收条码信息
        barcodeReceiver.setMessage(new BroadcastReceiver.Message() {
            @Override
            public void getMsg(String str) {
                if (txtDate.getText().toString().trim().equals("") || txtDepart.getText().toString().trim().equals("")) {
                    Toast.makeText(DevManagerActivity.this, "请先选择部门和日期", Toast.LENGTH_SHORT).show();
                } else {
                    QueryDevice(str);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(barcodeReceiver);     //注销广播接收器
    }

    private void InitData() {
        //初始化
        //首先查找数据库中是否有设备登记记录
        deviceInfo device = DataSupport.order("recordDate desc").findFirst(deviceInfo.class);
        if (device != null) {
            //如果有记录，查找最后一天登记的信息
            List<deviceInfo> devices = DataSupport.where("recordDate=? and depCode=?",
                    String.valueOf(device.getRecordDate().getTime()), device.getDepcode()).find(deviceInfo.class);

            //如果是当天信息，则直接显示，否则新增当天信息

            if (device.getRecordDate().getTime() != dateToday.getTime()) {
                for (int i = 0; i < devices.size(); i++) {
                    devices.get(i).setRecordDate(dateToday);
                }
                DataSupport.saveAll(devices);
            }

            department = new department();
            department.setcDepCode(device.getDepcode());
            department.setcDepName(device.getDepName());
            txtDepart.setText(device.getDepName());
            txtDate.setText(commonHelper.date2String(devices.get(0).getRecordDate()));
            devData = (ArrayList<com.model.deviceInfo>) devices;
        }
    }

    /**
     * 日期选择器对话框监听
     */
    private DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            String days;
            if (mMonth + 1 < 10) {
                if (mDay < 10) {
                    days = new StringBuffer().append(mYear).append("-").append("0").
                            append(mMonth + 1).append("-").append("0").append(mDay).toString();
                } else {
                    days = new StringBuffer().append(mYear).append("-").append("0").
                            append(mMonth + 1).append("-").append(mDay).toString();
                }

            } else {
                if (mDay < 10) {
                    days = new StringBuffer().append(mYear).append("-").
                            append(mMonth + 1).append("-").append("0").append(mDay).toString();
                } else {
                    days = new StringBuffer().append(mYear).append("-").
                            append(mMonth + 1).append("-").append(mDay).toString();
                }

            }
            txtDate.setText(days);
            //更换日期改变
            LoadData();
        }
    };


    /**
     * 从服务器获取部门信息填充到弹出框
     */
    private void ShowDepartment() {
        showProgressBar();
        HttpUtil.sendHttpRequest("GetDepart", new HttpUtil.HttpCallbackListener() {
            @Override
            public void onFinish(String response) {

                final String _response = response;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        departData = gson.fromJson(_response, new TypeToken<List<department>>() {
                        }.getType());
                        if (departData.size() > 0) {
                            MyAdapter<department> departAdapter = new MyAdapter<department>(departData, R.layout.listview_department) {
                                @Override
                                public void bindView(ViewHolder holder, department obj) {
                                    holder.setText(R.id.item_code, obj.getcDepCode());
                                    holder.setText(R.id.item_Name, obj.getcDepName());
                                }
                            };
                            View dialogView = View.inflate(DevManagerActivity.this, R.layout.dialog_department, null);
                            ListView listDepart = dialogView.findViewById(R.id.list_view);
                            listDepart.setAdapter(departAdapter);

                            //创建部门弹窗
                            AlertDialog.Builder builder = new AlertDialog.Builder(DevManagerActivity.this);
                            builder.setView(dialogView);
                            final AlertDialog dialog = builder.show();
                            showProgressBar();
                            listDepart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                                    View curr = parent.getChildAt(position);
//                                    txtItemName = curr.findViewById(R.id.item_Name);
                                    department = departData.get(position);
                                    txtDepart.setText(department.getcDepName());
                                    LoadData();
                                    dialog.dismiss();
                                }
                            });
                        }
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(DevManagerActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }, "");


    }
private void LoadData(){
    String strDepart = txtDepart.getText().toString().trim();
    String strDate = txtDate.getText().toString().trim();
    if (!strDepart.equals("") &&!strDate.equals("")) {
        List<deviceInfo> devices = DataSupport.where("recordDate=? and depCode=?",
                String.valueOf(commonHelper.string2Date(strDate).getTime()), department.getcDepCode()).find(deviceInfo.class);
        //如果数据库没有选定日期数据，显示空，有添加
        if (devices.size() == 0) {
            devData.clear();
        } else {
            devData.clear();
            devData.addAll(devices);
        }
        devAdapter.notifyDataSetChanged();
    }
}
    private void showProgressBar() {
        if (progressBar.getVisibility() == View.GONE)
            progressBar.setVisibility(View.VISIBLE);
        else
            progressBar.setVisibility(View.GONE);
    }


    /**
     * 查询设备，优先查询数据库，没有再到服务器查询
     */
    private void QueryDevice(String strBarcode) {
        final deviceInfo devInfo = DataSupport.where("devID=?", strBarcode).findFirst(deviceInfo.class);
        //deviceInfo = costDB.getDeviceById(strBarcode);
        if (devInfo == null) {
            queryFromServer("GetDeviceByID", "device", "strID=" + strBarcode);
        } else {
            deviceInfo devTemp = null;
            //扫码时如果当天已登记设备
            for (int i = 0; i < devData.size(); i++) {
                if (devData.get(i).getDevID().equals(devInfo.getDevID())) {
                    duplicateIndex = i;
                    devTemp = devData.get(duplicateIndex);

                    //提示框，是否删除
                    AlertDialog.Builder dialog = new AlertDialog.Builder(DevManagerActivity.this);
                    dialog.setTitle("提示");
                    dialog.setMessage("是否取消登记此设备？");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            devData.get(duplicateIndex).delete();
                            devData.remove(duplicateIndex);

                            devAdapter.notifyDataSetChanged();
                        }
                    });
                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dialog.show();

                    break;
                }
            }
            //没有重复的，插入
            if (devTemp == null) {
                deviceInfo = new deviceInfo();
                deviceInfo.setDevID(devInfo.getDevID());
                deviceInfo.setDevname(devInfo.getDevname());
                deviceInfo.setDepcode(department.cDepCode);
                deviceInfo.setDepName(department.cDepName);
                deviceInfo.setRecordDate(commonHelper.string2Date(txtDate.getText().toString()));
                deviceInfo.save();
                devData.add(deviceInfo);
                devAdapter.notifyDataSetChanged();
            }


        }
    }

    private void queryFromServer(final String code, final String type, final String strParams) {
        showProgressBar();
        HttpUtil.sendHttpRequest(code, new HttpUtil.HttpCallbackListener() {
            @Override
            public void onFinish(String response) {

                if ("device".equals(type)) {
                    Gson gson = new Gson();
                    deviceInfo = gson.fromJson(response, new TypeToken<deviceInfo>() {
                    }.getType());
                }
                if (deviceInfo != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            deviceInfo.setDepcode(department.cDepCode);
                            deviceInfo.setDepName(department.cDepName);
                            deviceInfo.setRecordDate(commonHelper.string2Date(txtDate.getText().toString()));
                            deviceInfo.save();
                            //costDB.insertDevice(deviceInfo);
                            devData.add(deviceInfo);
                            devAdapter.notifyDataSetChanged();
                        }
                    });
                } else {
                    Toast.makeText(DevManagerActivity.this, "未查到此人", Toast.LENGTH_SHORT).show();
                }
                showProgressBar();
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showProgressBar();
                        Toast.makeText(DevManagerActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }, strParams);
    }

}
