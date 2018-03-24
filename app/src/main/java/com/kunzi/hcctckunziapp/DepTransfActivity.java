package com.kunzi.hcctckunziapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.yzq.zxinglibrary.common.Constant;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DepTransfActivity extends AppCompatActivity implements View.OnClickListener {
    private android.support.v7.widget.Toolbar toolbar;

    private ProductAdapter myadapter;
    private ListView listView;

    private List productList = new ArrayList<ProductModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dep_transf);
        createProductList();
        myadapter = new ProductAdapter(DepTransfActivity.this, R.layout.listview, productList);

        listView = findViewById(R.id.list_view);
        listView.setAdapter(myadapter);

        //工具条返回
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("部门调动");//标题
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(this);
    }

    private void createProductList() {
        for (int i = 0; i < 3; i++) {
            ProductModel product = new ProductModel();
            product.depName = "部门名称:S-1";
            product.perName = "A0000" + 1 + i;
            product.workHour = 8;
            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            product.workDate = sdf.format(d);
            productList.add(i, product);

        }
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
