package com.example.administrator.gangmaijia.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.gangmaijia.Activity.ShowPhotoActivity;
import com.example.administrator.gangmaijia.Model.PurchasePhoto;
import com.example.administrator.gangmaijia.Model.PurchaseWrite;
import com.example.administrator.gangmaijia.R;
import com.example.administrator.gangmaijia.Util.HttpUtil;
import com.example.administrator.gangmaijia.Util.Internet;
import com.example.administrator.gangmaijia.View.NoScrollGridView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/22.
 */

public class PurchasePhotoAdapter extends BaseAdapter {
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

    public PurchasePhotoAdapter(Context context, List<PurchasePhoto> dataList) {
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
            convertView = mInflater.inflate(R.layout.list_order_photo, null);
            holder.tv_list_order_photo_biaohao = (TextView) convertView.findViewById(R.id.tv_list_order_photo_biaohao);
            holder.tv_list_order_photo_jieshou = (TextView) convertView.findViewById(R.id.tv_list_order_photo_jieshou);
            holder.iv_list_order_photo_delete = (ImageView) convertView.findViewById(R.id.iv_list_order_photo_delete);
            holder.gridView = (NoScrollGridView) convertView.findViewById(R.id.gridView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        try {
            holder.tv_list_order_photo_biaohao.setText(lists.get(position).getOrder_num());
            holder.tv_list_order_photo_jieshou.setText(lists.get(position).getAcceptor());
            System.out.println("-----------getPhoto_path---------"+lists.get(position).getPhoto_path());
            holder.gridView.setAdapter(new ImageAdapter(mainContext,lists.get(position).getPhoto_path()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                Intent intent = new Intent(mainContext, ShowPhotoActivity.class);
                ArrayList<String> photoList = new ArrayList<String>();
                for (String item:lists.get(position).getPhoto_path()){
                    photoList.add(item);
                }
                intent.putStringArrayListExtra("photo",photoList);
                intent.putExtra("ID", i);
                mainContext.startActivity(intent);
            }
        });


        holder.iv_list_order_photo_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p = position;
                List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                nvps.add(new BasicNameValuePair("phone_num",lists.get(position).getPhone_num()));
                nvps.add(new BasicNameValuePair("order_num",lists.get(position).getOrder_num()));
                httpUtil.post(mHandler, Internet.DELETE_PHOTO_URL,1,nvps);
            }
        });


        return convertView;
    }

    static class ViewHolder {
        TextView tv_list_order_photo_biaohao;
        TextView tv_list_order_photo_jieshou;
        NoScrollGridView gridView;
        ImageView iv_list_order_photo_delete;

    }
}
