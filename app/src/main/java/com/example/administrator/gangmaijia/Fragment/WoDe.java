package com.example.administrator.gangmaijia.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.gangmaijia.Activity.PersonalCenter.AllOrder;
import com.example.administrator.gangmaijia.Activity.PersonalCenter.CompleteOrder;
import com.example.administrator.gangmaijia.Activity.PersonalCenter.ConsumptionRecord;
import com.example.administrator.gangmaijia.Activity.PersonalCenter.MyOrder;
import com.example.administrator.gangmaijia.Activity.PersonalCenter.Recharge;
import com.example.administrator.gangmaijia.DAO.UserDAO;
import com.example.administrator.gangmaijia.Model.User;
import com.example.administrator.gangmaijia.R;
import com.example.administrator.gangmaijia.Util.HttpUtil;
import com.example.administrator.gangmaijia.Util.Internet;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/28.
 */
public class WoDe extends Fragment implements View.OnClickListener {

    private View view;
    private RelativeLayout iv_wode_all, iv_wode_chongzhi, iv_wode_complete, iv_wode_payrode, iv_wode_myorder;
    private TextView tv_wode_balance;
    private Intent it;
    private HttpUtil httpUtil = new HttpUtil();
    private UserDAO userDAO = new UserDAO(getActivity());
    private User user;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    try {
                        String s = (String) msg.obj;
                        JSONObject jsonObject = new JSONObject(s);
                        tv_wode_balance.setText(jsonObject.getJSONObject("data").getString("balance") + "￥");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.wode, container, false);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        initview();
        user = userDAO.getUserInfo();
        getBalance();
        return view;
    }

    private void initview() {
        iv_wode_all = (RelativeLayout) view.findViewById(R.id.iv_wode_all);
        iv_wode_chongzhi = (RelativeLayout) view.findViewById(R.id.iv_wode_chongzhi);
        iv_wode_complete = (RelativeLayout) view.findViewById(R.id.iv_wode_complete);
        iv_wode_payrode = (RelativeLayout) view.findViewById(R.id.iv_wode_payrode);
        iv_wode_myorder = (RelativeLayout) view.findViewById(R.id.iv_wode_myorder);
        tv_wode_balance = (TextView) view.findViewById(R.id.tv_wode_balance);

        iv_wode_all.setOnClickListener(this);
        iv_wode_chongzhi.setOnClickListener(this);
        iv_wode_complete.setOnClickListener(this);
        iv_wode_payrode.setOnClickListener(this);
        iv_wode_myorder.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_wode_all:
                Intent intent = new Intent(getActivity(), AllOrder.class);
                Bundle bundle2 = new Bundle();
                bundle2.putString("type", user.getUserId());
                intent.putExtras(bundle2);
                startActivityForResult(intent, 2);
                break;
            case R.id.iv_wode_chongzhi:
                it = new Intent(getActivity(), Recharge.class);
                startActivityForResult(it, 1);
                break;
            case R.id.iv_wode_complete:
                startActivity(new Intent(getActivity(), CompleteOrder.class));
                break;
            case R.id.iv_wode_payrode:
                startActivity(new Intent(getActivity(), ConsumptionRecord.class));
                break;
            case R.id.iv_wode_myorder:
                startActivity(new Intent(getActivity(), MyOrder.class));
                break;
        }
    }

    private void getBalance() {
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("phone_num", user.getUserPhone()));
        httpUtil.post(mHandler, Internet.SELECT_BALANCE_URL, 1, nvps);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 2) {
            getBalance();
        }

    }
}
