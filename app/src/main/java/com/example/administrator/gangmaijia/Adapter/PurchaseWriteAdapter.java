package com.example.administrator.gangmaijia.Adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.gangmaijia.Model.JiaGongOrder;
import com.example.administrator.gangmaijia.Model.PurchaseWrite;
import com.example.administrator.gangmaijia.R;
import com.example.administrator.gangmaijia.Util.HttpUtil;
import com.example.administrator.gangmaijia.Util.Internet;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/21.
 */

public class PurchaseWriteAdapter extends BaseAdapter {
    private List<PurchaseWrite> lists;
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

    public PurchaseWriteAdapter(Context context, List<PurchaseWrite> dataList) {
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
            convertView = mInflater.inflate(R.layout.list_order_write, null);
            holder.tv_list_order_write_biaohao = (TextView) convertView.findViewById(R.id.tv_list_order_write_biaohao);
            holder.tv_list_order_write_pinming = (TextView) convertView.findViewById(R.id.tv_list_order_write_pinming);
            holder.tv_list_order_write_guige = (TextView) convertView.findViewById(R.id.tv_list_order_write_guige);
            holder.tv_list_order_write_dunwei = (TextView) convertView.findViewById(R.id.tv_list_order_write_dunwei);
            holder.tv_list_order_write_caizhi = (TextView) convertView.findViewById(R.id.tv_list_order_write_caizhi);
            holder.tv_list_order_write_beizhu = (TextView) convertView.findViewById(R.id.tv_list_order_write_beizhu);
            holder.tv_list_order_write_type = (TextView) convertView.findViewById(R.id.tv_list_order_write_type);
            holder.tv_list_order_write_close = (TextView) convertView.findViewById(R.id.tv_list_order_write_close);
            holder.iv_list_order_write_delete = (ImageView) convertView.findViewById(R.id.iv_list_order_write_delete);
            holder.iv_list_order_write_xiala = (ImageView) convertView.findViewById(R.id.iv_list_order_write_xiala);
            holder.ly_list_order_write_show = (LinearLayout) convertView.findViewById(R.id.ly_list_order_write_show);
            holder.ly_list_order_write_look = (LinearLayout) convertView.findViewById(R.id.ly_list_order_write_look);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        try {
            holder.tv_list_order_write_biaohao.setText(lists.get(position).getOrder_num());
            holder.tv_list_order_write_pinming.setText(lists.get(position).getProduct_name());
            holder.tv_list_order_write_dunwei.setText(lists.get(position).getTon_amount());
            holder.tv_list_order_write_guige.setText(lists.get(position).getStandard());
            holder.tv_list_order_write_caizhi.setText(lists.get(position).getMaterial());
            holder.tv_list_order_write_beizhu.setText(lists.get(position).getComment());
            holder.tv_list_order_write_type.setText(lists.get(position).getAcceptor());
        } catch (Exception e) {
            e.printStackTrace();
        }

        final ViewHolder finalHolder = holder;
        final ViewHolder finalHolder1 = holder;
        holder.ly_list_order_write_look.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!lists.get(position).isShowup()){
                    finalHolder.ly_list_order_write_show.setVisibility(View.VISIBLE);
                    finalHolder.tv_list_order_write_close.setText("收起");
                    finalHolder1.iv_list_order_write_xiala.setImageResource(R.mipmap.shangla);
                    lists.get(position).setShowup(true);
                    notifyDataSetChanged();
                }else {
                    finalHolder.ly_list_order_write_show.setVisibility(View.GONE);
                    finalHolder.tv_list_order_write_close.setText("查看");
                    finalHolder1.iv_list_order_write_xiala.setImageResource(R.mipmap.xiala);
                    lists.get(position).setShowup(false);
                    notifyDataSetChanged();
                }
            }
        });

        holder.iv_list_order_write_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p = position;
                List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                nvps.add(new BasicNameValuePair("phone_num",lists.get(position).getPhone_num()));
                nvps.add(new BasicNameValuePair("order_num",lists.get(position).getOrder_num()));
                httpUtil.post(mHandler, Internet.DELETE_PURCHASE_URL,1,nvps);
            }
        });


        return convertView;
    }

    static class ViewHolder {
        TextView tv_list_order_write_biaohao;
        TextView tv_list_order_write_pinming;
        TextView tv_list_order_write_guige;
        TextView tv_list_order_write_dunwei;
        TextView tv_list_order_write_caizhi;
        TextView tv_list_order_write_beizhu;
        TextView tv_list_order_write_type;
        TextView tv_list_order_write_close;
        ImageView iv_list_order_write_delete;
        ImageView iv_list_order_write_xiala;
        LinearLayout ly_list_order_write_show;
        LinearLayout ly_list_order_write_look;

    }
}
