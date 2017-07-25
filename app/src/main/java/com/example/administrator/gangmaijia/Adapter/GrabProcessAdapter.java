package com.example.administrator.gangmaijia.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.gangmaijia.Activity.Purchase.Process_detail;
import com.example.administrator.gangmaijia.DAO.UserDAO;
import com.example.administrator.gangmaijia.Model.JiaGongOrder;
import com.example.administrator.gangmaijia.Model.User;
import com.example.administrator.gangmaijia.R;
import com.example.administrator.gangmaijia.Util.HttpUtil;
import com.example.administrator.gangmaijia.Util.Internet;
import com.example.administrator.gangmaijia.Util.OkHttpUtils;
import com.example.administrator.gangmaijia.View.DialogOnlyTitle;
import com.example.administrator.gangmaijia.View.DialogOrder;
import com.squareup.okhttp.FormEncodingBuilder;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/21.
 */

public class GrabProcessAdapter extends BaseAdapter {
    private List<JiaGongOrder> lists;
    private LayoutInflater mInflater;
    private Context mainContext;
    private HttpUtil httpUtil = new HttpUtil();
    private int p = -1;
    private int type;
    private int b;
    private UserDAO userDAO = new UserDAO(mainContext);
    private int n;

    User user = userDAO.getUserInfo();
    private String balance;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String s = (String) msg.obj;
                    Log.e("case 0  s", s);
                    showdialogPhoto(lists.get(p).getPhone_num());
                    lists.remove(p);
                    notifyDataSetChanged();
                    break;
                case -1:
                    String s1 = (String) msg.obj;
                    Log.e("case 1  s1", s1);
                    Toast.makeText(mainContext, s1, Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    try {
                        String data = (String) msg.obj;
                        JSONObject jsonObject = new JSONObject(data);
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        balance = jsonObject1.getString("balance");
                        Log.e("case 5:  balance", balance);

                        final FormEncodingBuilder builder = new FormEncodingBuilder();
                        builder.add("cu_phone_num", lists.get(p).getPhone_num());
                        builder.add("order_num", lists.get(p).getOrder_num());
                        builder.add("co_phone_num", user.getUserPhone());
                        builder.add("balance", balance);
                        builder.add("state", (Integer.parseInt(lists.get(p).getState()) + 1) + "");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    //上传抢单请求
                                    new OkHttpUtils().postKeyValue(mHandler, Internet.INSERTRELATIONPROCESS, builder);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 6:
                    lists.remove(p);
                    notifyDataSetChanged();
                    break;
            }

        }
    };

    public GrabProcessAdapter(Context context, List<JiaGongOrder> dataList, int type, int n) {
        mInflater = LayoutInflater.from(context);
        mainContext = context;
        lists = dataList;
        this.type = type;
        this.n = n;
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
            convertView = mInflater.inflate(R.layout.list_alldingdan, null);
            holder.tv_list_alldingdan_bianhao = (TextView) convertView.findViewById(R.id.tv_list_alldingdan_bianhao);
            holder.tv_list_alldingdan_name = (TextView) convertView.findViewById(R.id.tv_list_alldingdan_name);
            holder.tv_list_alldingdan_guige = (TextView) convertView.findViewById(R.id.tv_list_alldingdan_guige);
            holder.tv_list_alldingdan_caizhi = (TextView) convertView.findViewById(R.id.tv_list_alldingdan_caizhi);
            holder.tv_list_alldingdan_detail = (TextView) convertView.findViewById(R.id.tv_list_alldingdan_detail);
            holder.tv_list_alldingdan_qiangdan = (TextView) convertView.findViewById(R.id.tv_list_alldingdan_qiangdan);
            holder.et_list_alldingdan_name = (TextView) convertView.findViewById(R.id.et_list_alldingdan_name);
            holder.et_list_alldingdan_guige = (TextView) convertView.findViewById(R.id.et_list_alldingdan_guige);
            holder.et_list_alldingdan_caizhi = (TextView) convertView.findViewById(R.id.et_list_alldingdan_caizhi);
            holder.iv_list_alldingdan_delete = (ImageView) convertView.findViewById(R.id.iv_list_alldingdan_delete);
            holder.iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_list_alldingdan_qiangdan.setBackgroundResource(R.drawable.circlecase_orange);
        holder.tv_list_alldingdan_qiangdan.setEnabled(true);
        holder.tv_list_alldingdan_detail.setBackgroundResource(R.drawable.circlecase_green);
        holder.tv_list_alldingdan_detail.setEnabled(true);

//        try {
        holder.tv_list_alldingdan_bianhao.setText(lists.get(position).getOrder_num());
        holder.tv_list_alldingdan_name.setText("产品名称：");
        holder.tv_list_alldingdan_guige.setText("加工类型：");
        holder.tv_list_alldingdan_caizhi.setText("加工内容：");
        holder.et_list_alldingdan_name.setText(lists.get(position).getProduct_name());
        holder.et_list_alldingdan_guige.setText(lists.get(position).getProcess_type());
        holder.et_list_alldingdan_caizhi.setText(lists.get(position).getProcess_content());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        if (type == 0) {
            if (Integer.parseInt(lists.get(position).getState()) > 4) {
                holder.tv_list_alldingdan_qiangdan.setBackgroundResource(R.drawable.circlecase_hui);
                holder.tv_list_alldingdan_qiangdan.setEnabled(false);
                holder.tv_list_alldingdan_detail.setBackgroundResource(R.drawable.circlecase_hui);
                holder.tv_list_alldingdan_detail.setEnabled(false);
            }
        } else {
            holder.tv_list_alldingdan_qiangdan.setBackgroundResource(R.drawable.circlecase_hui);
            holder.tv_list_alldingdan_qiangdan.setEnabled(false);
//            holder.tv_list_alldingdan_detail.setBackgroundResource(R.drawable.circlecase_hui);
//            holder.tv_list_alldingdan_detail.setEnabled(false);
        }

        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("789", user.toString());
//                SharedPreferences sharedPreferences = mainContext.getSharedPreferences("machininginfo", Context.MODE_PRIVATE); //私有数据
//                SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
//                editor.putString("ordernum", "" + sharedPreferences.getString("ordernum", null) + lists.get(position).getOrder_num());
//                editor.commit();//提交
                p = position;
                if (n == 1) {
                    List<NameValuePair> nvps = new ArrayList<>();
                    nvps.add(new BasicNameValuePair("co_phone_num", userDAO.getUserInfo().getUserPhone()));
                    nvps.add(new BasicNameValuePair("order_num", lists.get(position).getOrder_num()));
                    //删除订单
                    new HttpUtil().post(mHandler, Internet.DELETENOORDER, 6, nvps);
                } else {
                    List<NameValuePair> nvps = new ArrayList<>();
                    nvps.add(new BasicNameValuePair("co_phone_num", userDAO.getUserInfo().getUserPhone()));
                    nvps.add(new BasicNameValuePair("order_num", lists.get(position).getOrder_num()));
                    nvps.add(new BasicNameValuePair("cu_phone_num", lists.get(position).getPhone_num()));
                    //删除订单
                    new HttpUtil().post(mHandler, Internet.DELETECOMPLETEORDER, 6, nvps);
                }
            }
        });
        holder.tv_list_alldingdan_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainContext, Process_detail.class);
                intent.putExtra("purchase", lists.get(position));
                intent.putExtra("type", type);
                mainContext.startActivity(intent);
            }
        });

        holder.tv_list_alldingdan_qiangdan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p = position;
                List<NameValuePair> nvps = new ArrayList<>();
                nvps.add(new BasicNameValuePair("phone_num", userDAO.getUserInfo().getUserPhone()));
                //查询余额
                new HttpUtil().post(mHandler, Internet.SELECT_BALANCE_URL, 5, nvps);
            }
        });


        return convertView;
    }

    static class ViewHolder {
        TextView tv_list_alldingdan_bianhao;
        TextView tv_list_alldingdan_name;
        TextView tv_list_alldingdan_guige;
        TextView tv_list_alldingdan_caizhi;
        TextView tv_list_alldingdan_detail;
        TextView tv_list_alldingdan_qiangdan;
        TextView et_list_alldingdan_guige;
        TextView et_list_alldingdan_caizhi;
        TextView et_list_alldingdan_name;
        ImageView iv_list_alldingdan_delete;
        ImageView iv_delete;
    }

    private void showdialog() {
        final DialogOnlyTitle dialogCommon = new DialogOnlyTitle(mainContext);
        dialogCommon.show();
    }

    private void showdialogPhoto(String photo) {
        final DialogOrder dialogCommon = new DialogOrder(mainContext, photo);
        dialogCommon.show();
    }

}
