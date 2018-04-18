package com.example.gunzecost;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.listAdapter.MyAdapter;
import com.model.department;
import com.model.deviceInfo;
import com.model.personRecord;

import java.util.ArrayList;

public class PerRecordActivity extends AppCompatActivity {
    private department department;
    private ArrayList<department> departData = new ArrayList<>();
    private MyAdapter<department> departAdapter = null;
    private MyAdapter<personRecord> personAdapter = null;
    private EditText txtDepart, txtDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_per_record);
        //控件ID绑定
        txtDepart=findViewById(R.id.txtDepart);
        txtDate=findViewById(R.id.txtDate);
        ListView listPerson = findViewById(R.id.lv_person);
        //填充适配器
        //人员
        personAdapter = new MyAdapter<deviceInfo>(devData, R.layout.listview_device) {
            @Override
            public void bindView(ViewHolder holder, deviceInfo obj) {
                holder.setText(R.id.item_code, obj.getDevID());
                holder.setText(R.id.item_Name, obj.getDevname());
            }
        };
        listDev.setAdapter(devAdapter);
        //部门
        departAdapter = new MyAdapter<department>(departData, R.layout.listview_department) {
            @Override
            public void bindView(ViewHolder holder, department obj) {
                holder.setText(R.id.item_code, obj.getcDepCode());
                holder.setText(R.id.item_Name, obj.getcDepName());
            }
        };
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
    }
    private void ShowDepartment(){
        if (departData.size() > 0) {
            MyAdapter<department> departAdapter = new MyAdapter<department>(departData, R.layout.listview_department) {
                @Override
                public void bindView(ViewHolder holder, department obj) {
                    holder.setText(R.id.item_code, obj.getcDepCode());
                    holder.setText(R.id.item_Name, obj.getcDepName());
                }
            };
            View dialogView = View.inflate(DevManagerActivity.this, R.layout.dialog_department, null);
            ListView listDepart = dialogView.findViewById(R.id.list_view);
            listDepart.setAdapter(departAdapter);

            //创建部门弹窗
            AlertDialog.Builder builder = new AlertDialog.Builder(DevManagerActivity.this);
            builder.setView(dialogView);
            final AlertDialog dialog = builder.show();
            showProgressBar();
            listDepart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                                    View curr = parent.getChildAt(position);
//                                    txtItemName = curr.findViewById(R.id.item_Name);
                    department = departData.get(position);
                    txtDepart.setText(department.getcDepName());
                    dialog.dismiss();
                }
            });
        }
    }

}
