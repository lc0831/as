package com.example.gunzecost;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnPerRecord, btnDevManager, btnPerManager, btnDataUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        btnPerRecord = findViewById(R.id.btnPerRecord);
        btnPerRecord.setOnClickListener(this);
        btnDevManager = findViewById(R.id.btnDevManager);
        btnDevManager.setOnClickListener(this);
        btnPerManager = findViewById(R.id.btnPerManager);
        btnPerManager.setOnClickListener(this);
        btnDataUpdate = findViewById(R.id.btnDataUpdate);
        btnDataUpdate.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.btnPerRecord:
                intent=new Intent(this,PerRecordActivity.class);
                startActivity(intent);
                break;
            case R.id.btnDevManager:
                intent=new Intent(this,DevManagerActivity.class);
                startActivity(intent);
                break;
            case R.id.btnPerManager:
                intent=new Intent(this,PerManagerActivity.class);
                startActivity(intent);
                break;
            case R.id.btnDataUpdate:
                intent=new Intent(this,DataUpdateActivity.class);
                startActivity(intent);
                break;
        }
    }
}
