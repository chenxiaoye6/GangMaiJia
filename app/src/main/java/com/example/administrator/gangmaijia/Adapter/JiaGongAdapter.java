package com.example.administrator.gangmaijia.Adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.gangmaijia.Model.JiaGongOrder;
import com.example.administrator.gangmaijia.Model.Province;
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
 * Created by Administrator on 2016/10/19.
 */

public class JiaGongAdapter extends BaseAdapter {
    private List<JiaGongOrder> lists;
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

    public JiaGongAdapter(Context context, List<JiaGongOrder> dataList) {
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
            convertView = mInflater.inflate(R.layout.list_order_jiagong, null);
            holder.tv_list_order_jiagong_biaohao = (TextView) convertView.findViewById(R.id.tv_list_order_jiagong_biaohao);
            holder.et_list_order_jiagong_name = (TextView) convertView.findViewById(R.id.et_list_order_jiagong_name);
            holder.et_list_order_jiagong_type = (TextView) convertView.findViewById(R.id.et_list_order_jiagong_type);
            holder.et_list_order_jiagong_content = (TextView) convertView.findViewById(R.id.et_list_order_jiagong_content);
            holder.et_list_order_jiagong_dunwei = (TextView) convertView.findViewById(R.id.et_list_order_jiagong_dunwei);
            holder.et_list_order_jiagong_beizhu = (TextView) convertView.findViewById(R.id.et_list_order_jiagong_beizhu);
            holder.tv_list_order_jiagong_close = (TextView) convertView.findViewById(R.id.tv_list_order_jiagong_close);
            holder.iv_list_order_jiagong_delete = (ImageView) convertView.findViewById(R.id.iv_list_order_jiagong_delete);
            holder.iv_list_order_jiagong_xiala = (ImageView) convertView.findViewById(R.id.iv_list_order_jiagong_xiala);
            holder.ly_list_order_jiagong_show = (LinearLayout) convertView.findViewById(R.id.ly_list_order_jiagong_show);
            holder.ly_list_order_jiagong_look = (LinearLayout) convertView.findViewById(R.id.ly_list_order_jiagong_look);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        try {
            holder.tv_list_order_jiagong_biaohao.setText(lists.get(position).getOrder_num());
            holder.et_list_order_jiagong_name.setText(lists.get(position).getProduct_name());
            holder.et_list_order_jiagong_type.setText(lists.get(position).getProcess_type());
            holder.et_list_order_jiagong_content.setText(lists.get(position).getProcess_content());
            holder.et_list_order_jiagong_dunwei.setText(lists.get(position).getTon_amount());
            holder.et_list_order_jiagong_beizhu.setText(lists.get(position).getComment());
        } catch (Exception e) {
            e.printStackTrace();
        }

        final ViewHolder finalHolder = holder;
        final ViewHolder finalHolder1 = holder;
        holder.ly_list_order_jiagong_look.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!lists.get(position).isShowup()){
                    finalHolder.ly_list_order_jiagong_show.setVisibility(View.VISIBLE);
                    finalHolder.tv_list_order_jiagong_close.setText("收起");
                    finalHolder1.iv_list_order_jiagong_xiala.setImageResource(R.mipmap.shangla);
                    lists.get(position).setShowup(true);
                    notifyDataSetChanged();
                }else {
                    finalHolder.ly_list_order_jiagong_show.setVisibility(View.GONE);
                    finalHolder.tv_list_order_jiagong_close.setText("查看");
                    finalHolder1.iv_list_order_jiagong_xiala.setImageResource(R.mipmap.xiala);
                    lists.get(position).setShowup(false);
                    notifyDataSetChanged();
                }
            }
        });

        holder.iv_list_order_jiagong_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p = position;
                List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                nvps.add(new BasicNameValuePair("phone_num",lists.get(position).getPhone_num()));
                nvps.add(new BasicNameValuePair("order_num",lists.get(position).getOrder_num()));
                httpUtil.post(mHandler, Internet.DELETE_PROCESS_URL,1,nvps);
            }
        });


        return convertView;
    }

    static class ViewHolder {
        TextView tv_list_order_jiagong_biaohao;
        TextView et_list_order_jiagong_name;
        TextView et_list_order_jiagong_type;
        TextView et_list_order_jiagong_content;
        TextView et_list_order_jiagong_dunwei;
        TextView et_list_order_jiagong_beizhu;
        TextView tv_list_order_jiagong_close;
        ImageView iv_list_order_jiagong_delete;
        ImageView iv_list_order_jiagong_xiala;
        LinearLayout ly_list_order_jiagong_show;
        LinearLayout ly_list_order_jiagong_look;
    }
}
