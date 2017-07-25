package com.example.administrator.gangmaijia.Activity.PersonalCenter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.gangmaijia.Adapter.GrabLogisticAdapter;
import com.example.administrator.gangmaijia.Adapter.GrabPhotoAdapter;
import com.example.administrator.gangmaijia.Adapter.GrabProcessAdapter;
import com.example.administrator.gangmaijia.Adapter.GrabWriteAdapter;
import com.example.administrator.gangmaijia.DAO.UserDAO;
import com.example.administrator.gangmaijia.Model.JiaGongOrder;
import com.example.administrator.gangmaijia.Model.Logistic;
import com.example.administrator.gangmaijia.Model.MessageEvent;
import com.example.administrator.gangmaijia.Model.PurchasePhoto;
import com.example.administrator.gangmaijia.Model.PurchaseWrite;
import com.example.administrator.gangmaijia.Model.User;
import com.example.administrator.gangmaijia.MyApplication;
import com.example.administrator.gangmaijia.R;
import com.example.administrator.gangmaijia.Util.HttpUtil;
import com.example.administrator.gangmaijia.Util.Internet;
import com.example.administrator.gangmaijia.View.ExpandView_zhaohuo;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Administrator on 2016/10/9.
 */

public class AllOrder extends Activity implements View.OnClickListener {

