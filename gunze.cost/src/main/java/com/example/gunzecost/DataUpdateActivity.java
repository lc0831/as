package com.example.gunzecost;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.common.commonHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.model.department;
import com.model.personRecord;
import com.sqlHelper.HttpUtil;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DataUpdateActivity extends AppCompatActivity {

    private List<String> list;
    private ArrayAdapter<String> adapter;
    private Spinner spDown;
    private String cityName;
    private int mYear, mMonth;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private Date beginDate, endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_update);
        /*设置数据源*/
        list = new ArrayList<>();
        list.add("1月");
        list.add("2月");
        list.add("3月");
        list.add("4月");
        list.add("5月");
        list.add("6月");
        list.add("7月");
        list.add("8月");
        list.add("9月");
        list.add("10月");
        list.add("11月");
        list.add("12月");
        //获取日期
        Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        spDown = findViewById(R.id.selectMoth);

        final OkHttpClient client = new OkHttpClient();
        /*新建适配器*/
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);

        /*adapter设置一个下拉列表样式，参数为系统子布局*/
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        /*spDown加载适配器*/
        spDown.setAdapter(adapter);
        spDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cityName = adapter.getItem(position);   //获取选中的那一项
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //上传
        findViewById(R.id.upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMonth = Integer.valueOf(cityName.substring(0, cityName.length() - 1));
                beginDate = commonHelper.getSupportBeginDayofMonth(mYear, mMonth);
                endDate = commonHelper.getSupportEndDayofMonth(mYear, mMonth);
                //提示框，是否删除
                AlertDialog.Builder dialog = new AlertDialog.Builder(DataUpdateActivity.this);
                dialog.setTitle("提示");
                dialog.setMessage("当前数据库中已有本月数据，是否覆盖？");
                dialog.setCancelable(false);
                dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UploadData();
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
            }
        });
    }

    public void UploadData() {
        List<personRecord> persons = DataSupport.where("recordDate>? and recordDate<?",
                String.valueOf(commonHelper.string2Date(commonHelper.date2String(beginDate)).getTime()),
                String.valueOf(commonHelper.string2Date(commonHelper.date2String(endDate)).getTime())).find(personRecord.class);
        Gson gson = new Gson();
        String personList = gson.toJson(persons);
        HttpUtil.sendOkHttpRequest("UploadPersonRecords",personList, new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(DataUpdateActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseData = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(DataUpdateActivity.this, responseData, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

}
