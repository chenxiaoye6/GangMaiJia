package com.example.administrator.gangmaijia.Activity.Purchase;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.gangmaijia.DAO.UserDAO;
import com.example.administrator.gangmaijia.Model.Logistic;
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

public class Logistic_detail extends Activity {

    private TextView tv_logistic_detail_bianhao ;
    private TextView tv_logistic_detail_transport_way ;
    private TextView tv_logistic_detail_delivery_place ;
    private TextView tv_logistic_detail_destination ;
    private TextView tv_logistic_detail_product_name ;
    private TextView tv_logistic_detail_ton_amount ;
    private TextView tv_logistic_detail_length ;
    private TextView tv_logistic_detail_delivery_time ;
    private TextView tv_logistic_detail_special_demand ;
    private TextView tv_logistic_detail_special_phone ;
    private TextView tv_logistic_detail_qinagdan ;
    private LinearLayout ly_logistic_phone ;
    private Logistic purchase ;
    private int b ;
    private int type;
    private UserDAO userDAO = new UserDAO(this);
    private HttpUtil httpUtil = new HttpUtil();
    private ProgressDialog progressDialog;

    User user = new UserDAO(Logistic_detail.this).getUserInfo();
    private String balance;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                //抢单成功
                case 0:
                    String s = (String) msg.obj;
                    Log.e("case 0  s" ,s);
                    progressDialog.dismiss();
                    //弹出对话框
                    showdialogPhoto(purchase.getPhone_num());
                    break;
                //抢单失败
                case -1:
                    String s1 = (String) msg.obj;
                    Log.e("case -1  s1" ,s1);
                    progressDialog.dismiss();
                    Toast.makeText(Logistic_detail.this,s1,Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    try {
                        //显示进度条对话框,正在抢单
                        System.out.println("--------purchase_detail--------");
                        progressDialog = new ProgressDialog(Logistic_detail.this);
                        progressDialog.setMessage("正在抢单");
                        progressDialog.setCancelable(false);
                        progressDialog.show();

                        String data=(String) msg.obj;
                        JSONObject jsonObject=new JSONObject(data);
                        JSONObject jsonObject1=jsonObject.getJSONObject("data");
                        balance = jsonObject1.getString("balance");
                        Log.e("case 5:  balance=", balance);

                        final FormEncodingBuilder builder = new FormEncodingBuilder();
                        builder.add("cu_phone_num",purchase.getPhone_num());
                        builder.add("order_num",purchase.getOrder_num());
                        builder.add("co_phone_num",user.getUserPhone());
                        builder.add("balance",balance);
                        builder.add("state",purchase.getState());
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    //上传抢单请求
                                    new OkHttpUtils().postKeyValue(mHandler, Internet.BASE_URL+"insert_relation_logistic.action",builder);
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
                    tv_logistic_detail_bianhao = (TextView) findViewById(R.id.tv_logistic_detail_bianhao);
                    tv_logistic_detail_transport_way = (TextView) findViewById(R.id.tv_logistic_detail_transport_way);
                    tv_logistic_detail_delivery_place = (TextView) findViewById(R.id.tv_logistic_detail_delivery_place);
                    tv_logistic_detail_destination = (TextView) findViewById(R.id.tv_logistic_detail_destination);
                    tv_logistic_detail_product_name = (TextView) findViewById(R.id.tv_logistic_detail_product_name);
                    tv_logistic_detail_ton_amount = (TextView) findViewById(R.id.tv_logistic_detail_ton_amount);
                    tv_logistic_detail_length = (TextView) findViewById(R.id.tv_logistic_detail_length);
                    tv_logistic_detail_delivery_time = (TextView) findViewById(R.id.tv_logistic_detail_delivery_time);
                    tv_logistic_detail_special_demand = (TextView) findViewById(R.id.tv_logistic_detail_special_demand);
                    tv_logistic_detail_special_phone = (TextView) findViewById(R.id.tv_logistic_detail_special_phone);
                    tv_logistic_detail_qinagdan = (TextView) findViewById(R.id.tv_logistic_detail_qinagdan);
                    ly_logistic_phone = (LinearLayout) findViewById(R.id.ly_logistic_phone);
                    //设置跳转过后页面属性值
                    tv_logistic_detail_bianhao.setText(purchase.getOrder_num());
                    tv_logistic_detail_transport_way.setText(purchase.getTransport_way());
                    tv_logistic_detail_delivery_place.setText(purchase.getDelivery_place());
                    tv_logistic_detail_destination.setText(purchase.getDestination());
                    tv_logistic_detail_product_name.setText(purchase.getProduct_name());
                    tv_logistic_detail_ton_amount.setText(purchase.getTon_amount());
                    tv_logistic_detail_length.setText(purchase.getLength());
                    tv_logistic_detail_delivery_time.setText(purchase.getDelivery_time());
                    tv_logistic_detail_special_demand.setText(purchase.getSpecial_demand());
                    findViewById(R.id.tv_logistic_detail_qinagdan).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            List<NameValuePair> nvps = new ArrayList<>();
                            nvps.add(new BasicNameValuePair("phone_num",user.getUserPhone()));
                            Log.e("抢单被点击",user.getUserPhone());
                            //查询余额
                            new HttpUtil().post(mHandler,Internet.BASE_URL+"select_balance.action",5,nvps);

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
        setContentView(R.layout.logistic_detail);
        MyApplication.getInstance().addActivity(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        initview();
        purchase = (Logistic) getIntent().getSerializableExtra("purchase");
        type =  getIntent().getIntExtra("type",0);

        if (type==0){
            initview();
            showview();
            tv_logistic_detail_qinagdan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<NameValuePair> nvps = new ArrayList<>();
                    nvps.add(new BasicNameValuePair("phone_num",userDAO.getUserInfo().getUserPhone()));
                    //查询余额
                    new HttpUtil().post(mHandler,Internet.BASE_URL+"select_balance.action",5,nvps);
                    //
                }
            });
        }else if (type==3){
            initview3();
        } else {
            initview();
            showview();
            tv_logistic_detail_special_phone.setText(purchase.getPhone_num());
            ly_logistic_phone.setVisibility(View.VISIBLE);
            tv_logistic_detail_qinagdan.setVisibility(View.GONE);
        }


    }

    private void initview3() {
        final String url=getIntent().getStringExtra("url");
        Log.e("url",url);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.e("thread url",url);
                    String mdata=OkHttpUtils.getKeyValue(url);
                    Log.e("data",mdata);
                    JSONObject jsonObject=new JSONObject(mdata);
                    JSONObject data=jsonObject.getJSONObject("data");
                    JSONObject news=data.getJSONObject("new1");
                    Log.e("news",news.toString());
                    purchase=new Logistic();
//delivery_time":"w","delivery_place":"w","product_name":"w","state":0,"gener_time":"2016-11-16 13:15:52","ton_amount":0,"transport_way":"汽车",
// "target_city":"天津市","destination":"w","show1":1,"page":0,"phone_num":"100","order_num":"20161116131552","length":0,"special_demand":"w"}
                    purchase.setDelivery_time(news.getString("delivery_time"));
                    purchase.setDelivery_place(news.getString("delivery_place"));
                    purchase.setProduct_name(news.getString("product_name"));
                    purchase.setState(news.getString("state"));
                    purchase.setGener_time(news.getString("gener_time"));
                    purchase.setTon_amount(news.getString("ton_amount"));
                    purchase.setTransport_way(news.getString("transport_way"));
                    purchase.setTarget_city("target_city");
                    purchase.setDestination(news.getString("destination"));
                    purchase.setShow1(news.getString("show1"));
                    purchase.setPage(news.getString("page"));
                    purchase.setPhone_num(news.getString("phone_num"));
                    purchase.setOrder_num(news.getString("order_num"));
                    purchase.setLength(news.getString("length"));
                    purchase.setSpecial_demand(news.getString("special_demand"));
                    Log.e("purchase","purchase.tosting="+purchase.toString());
                    Message msg = new Message();
                    msg.what = 6;//可以是基本类型，可以是对象，可以是List、map等；
                    mHandler.sendMessage(msg);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private void initview(){
        tv_logistic_detail_bianhao = (TextView) findViewById(R.id.tv_logistic_detail_bianhao);
        tv_logistic_detail_transport_way = (TextView) findViewById(R.id.tv_logistic_detail_transport_way);
        tv_logistic_detail_delivery_place = (TextView) findViewById(R.id.tv_logistic_detail_delivery_place);
        tv_logistic_detail_destination = (TextView) findViewById(R.id.tv_logistic_detail_destination);
        tv_logistic_detail_product_name = (TextView) findViewById(R.id.tv_logistic_detail_product_name);
        tv_logistic_detail_ton_amount = (TextView) findViewById(R.id.tv_logistic_detail_ton_amount);
        tv_logistic_detail_length = (TextView) findViewById(R.id.tv_logistic_detail_length);
        tv_logistic_detail_delivery_time = (TextView) findViewById(R.id.tv_logistic_detail_delivery_time);
        tv_logistic_detail_special_demand = (TextView) findViewById(R.id.tv_logistic_detail_special_demand);
        tv_logistic_detail_special_phone = (TextView) findViewById(R.id.tv_logistic_detail_special_phone);
        tv_logistic_detail_qinagdan = (TextView) findViewById(R.id.tv_logistic_detail_qinagdan);
        ly_logistic_phone = (LinearLayout) findViewById(R.id.ly_logistic_phone);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void showview(){
        tv_logistic_detail_bianhao.setText(purchase.getOrder_num());
        tv_logistic_detail_transport_way.setText(purchase.getTransport_way());
        tv_logistic_detail_delivery_place.setText(purchase.getDelivery_place());
        tv_logistic_detail_destination.setText(purchase.getDestination());
        tv_logistic_detail_product_name.setText(purchase.getProduct_name());
        tv_logistic_detail_ton_amount.setText(purchase.getTon_amount());
        tv_logistic_detail_length.setText(purchase.getLength());
        tv_logistic_detail_delivery_time.setText(purchase.getDelivery_time());
        tv_logistic_detail_special_demand.setText(purchase.getSpecial_demand());
    }
    private void showdialog() {
        final DialogOnlyTitle dialogCommon = new DialogOnlyTitle(this);
        dialogCommon.show();
    }
    private void showdialogPhoto(String photo) {
        final DialogOrder dialogCommon = new DialogOrder(this,photo);
        dialogCommon.show();
    }
}
