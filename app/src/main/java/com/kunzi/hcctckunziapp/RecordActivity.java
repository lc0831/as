package com.kunzi.hcctckunziapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import java.text.SimpleDateFormat;

import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecordActivity extends AppCompatActivity implements View.OnClickListener {
    private android.support.v7.widget.Toolbar toolbar;
    private List productList = new ArrayList<ProductModel>();
    private TextView barcodeText;
    private ProductAdapter myadapter;
    private ListView listView;
    private String workDate, workDep;
    private BootstrapButton btnDep, btnPer;
    private int REQUEST_CODE_SCAN_DEP = 1;
    private int REQUEST_CODE_SCAN_PER = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        TypefaceProvider.registerDefaultIconSets();

        initView();
    }

    private void initView() {
        //部门
        btnDep = findViewById(R.id.btn_depLogin);
        btnDep.setOnClickListener(this);

        //人员
        btnPer = findViewById(R.id.btn_perLogin);
        btnPer.setOnClickListener(this);

      //工具条返回
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("设备登记");//标题
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(this);

        //加载数据
        myadapter = new ProductAdapter(RecordActivity.this, R.layout.listview, productList);

        listView = findViewById(R.id.list_view);
        listView.setAdapter(myadapter);

    }

    @Override
    public void onClick(View v) {

        Intent intent ;
        ZxingConfig config ;

        switch (v.getId()) {
            case R.id.btn_depLogin:
                intent = new Intent(RecordActivity.this, CaptureActivity.class);
                config = new ZxingConfig();
                config.setPlayBeep(true);
                config.setShake(true);
                intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
                startActivityForResult(intent, REQUEST_CODE_SCAN_DEP);
                break;
            case R.id.btn_perLogin:
                intent = new Intent(RecordActivity.this, CaptureActivity.class);
                config = new ZxingConfig();
                config.setPlayBeep(true);
                config.setShake(true);
                intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
                startActivityForResult(intent, REQUEST_CODE_SCAN_PER);
                break;
            case -1:
                finish();
                break;


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN_DEP && resultCode == RESULT_OK) {
            if (data != null) {
                workDep = data.getStringExtra(Constant.CODED_CONTENT);
            }
        }
        if (requestCode == REQUEST_CODE_SCAN_PER && resultCode == RESULT_OK) {
            if (data != null) {
                if (workDep == "" || workDep == null) {
                    Toast.makeText(RecordActivity.this, "请先登录设备", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (workDate == "" || workDate == null) {

                    Date d = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    workDate = sdf.format(d);
                }

                ProductModel product = new ProductModel();
                product.depName = "部门名称:" + workDep;
                product.perName = data.getStringExtra(Constant.CODED_CONTENT);
                product.workHour = 8;
                product.workDate = workDate;
                productList.add(product);
                myadapter.notifyDataSetChanged();
            }
        }
    }


}

