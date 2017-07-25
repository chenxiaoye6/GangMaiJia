package com.example.administrator.gangmaijia.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.gangmaijia.Model.Province;
import com.example.administrator.gangmaijia.R;

import java.util.List;

/**
 * Created by Administrator on 2016/10/14.
 */

public class CityAdapter extends BaseAdapter {

    private List<Province> lists;
    private LayoutInflater mInflater;
    private Context mainContext;

    public CityAdapter(Context context, List<Province> dataList) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.list_city, null);
            holder.tv_list_newmatch_title = (TextView) convertView.findViewById(R.id.tv_city_list);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        try {
            holder.tv_list_newmatch_title.setText(lists.get(position).getProvinceName());
        } catch (Exception e) {
            e.printStackTrace();
        }



        return convertView;
    }

    static class ViewHolder {
        TextView tv_list_newmatch_title;
    }
}
