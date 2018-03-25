package com.listAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.gunzecost.R;
import com.model.department;

import java.util.List;

import model.DeviceInfo;

/**
 * Created by 刘畅 on 2018/3/25.
 */

public class DepartAdapter extends ArrayAdapter<department> {
    private int resourceId;
    /**
     * Constructor
     *
     * @param context  listView所在的上下文，也就是ListView所在的Activity
     * @param resource Cell的布局资源文件
     * @param objects  Cell上要显示的数据list，也就是实体类集合
     */
    public DepartAdapter(Context context, int resource, List<department> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    /**
     * @param position 当前设置的Cell行数，类似于iOS开发中的indexPath.row
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        department depart = getItem(position);

        View departView = LayoutInflater.from(getContext()).inflate(resourceId, null);

        TextView departCode =  departView.findViewById(R.id.item_code);
        TextView departName = departView.findViewById(R.id.item_Name);

        departCode.setText(depart.depCode);
        departName.setText(depart.depName);


        return departView;
    }
}
