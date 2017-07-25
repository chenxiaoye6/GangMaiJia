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
import com.example.administrator.gangmaijia.Model.PurchaseWrite;
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
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2016/10/21.
 */

public class PurchaseWrite_detail extends Activity {

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("123", "ondestory------------------");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("123", "PurchaseWrite_detail onStart------------------");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("123", "onPause------------------");

    }

    private PurchaseWrite purchase;
    private int type;
    private int b;
    private UserDAO userDAO = new UserDAO(this);
    private HttpUtil httpUtil = new HttpUtil();
    private ProgressDialog progressDialog;

    User user = new UserDAO(PurchaseWrite_detail.this).getUserInfo();
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
                    Toast.makeText(PurchaseWrite_detail.this, s1, Toast.LENGTH_SHORT).show();
                    break;
                case 5:

                    try {
                        //显示进度条对话框,正在抢单
                        System.out.println("--------purchase_detail--------");
                        progressDialog = new ProgressDialog(PurchaseWrite_detail.this);
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
                                    new OkHttpUtils().postKeyValue(mHandler, Internet.INSERTRELATIONPURCHASE, builder);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    } catch (Exception e) {
                        Log.e("123123123", e.toString() + "------------------------");
                        e.printStackTrace();
                    }
                    break;
                case 6:
                    Log.e("123123123", "  case 6:------------------");

                    ((TextView) findViewById(R.id.tv_write_detail_bianhao)).setText(purchase.getOrder_num());
                    Log.e("purchase.getOrder_num()", purchase.getOrder_num());
                    ((TextView) findViewById(R.id.tv_write_detail_name)).setText(purchase.getProduct_name());
                    Log.e("123", "执行不安好3");
                    ((TextView) findViewById(R.id.tv_write_detail_guige)).setText(purchase.getStandard());
                    Log.e("initview()3", "执行不安好4");
                    ((TextView) findViewById(R.id.tv_write_detail_caizhi)).setText(purchase.getMaterial());
                    Log.e("initview()3", "执行不安好5");
                    ((TextView) findViewById(R.id.tv_write_detail_dunwei)).setText(purchase.getTon_amount());
                    Log.e("initview()3", "执行不安好6");
                    ((TextView) findViewById(R.id.tv_write_detail_beizhu)).setText(purchase.getComment());
                    Log.e("initview()3", "执行不安好7");
                    ((TextView) findViewById(R.id.tv_write_detail_jieshou)).setText(purchase.getAcceptor());
                    Log.e("initview()3", "执行不安好8");
                    findViewById(R.id.tv_write_detail_qinagdan).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            List<NameValuePair> nvps = new ArrayList<>();
                            nvps.add(new BasicNameValuePair("phone_num", purchase.getPhone_num()));
                            //查询余额
                            new HttpUtil().post(mHandler, Internet.SELECT_BALANCE_URL, 5, nvps);
                            Log.e("initview()3", "break");
                        }
                    });
                    findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.e("initview()3", "break");
                            finish();
                        }
                    });

                    Log.e("initview()3", "break");

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchase_detail);
        MyApplication.getInstance().addActivity(this);
        //标题栏透明
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //通过bundle穿过来的数据
        type = getIntent().getIntExtra("type", 0);
        Log.e("123", "type------------------" + type);
        if (type == 0) {
            purchase = (PurchaseWrite) getIntent().getSerializableExtra("purchase");
            initview();
        } else if (type == 3) {
            Log.e("123", "initview3****************************");
            //抢单
            initview3();
        } else {
            purchase = (PurchaseWrite) getIntent().getSerializableExtra("purchase");
            initview2();
        }


    }

    private void initview3() {
        final String url = getIntent().getStringExtra("url");

        Log.e("012", "url:=======================+url" + url);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e("012", "Runnable" + url);
                try {
                    String mdata = OkHttpUtils.getKeyValue(url);
                    Log.e("012", mdata + "============1");
                    JSONObject jsonObject = new JSONObject(mdata);
                    JSONObject data = jsonObject.getJSONObject("data");
                    Iterator<String> keys01 = data.keys();//应用迭代器iterator获取所有的key的值
                    //解析出详情
                    String standard = "";
                    String product_name = "";
                    String state = "";
                    String gener_time = "";
                    String ton_amount = "";
                    String type_num = "";
                    String material = "";
                    String acceptor = "";
                    String show1 = "";
                    String page = "";
                    String phone_num = "";
                    String order_num = "";
                    String comment = "";
                    String back_stage = "";
                    while (keys01.hasNext()) {
                        //遍历每个key
                        String key01 = keys01.next().toString();
                        Log.e("012", "key01========" + key01);
                        JSONObject news = data.getJSONObject(key01);
                        standard = news.getString("standard") + ";" + standard;
                        product_name = news.getString("product_name");
                        state = news.getString("state");
                        gener_time = news.getString("gener_time");
                        ton_amount = news.getString("ton_amount") + ";" + ton_amount;
                        type_num = news.getString("type_num");
                        material = news.getString("material") + ";" + material;
                        acceptor = news.getString("acceptor");
                        show1 = news.getString("show1");
                        page = news.getString("page");
                        phone_num = news.getString("phone_num");
                        order_num = news.getString("order_num");
                        comment = news.getString("comment");
                        back_stage = news.getString("back_stage");
                    }
                    purchase = new PurchaseWrite();
                    purchase.setStandard(standard);
                    purchase.setProduct_name(product_name);
                    purchase.setState(state);
                    purchase.setGener_time(gener_time);
                    purchase.setTon_amount(ton_amount);
                    purchase.setType_num(type_num);
                    purchase.setMaterial(material);
                    purchase.setAcceptor(acceptor);
                    purchase.setShow1(show1);
                    purchase.setPage(page);
                    purchase.setPhone_num(phone_num);
                    purchase.setOrder_num(order_num);
                    purchase.setComment(comment);
                    purchase.setBack_stage(back_stage);
                    Log.e("012", purchase.toString());
//                    JSONObject news = data.getJSONObject("new1");
//                    Log.e("012",news.toString()+"================2");
//                    purchase = new PurchaseWrite();
//                    purchase.setStandard(news.getString("standard"));
//                    purchase.setProduct_name(news.getString("product_name"));
//                    purchase.setState(news.getString("state"));
//                    purchase.setGener_time(news.getString("gener_time"));
//                    purchase.setTon_amount(news.getString("ton_amount"));
//                    purchase.setType_num(news.getString("type_num"));
//                    purchase.setMaterial(news.getString("material"));
//                    purchase.setAcceptor(news.getString("acceptor"));
//                    purchase.setShow1(news.getString("show1"));
//                    purchase.setPage(news.getString("page"));
//                    purchase.setPhone_num(news.getString("phone_num"));
//                    purchase.setOrder_num(news.getString("order_num"));
//                    purchase.setComment(news.getString("comment"));
//                    purchase.setBack_stage(news.getString("back_stage"));
                    Message msg = new Message();
                    msg.what = 6;//可以是基本类型，可以是对象，可以是List、map等；
                    mHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void initview() {
        Log.e("initview()", "initview()");
        ((TextView) findViewById(R.id.tv_write_detail_bianhao)).setText(purchase.getOrder_num());
        ((TextView) findViewById(R.id.tv_write_detail_name)).setText(purchase.getProduct_name());
        ((TextView) findViewById(R.id.tv_write_detail_guige)).setText(purchase.getStandard());
        ((TextView) findViewById(R.id.tv_write_detail_caizhi)).setText(purchase.getMaterial());
        ((TextView) findViewById(R.id.tv_write_detail_dunwei)).setText(purchase.getTon_amount());
        ((TextView) findViewById(R.id.tv_write_detail_beizhu)).setText(purchase.getComment());
        ((TextView) findViewById(R.id.tv_write_detail_jieshou)).setText(purchase.getAcceptor());
        Log.e("findviewbyid", "findViewById()");


        findViewById(R.id.tv_write_detail_qinagdan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<NameValuePair> nvps = new ArrayList<>();
                nvps.add(new BasicNameValuePair("phone_num", userDAO.getUserInfo().getUserPhone()));
                //查询余额
                new HttpUtil().post(mHandler, Internet.SELECT_BALANCE_URL, 5, nvps);
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

    private void initview2() {
        ((TextView) findViewById(R.id.tv_write_detail_bianhao)).setText(purchase.getOrder_num());
        ((TextView) findViewById(R.id.tv_write_detail_name)).setText(purchase.getProduct_name());
        ((TextView) findViewById(R.id.tv_write_detail_guige)).setText(purchase.getStandard());
        ((TextView) findViewById(R.id.tv_write_detail_caizhi)).setText(purchase.getMaterial());
        ((TextView) findViewById(R.id.tv_write_detail_dunwei)).setText(purchase.getTon_amount());
        ((TextView) findViewById(R.id.tv_write_detail_beizhu)).setText(purchase.getComment());
        ((TextView) findViewById(R.id.tv_write_detail_jieshou)).setText(purchase.getAcceptor());
        ((TextView) findViewById(R.id.tv_write_detail_phone)).setText(purchase.getPhone_num());
        findViewById(R.id.ly_purchase_phone).setVisibility(View.VISIBLE);
        findViewById(R.id.tv_write_detail_qinagdan).setVisibility(View.GONE);
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