    private TextView tv_allorder_line1, tv_allorder_line2, tv_allorder_line3, tv_allorder_text1, tv_allorder_text2, tv_allorder_text3;
    private PullToRefreshListView allorder_list2, allorder_list3, allorder_list4;
    private ListView allorder_list1;
    //    private ImageView iv_allorder_zhaohuo ;
    private RelativeLayout ry_allorder_type1, ry_allorder_type2, ry_allorder_type3;
    private LinearLayout ll_head;
    private ExpandView_zhaohuo expandView;
    private TextView expand_line1, expand_line2;
    private int refreshType = 1;
    private int page1 = 0;
    private int page2 = 0;
    private int page3 = 0;
    private int page4 = 0;
    private HttpUtil httpUtil = new HttpUtil();
    private UserDAO userDAO = new UserDAO(this);
    private User user;
    private List<PurchaseWrite> list1 = new ArrayList<>();
    private List<JiaGongOrder> list2 = new ArrayList<>();
    private List<Logistic> list3 = new ArrayList<>();
    private List<PurchasePhoto> list4 = new ArrayList<>();
    private GrabWriteAdapter adapter1;
    private GrabProcessAdapter adapter2;
    private GrabLogisticAdapter adapter3;
    private GrabPhotoAdapter adapter4;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    showWritePurchaseResult(msg);
                    break;
                case 2:
                    showJiaGongResult(msg);
                    break;
                case 3:
                    showLogisticsResult(msg);
                    break;
                case 4:
                    showPhotoPurchaseResult(msg);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alldingdan);
        MyApplication.getInstance().addActivity(this);
        EventBus.getDefault().register(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        initview();
        setAdapter();
        ll_head.setVisibility(GONE);
        user = new User();
        user = userDAO.getUserInfo();
        Log.e("012", "userid=========" + user.getUserId());

        showWritePurchase();
        showJiaGong();
        showLogistics();
        showPhotoPurchase();
        refreshList();


        //新增
        //接收传值
        Bundle bundle = this.getIntent().getExtras();
        String type = bundle.getString("type");
        Log.e("012", type);
        switch (type) {
            case "1":
                allorder_list1.setVisibility(VISIBLE);
                allorder_list2.setVisibility(GONE);
                allorder_list3.setVisibility(GONE);
                break;
            case "2":
                allorder_list1.setVisibility(VISIBLE);
                allorder_list2.setVisibility(GONE);
                allorder_list3.setVisibility(GONE);
                break;
            case "3":
                allorder_list1.setVisibility(VISIBLE);
                allorder_list2.setVisibility(GONE);
                allorder_list3.setVisibility(GONE);
                break;
            case "4":
                allorder_list1.setVisibility(GONE);
                allorder_list2.setVisibility(VISIBLE);
                allorder_list3.setVisibility(GONE);
                break;
            case "process":
                allorder_list1.setVisibility(GONE);
                allorder_list2.setVisibility(VISIBLE);
                allorder_list3.setVisibility(GONE);
                break;

            case "5":
                allorder_list1.setVisibility(GONE);
                allorder_list2.setVisibility(GONE);
                allorder_list3.setVisibility(VISIBLE);
                break;
            case "logistic":
                allorder_list1.setVisibility(GONE);
                allorder_list2.setVisibility(GONE);
                allorder_list3.setVisibility(VISIBLE);
                break;
        }
//        Log.e("789", getIntent().getExtras() + "");
//        if ("".equals(bundle)) {
//            tv_allorder_line1.setBackgroundResource(R.color.text_color);
//            tv_allorder_line2.setBackgroundResource(R.color.transform);
//            tv_allorder_line3.setBackgroundResource(R.color.transform);
//            tv_allorder_text1.setTextColor(getResources().getColor(R.color.text_color));
//            tv_allorder_text2.setTextColor(getResources().getColor(R.color.gray));
//            tv_allorder_text3.setTextColor(getResources().getColor(R.color.gray));
//
//            switch (user.getUserId()) {
//                case "1":
//                    Log.e("012", "allorder_list1");
//                    allorder_list1.setVisibility(VISIBLE);
//                    allorder_list2.setVisibility(GONE);
//                    allorder_list3.setVisibility(GONE);
//                    break;
//                case "2":
//                    Log.e("012", "allorder_list1");
//                    allorder_list1.setVisibility(VISIBLE);
//                    allorder_list2.setVisibility(GONE);
//                    allorder_list3.setVisibility(GONE);
//                    break;
//                case "3":
//                    Log.e("012", "allorder_list1");
//                    allorder_list1.setVisibility(VISIBLE);
//                    allorder_list2.setVisibility(GONE);
//                    allorder_list3.setVisibility(GONE);
//                    break;
//                case "4":
//                    Log.e("012", "allorder_list2");
//                    allorder_list1.setVisibility(GONE);
//                    allorder_list2.setVisibility(VISIBLE);
//                    allorder_list3.setVisibility(GONE);
//                    break;
//                case "5":
//                    Log.e("012", "allorder_list3");
//                    allorder_list1.setVisibility(GONE);
//                    allorder_list2.setVisibility(GONE);
//                    allorder_list3.setVisibility(VISIBLE);
//                    break;
//                default:
//                    break;
//            }
//
//        } else {
//            Log.e("012", "else");
//            String type = bundle.getString("type");
//            if ("process".equals(type)) {
//                tv_allorder_line1.setBackgroundResource(R.color.transform);
//                tv_allorder_line2.setBackgroundResource(R.color.text_color);
//                tv_allorder_line3.setBackgroundResource(R.color.transform);
//                tv_allorder_text1.setTextColor(getResources().getColor(R.color.gray));
//                tv_allorder_text2.setTextColor(getResources().getColor(R.color.text_color));//找加工
//                tv_allorder_text3.setTextColor(getResources().getColor(R.color.gray));
//                allorder_list1.setVisibility(GONE);
//                allorder_list2.setVisibility(VISIBLE);
//                allorder_list3.setVisibility(GONE);
//                allorder_list4.setVisibility(GONE);
//
//            } else if ("logistic".equals(type)) {
//                tv_allorder_line1.setBackgroundResource(R.color.transform);
//                tv_allorder_line2.setBackgroundResource(R.color.transform);
//                tv_allorder_line3.setBackgroundResource(R.color.text_color);
//                tv_allorder_text1.setTextColor(getResources().getColor(R.color.gray));
//                tv_allorder_text2.setTextColor(getResources().getColor(R.color.gray));
//                tv_allorder_text3.setTextColor(getResources().getColor(R.color.text_color));
//                allorder_list1.setVisibility(GONE);
//                allorder_list2.setVisibility(GONE);
//                allorder_list3.setVisibility(VISIBLE);
//                allorder_list4.setVisibility(GONE);
//            }
//        }
//        //新增
    }

    private void initview() {
        ll_head = (LinearLayout) findViewById(R.id.ll_head);
        tv_allorder_line1 = (TextView) findViewById(R.id.tv_allorder_line1);
        tv_allorder_line2 = (TextView) findViewById(R.id.tv_allorder_line2);
        tv_allorder_line3 = (TextView) findViewById(R.id.tv_allorder_line3);
        tv_allorder_text1 = (TextView) findViewById(R.id.tv_allorder_text1);
        tv_allorder_text2 = (TextView) findViewById(R.id.tv_allorder_text2);//找加工
        tv_allorder_text3 = (TextView) findViewById(R.id.tv_allorder_text3);
        ry_allorder_type1 = (RelativeLayout) findViewById(R.id.ry_allorder_type1);
        ry_allorder_type2 = (RelativeLayout) findViewById(R.id.ry_allorder_type2);
        ry_allorder_type3 = (RelativeLayout) findViewById(R.id.ry_allorder_type3);
        allorder_list1 = (ListView) findViewById(R.id.allorder_list1);
        allorder_list2 = (PullToRefreshListView) findViewById(R.id.allorder_list2);
        allorder_list3 = (PullToRefreshListView) findViewById(R.id.allorder_list3);
        allorder_list4 = (PullToRefreshListView) findViewById(R.id.allorder_list4);
        expandView = (ExpandView_zhaohuo) findViewById(R.id.expandView_zhaohuo);
        expand_line1 = (TextView) findViewById(R.id.expand_zhaohuo_line1);
        expand_line2 = (TextView) findViewById(R.id.expand_zhaohuo_line2);

        allorder_list2.setVisibility(GONE);
        allorder_list3.setVisibility(GONE);
        allorder_list4.setVisibility(GONE);

        ry_allorder_type1.setOnClickListener(this);
        ry_allorder_type2.setOnClickListener(this);
        ry_allorder_type3.setOnClickListener(this);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllOrder.this.setResult(2, getIntent());
                AllOrder.this.finish();
            }
        });
    }

    private void setAdapter() {
        adapter1 = new GrabWriteAdapter(this, list1, 0, 1);
        allorder_list1.setAdapter(adapter1);
        adapter2 = new GrabProcessAdapter(this, list2, 0, 1);
        allorder_list2.setAdapter(adapter2);
        adapter3 = new GrabLogisticAdapter(this, list3, 0, 1);
        allorder_list3.setAdapter(adapter3);
        adapter4 = new GrabPhotoAdapter(this, list4);
        allorder_list4.setAdapter(adapter4);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ry_allorder_type1:
                tv_allorder_line1.setBackgroundResource(R.color.text_color);
                tv_allorder_line2.setBackgroundResource(R.color.transform);
                tv_allorder_line3.setBackgroundResource(R.color.transform);
                tv_allorder_text1.setTextColor(getResources().getColor(R.color.text_color));
                tv_allorder_text2.setTextColor(getResources().getColor(R.color.gray));
                tv_allorder_text3.setTextColor(getResources().getColor(R.color.gray));
                allorder_list1.setVisibility(VISIBLE);
                allorder_list2.setVisibility(GONE);
                allorder_list3.setVisibility(GONE);
//                if (tv_allorder_text1.getText().equals("写的采购单")){
//                    allorder_list1.setVisibility(View.VISIBLE);
//                    allorder_list4.setVisibility(View.GONE);
//                }else {
//                    allorder_list1.setVisibility(View.GONE);
//                    allorder_list4.setVisibility(View.VISIBLE);
//                }
                break;
            case R.id.ry_allorder_type2:
                tv_allorder_line1.setBackgroundResource(R.color.transform);
                tv_allorder_line2.setBackgroundResource(R.color.text_color);
                tv_allorder_line3.setBackgroundResource(R.color.transform);
                tv_allorder_text1.setTextColor(getResources().getColor(R.color.gray));
                tv_allorder_text2.setTextColor(getResources().getColor(R.color.text_color));//找加工
                tv_allorder_text3.setTextColor(getResources().getColor(R.color.gray));
                allorder_list1.setVisibility(GONE);
                allorder_list2.setVisibility(VISIBLE);
                allorder_list3.setVisibility(GONE);
                allorder_list4.setVisibility(GONE);
                break;
            case R.id.ry_allorder_type3:
                tv_allorder_line1.setBackgroundResource(R.color.transform);
                tv_allorder_line2.setBackgroundResource(R.color.transform);
                tv_allorder_line3.setBackgroundResource(R.color.text_color);
                tv_allorder_text1.setTextColor(getResources().getColor(R.color.gray));
                tv_allorder_text2.setTextColor(getResources().getColor(R.color.gray));
                tv_allorder_text3.setTextColor(getResources().getColor(R.color.text_color));
                allorder_list1.setVisibility(GONE);
                allorder_list2.setVisibility(GONE);
                allorder_list3.setVisibility(VISIBLE);
                allorder_list4.setVisibility(GONE);
                break;
//            case R.id.iv_myorder_zhaohuo:
//                if(expandView.isExpand()){
//                    expandView.collapse();
//                }else{
//                    expandView.expand();
//                }
//                break;
        }
    }

    private void expand(TextView tv1, TextView tv2, final TextView tv, final ExpandView_zhaohuo mExpandView) {
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setText("写的采购单");
                mExpandView.collapse();
                allorder_list1.setVisibility(VISIBLE);
                allorder_list4.setVisibility(GONE);
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setText("拍的采购单");
                mExpandView.collapse();
                allorder_list1.setVisibility(GONE);
                allorder_list4.setVisibility(VISIBLE);
            }
        });

    }

    private void showWritePurchase() {
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("order_num", user.getUserPhone()));
        nvps.add(new BasicNameValuePair("page", page1 + ""));
        String url = Internet.SHOW_PURCHASE_URL + "?order_num=" + user.getUserPhone() + "&page=" + page1;
        httpUtil.get(mHandler, url, 1);
    }

    private void showWritePurchaseResult(Message msg) {
        String s = (String) msg.obj;
        Log.e("012", "采购单============" + s);
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONObject data = jsonObject.getJSONObject("data");
            SharedPreferences sp = getSharedPreferences("xiedan", MODE_PRIVATE);
            Log.e("012", "sp+========" + sp.getString("ordernum", ""));
            for (int i = 1; i < data.length() + 1; i++) {
                PurchaseWrite order = new PurchaseWrite();
                JSONObject j = data.getJSONObject("new" + i);
                String show1 = j.getString("show1");
                String phone_num = j.getString("phone_num");
                String page = j.getString("page");
                Log.e("012", "page+========" + sp.getString("ordernum", ""));
                String acceptor = j.getString("acceptor");
                String order_num = j.getString("order_num");
                String product_name = j.getString("product_name");
                String back_stage = j.getString("back_stage");
                String ton_amount = j.getString("ton_amount");
                String gener_time = j.getString("gener_time");
                String comment = j.getString("comment");
                String standard = j.getString("standard");
                String type_num = j.getString("type_num");
                String material = j.getString("material");
                String state = j.getString("state");
                order.setState(state);
                order.setAcceptor(acceptor);
                order.setBack_stage(back_stage);
                order.setStandard(standard);
                order.setType_num(type_num);
                order.setMaterial(material);
                order.setComment(comment);
                order.setGener_time(gener_time);
                order.setOrder_num(order_num);
                order.setPage(page);
                order.setPhone_num(phone_num);
                order.setProduct_name(product_name);
                order.setShow1(show1);
                order.setTon_amount(ton_amount);
                order.setShowup(false);
                if (list1.size() == 0) {
                    list1.add(order);
                } else if (order_num.equals((list1.get(list1.size() - 1)).getOrder_num())) {
                    Log.e("012", "1===========");
                    list1.get(list1.size() - 1).setStandard(list1.get(list1.size() - 1).getStandard() + ";" + standard);
                    list1.get(list1.size() - 1).setMaterial(list1.get(list1.size() - 1).getMaterial() + ";" + material);
                    list1.get(list1.size() - 1).setTon_amount(list1.get(list1.size() - 1).getTon_amount() + ";" + ton_amount);
                } else if (sp.getString("ordernum", "").contains(order_num)) {
                    Log.e("012", "2==========");
                } else {
                    Log.e("012", "3===========");
                    list1.add(order);
                }
//                if (order_num.equals(list1.get(list1.size()).getOrder_num())) {
//                    Log.e("012", "1===========");
//                    list1.get(list1.size()).setStandard(list1.get(list1.size()).getStandard() + ";" + standard);
//                    list1.get(list1.size()).setMaterial(list1.get(list1.size()).getMaterial() + ";" + material);
//                    list1.get(list1.size()).setTon_amount(list1.get(list1.size()).getTon_amount() + ";" + ton_amount);
//                } else if (sp.getString("ordernum", "").contains(order_num)) {
//                    Log.e("012", "2==========");
//                } else {
//                    Log.e("012", "3===========");
//                    list1.add(order);
//                }
            }

            Collections.sort(list1, new Comparator<PurchaseWrite>() {
                /**
                 *
                 * @param lhs
                 * @param rhs
                 * @return an integer < 0 if lhs is less than rhs, 0 if they are
                 *         equal, and > 0 if lhs is greater than rhs,比较数据大小时,这里比的是时间
                 */
                @Override
                public int compare(PurchaseWrite lhs, PurchaseWrite rhs) {
                    Date date1 = com.example.administrator.gangmaijia.Util.DateUtils.stringToDate(lhs.getGener_time());
                    Date date2 = com.example.administrator.gangmaijia.Util.DateUtils.stringToDate(rhs.getGener_time());
                    // 对日期字段进行升序，如果欲降序可采用after方法
                    if (date1.before(date2)) {
                        return 1;
                    }
                    return -1;
                }
            });
            Log.e("012", "list=======" + list1.toString());
            //将相同订单号的定单整合成一个订单
            adapter1.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showPhotoPurchase() {
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("phone_num", user.getUserPhone()));
//        httpUtil.post(mHandler, Internet.SHOW_PHOTO_URL,4,nvps);
        httpUtil.get(mHandler, Internet.SHOW_PHOTO_URL, 4);
    }

    private void showPhotoPurchaseResult(Message msg) {
        String s = (String) msg.obj;
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONObject data = jsonObject.getJSONObject("data");
            com.orhanobut.logger.Logger.e(data.toString());
            for (int i = 1; i < data.length() + 1; i++) {
                PurchasePhoto order = new PurchasePhoto();
                JSONObject j = data.getJSONObject("new" + i);
                String show1 = j.getString("show1");
                String phone_num = j.getString("phone_num");
                String page = j.getString("page");
                String acceptor = j.getString("acceptor");
                String order_num = j.getString("order_num");
                String state = j.getString("state");
                String photo = j.getString("photo_path");
                String[] split = photo.split(";");
                List<String> photo_path = new ArrayList<>();
                for (String url : split) {
                    photo_path.add(url);
                }
                order.setAcceptor(acceptor);
                order.setState(state);
                order.setOrder_num(order_num);
                order.setPage(page);
                order.setPhone_num(phone_num);
                order.setShow1(show1);
                order.setPhoto_path(photo_path);
                list4.add(order);
            }
            adapter4.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showJiaGong() {
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("order_num", user.getUserPhone()));
        nvps.add(new BasicNameValuePair("page", page2 + ""));
//        httpUtil.post(mHandler, Internet.SHOW_PROCESS_URL,2,nvps);
        String url = Internet.SHOW_PROCESS_URL + "?order_num=" + user.getUserPhone() + "&page=" + page2;
        Log.e("789", "url======" + url);
        httpUtil.get(mHandler, url, 2);
    }

    private void showJiaGongResult(Message msg) {
        String s = (String) msg.obj;
        try {
            Log.e("789", "加工============" + s);
            JSONObject jsonObject = new JSONObject(s);
            JSONObject data = jsonObject.getJSONObject("data");
            SharedPreferences sharedPreferences = getSharedPreferences("machininginfo", Context.MODE_PRIVATE); //私有数据
//        if (sharedPreferences.getString("ordernum", "").equals(lists.get(position).getOrder_num())) {
//            //如果 这个订单被删除过 那么 就不再显示
////            lists.remove(position);
////            GrabProcessAdapter.this.notifyDataSetChanged();
//            return null;
//        }
            for (int i = 1; i < data.length() + 1; i++) {
                JiaGongOrder order = new JiaGongOrder();
                JSONObject j = data.getJSONObject("new" + i);
                String show1 = j.getString("show1");
                String phone_num = j.getString("phone_num");
                String page = j.getString("page");
                String process_type = j.getString("process_type");
                String order_num = j.getString("order_num");

                String product_name = j.getString("product_name");
                String state = j.getString("state");
                String ton_amount = j.getString("ton_amount");
                String gener_time = j.getString("gener_time");
                String comment = j.getString("comment");
                String process_content = j.getString("process_content");
                order.setComment(comment);
                order.setGener_time(gener_time);
                order.setOrder_num(order_num);
                order.setPage(page);
                order.setPhone_num(phone_num);
                order.setProcess_content(process_content);
                order.setProcess_type(process_type);
                order.setProduct_name(product_name);
                order.setShow1(show1);
                order.setState(state);
                order.setTon_amount(ton_amount);
                order.setShowup(false);
                if (sharedPreferences.getString("ordernum", "").contains(order_num)) {

                } else {
                    list2.add(order);
                }
                Log.d("ddd", order_num);
            }
            adapter2.notifyDataSetChanged();
//            Log.e("789", list2.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showLogistics() {
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("order_num", user.getUserPhone()));
        nvps.add(new BasicNameValuePair("page", page3 + ""));
//        httpUtil.post(mHandler, Internet.SHOW_LGOISTIC_URL,3,nvps);
        String url = Internet.SHOW_LGOISTIC_URL + "?order_num=" + user.getUserPhone() + "&page=" + page3;
        httpUtil.get(mHandler, url, 3);
    }

    private void showLogisticsResult(Message msg) {
        String s = (String) msg.obj;
        Log.e("789", "物流============" + s);
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONObject data = jsonObject.getJSONObject("data");
            SharedPreferences sp = getSharedPreferences("cheyuan", MODE_PRIVATE);
            for (int i = 1; i < data.length() + 1; i++) {
                Logistic order = new Logistic();
                JSONObject j = data.getJSONObject("new" + i);
                String show1 = j.getString("show1");
                String phone_num = j.getString("phone_num");
                String page = j.getString("page");
                String length = j.getString("length");
                String order_num = j.getString("order_num");
                String product_name = j.getString("product_name");
                String state = j.getString("state");
                String ton_amount = j.getString("ton_amount");
                String gener_time = j.getString("gener_time");
                String delivery_time = j.getString("delivery_time");
                String delivery_place = j.getString("delivery_place");
                String transport_way = j.getString("transport_way");
                String special_demand = j.getString("special_demand");
                String destination = j.getString("destination");
                order.setTransport_way(transport_way);
                order.setLength(length);
                order.setDelivery_place(delivery_place);
                order.setDelivery_time(delivery_time);
                order.setSpecial_demand(special_demand);
                order.setDestination(destination);
                order.setGener_time(gener_time);
                order.setOrder_num(order_num);
                order.setPage(page);
                order.setPhone_num(phone_num);
                order.setProduct_name(product_name);
                order.setShow1(show1);
                order.setState(state);
                order.setTon_amount(ton_amount);
                order.setShowup(false);
                if (sp.getString("ordernum", "").contains(order_num)) {

                } else {
                    list3.add(order);
                }
            }
            adapter3.notifyDataSetChanged();
//            Log.e("789", list3.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshList() {
//        allorder_list1.setMode(PullToRefreshBase.Mode.BOTH);
        allorder_list2.setMode(PullToRefreshBase.Mode.BOTH);
        allorder_list3.setMode(PullToRefreshBase.Mode.BOTH);
        allorder_list4.setMode(PullToRefreshBase.Mode.BOTH);

//        allorder_list1.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
//            @Override
//            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
//                String str = DateUtils.formatDateTime(AllOrder.this, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
//                refreshType = 1;
//                if (refreshView.isShownHeader()) {
//                    refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后更新时间:" + str);
//                    page1 = 0;
//                    new GetDataTask1().execute();
//                }
//                if (refreshView.isShownFooter()) {
//                    refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后加载时间:" + str);
//                    page1++;
//                    new GetDataTask2().execute();
//                }
//            }
//        });

        allorder_list2.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String str = DateUtils.formatDateTime(AllOrder.this, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshType = 2;
                if (refreshView.isShownHeader()) {
                    refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后更新时间:" + str);
                    page2 = 0;
                    new GetDataTask1().execute();
                }
                if (refreshView.isShownFooter()) {
                    refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后加载时间:" + str);
                    page2++;
                    new GetDataTask2().execute();
                }
            }
        });

        allorder_list3.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String str = DateUtils.formatDateTime(AllOrder.this, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshType = 3;
                if (refreshView.isShownHeader()) {
                    refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后更新时间:" + str);
                    page3 = 0;
                    new GetDataTask1().execute();
                }
                if (refreshView.isShownFooter()) {
                    refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后加载时间:" + str);
                    page3++;
                    new GetDataTask2().execute();
                }
            }
        });

