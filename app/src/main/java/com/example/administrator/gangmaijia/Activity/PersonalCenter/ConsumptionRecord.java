package com.example.administrator.gangmaijia.Activity.PersonalCenter;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;

import com.example.administrator.gangmaijia.Adapter.ConsumptionAdapter;
import com.example.administrator.gangmaijia.Adapter.ConsumptionAdapter1;
import com.example.administrator.gangmaijia.DAO.UserDAO;
import com.example.administrator.gangmaijia.Model.Consumption;
import com.example.administrator.gangmaijia.Model.User;
import com.example.administrator.gangmaijia.MyApplication;
import com.example.administrator.gangmaijia.R;
import com.example.administrator.gangmaijia.Util.HttpUtil;
import com.example.administrator.gangmaijia.Util.Internet;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by Administrator on 2016/10/9.
 */

public class ConsumptionRecord extends Activity {

    private HttpUtil httpUtil = new HttpUtil();
    private UserDAO userDAO = new UserDAO(this);
    private User user ;
    private int page = 0;
    private List<Consumption> list = new ArrayList<>();
    private ConsumptionAdapter1 adapter ;
    private StickyListHeadersListView lv_consumption ;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    getListResult(msg);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mingxichaxun);
        MyApplication.getInstance().addActivity(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        initview();
        user = userDAO.getUserInfo();
        getList();

    }

    private void initview(){
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lv_consumption = (StickyListHeadersListView) findViewById(R.id.lv_consumption);
        adapter = new ConsumptionAdapter1(this,list);
        lv_consumption.setAdapter(adapter);

    }

    private void getList(){
        String url = Internet.SELECT_DETAILS_URL+"?phone_num="+user.getUserPhone()+"&page="+page ;
        httpUtil.get(mHandler, url,1);
    }

    private void getListResult(Message msg){
        try {
            String s = (String)msg.obj;
            JSONObject jsonObject = new JSONObject(s);
            try {
                JSONObject data = jsonObject.getJSONObject("data");
                for (int i = 0;i<data.length();i++){
                    Consumption c = new Consumption();
                    JSONObject j = data.getJSONObject("new" + (i));
                    String d_week = j.getString("d_week");
                    String phone_num = j.getString("phone_num");
                    String page = j.getString("page");
                    String order_num = j.getString("order_num");
                    String d_monthday = j.getString("d_monthday");
                    String d_month = j.getString("d_month");
                    String d_year = j.getString("d_year");
                    String type = j.getString("type");
                    String d_amount = j.getString("d_amount");

                    c.setD_amount(d_amount);
                    c.setD_month(Integer.parseInt(d_month));
                    c.setD_monthday(d_monthday);
                    c.setD_week(d_week);
//                    c.setD_year(Integer.parseInt(d_year));
                    c.setTime(d_year);
                    c.setOrder_num(order_num);
                    c.setPage(page);
                    c.setPhone_num(phone_num);
                    c.setType(Integer.parseInt(type));

                    list.add(c);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
    }

//    private void

}
