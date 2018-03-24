package com.kunzi.hcctckunziapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 刘畅 on 2018/3/4.
 */

public class MyShowAdapter extends BaseAdapter {

    private Context context;
    private List list;
    public MyShowAdapter(Context context, List list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=LayoutInflater.from(context).inflate(R.layout.listview, null);
        }
        ProductModel product = (ProductModel) getItem(position);



        TextView depName =  convertView.findViewById(R.id.product_depName);
        TextView perName = convertView.findViewById(R.id.product_perName);
        TextView workHour = convertView.findViewById(R.id.product_workHour);
        TextView workDate =  convertView.findViewById(R.id.product_workDate);

        depName.setText(product.depName);
        perName.setText(product.perName);
        workHour.setText(product.workHour);
        workDate.setText(product.workDate);
        return convertView;
    }

}