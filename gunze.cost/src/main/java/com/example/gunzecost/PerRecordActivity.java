package com.example.gunzecost;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TimePicker;
import android.widget.Toast;

import com.Receivers.BroadcastReceiver;
import com.common.commonHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.listAdapter.MyAdapter;
import com.model.department;
import com.model.deviceInfo;
import com.model.personRecord;
import com.sqlHelper.HttpUtil;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PerRecordActivity extends AppCompatActivity {
    private department department;
    private personRecord personRecord;
    private ArrayList<department> departData = new ArrayList<>();
    private ArrayList<personRecord> personData = new ArrayList<>();
    private MyAdapter<department> departAdapter = null;
    private MyAdapter<personRecord> personAdapter = null;
    private BroadcastReceiver barcodeReceiver = new BroadcastReceiver();
    private EditText txtDepart, txtDate;
    private View dialogView;
    private ProgressBar progressBar;
    private ListView listPerson, listDepart;
    private int mYear, mMonth, mDay;
    private Date dateToday;
    private int duplicateIndex,personItemPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_per_record);
        //注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.ACTION_DECODE_DATA");
        registerReceiver(barcodeReceiver, filter);
        //控件ID绑定
        txtDepart = findViewById(R.id.txtDepart);
        txtDate = findViewById(R.id.txtDate);
        listPerson = findViewById(R.id.lv_person);
        progressBar = findViewById(R.id.progressBar);
        //创建部门弹窗
        dialogView = View.inflate(PerRecordActivity.this, R.layout.dialog_department, null);

        listDepart = dialogView.findViewById(R.id.list_view);
        //注册DBHelper
        //costDB = CostDB.getInstance(this);

        LitePal.getDatabase();
        //获取日期
        Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);

        dateToday = commonHelper.string2Date(commonHelper.date2String(new Date()));
        txtDate.setText(commonHelper.date2String(new Date()));
        //填充适配器
        //人员
        personAdapter = new MyAdapter<personRecord>(personData, R.layout.listview_person) {
            @Override
            public void bindView(ViewHolder holder, personRecord obj) {
                holder.setText(R.id.item_perCode, obj.getPerCode());
                holder.setText(R.id.item_perName, obj.getPerName());
                holder.setText(R.id.item_workHour,obj.getWorkHour());
            }
        };

        listPerson.setAdapter(personAdapter);
        //部门
        departAdapter = new MyAdapter<department>(departData, R.layout.listview_department) {
            @Override
            public void bindView(ViewHolder holder, department obj) {
                holder.setText(R.id.item_code, obj.getcDepCode());
                holder.setText(R.id.item_Name, obj.getcDepName());
            }
        };
        listDepart.setAdapter(departAdapter);

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
                    new DatePickerDialog(PerRecordActivity.this, onDateSetListener, mYear, mMonth, mDay).show();
                }
                return true;
            }
        });
        //点击人员列表设置工时
        listPerson.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                personRecord = personData.get(position);
                personItemPosition=position;
                new TimePickerDialog(PerRecordActivity.this, 2, onTimeSetListener, 0, 0, true).show();
            }
        });
        //接收条码信息
        barcodeReceiver.setMessage(new BroadcastReceiver.Message() {
            @Override
            public void getMsg(String str) {
                if (txtDate.getText().toString().trim().equals("") || txtDepart.getText().toString().trim().equals("")) {
                    Toast.makeText(PerRecordActivity.this, "请先选择部门和日期", Toast.LENGTH_SHORT).show();
                } else {
                    deviceInfo depart = DataSupport.where("depCode=?", department.getcDepCode()).findFirst(deviceInfo.class);
                    if (depart == null) {
                        Toast.makeText(PerRecordActivity.this, "请先进行设备登记", Toast.LENGTH_SHORT).show();
                    } else {
                        QueryPersonRecord(str);
                    }
                }
            }
        });
    }

    private TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            personRecord.setWorkHour(String.valueOf(hourOfDay) +"."+ String.valueOf(minute));
            personRecord.save();
            personData.set(personItemPosition,personRecord);
            personAdapter.notifyDataSetChanged();
        }
    };
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
            InitData();
        }
    };

    /**
     * 弹出部门弹窗
     */
    private void ShowDepartment() {
        List<department> dep=DataSupport.findAll(department.class);
        if(dep.size()<=0){
            queryFromServer("GetDepart", "department", null);
        }else{
            departData.clear();
            departData.addAll(dep);
            if (departData.size() > 0) {

                departAdapter.notifyDataSetChanged();

                //创建部门弹窗
                ViewGroup parent = (ViewGroup) dialogView.getParent();
                if (parent != null) {
                    parent.removeAllViews();
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(PerRecordActivity.this);
                builder.setView(dialogView);
                final AlertDialog dialog = builder.show();

                listDepart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        department = departData.get(position);
                        txtDepart.setText(department.getcDepName());
                        InitData();
                        dialog.dismiss();
                    }
                });
            }
        }



    }

    /**
     * 进度条显示
     */
    private void showProgressBar() {
        if (progressBar.getVisibility() == View.GONE)
            progressBar.setVisibility(View.VISIBLE);
        else
            progressBar.setVisibility(View.GONE);
    }

    /**
     * 查询
     *
     * @param code
     * @param type
     * @param strParams
     */
    private void queryFromServer(final String code, final String type, final String strParams) {
        showProgressBar();
        HttpUtil.sendHttpRequest(code, new HttpUtil.HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                final String _response = response;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if ("person".equals(type)) {
                            Gson gson = new Gson();
                            personRecord = gson.fromJson(_response, new TypeToken<personRecord>() {
                            }.getType());
                            if (personRecord != null) {

                                personRecord.setDepCode(department.cDepCode);
                                personRecord.setDepName(department.cDepName);
                                personRecord.setWorkHour(String.valueOf(8));
                                personRecord.setRecordDate(commonHelper.string2Date(txtDate.getText().toString()));
                                personRecord.save();
                                personData.add(personRecord);
                                personAdapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(PerRecordActivity.this, "未查到此人", Toast.LENGTH_SHORT).show();
                            }
                            showProgressBar();
                        } else if ("department".equals(type)) {
                            Gson gson = new Gson();
                            departData.clear();
                            ArrayList<department> departDatas = gson.fromJson(_response, new TypeToken<List<department>>() {
                            }.getType());
                            departData.addAll(departDatas);
                            if (departData != null) {
                                for (department dep :departData) {
                                    dep.save();
                                }
                                ShowDepartment();
                            } else {
                                Toast.makeText(PerRecordActivity.this, "未获取到部门", Toast.LENGTH_SHORT).show();
                            }
                            showProgressBar();
                        }
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showProgressBar();
                        Toast.makeText(PerRecordActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }, strParams);
    }

    /**
     * 查询人员，优先查询数据库，没有再到服务器查询
     */
    private void QueryPersonRecord(String strBarcode) {
        final personRecord personInfo = DataSupport.where("perCode=?", strBarcode).findFirst(personRecord.class);

        if (personInfo == null) {
            queryFromServer("GetPersonByID", "person", "strID=" + strBarcode);
        } else {
            personRecord perTemp = null;
            //扫码时如果当天已登记设备
            for (int i = 0; i < personData.size(); i++) {
                if (personData.get(i).getPerCode().equals(personInfo.getPerCode())) {
                    duplicateIndex = i;
                    perTemp = personData.get(duplicateIndex);

                    //提示框，是否删除
                    AlertDialog.Builder dialog = new AlertDialog.Builder(PerRecordActivity.this);
                    dialog.setTitle("提示");
                    dialog.setMessage("是否取消登记？");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            personData.get(duplicateIndex).delete();
                            personData.remove(duplicateIndex);
                            personAdapter.notifyDataSetChanged();
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
            if (perTemp == null) {
                personRecord = new personRecord();
                personRecord.setPerCode(personInfo.getPerCode());
                personRecord.setPerName(personInfo.getPerName());
                personRecord.setDepCode(department.cDepCode);
                personRecord.setDepName(department.cDepName);
                personRecord.setWorkHour(String.valueOf(8));
                personRecord.setRecordDate(commonHelper.string2Date(txtDate.getText().toString()));
                personRecord.save();
                personData.add(personRecord);
                personAdapter.notifyDataSetChanged();
            }


        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(barcodeReceiver);     //注销广播接收器
    }

    private void InitData() {
        String strDepart = txtDepart.getText().toString().trim();
        String strDate = txtDate.getText().toString().trim();
        //初始化
        if (!strDepart.equals("") && !strDate.equals("")) {
            //查找当天信息，有则显示没有就不显示
            List<personRecord> persons = DataSupport.where("recordDate=? and depCode=?",
                    String.valueOf(commonHelper.string2Date(strDate).getTime()), department.getcDepCode()).find(personRecord.class);

            if (persons.size() == 0) {
                personData.clear();
            } else {
                personData.clear();
                personData.addAll(persons);
            }

            personAdapter.notifyDataSetChanged();

        }


    }
}
