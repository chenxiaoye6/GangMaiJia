package com.example.administrator.gangmaijia.Adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.gangmaijia.Model.JiaGongOrder;
import com.example.administrator.gangmaijia.Model.Logistic;
import com.example.administrator.gangmaijia.R;
import com.example.administrator.gangmaijia.Util.HttpUtil;
import com.example.administrator.gangmaijia.Util.Internet;
import com.example.administrator.gangmaijia.Util.setListLenth;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/19.
 */

public class LogisticAdapter extends BaseAdapter {
    private List<Logistic> lists;
    private LayoutInflater mInflater;
    private Context mainContext;
    private HttpUtil httpUtil = new HttpUtil();
    private int p = -1;


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==1){
                String s = (String) msg.obj;
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("result").equals("0")){
                        if (p!=-1)
                            lists.remove(p);
                        notifyDataSetChanged();
                    }else {
                        Toast.makeText(mainContext,jsonObject.getString("msg"),Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                p = -1;
            }
        }
    };

    public LogisticAdapter(Context context, List<Logistic> dataList) {
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
            convertView = mInflater.inflate(R.layout.list_order_logistic, null);
            holder.tv_list_order_logistic_biaohao = (TextView) convertView.findViewById(R.id.tv_list_order_logistic_biaohao);
            holder.tv_list_order_logistic_changpin = (TextView) convertView.findViewById(R.id.tv_list_order_logistic_changpin);
            holder.tv_list_order_logistic_fahuo = (TextView) convertView.findViewById(R.id.tv_list_order_logistic_fahuo);
            holder.tv_list_order_logistic_shouhuo = (TextView) convertView.findViewById(R.id.tv_list_order_logistic_shouhuo);
            holder.tv_list_order_logistic_type = (TextView) convertView.findViewById(R.id.tv_list_order_logistic_type);
            holder.tv_list_order_logistic_dunwei = (TextView) convertView.findViewById(R.id.tv_list_order_logistic_dunwei);
            holder.tv_list_order_logistic_changdu = (TextView) convertView.findViewById(R.id.tv_list_order_logistic_changdu);
            holder.tv_list_order_logistic_date = (TextView) convertView.findViewById(R.id.tv_list_order_logistic_date);
            holder.tv_list_order_logistic_yaoqiu = (TextView) convertView.findViewById(R.id.tv_list_order_logistic_yaoqiu);
            holder.tv_list_order_logistic_close = (TextView) convertView.findViewById(R.id.tv_list_order_logistic_close);
            holder.iv_list_order_logistic_delete = (ImageView) convertView.findViewById(R.id.iv_list_order_logistic_delete);
            holder.iv_list_order_logistic_xiala = (ImageView) convertView.findViewById(R.id.iv_list_order_logistic_xiala);
            holder.ly_list_order_logistic_show = (LinearLayout) convertView.findViewById(R.id.ly_list_order_logistic_show);
            holder.ly_list_order_logistic_look = (LinearLayout) convertView.findViewById(R.id.ly_list_order_logistic_look);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        try {
            holder.tv_list_order_logistic_biaohao.setText(lists.get(position).getOrder_num());
            holder.tv_list_order_logistic_changpin.setText(lists.get(position).getProduct_name());
            holder.tv_list_order_logistic_dunwei.setText(lists.get(position).getTon_amount());
            holder.tv_list_order_logistic_fahuo.setText(lists.get(position).getDelivery_place());
            holder.tv_list_order_logistic_shouhuo.setText(lists.get(position).getDestination());
            holder.tv_list_order_logistic_type.setText(lists.get(position).getTransport_way());
            holder.tv_list_order_logistic_changdu.setText(lists.get(position).getLength());
            holder.tv_list_order_logistic_date.setText(lists.get(position).getDelivery_time());
            holder.tv_list_order_logistic_yaoqiu.setText(lists.get(position).getSpecial_demand());
        } catch (Exception e) {
            e.printStackTrace();
        }

        final ViewHolder finalHolder = holder;
        final ViewHolder finalHolder1 = holder;
        holder.ly_list_order_logistic_look.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!lists.get(position).isShowup()){
                    finalHolder.ly_list_order_logistic_show.setVisibility(View.VISIBLE);
                    finalHolder.tv_list_order_logistic_close.setText("收起");
                    finalHolder1.iv_list_order_logistic_xiala.setImageResource(R.mipmap.shangla);
                    lists.get(position).setShowup(true);
                    notifyDataSetChanged();
                }else {
                    finalHolder.ly_list_order_logistic_show.setVisibility(View.GONE);
                    finalHolder.tv_list_order_logistic_close.setText("查看");
                    finalHolder1.iv_list_order_logistic_xiala.setImageResource(R.mipmap.xiala);
                    lists.get(position).setShowup(false);
                    notifyDataSetChanged();
                }
            }
        });

        holder.iv_list_order_logistic_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p = position;
                List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                nvps.add(new BasicNameValuePair("phone_num",lists.get(position).getPhone_num()));
                nvps.add(new BasicNameValuePair("order_num",lists.get(position).getOrder_num()));
                httpUtil.post(mHandler, Internet.DELETE_LGOISTIC_URL,1,nvps);
            }
        });


        return convertView;
    }

    static class ViewHolder {
        TextView tv_list_order_logistic_biaohao;
        TextView tv_list_order_logistic_changpin;
        TextView tv_list_order_logistic_fahuo;
        TextView tv_list_order_logistic_shouhuo;
        TextView tv_list_order_logistic_type;
        TextView tv_list_order_logistic_dunwei;
        TextView tv_list_order_logistic_changdu;
        TextView tv_list_order_logistic_date;
        TextView tv_list_order_logistic_yaoqiu;
        TextView tv_list_order_logistic_close;
        ImageView iv_list_order_logistic_delete;
        ImageView iv_list_order_logistic_xiala;
        LinearLayout ly_list_order_logistic_show;
        LinearLayout ly_list_order_logistic_look;

    }
}
