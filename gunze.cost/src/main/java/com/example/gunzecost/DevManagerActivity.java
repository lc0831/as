package com.example.gunzecost;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.sqlHelper.departManager;
import com.text.sql.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import adapter.ProductAdapter;

public class DevManagerActivity extends AppCompatActivity implements View.OnClickListener {
    Calendar calendar = Calendar.getInstance(Locale.CHINA);
    private EditText txtDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_manager);
        InitView();
    }

    private void InitView() {
        txtDate = findViewById(R.id.txtDate);
        txtDate.setOnTouchListener(this);

        departManager dm = new departManager(this);

        ArrayList<HashMap<String, String>> departList =  dm.getdepartList();
        if(departList.size()!=0) {
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
        }

    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.datapicker, null);
        final DatePicker datePicker =  view.findViewById(R.id.date_picker);

        builder.setView(view);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null);


        Dialog dialog = builder.create();
        dialog.show();
    }


}
