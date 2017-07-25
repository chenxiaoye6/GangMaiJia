package com.example.administrator.gangmaijia.Activity.Purchase;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.gangmaijia.DAO.UserDAO;
import com.example.administrator.gangmaijia.Model.JiaGongOrder;
import com.example.administrator.gangmaijia.Model.User;
import com.example.administrator.gangmaijia.MyApplication;
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

public class Process_detail extends Activity {

    private JiaGongOrder purchase;
    private int type;
    private int b;
    private UserDAO userDAO = new UserDAO(this);
    private HttpUtil httpUtil = new HttpUtil();
    private ProgressDialog progressDialog;

    User user = new UserDAO(Process_detail.this).getUserInfo();
    private String balance;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //抢单成功
                case 0:
                    String s = (String) msg.obj;
                    Log.e("case 0  s", s);
                    progressDialog.dismiss();
                    //弹出对话框
                    showdialogPhoto(purchase.getPhone_num());
                    break;
                //抢单失败
                case -1:
                    String s1 = (String) msg.obj;
                    Log.e("case 1  s1", s1);
                    progressDialog.dismiss();
                    Toast.makeText(Process_detail.this, s1, Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    try {
                        //显示进度条对话框,正在抢单
                        System.out.println("--------purchase_detail--------");
                        progressDialog = new ProgressDialog(Process_detail.this);
                        progressDialog.setMessage("正在抢单");
                        progressDialog.setCancelable(false);
                        progressDialog.show();

                        String data = (String) msg.obj;
                        JSONObject jsonObject = new JSONObject(data);
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        balance = jsonObject1.getString("balance");
                        Log.e("case 5:  balance", balance);

                        final FormEncodingBuilder builder = new FormEncodingBuilder();
                        builder.add("cu_phone_num", purchase.getPhone_num());
                        builder.add("order_num", purchase.getOrder_num());
                        builder.add("co_phone_num", user.getUserPhone());
                        builder.add("balance", balance);
                        builder.add("state", purchase.getState());
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
                    ((TextView) findViewById(R.id.tv_process_detail_bianhao)).setText(purchase.getOrder_num());
                    ((TextView) findViewById(R.id.tv_process_detail_type)).setText(purchase.getProcess_type());
                    ((TextView) findViewById(R.id.tv_process_detail_name)).setText(purchase.getProduct_name());
                    ((TextView) findViewById(R.id.tv_process_detail_content)).setText(purchase.getProcess_content());
                    ((TextView) findViewById(R.id.tv_process_detail_dunwei)).setText(purchase.getTon_amount());
                    ((TextView) findViewById(R.id.tv_process_detail_beizhu)).setText(purchase.getComment());
                    findViewById(R.id.tv_process_detail_qinagdan).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            List<NameValuePair> nvps = new ArrayList<>();
                            nvps.add(new BasicNameValuePair("phone_num", user.getUserPhone()));
                            Log.e("抢单被点击", user.getUserPhone());
                            //查询余额
                            new HttpUtil().post(mHandler, Internet.SELECT_BALANCE_URL, 5, nvps);

                        }
                    });

                    findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.process_detail);
        MyApplication.getInstance().addActivity(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        purchase = (JiaGongOrder) getIntent().getSerializableExtra("purchase");
        type = getIntent().getIntExtra("type", 0);
        if (type == 0) {
            initivew();
        } else if (type == 3) {
            initview3();
        } else {
            initivew2();
        }
    }

    private void initview3() {
        final String url = getIntent().getStringExtra("url");
        Log.e("url", url);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.e("thread url", url);
                    String mdata = OkHttpUtils.getKeyValue(url);
                    Log.e("data", mdata);
                    JSONObject jsonObject = new JSONObject(mdata);
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONObject news = data.getJSONObject("new1");
                    Log.e("news", news.toString());
                    purchase = new JiaGongOrder();
                    purchase.setProduct_name(news.getString("product_name"));
                    purchase.setState(news.getString("state"));
                    purchase.setGener_time(news.getString("gener_time"));
                    purchase.setProcess_content(news.getString("process_content"));
                    purchase.setTon_amount(news.getString("ton_amount"));
                    purchase.setProcess_type(news.getString("process_type"));
                    purchase.setShow1(news.getString("show1"));
                    purchase.setPage(news.getString("page"));
                    purchase.setPhone_num(news.getString("phone_num"));
                    purchase.setOrder_num(news.getString("order_num"));
                    purchase.setComment(news.getString("comment"));
                    purchase.setTarget_city("target_city");
                    Log.e("purchase", purchase.toString());
                    Message msg = new Message();
                    msg.what = 6;//可以是基本类型，可以是对象，可以是List、map等；
                    mHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void initivew() {
        ((TextView) findViewById(R.id.tv_process_detail_bianhao)).setText(purchase.getOrder_num());
        ((TextView) findViewById(R.id.tv_process_detail_type)).setText(purchase.getProcess_type());
        ((TextView) findViewById(R.id.tv_process_detail_name)).setText(purchase.getProduct_name());
        ((TextView) findViewById(R.id.tv_process_detail_content)).setText(purchase.getProcess_content());
        ((TextView) findViewById(R.id.tv_process_detail_dunwei)).setText(purchase.getTon_amount());
        ((TextView) findViewById(R.id.tv_process_detail_beizhu)).setText(purchase.getComment());

        findViewById(R.id.tv_process_detail_qinagdan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<NameValuePair> nvps = new ArrayList<>();
                nvps.add(new BasicNameValuePair("phone_num", userDAO.getUserInfo().getUserPhone()));
                //查询余额
                new HttpUtil().post(mHandler, Internet.BASE_URL + "select_balance.action", 5, nvps);
                //
            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initivew2() {
        ((TextView) findViewById(R.id.tv_process_detail_bianhao)).setText(purchase.getOrder_num());
        ((TextView) findViewById(R.id.tv_process_detail_type)).setText(purchase.getProcess_type());
        ((TextView) findViewById(R.id.tv_process_detail_name)).setText(purchase.getProduct_name());
        ((TextView) findViewById(R.id.tv_process_detail_content)).setText(purchase.getProcess_content());
        ((TextView) findViewById(R.id.tv_process_detail_dunwei)).setText(purchase.getTon_amount());
        ((TextView) findViewById(R.id.tv_process_detail_beizhu)).setText(purchase.getComment());
        ((TextView) findViewById(R.id.tv_process_detail_hpone)).setText(purchase.getPhone_num());
        findViewById(R.id.ly_process_phone).setVisibility(View.VISIBLE);
        findViewById(R.id.tv_process_detail_qinagdan).setVisibility(View.GONE);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void showdialog() {
        final DialogOnlyTitle dialogCommon = new DialogOnlyTitle(this);
        dialogCommon.show();
    }

    private void showdialogPhoto(String photo) {
        final DialogOrder dialogCommon = new DialogOrder(this, photo);
        dialogCommon.show();
    }

}
