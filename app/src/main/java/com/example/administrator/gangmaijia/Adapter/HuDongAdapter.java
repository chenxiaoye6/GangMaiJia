package com.example.administrator.gangmaijia.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.gangmaijia.Model.HuDongBean;
import com.example.administrator.gangmaijia.R;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2017/7/22.
 */

public class HuDongAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<HuDongBean.DataBean> list;

    public HuDongAdapter(Context context, ArrayList<HuDongBean.DataBean> hudongList) {
        this.context = context;
        this.list = hudongList;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder vh = null;
        if (vh == null) {
            view = View.inflate(context, R.layout.hudong_item, null);
            vh = new ViewHolder(view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        HuDongBean.DataBean dataBean = list.get(position);
        if (dataBean != null) {
            vh.hudongItemType.setText(dataBean.getCategory());
            vh.hudongItemName.setText("用户 :" + dataBean.getPhone_num());
            vh.hudongItemContent.setText(dataBean.getContent());
            vh.hudongItemInfo.setText(dataBean.getTimess().split(" ")[0] + "   " + dataBean.getArea());
        }
        return view;
    }

    static class ViewHolder {
        @InjectView(R.id.hudong_item_type)
        TextView hudongItemType;
        @InjectView(R.id.hudong_item_name)
        TextView hudongItemName;
        @InjectView(R.id.hudong_item_content)
        TextView hudongItemContent;
        @InjectView(R.id.hudong_item_info)
        TextView hudongItemInfo;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
