package com.example.administrator.gangmaijia.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.gangmaijia.Model.Bell;
import com.example.administrator.gangmaijia.Model.Province;
import com.example.administrator.gangmaijia.R;

import java.util.List;

/**
 * Created by Administrator on 2016/10/15.
 */

public class BellAdapter extends BaseAdapter {
    private List<Bell> lists;
    private LayoutInflater mInflater;
    private Context mainContext;

    public BellAdapter(Context context, List<Bell> dataList) {
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
            convertView = mInflater.inflate(R.layout.list_setbell, null);
            holder.tv_list_bell = (TextView) convertView.findViewById(R.id.tv_list_bell);
            holder.iv_list_bell_select = (ImageView) convertView.findViewById(R.id.iv_list_bell_select);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        try {
            holder.tv_list_bell.setText(lists.get(position).getBellName());
            if (lists.get(position).getIsCheck().equals("1")){
                holder.iv_list_bell_select.setImageResource(R.drawable.circlecase_blue);
            }else {
                holder.iv_list_bell_select.setImageResource(R.drawable.circleemptycase_blue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        final ViewHolder finalHolder = holder;
        final ViewHolder finalHolder1 = holder;
        holder.iv_list_bell_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0;i<lists.size();i++){
                    if (i==position){
                        lists.get(i).setIsCheck("1");
                        finalHolder.iv_list_bell_select.setImageResource(R.drawable.circlecase_blue);
                    }else {
                        lists.get(i).setIsCheck("0");
                        finalHolder1.iv_list_bell_select.setImageResource(R.drawable.circleemptycase_blue);
                    }
                }
                notifyDataSetChanged();
            }
        });



        return convertView;
    }

    static class ViewHolder {
        TextView tv_list_bell;
        ImageView iv_list_bell_select ;
    }

}
