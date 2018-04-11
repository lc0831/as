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

    /**
     * 解析SoapObject对象
     *
     * @param result
     * @return
     */
    private List<String> parseSoapObject(SoapObject result) {
        List<String> list = new ArrayList<String>();
        SoapObject provinceSoapObject = (SoapObject) result.getProperty("getSupportProvinceResult");
        if (provinceSoapObject == null) {
            return null;
        }
        for (int i = 0; i < provinceSoapObject.getPropertyCount(); i++) {
            list.add(provinceSoapObject.getProperty(i).toString());
        }

        return list;
    }

    private void queryFromServer(final String code, final String type) {
        String address;
        //address = "http://www.weather.com.cn/data/list3/city.xml";
        address="http://192.168.3.164:6666/api/webapi";
        HttpUtil.sendHttpRequest(address, new HttpUtil.HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                boolean result = false;
                if ("province".equals(type)) {

                }
            }

            @Override
            public void onError(Exception e) {

            }
        });

    }


}