//        allorder_list4.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
//            @Override
//            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
//                String str = DateUtils.formatDateTime(MyOrder.this, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
//                refreshType = 4;
//                if (refreshView.isShownHeader()) {
//                    refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后更新时间:" + str);
//                    page4=1;
//                    new MyOrder.GetDataTask1().execute();
//                }
//                if (refreshView.isShownFooter()) {
//                    refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后加载时间:" + str);
//                    page4++;
//                    new MyOrder.GetDataTask2().execute();
//                }
//            }
//        });

    }

    private class GetDataTask1 extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(1500);
                switch (refreshType) {
                    case 1:
                        list1.clear();
                        showWritePurchase();
                        break;
                    case 2:
                        list2.clear();
                        showJiaGong();
                        break;
                    case 3:
                        list3.clear();
                        showLogistics();
                        break;
//                    case 4:
//                        list4.clear();
//                        showPhotoPurchase();
//                        break;
                }
            } catch (InterruptedException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            // Do some stuff here

            // Call onRefreshComplete when the list has been refreshed.
//            myorder_list1.onRefreshComplete();
            switch (refreshType) {
                case 1:
//                    allorder_list1.onRefreshComplete();
//                    allorder_list1.setVisibility(VISIBLE);
                    break;
                case 2:
                    allorder_list2.onRefreshComplete();
                    allorder_list2.setVisibility(VISIBLE);
                    break;
                case 3:
                    allorder_list3.onRefreshComplete();
                    allorder_list3.setVisibility(VISIBLE);
                    break;
//                case 4:
//                    allorder_list4.onRefreshComplete();
//                    break;
            }
            super.onPostExecute(result);
        }

    }

    private class GetDataTask2 extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(1500);
                switch (refreshType) {
                    case 1:
                        showWritePurchase();
                        break;
                    case 2:
                        showJiaGong();
                        break;
                    case 3:
                        showLogistics();
                        break;
//                    case 4:
//                        showPhotoPurchase();
//                        break;
                }

            } catch (InterruptedException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            // Do some stuff here

            // Call onRefreshComplete when the list has been refreshed.
//            myorder_list1.onRefreshComplete();
            switch (refreshType) {
                case 1:
//                    allorder_list1.onRefreshComplete();
//                    allorder_list1.setVisibility(VISIBLE);
                    break;
                case 2:
                    allorder_list2.onRefreshComplete();
                    allorder_list2.setVisibility(VISIBLE);
                    break;
                case 3:
                    allorder_list3.onRefreshComplete();
                    allorder_list3.setVisibility(VISIBLE);
                    break;
//                case 4:
//                    allorder_list4.onRefreshComplete();
//                    break;
            }
            super.onPostExecute(result);
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        System.out.println("----------onMessageEvent----------");
        if (event.getType().equals("qiangdan")) {

            if (event.getMsg().equals("purchase")) {
                list1.clear();
                page1 = 0;
                showWritePurchase();
            } else if (event.getMsg().equals("process")) {
                list2.clear();
                page2 = 0;
                showJiaGong();
            } else if (event.getMsg().equals("logistics")) {
                list3.clear();
                page3 = 0;
                showLogistics();
            }
        }
    }

    ;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AllOrder.this.setResult(2, getIntent());
            AllOrder.this.finish();
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
