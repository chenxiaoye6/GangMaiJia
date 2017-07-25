package com.example.administrator.gangmaijia.Activity.Setting;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.gangmaijia.Activity.NewsRecord;
import com.example.administrator.gangmaijia.DAO.UserDAO;
import com.example.administrator.gangmaijia.Model.User;
import com.example.administrator.gangmaijia.MyApplication;
import com.example.administrator.gangmaijia.R;
import com.example.administrator.gangmaijia.Util.HttpUtil;
import com.example.administrator.gangmaijia.Util.Internet;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/8.
 */

public class setting extends Activity implements View.OnClickListener {

    private ImageView iv_setting_lingsheng, back;
    private TextView tv_setting_quit;
    private LinearLayout ly_setting_bell, iv_setting_changephone, ly_checkupdate, ll_setting_tuisong;
    ;
    private Intent it;
    private User userInfo;
    UserDAO userDAO = new UserDAO(this);
    User user = userDAO.getUserInfo();

    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e("msg", "结果是" + msg.toString());
            switch (msg.what) {
                case 1:
                    Object obj = msg.obj;
                    Log.e("result", "结果是" + obj);
                    try {
                        JSONObject jsonObject1 = new JSONObject(obj.toString());
                        JSONObject jsonObject2 = jsonObject1.getJSONObject("data");
                        Log.e("123", "obj=++++" + obj.toString());
                        //版本的编号,跟后台对比
                        String i = "12";
                        //设置更新
                        if (i.equals(jsonObject2.getString("system_version"))) {
                            Toast.makeText(setting.this, "当前版本是最新版本", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(setting.this, "请下载最新版本", Toast.LENGTH_SHORT).show();
                            Uri uri = Uri.parse("http://m.gangmaijiaw.com/demo/demo.html");
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            //建立Intent对象，传入uri
                            startActivity(intent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    Log.e("result", "结果是" + msg.obj);
                    user.setIsLogin("0");
                    userDAO.updateUser(user);
                    MyApplication.getInstance().exit();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mysetting);
        MyApplication.getInstance().addActivity(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        userInfo = new UserDAO(this).getUserInfo();
        initview();
        SharedPreferences sharedPreferences = getSharedPreferences("settings", 0);
        Log.e("456", sharedPreferences.getString("bellPath", "1"));


    }

    private void initview() {
        ll_setting_tuisong = (LinearLayout) findViewById(R.id.ll_setting_tuisong);
        ly_checkupdate = (LinearLayout) findViewById(R.id.ly_checkupdate);
        iv_setting_changephone = (LinearLayout) findViewById(R.id.iv_setting_changephone);
        iv_setting_lingsheng = (ImageView) findViewById(R.id.iv_setting_lingsheng);
        back = (ImageView) findViewById(R.id.back);
        tv_setting_quit = (TextView) findViewById(R.id.tv_setting_quit);
        ly_setting_bell = (LinearLayout) findViewById(R.id.ly_setting_bell);

        if (userInfo.getUserType().equals("2")) {
            ly_setting_bell.setVisibility(View.GONE);
        }
        ll_setting_tuisong.setOnClickListener(this);
        tv_setting_quit.setOnClickListener(this);
        iv_setting_changephone.setOnClickListener(this);
        iv_setting_lingsheng.setOnClickListener(this);
        ly_setting_bell.setOnClickListener(this);
        ly_checkupdate.setOnClickListener(this);
        back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.iv_setting_changephone:
                startActivity(new Intent(this, ChangePhone.class));
                break;
            case R.id.ly_setting_bell:
                startActivity(new Intent(this, SetBell.class));
                break;
            case R.id.tv_setting_quit:
                quit();
                break;
            //检查更新
            case R.id.ly_checkupdate:
                Log.e("更新", "点击了");
                List<NameValuePair> nvps = new ArrayList<>();
                new HttpUtil().post(mhandler, Internet.SELECTVERSION, 1, nvps);
                break;
            case R.id.ll_setting_tuisong:
                startActivity(new Intent(this, NewsRecord.class));
                break;
        }
    }

    private void quit() {
        List<NameValuePair> nvps2 = new ArrayList<>();
        String phone_num = user.getUserPhone();
        nvps2.add(new BasicNameValuePair("phone_num", phone_num));
        //退出注销
        new HttpUtil().post(mhandler, Internet.UPDATECID, 2, nvps2);
        user.setIsLogin("0");
        userDAO.updateUser(user);
        MyApplication.getInstance().exit();
    }


}
