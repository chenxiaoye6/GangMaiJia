package com.example.administrator.gangmaijia.Activity.UserInfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.gangmaijia.DAO.UserDAO;
import com.example.administrator.gangmaijia.MainActivity;
import com.example.administrator.gangmaijia.Model.User;
import com.example.administrator.gangmaijia.MyApplication;
import com.example.administrator.gangmaijia.R;
import com.example.administrator.gangmaijia.Util.Internet;
import com.example.administrator.gangmaijia.Util.OkHttpUtils;
import com.igexin.sdk.PushManager;
import com.squareup.okhttp.FormEncodingBuilder;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/11/9.
 */

public class Login extends Activity implements View.OnClickListener {
    private TextView tv_login_denglu, tv_login_zhuce;
    private EditText et_login_phonenum, et_login_pwd;
    private String phone_num, balance;
    private String password;
    private String cid;
    private UserDAO userDAO = new UserDAO(this);
    User user = new User();

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Log.e("789", msg + "");
                    Toast.makeText(Login.this, msg.obj + "", Toast.LENGTH_SHORT).show();
                    //返回值为0时登录
                    startActivity(new Intent(Login.this, MainActivity.class));
                    break;
                case -1:
                    Toast.makeText(Login.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    tv_login_denglu.setText("登录");
                    break;
            }
        }
    };

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        MyApplication.getInstance().addActivity(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        initview();
    }

    private void initview() {
        tv_login_denglu = (TextView) findViewById(R.id.tv_login_denglu);
        tv_login_zhuce = (TextView) findViewById(R.id.tv_login_zhuce);
        et_login_phonenum = (EditText) findViewById(R.id.et_login_phonenum);
        et_login_pwd = (EditText) findViewById(R.id.et_login_pwd);
        tv_login_denglu.setOnClickListener(this);
        tv_login_zhuce.setOnClickListener(this);
        et_login_phonenum.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });
        et_login_pwd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //登录
            case R.id.tv_login_denglu:
                login();
                break;
            case R.id.tv_login_zhuce:
                //跳转到注册界面
                startActivity(new Intent(Login.this, Register.class));
        }
    }

    private void login() {
        phone_num = et_login_phonenum.getText().toString();
        password = et_login_pwd.getText().toString();


        if (phone_num == null || phone_num.equals("")) {
            Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password == null | password.equals("")) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        tv_login_denglu.setText("登录中...");
        final String url = Internet.LOGIN_URL;
        cid = PushManager.getInstance().getClientid(this);
//        List<NameValuePair> nvps = new ArrayList<>();
//        nvps.add(new BasicNameValuePair("phone_num",phone_num));
//        HttpUtil httpUtil=new HttpUtil();
//        httpUtil.post(mHandler,Internet.BASE_URL+"select_balance.action",4,nvps);
        //创建数据体
        final FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("phone_num", phone_num);
        builder.add("password", password);
        Log.e("cid", "cid==============" + cid + "");
        builder.add("CID", cid + "");
        builder.add("phone_type", "1");
        Log.e("789", "phone_num=" + phone_num + "     " + "password=" + password + "     " + "cid=" + cid + "     ");
        //添加到本地数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //请求网络得到数据
                    String s = OkHttpUtils.postKeyValue(mHandler, url, builder);
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    Log.e("789", "s========" + s);
                    String type = jsonObject1.getString("type");
                    user.setUserId(jsonObject1.getString("type_num"));
                    user.setIsLogin("1");
                    user.setUserPhone(phone_num);
                    user.setUserType(type + "");
                    user.setBalance(balance);
                    userDAO.addUser(user);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Login.this.setResult(1, getIntent());
            Login.this.finish();
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
