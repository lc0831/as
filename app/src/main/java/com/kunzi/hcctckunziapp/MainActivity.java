package com.kunzi.hcctckunziapp;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;



public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btnRecord,btnDepTransf,btnDepMana,btnPerMana;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

    }
    private void initView() {
        btnRecord=findViewById(R.id.btn_record);
        btnRecord.setOnClickListener(this);

        btnDepTransf=findViewById(R.id.btn_depTransf);
        btnDepTransf.setOnClickListener(this);

        btnDepMana=findViewById(R.id.btn_depMana);
        btnDepMana.setOnClickListener(this);

        btnPerMana=findViewById(R.id.btn_perMana);
        btnPerMana.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btn_record:
                intent =new Intent(this,RecordActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_depTransf:
                intent =new Intent(this,DepTransfActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_depMana:
                intent =new Intent(this,DepManaActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_perMana:
                intent =new Intent(this,PerManaActivity.class);
                startActivity(intent);
                break;

    }}
}
