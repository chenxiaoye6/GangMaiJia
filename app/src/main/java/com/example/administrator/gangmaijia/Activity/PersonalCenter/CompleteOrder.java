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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/9.
 */

public class CompleteOrder extends Activity {

    private TextView tv_completed_line1, tv_completed_line2, tv_completed_line3, tv_completed_text1, tv_completed_text2, tv_completed_text3;
    private PullToRefreshListView completed_list1, completed_list2, completed_list3, completed_list4;
    private RelativeLayout ry_completed_type1, ry_completed_type2, ry_completed_type3;
    private LinearLayout ll_head_complete;
    private ExpandView_zhaohuo expandView;
    private TextView expand_line1, expand_line2;
    private int refreshType = 1;
    private int page1 = 0;
    private int page2 = 0;
    private int page3 = 0;
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
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.completedingdan);
        MyApplication.getInstance().addActivity(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        initview();
        user = new User();
        user = userDAO.getUserInfo();
        //根据用户类型判断显示的内容
        switch (user.getUserId()) {
            case "1":
                completed_list1.setVisibility(View.VISIBLE);
                completed_list2.setVisibility(View.GONE);
                completed_list3.setVisibility(View.GONE);
                break;
            case "2":
                completed_list1.setVisibility(View.VISIBLE);
                completed_list2.setVisibility(View.GONE);
                completed_list3.setVisibility(View.GONE);
                break;
            case "3":
                completed_list1.setVisibility(View.VISIBLE);
                completed_list2.setVisibility(View.GONE);
                completed_list3.setVisibility(View.GONE);
                break;
            case "4":
                completed_list1.setVisibility(View.GONE);
                completed_list2.setVisibility(View.VISIBLE);
                completed_list3.setVisibility(View.GONE);
                break;
            case "5":
                completed_list1.setVisibility(View.GONE);
                completed_list2.setVisibility(View.GONE);
                completed_list3.setVisibility(View.VISIBLE);
                break;
        }
        setAdapter();
        showWritePurchase();
        showJiaGong();
        showLogistics();
        refreshList();
        ll_head_complete.setVisibility(View.GONE);

    }

    private void initview() {
        ll_head_complete = (LinearLayout) findViewById(R.id.ll_head_complete);
        tv_completed_line1 = (TextView) findViewById(R.id.tv_completed_line1);
        tv_completed_line2 = (TextView) findViewById(R.id.tv_completed_line2);
        tv_completed_line3 = (TextView) findViewById(R.id.tv_completed_line3);
        tv_completed_text1 = (TextView) findViewById(R.id.tv_completed_text1);
        tv_completed_text2 = (TextView) findViewById(R.id.tv_completed_text2);
        tv_completed_text3 = (TextView) findViewById(R.id.tv_completed_text3);
        ry_completed_type1 = (RelativeLayout) findViewById(R.id.ry_completed_type1);
        ry_completed_type2 = (RelativeLayout) findViewById(R.id.ry_completed_type2);
        ry_completed_type3 = (RelativeLayout) findViewById(R.id.ry_completed_type3);
        completed_list1 = (PullToRefreshListView) findViewById(R.id.completed_list1);
        completed_list2 = (PullToRefreshListView) findViewById(R.id.completed_list2);
        completed_list3 = (PullToRefreshListView) findViewById(R.id.completed_list3);
        completed_list4 = (PullToRefreshListView) findViewById(R.id.completed_list4);
        expandView = (ExpandView_zhaohuo) findViewById(R.id.expandView_zhaohuo);
        expand_line1 = (TextView) findViewById(R.id.expand_zhaohuo_line1);
        expand_line2 = (TextView) findViewById(R.id.expand_zhaohuo_line2);

        completed_list2.setVisibility(View.GONE);
        completed_list3.setVisibility(View.GONE);
        completed_list4.setVisibility(View.GONE);

//        ry_completed_type1.setOnClickListener(this);
//        ry_completed_type2.setOnClickListener(this);
//        ry_completed_type3.setOnClickListener(this);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setAdapter() {
        adapter1 = new GrabWriteAdapter(this, list1, 1, 2);
        completed_list1.setAdapter(adapter1);
        adapter2 = new GrabProcessAdapter(this, list2, 1, 2);
        completed_list2.setAdapter(adapter2);
        adapter3 = new GrabLogisticAdapter(this, list3, 1, 2);
        completed_list3.setAdapter(adapter3);
        adapter4 = new GrabPhotoAdapter(this, list4);
        completed_list4.setAdapter(adapter4);
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.ry_completed_type1:
//                tv_completed_line1.setBackgroundResource(R.color.text_color);
//                tv_completed_line2.setBackgroundResource(R.color.transform);
//                tv_completed_line3.setBackgroundResource(R.color.transform);
//                tv_completed_text1.setTextColor(getResources().getColor(R.color.text_color));
//                tv_completed_text2.setTextColor(getResources().getColor(R.color.gray));
//                tv_completed_text3.setTextColor(getResources().getColor(R.color.gray));
//                completed_list1.setVisibility(View.VISIBLE);
//                completed_list2.setVisibility(View.GONE);
//                completed_list3.setVisibility(View.GONE);
////                if (tv_allorder_text1.getText().equals("写的采购单")){
////                    allorder_list1.setVisibility(View.VISIBLE);
////                    allorder_list4.setVisibility(View.GONE);
////                }else {
////                    allorder_list1.setVisibility(View.GONE);
////                    allorder_list4.setVisibility(View.VISIBLE);
////                }
//                break;
//            case R.id.ry_completed_type2:
//                tv_completed_line1.setBackgroundResource(R.color.transform);
//                tv_completed_line2.setBackgroundResource(R.color.text_color);
//                tv_completed_line3.setBackgroundResource(R.color.transform);
//                tv_completed_text1.setTextColor(getResources().getColor(R.color.gray));
//                tv_completed_text2.setTextColor(getResources().getColor(R.color.text_color));
//                tv_completed_text3.setTextColor(getResources().getColor(R.color.gray));
//                completed_list1.setVisibility(View.GONE);
//                completed_list2.setVisibility(View.VISIBLE);
//                completed_list3.setVisibility(View.GONE);
//                completed_list4.setVisibility(View.GONE);
//                break;
//            case R.id.ry_completed_type3:
//                tv_completed_line1.setBackgroundResource(R.color.transform);
//                tv_completed_line2.setBackgroundResource(R.color.transform);
//                tv_completed_line3.setBackgroundResource(R.color.text_color);
//                tv_completed_text1.setTextColor(getResources().getColor(R.color.gray));
//                tv_completed_text2.setTextColor(getResources().getColor(R.color.gray));
//                tv_completed_text3.setTextColor(getResources().getColor(R.color.text_color));
//                completed_list1.setVisibility(View.GONE);
//                completed_list2.setVisibility(View.GONE);
//                completed_list3.setVisibility(View.VISIBLE);
//                completed_list4.setVisibility(View.GONE);
//                break;
////            case R.id.iv_myorder_zhaohuo:
////                if(expandView.isExpand()){
////                    expandView.collapse();
////                }else{
////                    expandView.expand();
////                }
////                break;
//        }
//    }


    private void showWritePurchase() {
        Log.e("111", "1");
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("co_phone_num", user.getUserPhone()));
        nvps.add(new BasicNameValuePair("page", page1 + ""));
        Log.e("111", "page=" + page1 + "==============");
        Log.e("789", "nvps" + nvps.toString());
        httpUtil.post(mHandler, Internet.ALL_PURCHASE_URL, 1, nvps);
//        Log.e("111","userpjhpne"+user.getUserPhone());
//        httpUtil.get(mHandler, Internet.ALL_PURCHASE_URL + "?co_phone_num=" + user.getUserPhone() + "&page=" + page1, 1);
    }

    private void showWritePurchaseResult(Message msg) {
        Log.e("111", "2");
        String s = (String) msg.obj;
        Log.e("111", s + "s+=====================");
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONObject data = jsonObject.getJSONObject("data");
            SharedPreferences sp = getSharedPreferences("xiedan", MODE_PRIVATE);
            for (int i = 1; i < data.length() + 1; i++) {
                try {
                    PurchaseWrite order = new PurchaseWrite();
                    JSONObject j = data.getJSONObject("new" + i);
                    String show1 = j.getString("show1");
                    String phone_num = j.getString("phone_num");
                    String page = j.getString("page");
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
                    adapter1.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showJiaGong() {
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("co_phone_num", user.getUserPhone()));
        nvps.add(new BasicNameValuePair("page", page2 + ""));
//        httpUtil.post(mHandler, Internet.ALL_PROCESS_URL,2,nvps);
        httpUtil.get(mHandler, Internet.ALL_PROCESS_URL + "?co_phone_num=" + user.getUserPhone() + "&page=" + page2, 2);
    }

    private void showJiaGongResult(Message msg) {
        String s = (String) msg.obj;
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONObject data = jsonObject.getJSONObject("data");
            SharedPreferences sharedPreferences = getSharedPreferences("machininginfo", Context.MODE_PRIVATE); //私有数据
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
            }
            adapter2.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showLogistics() {
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("co_phone_num", user.getUserPhone()));
        nvps.add(new BasicNameValuePair("page", page3 + ""));
//        httpUtil.post(mHandler, Internet.ALL_LOGISTIC_URL,3,nvps);
        httpUtil.get(mHandler, Internet.ALL_LOGISTIC_URL + "?co_phone_num=" + user.getUserPhone() + "&page=" + page3, 3);
    }

    private void showLogisticsResult(Message msg) {
        String s = (String) msg.obj;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void refreshList() {
        completed_list1.setMode(PullToRefreshBase.Mode.BOTH);
        completed_list2.setMode(PullToRefreshBase.Mode.BOTH);
        completed_list3.setMode(PullToRefreshBase.Mode.BOTH);
        completed_list4.setMode(PullToRefreshBase.Mode.BOTH);

        completed_list1.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String str = DateUtils.formatDateTime(CompleteOrder.this, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshType = 1;
                if (refreshView.isShownHeader()) {
                    refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后更新时间:" + str);
                    page1 = 0;
                    new GetDataTask1().execute();
                }
                if (refreshView.isShownFooter()) {
                    refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后加载时间:" + str);
                    page1++;
                    new GetDataTask2().execute();
                }
            }
        });

        completed_list2.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String str = DateUtils.formatDateTime(CompleteOrder.this, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
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

        completed_list3.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String str = DateUtils.formatDateTime(CompleteOrder.this, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
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
                        Log.e("123", "list1clean");
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
                    completed_list1.onRefreshComplete();
                    break;
                case 2:
                    completed_list2.onRefreshComplete();
                    break;
                case 3:
                    completed_list3.onRefreshComplete();
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
                    completed_list1.onRefreshComplete();
                    break;
                case 2:
                    completed_list2.onRefreshComplete();
                    break;
                case 3:
                    completed_list3.onRefreshComplete();
                    break;
//                case 4:
//                    allorder_list4.onRefreshComplete();
//                    break;
            }
            super.onPostExecute(result);
        }

    }

}
