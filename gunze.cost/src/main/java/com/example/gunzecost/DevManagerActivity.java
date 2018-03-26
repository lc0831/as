package com.example.gunzecost;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.Receivers.BroadcastReceiver;
import com.listAdapter.DepartAdapter;
import com.model.department;
import com.sqlHelper.departManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class DevManagerActivity extends AppCompatActivity {

    private EditText txtDepart, txtDate;

    private List departList = new ArrayList<department>();
    BroadcastReceiver mbcr = new BroadcastReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_manager);
        //注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.ACTION_DECODE_DATA");
        registerReceiver(mbcr, filter);// 注册
        //选择部门
        findViewById(R.id.txtDepart).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //创建弹窗list
                    AlertDialog.Builder builder = new AlertDialog.Builder(DevManagerActivity.this);
                    View view = View.inflate(DevManagerActivity.this, R.layout.dialog_department, null);
                    ListView departListView = view.findViewById(R.id.list_view);
                    //填充弹窗数据
                    createProductList();
                    DepartAdapter myadapter = new DepartAdapter(DevManagerActivity.this, R.layout.list_view, departList);
                    departListView.setAdapter(myadapter);
                    //构建弹窗显示
                    builder.setView(view);
                    final AlertDialog dialog = builder.show();


                    departListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            View curr = parent.getChildAt((int) id);
                            TextView c = curr.findViewById(R.id.item_Name);
                            txtDepart.setText(c.getText().toString());
                            dialog.dismiss();
                        }
                    });


                }
                return true;
            }

        });
        findViewById(R.id.txtDate).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
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
                return true;
            }
        });

    }

    private void InitView() {

        txtDepart = findViewById(R.id.txtDepart);
        txtDepart.setOnTouchListener(this);

        txtDate = findViewById(R.id.txtDate);
        txtDate.setOnTouchListener(this);



      /*  departManager dm = new departManager(this);

        ArrayList<HashMap<String, String>> departList =  dm.getdepartList();*/
      /*  if(departList.size()!=0) {
            //绑定listview
            ProductAdapter adapter = new ProductAdapter(CustomeItemListViewActivity.this, R.layout.custome_item, productList);
            ListView listView = (ListView)findViewById(R.id.second_list_view);
            listView.setAdapter(adapter);
            ListView lv = getListView();
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                    student_Id = (TextView) view.findViewById(R.id.student_Id);
                    String studentId = student_Id.getText().toString();
                    Intent objIndent = new Intent(getApplicationContext(),StudentDetail.class);
                    objIndent.putExtra("student_Id", Integer.parseInt( studentId));
                    startActivity(objIndent);
                }
            });
            ListAdapter adapter = new SimpleAdapter( MainActivity.this,studentList, R.layout.view_student_entry, new String[] { "id","name"}, new int[] {R.id.student_Id, R.id.student_name});
            setListAdapter(adapter);
        }else{
            Toast.makeText(this, "No depart!", Toast.LENGTH_SHORT).show();
        }*/

    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            action = intent.getStringExtra("barcode_string");
        }

    };

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            AlertDialog.Builder builder;
            View view;
            final AlertDialog dialog;
            DepartAdapter myadapter;
            switch (v.getId()) {
                case R.id.txtDepart:
                    builder = new AlertDialog.Builder(this);
                    view = View.inflate(this, R.layout.dialog_department, null);
                    ListView departListView = view.findViewById(R.id.list_view);

                    createProductList();
                    myadapter = new DepartAdapter(this, R.layout.list_view, departList);
                    departListView.setAdapter(myadapter);
                    builder.setView(view);
                    dialog = builder.show();


                    departListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            View curr = parent.getChildAt((int) id);
                            TextView c = curr.findViewById(R.id.item_Name);
                            txtDepart.setText(c.getText().toString());
                            dialog.dismiss();
                        }
                    });

                    break;
                case R.id.txtDate:

                    break;
            }

        }


        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mbcr);     //注销广播接收器
    }

    private void createProductList() {
        for (int i = 0; i < 3; i++) {
            department depart = new department();
            depart.depName = "缝制" + i + 1 + " A";
            depart.depCode = "0407003" + i;


            departList.add(i, depart);

        }
    }


}
