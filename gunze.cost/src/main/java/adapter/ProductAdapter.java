package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.gunzecost.R;

import java.lang.reflect.Array;
import java.util.List;

import model.DeviceInfo;

/**
 * Created by 刘畅 on 2018/3/24.
 */

public class ProductAdapter extends ArrayAdapter<DeviceInfo>{
    private int resourceId;
    /**
     * Constructor
     *
     * @param context  listView所在的上下文，也就是ListView所在的Activity
     * @param resource Cell的布局资源文件
     * @param objects  Cell上要显示的数据list，也就是实体类集合
     */
    public ProductAdapter(Context context, int resource, List<DeviceInfo> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    /**
     * @param position 当前设置的Cell行数，类似于iOS开发中的indexPath.row
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        DeviceInfo product = getItem(position);

        View productView = LayoutInflater.from(getContext()).inflate(resourceId, null);

        TextView deviceid =  productView.findViewById(R.id.device_id);
        TextView deviceName = productView.findViewById(R.id.device_name);

        deviceid.setText(product.devID);
        deviceName.setText(product.decname);


        return productView;
    }

}
