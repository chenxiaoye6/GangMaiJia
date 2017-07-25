package com.example.administrator.gangmaijia.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.gangmaijia.Model.Consumption;
import com.example.administrator.gangmaijia.R;

import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by hlj on 2016/10/30.
 */

public class ConsumptionAdapter1 extends BaseAdapter implements StickyListHeadersAdapter {

    private String[] countries;
    private LayoutInflater inflater;
    private List<Consumption> lists;
    private Context mainContext;

    public ConsumptionAdapter1(Context context, List<Consumption> dataList) {
        inflater = LayoutInflater.from(context);
        mainContext = context;
//        countries = context.getResources().getStringArray(R.array.countries);
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_mingxi, parent, false);
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

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = inflater.inflate(R.layout.header, parent, false);
            holder.tv_header = (TextView) convertView.findViewById(R.id.tv_header);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        //set header text as first char in name
//        String headerText = "" + countries[position].subSequence(0, 1).charAt(0);
        holder.tv_header.setText(lists.get(position).getD_month()+"月");
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        //return the first character of the country as ID because this is what headers are based upon
        return 1111111111;
//        return countries[position].subSequence(0, 1).charAt(0);
    }

    class HeaderViewHolder {
        TextView tv_header;
    }

    class ViewHolder {
        TextView tv_list_mingxi_weekday;
        TextView tv_list_mingxi_money;
        TextView tv_list_mingxi_type;
        TextView tv_list_mingxi_time ;
    }

}
