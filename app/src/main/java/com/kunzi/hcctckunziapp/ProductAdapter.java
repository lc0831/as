package com.kunzi.hcctckunziapp;

/**
 * Created by 刘畅 on 2018/3/4.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by lizelu on 15/12/20.
 * Adapter类似于iOS开发中UITableViewCell源文件，就是给每个Cell赋值的
 */
public class ProductAdapter extends ArrayAdapter<ProductModel> {
    private int resourceId;
    /**
     * Constructor
     *
     * @param context  listView所在的上下文，也就是ListView所在的Activity
     * @param resource Cell的布局资源文件
     * @param objects  Cell上要显示的数据list，也就是实体类集合
     */
    public ProductAdapter(Context context, int resource, List<ProductModel> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    /**
     * @param position 当前设置的Cell行数，类似于iOS开发中的indexPath.row
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        ProductModel product = getItem(position);

        View productView = LayoutInflater.from(getContext()).inflate(resourceId, null);

        TextView depName =  productView.findViewById(R.id.product_depName);
        TextView perName = productView.findViewById(R.id.product_perName);
        TextView workHour = productView.findViewById(R.id.product_workHour);
        TextView workDate =  productView.findViewById(R.id.product_workDate);

        depName.setText(product.depName);
        perName.setText(product.perName);
        workHour.setText(""+product.workHour);
        workDate.setText(product.workDate);

        return productView;
    }
}