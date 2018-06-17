package com.example.gunzecost;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.common.commonHelper;
import com.google.gson.Gson;
import com.model.personRecord;
import com.sqlHelper.HttpUtil;

import org.litepal.crud.DataSupport;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SysOptionActivity extends AppCompatActivity {
    private EditText strIP, strPost;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sys_option);
        strIP = findViewById(R.id.txtIP);
        strPost = findViewById(R.id.txtPort);
        findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Save(strIP.getText().toString(), strPost.getText().toString());
            }
        });
        Load();
    }

    public void Load() {
        FileInputStream in;
        BufferedReader reader = null;
        List<String> data = new ArrayList<>();
        try {
            in = openFileInput("serverIP");
            reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                data.add(line);
            }
        } catch (IOException e) {
            Toast.makeText(SysOptionActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                    Toast.makeText(SysOptionActivity.this, "保存成功！", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                Toast.makeText(SysOptionActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
        if(data!=null &&data.size()>0){

            strIP.setText(data.get(0));
            strPost.setText(data.get(1));
        }

    }

    public void Save(String strIP, String strPort) {
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            out = openFileOutput("serverIP", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(strIP);
            writer.newLine();
            writer.write(strPort);
        } catch (IOException e) {
            Toast.makeText(SysOptionActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                    Toast.makeText(SysOptionActivity.this, "保存成功！", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                Toast.makeText(SysOptionActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }
}
