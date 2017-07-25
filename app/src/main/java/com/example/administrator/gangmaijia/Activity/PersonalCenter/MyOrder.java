package com.example.administrator.gangmaijia.Activity.PersonalCenter;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.gangmaijia.Adapter.JiaGongAdapter;
import com.example.administrator.gangmaijia.Adapter.LogisticAdapter;
import com.example.administrator.gangmaijia.Adapter.PurchasePhotoAdapter;
import com.example.administrator.gangmaijia.Adapter.PurchaseWriteAdapter;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/21.
 */

public class MyOrder extends Activity implements View.OnClickListener{

    private TextView tv_myorder_line1,tv_myorder_line2,tv_myorder_line3,tv_myorder_text1,tv_myorder_text2,tv_myorder_text3 ;
    private PullToRefreshListView myorder_list1,myorder_list2,myorder_list3,myorder_list4 ;
    private ImageView iv_myorder_zhaohuo,back ;
    private RelativeLayout ry_myorder_type1,ry_myorder_type2,ry_myorder_type3 ;
    private ExpandView_zhaohuo expandView ;
    private TextView expand_line1,expand_line2 ;
    private int refreshType = 1 ;
    private int page1 = 0 ;
    private int page2 = 0 ;
    private int page3 = 0 ;
    private int page4 = 0 ;
    private HttpUtil httpUtil = new HttpUtil();

    private UserDAO userDAO = new UserDAO(this);
    private  User user ;
    private List<PurchaseWrite> list1 = new ArrayList<>();
    private List<JiaGongOrder> list2 = new ArrayList<>();
    private List<Logistic> list3 = new ArrayList<>();
    private List<PurchasePhoto> list4 = new ArrayList<>();
    private PurchaseWriteAdapter adapter1;
    private JiaGongAdapter adapter2;
    private LogisticAdapter adapter3;
    private PurchasePhotoAdapter adapter4;


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
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
        setContentView(R.layout.myorder);
        MyApplication.getInstance().addActivity(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        initview();
        setAdapter();
        user = new User();
        user = userDAO.getUserInfo();
        showWritePurchase();
        showPhotoPurchase();
        showJiaGong();
        showLogistics();
        refreshList();
    }

    private void initview(){
        tv_myorder_line1 = (TextView) findViewById(R.id.tv_myorder_line1);
        tv_myorder_line2 = (TextView) findViewById(R.id.tv_myorder_line2);
        tv_myorder_line3 = (TextView) findViewById(R.id.tv_myorder_line3);
        tv_myorder_text1 = (TextView) findViewById(R.id.tv_myorder_text1);
        tv_myorder_text2 = (TextView) findViewById(R.id.tv_myorder_text2);
        tv_myorder_text3 = (TextView) findViewById(R.id.tv_myorder_text3);
        ry_myorder_type1 = (RelativeLayout) findViewById(R.id.ry_myorder_type1);
        ry_myorder_type2 = (RelativeLayout) findViewById(R.id.ry_myorder_type2);
        ry_myorder_type3 = (RelativeLayout) findViewById(R.id.ry_myorder_type3);
        myorder_list1 = (PullToRefreshListView) findViewById(R.id.myorder_list1);
        myorder_list2 = (PullToRefreshListView) findViewById(R.id.myorder_list2);
        myorder_list3 = (PullToRefreshListView) findViewById(R.id.myorder_list3);
        myorder_list4 = (PullToRefreshListView) findViewById(R.id.myorder_list4);
        iv_myorder_zhaohuo = (ImageView) findViewById(R.id.iv_myorder_zhaohuo);
        back = (ImageView) findViewById(R.id.back);
        expandView = (ExpandView_zhaohuo) findViewById(R.id.expandView_zhaohuo);
        expand_line1 = (TextView) findViewById(R.id.expand_zhaohuo_line1);
        expand_line2 = (TextView) findViewById(R.id.expand_zhaohuo_line2);

        myorder_list2.setVisibility(View.GONE);
        myorder_list3.setVisibility(View.GONE);
        myorder_list4.setVisibility(View.GONE);

        back.setOnClickListener(this);
        ry_myorder_type1.setOnClickListener(this);
        ry_myorder_type2.setOnClickListener(this);
        ry_myorder_type3.setOnClickListener(this);
        iv_myorder_zhaohuo.setOnClickListener(this);

        expand(expand_line1,expand_line2,tv_myorder_text1,expandView);
    }

