package com.example.administrator.gangmaijia.Activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.example.administrator.gangmaijia.Activity.UserInfo.Login;
import com.example.administrator.gangmaijia.DAO.UserDAO;
import com.example.administrator.gangmaijia.MainActivity;
import com.example.administrator.gangmaijia.Model.User;
import com.example.administrator.gangmaijia.MyApplication;
import com.example.administrator.gangmaijia.R;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.PushService;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/12.
 */

public class WelcomeActivity extends Activity {


    private final long SPLASH_LENGTH = 1000;
    Handler handler = new Handler();
    private UserDAO userDao = new UserDAO(this);
    private Intent it;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        MyApplication.getInstance().addActivity(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        Intent intent = new Intent(WelcomeActivity.this, PushService.class);
        PendingIntent sender = PendingIntent.getService(this, 0, intent, 0);
        AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 5 * 1000, sender);
        PushManager.getInstance().initialize(this.getApplicationContext());
        PushManager.getInstance().turnOnPush(this.getApplicationContext());
        List<NameValuePair> nvps = new ArrayList<>();

        handler.postDelayed(new Runnable() {  //使用handler的postDelayed实现延时跳转

            public void run() {
                it = getIntent();
                User user = userDao.getUserInfo();
                try {
                    if (user != null) {
                        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                    } else {
                        System.out.println("-----------LoginActivity------------");
                        startActivityForResult(new Intent(WelcomeActivity.this, Login.class), 1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, SPLASH_LENGTH);//2秒后跳转至应用主界面MainActivity或登录界面Login

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            finish();
        }
    }
}
