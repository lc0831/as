package com.example.gunzecost;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.Receivers.BroadcastReceiver;
import com.dialog.DateDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.listAdapter.MyAdapter;
import com.model.department;
import com.model.deviceInfo;
import com.sqlHelper.HttpUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class DevManagerActivity extends AppCompatActivity {

    private EditText txtDepart, txtDate;
    private MyAdapter<deviceInfo> devAdapter = null;
    private ArrayList<deviceInfo> devData = new ArrayList<>();

    private ArrayList<department> departData = new ArrayList<>();
    BroadcastReceiver barcodeReceiver = new BroadcastReceiver();


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
        txtDate=findViewById(R.id.txtDate);
        ListView listDev = findViewById(R.id.lv_dev);


        //填充适配器
        //设备
        devAdapter = new MyAdapter<deviceInfo>(devData, R.layout.list_view) {
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
                showDatePickerFragemnt();
                /*
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
                */
                return true;
            }
        });
        barcodeReceiver.setMessage(new BroadcastReceiver.Message() {
            @Override
            public void getMsg(String str) {
                //Adapter初始化
                deviceInfo devInfo = new deviceInfo();
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
        unregisterReceiver(barcodeReceiver);     //注销广播接收器
    }


    private void showDatePickerFragemnt() {


        DialogFragment fragment = new DateDialog();
        fragment.show(getFragmentManager(), "datePicker");

    }
    /**
     * 从服务器获取部门信息填充到弹出框
     */
    private void ShowDepartment() {
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
                            MyAdapter<department> departAdapter = new MyAdapter<department>(departData, R.layout.list_view) {
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

                            listDepart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    View curr = parent.getChildAt((int) id);
                                    TextView c = curr.findViewById(R.id.item_Name);
                                    txtDepart.setText(c.getText());
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
        });


    }

    /**
     * 查询设备，优先查询数据库，没有再到服务器查询
     */
    private void QueryDevice() {

    }

}
