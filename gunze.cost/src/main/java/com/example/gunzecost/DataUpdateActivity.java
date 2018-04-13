package com.example.gunzecost;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.model.department;
import com.sqlHelper.DBUtil;
import com.sqlHelper.HttpUtil;
import com.sqlHelper.WebServiceUtils;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

public class DataUpdateActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnGetDepart;
    private List<String> provinceList = new ArrayList<String>();
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_update);

        InitView();
    }

    private void InitView() {
        btnGetDepart = findViewById(R.id.btn_updatedepart);
        btnGetDepart.setOnClickListener(this);
        progressBar = findViewById(R.id.progressBar);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_updatedepart) {
//            //通过工具类调用WebService接口
//            WebServiceUtils.callWebService(WebServiceUtils.WEB_SERVER_URL, "getDepartInfo", null,new WebServiceUtils.WebServiceCallBack() {
//                //WebService接口返回的数据回调到这个方法中
//                @Override
//                public void callBack(SoapObject result) {
//                    if(result != null){
//                        provinceList = parseSoapObject(result);
//                    }else{
//                        Toast.makeText(DataUpdateActivity.this, "获取WebService数据错误", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
            queryFromServer(null, "province");
        }
    }




    private void queryFromServer(final String code, final String type) {


        HttpUtil.sendHttpRequest("GetDepart", new HttpUtil.HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                boolean result = false;
                showProgressDialog();
                Gson gson =new Gson();
                List<department> departments=gson.fromJson(response,new TypeToken<List<department>>(){}.getType());

            }

            @Override
            public void onError(Exception e) {

            }
        });

    }

    private void showProgressDialog() {
        if (progressBar.getVisibility() == ProgressBar.VISIBLE) {
            progressBar.setVisibility(ProgressBar.VISIBLE);
        } else {
            progressBar.setVisibility(ProgressBar.GONE);
        }
    }


}