    private void setAdapter(){
        adapter1 = new PurchaseWriteAdapter(this,list1);
        myorder_list1.setAdapter(adapter1);
        adapter2 = new JiaGongAdapter(this,list2);
        myorder_list2.setAdapter(adapter2);
        adapter3 = new LogisticAdapter(this,list3);
        myorder_list3.setAdapter(adapter3);
        adapter4 = new PurchasePhotoAdapter(this,list4);
        myorder_list4.setAdapter(adapter4);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ry_myorder_type1:
                Log.e("点击采购单","采购单");
                tv_myorder_line1.setBackgroundResource(R.color.text_color);
                tv_myorder_line2.setBackgroundResource(R.color.transform);
                tv_myorder_line3.setBackgroundResource(R.color.transform);
                tv_myorder_text1.setTextColor(getResources().getColor(R.color.text_color));
                tv_myorder_text2.setTextColor(getResources().getColor(R.color.gray));
                tv_myorder_text3.setTextColor(getResources().getColor(R.color.gray));
                myorder_list2.setVisibility(View.GONE);
                myorder_list3.setVisibility(View.GONE);
                if (tv_myorder_text1.getText().equals("写的采购单")){
                    myorder_list1.setVisibility(View.VISIBLE);
                    myorder_list4.setVisibility(View.GONE);
                }else {
                    myorder_list1.setVisibility(View.GONE);
                    myorder_list4.setVisibility(View.VISIBLE);
                }
                iv_myorder_zhaohuo.setEnabled(true);
                break;
            case R.id.ry_myorder_type2:
                Log.e("点击采购单","加工单");
                tv_myorder_line1.setBackgroundResource(R.color.transform);
                tv_myorder_line2.setBackgroundResource(R.color.text_color);
                tv_myorder_line3.setBackgroundResource(R.color.transform);
                tv_myorder_text1.setTextColor(getResources().getColor(R.color.gray));
                tv_myorder_text2.setTextColor(getResources().getColor(R.color.text_color));
                tv_myorder_text3.setTextColor(getResources().getColor(R.color.gray));
                myorder_list1.setVisibility(View.GONE);
                myorder_list2.setVisibility(View.VISIBLE);
                myorder_list3.setVisibility(View.GONE);
                myorder_list4.setVisibility(View.GONE);
                iv_myorder_zhaohuo.setEnabled(false);
                break;
            case R.id.ry_myorder_type3:
                Log.e("点击物流单","物流单");
                tv_myorder_line1.setBackgroundResource(R.color.transform);
                tv_myorder_line2.setBackgroundResource(R.color.transform);
                tv_myorder_line3.setBackgroundResource(R.color.text_color);
                tv_myorder_text1.setTextColor(getResources().getColor(R.color.gray));
                tv_myorder_text2.setTextColor(getResources().getColor(R.color.gray));
                tv_myorder_text3.setTextColor(getResources().getColor(R.color.text_color));
                myorder_list1.setVisibility(View.GONE);
                myorder_list2.setVisibility(View.GONE);
                myorder_list3.setVisibility(View.VISIBLE);
                myorder_list4.setVisibility(View.GONE);
                iv_myorder_zhaohuo.setEnabled(false);
                break;
            case R.id.iv_myorder_zhaohuo:
                if(expandView.isExpand()){
                    expandView.collapse();
                }else{
                    expandView.expand();
                }
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    private void expand(TextView tv1, TextView tv2,  final TextView tv, final ExpandView_zhaohuo mExpandView){
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setText("写的采购单");
                mExpandView.collapse();
                myorder_list1.setVisibility(View.VISIBLE);
                myorder_list4.setVisibility(View.GONE);
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setText("拍的采购单");
                mExpandView.collapse();
                myorder_list1.setVisibility(View.GONE);
                myorder_list4.setVisibility(View.VISIBLE);
            }
        });

    }

    private void showWritePurchase(){
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("phone_num",user.getUserPhone()));
        nvps.add(new BasicNameValuePair("page",page1+""));
        httpUtil.post(mHandler, Internet.SHOW_PURCHASE_URL,1,nvps);
    }

    private void showWritePurchaseResult(Message msg){
        String s = (String)msg.obj;
        Log.e("采购单","采购单="+s);
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONObject data = jsonObject.getJSONObject("data");
            for (int i = data.length();i>0;i--){
                PurchaseWrite order = new PurchaseWrite();
                JSONObject j = data.getJSONObject("new" + i );
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
                list1.add(order);
                adapter1.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showPhotoPurchase(){
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("phone_num",user.getUserPhone()));
        nvps.add(new BasicNameValuePair("page",page4+""));
        httpUtil.post(mHandler, Internet.SHOW_PHOTO_URL,4,nvps);
    }

    private void showPhotoPurchaseResult(Message msg){
        String s = (String)msg.obj;
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONObject data = jsonObject.getJSONObject("data");
            for (int i = data.length();i>0;i--){
                PurchasePhoto order = new PurchasePhoto();
                JSONObject j = data.getJSONObject("new" + i );
                String show1 = j.getString("show1");
                String phone_num = j.getString("phone_num");
                String page = j.getString("page");
                String acceptor = j.getString("acceptor");
                String order_num = j.getString("order_num");
                String state = j.getString("state");
                String photo = j.getString("photo_path");
                String[] split = photo.split(";");
                List<String> photo_path = new ArrayList<>();
//                for (String url:split){
//                    photo_path.add(url);
//                }
                for (int k = 1;k<split.length;k++){
                    photo_path.add(split[k]);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapter4.notifyDataSetChanged();
    }

    //有问题,应该是post
    private void showJiaGong(){
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("phone_num",user.getUserPhone()));
        nvps.add(new BasicNameValuePair("page",page2+""));
//        httpUtil.post(mHandler, Internet.SHOW_PROCESS_URL,2,nvps);
        Log.e("e","加工单电话="+user.getUserPhone());
        httpUtil.get(mHandler, Internet.SHOW_PROCESS_URL+"?phone_num="+user.getUserPhone()+"&page="+page2,2);
    }

    private void showJiaGongResult(Message msg){
        String s = (String)msg.obj;
        try {
            JSONObject jsonObject = new JSONObject(s);
            Log.e("e","加工单="+jsonObject.toString());
            JSONObject data = jsonObject.getJSONObject("data");
            for (int i = data.length();i>0;i--){
                JiaGongOrder order = new JiaGongOrder();
                JSONObject j = data.getJSONObject("new" + i );
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
                list2.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapter2.notifyDataSetChanged();
    }


    //post
    private void showLogistics(){
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("phone_num",user.getUserPhone()));
        nvps.add(new BasicNameValuePair("page",page3+""));
        Log.e("e","物流单电话="+user.getUserPhone());
        httpUtil.post(mHandler, Internet.SHOW_LGOISTIC_URL,3,nvps);
    }

    private void showLogisticsResult(Message msg){
        String s = (String)msg.obj;
        Log.e("e","物流单="+s);
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONObject data = jsonObject.getJSONObject("data");
            for (int i = data.length();i>0;i--){
                Logistic order = new Logistic();
                JSONObject j = data.getJSONObject("new" + i );
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
                list3.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapter3.notifyDataSetChanged();
    }

    private void refreshList(){
        myorder_list1.setMode(PullToRefreshBase.Mode.BOTH);
        myorder_list2.setMode(PullToRefreshBase.Mode.BOTH);
        myorder_list3.setMode(PullToRefreshBase.Mode.BOTH);
        myorder_list4.setMode(PullToRefreshBase.Mode.BOTH);

        myorder_list1.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String str = DateUtils.formatDateTime(MyOrder.this, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshType = 1;
                if (refreshView.isShownHeader()) {
                    refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后更新时间:" + str);
                    page1=0;
                    new GetDataTask1().execute();
                }
                if (refreshView.isShownFooter()) {
                    refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后加载时间:" + str);
                    new GetDataTask2().execute();
                }
            }
        });

        myorder_list2.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String str = DateUtils.formatDateTime(MyOrder.this, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshType = 2;
                if (refreshView.isShownHeader()) {
                    refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后更新时间:" + str);
                    page2=0;
                    new GetDataTask1().execute();
                }
                if (refreshView.isShownFooter()) {
                    refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后加载时间:" + str);
                    page2++;
                    new GetDataTask2().execute();
                }
            }
        });

        myorder_list3.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String str = DateUtils.formatDateTime(MyOrder.this, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshType = 3;
                if (refreshView.isShownHeader()) {
                    refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后更新时间:" + str);
                    page3=0;
                    new GetDataTask1().execute();
                }
                if (refreshView.isShownFooter()) {
                    refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后加载时间:" + str);
                    page3++;
                    new GetDataTask2().execute();
                }
            }
        });

        myorder_list4.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String str = DateUtils.formatDateTime(MyOrder.this, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshType = 4;
                if (refreshView.isShownHeader()) {
                    refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后更新时间:" + str);
                    page4=0;
                    new GetDataTask1().execute();
                }
                if (refreshView.isShownFooter()) {
                    refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后加载时间:" + str);
                    page4++;
                    new GetDataTask2().execute();
                }
            }
        });

    }

    private class GetDataTask1 extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(1500);
                switch (refreshType ){
                    case 1:
                        list1.clear();
                        Log.e("123","clean");
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
                    case 4:
                        list4.clear();
                        showPhotoPurchase();
                        break;
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
            switch (refreshType ){
                case 1:
                    myorder_list1.onRefreshComplete();
                    break;
                case 2:
                    myorder_list2.onRefreshComplete();
                    break;
                case 3:
                    myorder_list3.onRefreshComplete();
                    break;
                case 4:
                    myorder_list4.onRefreshComplete();
                    break;
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
                switch (refreshType ){
                    case 1:
                        page1++;
                        showWritePurchase();
                        break;
                    case 2:
                        showJiaGong();
                        break;
                    case 3:
                        showLogistics();
                        break;
                    case 4:
                        showPhotoPurchase();
                        break;
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
            switch (refreshType ){
                case 1:
                    myorder_list1.onRefreshComplete();
                    break;
                case 2:
                    myorder_list2.onRefreshComplete();
                    break;
                case 3:
                    myorder_list3.onRefreshComplete();
                    break;
                case 4:
                    myorder_list4.onRefreshComplete();
                    break;
            }
            super.onPostExecute(result);
        }

    }

}
