package com.example.gunzecost;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.sqlHelper.DBUtil;

public class DataUpdateActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnGetDepart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_update);
        InitView();
    }

    private void InitView() {
        btnGetDepart = findViewById(R.id.btn_updatedepart);
        btnGetDepart.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_updatedepart) {
            String ret = DBUtil.QuerySQL();

            Bundle data = new Bundle();
            data.putString("result", ret);

        }
    }
}
