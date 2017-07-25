package com.example.administrator.gangmaijia.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.gangmaijia.Activity.Purchase.PurchaseWrite_detail;
import com.example.administrator.gangmaijia.Model.PurchasePhoto;
import com.example.administrator.gangmaijia.R;
import com.example.administrator.gangmaijia.Util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2016/10/22.
 */

public class GrabPhotoAdapter extends BaseAdapter {
    private List<PurchasePhoto> lists;
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

    public GrabPhotoAdapter(Context context, List<PurchasePhoto> dataList) {
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
            convertView = mInflater.inflate(R.layout.list_grab_photo, null);
            holder.tv_list_grab_photo_bianhao = (TextView) convertView.findViewById(R.id.tv_list_grab_photo_bianhao);
            holder.et_list_grab_photo_jiashou = (TextView) convertView.findViewById(R.id.et_list_grab_photo_jiashou);
            holder.tv_list_grab_photo_detail = (TextView) convertView.findViewById(R.id.tv_list_grab_photo_detail);
            holder.tv_list_grab_photo_qiangdan = (TextView) convertView.findViewById(R.id.tv_list_grab_photo_qiangdan);
            holder.iv_list_alldingdan_delete = (ImageView) convertView.findViewById(R.id.iv_list_alldingdan_delete);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        try {
            holder.tv_list_grab_photo_bianhao.setText(lists.get(position).getOrder_num());
            holder.tv_list_grab_photo_bianhao.setText(lists.get(position).getAcceptor());
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.tv_list_grab_photo_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainContext, PurchaseWrite_detail.class);
                intent.putExtra("purchase",lists.get(position));
                mainContext.startActivity(intent);
            }
        });

        holder.tv_list_grab_photo_qiangdan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p = position;
//                List<NameValuePair> nvps = new ArrayList<NameValuePair>();
//                nvps.add(new BasicNameValuePair("phone_num",lists.get(position).getPhone_num()));
//                nvps.add(new BasicNameValuePair("order_num",lists.get(position).getOrder_num()));
//                httpUtil.post(mHandler, Internet.DELETE_PROCESS_URL,1,nvps);
            }
        });


        return convertView;
    }

    static class ViewHolder {
        TextView tv_list_grab_photo_bianhao;
        TextView et_list_grab_photo_jiashou;
        TextView tv_list_grab_photo_detail;
        TextView tv_list_grab_photo_qiangdan;
        ImageView iv_list_alldingdan_delete;
    }
}
