package com.example.administrator.gangmaijia.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.gangmaijia.Model.Bell;
import com.example.administrator.gangmaijia.Model.Consumption;
import com.example.administrator.gangmaijia.R;

import java.util.List;

/**
 * Created by wangliang on 2016/10/28.
 */

public class ConsumptionAdapter extends BaseAdapter {
    private List<Consumption> lists;
    private LayoutInflater mInflater;
    private Context mainContext;

    public ConsumptionAdapter(Context context, List<Consumption> dataList) {
        mInflater = LayoutInflater.from(context);
        mainContext = context;
        lists = dataList;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.list_mingxi, null);
            holder.tv_list_mingxi_weekday = (TextView) convertView.findViewById(R.id.tv_list_mingxi_weekday);
            holder.tv_list_mingxi_time = (TextView) convertView.findViewById(R.id.tv_list_mingxi_time);
            holder.tv_list_mingxi_money = (TextView) convertView.findViewById(R.id.tv_list_mingxi_money);
            holder.tv_list_mingxi_type = (TextView) convertView.findViewById(R.id.tv_list_mingxi_type);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        try {
            holder.tv_list_mingxi_weekday.setText(lists.get(position).getD_week());
            holder.tv_list_mingxi_time.setText(lists.get(position).getD_monthday());
            holder.tv_list_mingxi_money.setText(lists.get(position).getD_amount());
            if (lists.get(position).getType()==0){
                holder.tv_list_mingxi_type.setText("消费");
            }else {
                holder.tv_list_mingxi_type.setText("充值");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }





        return convertView;
    }

    static class ViewHolder {
        TextView tv_list_mingxi_weekday;
        TextView tv_list_mingxi_money;
        TextView tv_list_mingxi_type;
        TextView tv_list_mingxi_time ;
    }

}
