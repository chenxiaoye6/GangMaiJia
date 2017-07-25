package com.example.administrator.gangmaijia.Fragment;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Created by Administrator on 2016/9/28.
 */
public class DingDan extends Fragment implements View.OnClickListener {

    private View view;
    private int refreshType = 1;
    private PullToRefreshListView mydingdan_list1, mydingdan_list2, mydingdan_list3, mydingdan_list4;
    private TextView tv_dingdan_line1, tv_dingdan_line2, tv_dingdan_line3, tv_dingdan_text1, tv_dingdan_text2, tv_dingdan_text3;
    private ImageView iv_dingdan_zhaohuo;
    private RelativeLayout ry_dingdan_type1, ry_dingdan_type2, ry_dingdan_type3;
    private ExpandView_zhaohuo expandView;
    private TextView expand_line1, expand_line2;
    private int type = 1;
    private int page1 = 0;
    private int page2 = 0;
    private int page3 = 0;
    private int page4 = 0;
    private HttpUtil httpUtil = new HttpUtil();
    private UserDAO userDAO = new UserDAO(getActivity());
    private User user;
    private List<PurchaseWrite> list1 = new ArrayList<>();
    private List<JiaGongOrder> list2 = new ArrayList<>();
    private List<Logistic> list3 = new ArrayList<>();
    private List<PurchasePhoto> list4 = new ArrayList<>();
    private PurchaseWriteAdapter adapter1;
    private JiaGongAdapter adapter2;
    private LogisticAdapter adapter3;
    private PurchasePhotoAdapter adapter4;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e("meswhat", "meswhat=" + msg.what);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dingdan, container, false);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        initview();
        Log.e("initview", "initview");
        setAdapter();
        user = new User();
        user = userDAO.getUserInfo();
        showWritePurchase();
        showLogistics();
        showPhotoPurchase();
        showJiaGong();
        //设置下拉刷新界面
        refreshList();
        return view;
    }

    private void initview() {
        tv_dingdan_line1 = (TextView) view.findViewById(R.id.tv_dingdan_line1);
        tv_dingdan_line2 = (TextView) view.findViewById(R.id.tv_dingdan_line2);
        tv_dingdan_line3 = (TextView) view.findViewById(R.id.tv_dingdan_line3);
        tv_dingdan_text1 = (TextView) view.findViewById(R.id.tv_dingdan_text1);
        tv_dingdan_text2 = (TextView) view.findViewById(R.id.tv_dingdan_text2);
        tv_dingdan_text3 = (TextView) view.findViewById(R.id.tv_dingdan_text3);
        ry_dingdan_type1 = (RelativeLayout) view.findViewById(R.id.ry_dingdan_type1);
        ry_dingdan_type2 = (RelativeLayout) view.findViewById(R.id.ry_dingdan_type2);
        ry_dingdan_type3 = (RelativeLayout) view.findViewById(R.id.ry_dingdan_type3);

        mydingdan_list1 = (PullToRefreshListView) view.findViewById(R.id.mydingdan_list1);
        mydingdan_list2 = (PullToRefreshListView) view.findViewById(R.id.mydingdan_list2);
        mydingdan_list3 = (PullToRefreshListView) view.findViewById(R.id.mydingdan_list3);
        mydingdan_list4 = (PullToRefreshListView) view.findViewById(R.id.mydingdan_list4);
        iv_dingdan_zhaohuo = (ImageView) view.findViewById(R.id.iv_dingdan_zhaohuo);
        expandView = (ExpandView_zhaohuo) view.findViewById(R.id.expandView_zhaohuo);
        expand_line1 = (TextView) view.findViewById(R.id.expand_zhaohuo_line1);
        expand_line2 = (TextView) view.findViewById(R.id.expand_zhaohuo_line2);
        ry_dingdan_type1.setOnClickListener(this);
        ry_dingdan_type2.setOnClickListener(this);
        ry_dingdan_type3.setOnClickListener(this);
        iv_dingdan_zhaohuo.setOnClickListener(this);
        expand(expand_line1, expand_line2, tv_dingdan_text1, expandView);
    }

    private void setAdapter() {
        adapter1 = new PurchaseWriteAdapter(getActivity(), list1);
        mydingdan_list1.setAdapter(adapter1);
        adapter2 = new JiaGongAdapter(getActivity(), list2);
        mydingdan_list2.setAdapter(adapter2);
        adapter3 = new LogisticAdapter(getActivity(), list3);
        mydingdan_list3.setAdapter(adapter3);
        adapter4 = new PurchasePhotoAdapter(getActivity(), list4);
        mydingdan_list4.setAdapter(adapter4);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ry_dingdan_type1:
                tv_dingdan_line1.setBackgroundResource(R.color.text_color);
                tv_dingdan_line2.setBackgroundResource(R.color.transform);
                tv_dingdan_line3.setBackgroundResource(R.color.transform);
                tv_dingdan_text1.setTextColor(getResources().getColor(R.color.text_color));
                tv_dingdan_text2.setTextColor(getResources().getColor(R.color.gray));
                tv_dingdan_text3.setTextColor(getResources().getColor(R.color.gray));
                mydingdan_list2.setVisibility(View.GONE);
                mydingdan_list3.setVisibility(View.GONE);
                if (tv_dingdan_text1.getText().equals("写的采购单")) {
                    mydingdan_list1.setVisibility(View.VISIBLE);
                    mydingdan_list4.setVisibility(View.GONE);
                } else {
                    mydingdan_list1.setVisibility(View.GONE);
                    mydingdan_list4.setVisibility(View.VISIBLE);
                }
                iv_dingdan_zhaohuo.setEnabled(true);
                type = 1;
                break;
            case R.id.ry_dingdan_type2:

                tv_dingdan_line1.setBackgroundResource(R.color.transform);
                tv_dingdan_line2.setBackgroundResource(R.color.text_color);
                tv_dingdan_line3.setBackgroundResource(R.color.transform);
                tv_dingdan_text1.setTextColor(getResources().getColor(R.color.gray));
                tv_dingdan_text2.setTextColor(getResources().getColor(R.color.text_color));
                tv_dingdan_text3.setTextColor(getResources().getColor(R.color.gray));
                mydingdan_list1.setVisibility(View.GONE);
                mydingdan_list2.setVisibility(View.VISIBLE);
                mydingdan_list3.setVisibility(View.GONE);
                mydingdan_list4.setVisibility(View.GONE);
                iv_dingdan_zhaohuo.setEnabled(false);
                type = 2;
                break;
            case R.id.ry_dingdan_type3:

                tv_dingdan_line1.setBackgroundResource(R.color.transform);
                tv_dingdan_line2.setBackgroundResource(R.color.transform);
                tv_dingdan_line3.setBackgroundResource(R.color.text_color);
                tv_dingdan_text1.setTextColor(getResources().getColor(R.color.gray));
                tv_dingdan_text2.setTextColor(getResources().getColor(R.color.gray));
                tv_dingdan_text3.setTextColor(getResources().getColor(R.color.text_color));
                mydingdan_list1.setVisibility(View.GONE);
                mydingdan_list2.setVisibility(View.GONE);
                mydingdan_list3.setVisibility(View.VISIBLE);
                mydingdan_list4.setVisibility(View.GONE);
                iv_dingdan_zhaohuo.setEnabled(false);
                type = 3;
                break;
            case R.id.iv_dingdan_zhaohuo:
                if (expandView.isExpand()) {
                    expandView.collapse();
                } else {
                    expandView.expand();
                }
                break;
        }
    }

    private void expand(TextView tv1, TextView tv2, final TextView tv, final ExpandView_zhaohuo mExpandView) {
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setText("写的采购单");
                mExpandView.collapse();
                mydingdan_list1.setVisibility(View.VISIBLE);
                mydingdan_list4.setVisibility(View.GONE);
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setText("拍的采购单");
                mExpandView.collapse();
                mydingdan_list1.setVisibility(View.GONE);
                mydingdan_list4.setVisibility(View.VISIBLE);

            }
        });

    }

    private void showWritePurchase() {
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("phone_num", user.getUserPhone()));
        Log.e("我的phone=", user.getUserPhone());
        httpUtil.post(mHandler, Internet.SHOW_PURCHASE_URL, 1, nvps);
    }

    private void showWritePurchaseResult(Message msg) {
        String s = (String) msg.obj;
        Log.e("789", "写单=========" + s);
        try {
            JSONObject jsonObject = new JSONObject(s);
            Log.e("wode ", "我的采购单=" + jsonObject.toString());
            JSONObject data = jsonObject.getJSONObject("data");
            for (int i = 1; i < data.length() + 1; i++) {
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
                list1.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapter1.notifyDataSetChanged();
    }

    private void showPhotoPurchase() {
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("phone_num", user.getUserPhone()));
        httpUtil.post(mHandler, Internet.SHOW_PHOTO_URL, 4, nvps);
    }

    private void showPhotoPurchaseResult(Message msg) {
        String s = (String) msg.obj;
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONObject data = jsonObject.getJSONObject("data");
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapter4.notifyDataSetChanged();
    }

    private void showJiaGong() {
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("phone_num", user.getUserPhone()));
        httpUtil.post(mHandler, Internet.SHOW_PROCESS_URL, 2, nvps);
    }

    private void showJiaGongResult(Message msg) {
        String s = (String) msg.obj;
        Log.e("我的加工单=", s);
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONObject data = jsonObject.getJSONObject("data");
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
                list2.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapter2.notifyDataSetChanged();
    }


    private void showLogistics() {
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("phone_num", user.getUserPhone()));
        Log.e("789", "nvps" + nvps.toString());
        httpUtil.post(mHandler, Internet.SHOW_LGOISTIC_URL, 3, nvps);
    }

    private void showLogisticsResult(Message msg) {
        String s = (String) msg.obj;
        Log.e("我的物流单=", s);
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONObject data = jsonObject.getJSONObject("data");
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
                list3.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapter3.notifyDataSetChanged();
    }

    private void refreshList() {
        mydingdan_list1.setMode(PullToRefreshBase.Mode.BOTH);
        mydingdan_list2.setMode(PullToRefreshBase.Mode.BOTH);
        mydingdan_list3.setMode(PullToRefreshBase.Mode.BOTH);
        mydingdan_list4.setMode(PullToRefreshBase.Mode.BOTH);
        mydingdan_list2.setVisibility(View.INVISIBLE);
        mydingdan_list3.setVisibility(View.INVISIBLE);
        mydingdan_list4.setVisibility(View.INVISIBLE);

        mydingdan_list1.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String str = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshType = 1;
                if (refreshView.isShownHeader()) {
                    refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后更新时间:" + str);
                    page1 = 0;
                    new GetDataTask1().execute();
                }
                if (refreshView.isShownFooter()) {
                    refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后加载时间:" + str);

                    new GetDataTask2().execute();
                }
            }
        });

        mydingdan_list2.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String str = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
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

        mydingdan_list3.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String str = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
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

        mydingdan_list4.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String str = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshType = 4;
                if (refreshView.isShownHeader()) {
                    refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后更新时间:" + str);
                    page4 = 0;
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
                switch (refreshType) {
                    case 1:
                        list1.clear();
                        Log.e("GetDataTask1", "list" + list1.toString());
                        showWritePurchase();
                        break;
                    case 2:
                        list2.clear();
                        Log.e("GetDataTask1", "GetDataTask1" + list1.toString());
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
            switch (refreshType) {
                case 1:
                    mydingdan_list1.onRefreshComplete();
                    break;
                case 2:
                    mydingdan_list2.onRefreshComplete();
                    break;
                case 3:
                    mydingdan_list3.onRefreshComplete();
                    break;
                case 4:
                    mydingdan_list4.onRefreshComplete();
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
                switch (refreshType) {
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
            switch (refreshType) {
                case 1:
                    mydingdan_list1.onRefreshComplete();
                    break;
                case 2:
                    mydingdan_list2.onRefreshComplete();
                    break;
                case 3:
                    mydingdan_list3.onRefreshComplete();
                    break;
                case 4:
                    mydingdan_list4.onRefreshComplete();
                    break;
            }
            super.onPostExecute(result);
        }
    }
}
