package com.example.administrator.gangmaijia.Activity.UserInfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.gangmaijia.MyApplication;
import com.example.administrator.gangmaijia.R;
import com.example.administrator.gangmaijia.Util.Internet;
import com.example.administrator.gangmaijia.Util.OkHttpUtils;
import com.igexin.sdk.PushManager;
import com.squareup.okhttp.FormEncodingBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/11/9.
 */

public class Register extends Activity implements View.OnClickListener {
    private EditText et_register_phone, et_register_code, et_register_pwd, et_register_pwdconfig;
    private TextView tv_register_code, tv_register_tiaokuang, tv_register_yiyou, tv_register_next;
    private CheckBox cb_register;
    private String cid, phone_num;
    private String url, url2;
    private String s, ss;
    private String code, password, validat_num;
    private TimerTask task;
    private int time = 60;
    private Timer timer;
    private String pwdconfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        MyApplication.getInstance().addActivity(this);
        initview();
        cid = PushManager.getInstance().getClientid(Register.this);
    }

    private void initview() {
        et_register_phone = (EditText) findViewById(R.id.et_register_phone);
        et_register_code = (EditText) findViewById(R.id.et_register_code);
        et_register_pwd = (EditText) findViewById(R.id.et_register_pwd);
        et_register_pwdconfig = (EditText) findViewById(R.id.et_register_pwdconfig);
        tv_register_code = (TextView) findViewById(R.id.tv_register_code);
        tv_register_tiaokuang = (TextView) findViewById(R.id.tv_register_tiaokuang);
        tv_register_yiyou = (TextView) findViewById(R.id.tv_register_yiyou);
        tv_register_next = (TextView) findViewById(R.id.tv_register_next);
        cb_register = (CheckBox) findViewById(R.id.cb_register);
        et_register_phone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });
        et_register_pwd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });
        et_register_code.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });
        et_register_pwdconfig.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });

        //设置监听,
        tv_register_code.setOnClickListener(this);
        tv_register_next.setOnClickListener(this);
        tv_register_yiyou.setOnClickListener(this);
        tv_register_tiaokuang.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_register_code:
                if (et_register_phone.getText().toString().length() == 11) {
                    sendCode();
                } else {
                    Toast.makeText(Register.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.tv_register_next:
                registerAcount();
                break;
            case R.id.tv_register_yiyou:
                startActivity(new Intent(Register.this, Login.class));
                break;
            case R.id.tv_register_tiaokuang:
                startActivity(new Intent(Register.this, ServerArticle.class));
                break;
        }

    }

    private void registerAcount() {
        phone_num = et_register_phone.getText().toString();
        validat_num = et_register_code.getText().toString();
        pwdconfig = et_register_pwdconfig.getText().toString();
        password = et_register_pwd.getText().toString();
        if (phone_num == null | phone_num.equals("")) {
            Toast.makeText(Register.this, "请输入号码", Toast.LENGTH_SHORT).show();
        }
        if (validat_num == null | "".equals(validat_num)) {
            Toast.makeText(Register.this, "请输入验证码", Toast.LENGTH_SHORT).show();
        }
        if (password == null | password.equals("")) {
            Toast.makeText(Register.this, "请输入密码", Toast.LENGTH_SHORT).show();
        }
        if (!(password.equals(pwdconfig))) {
            Toast.makeText(Register.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
        }
        if (!cb_register.isChecked()) {
            Toast.makeText(Register.this, "请同意服务条款", Toast.LENGTH_SHORT).show();
        }
        //将账号密码以及验证码post上去
        url2 = Internet.SIGNLOGIN;
        final FormEncodingBuilder builder2 = new FormEncodingBuilder();
        builder2.add("phone_num", phone_num);
        builder2.add("validat_num", validat_num);
        builder2.add("password", password);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //请求网络得到数据
                    ss = OkHttpUtils.postKeyValue(hHandler, url2, builder2);
                    Log.e("s", s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private Handler hHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    //注册请求成功
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    //传入电话号码
                    bundle.putString("phone_num", phone_num);
                    intent.setClass(Register.this, RegisterType.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                //请求失败
                case -1:
                    Toast.makeText(Register.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void sendCode() {
        phone_num = et_register_phone.getText().toString();
        if (phone_num == null | phone_num.equals("")) {
            Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
        } else {
            tv_register_code.setEnabled(false);
            url = Internet.CODE_SEND_URL;
            Log.e("123", "url=" + url + "=============");
            final FormEncodingBuilder builder = new FormEncodingBuilder();
            builder.add("phone_num", phone_num);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //请求网络得到数据
                        s = OkHttpUtils.postKeyValue(mHandler, url, builder);
                        Log.e("s", s);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    //请求成功
                    Toast.makeText(Register.this, msg.obj + "", Toast.LENGTH_SHORT).show();
                    //返回值为0时,执行操作
                    sendCodeResult(msg);
                    break;
                //请求失败
                case -1:
                    Toast.makeText(Register.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    //设置获取验证码能点击
                    tv_register_code.setEnabled(true);

                    break;
                case 4:
                    if (time == 0) {
                        tv_register_code.setText("获取验证码");
                        tv_register_code.setEnabled(true);
                        task.cancel();
                        time = 60;
                    } else {
                        tv_register_code.setText(time + "s后重发");
                    }
                    break;
            }
        }
    };

    //发送验证码成功的时候执行的操作
    private void sendCodeResult(Message msg) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            String codeMessage = jsonObject.getString("msg");
            Log.e("msg", codeMessage);
            //code = jsonObject.getString("data");
            if (codeMessage.contains("成功")) {
                tv_register_code.setEnabled(false);
                task = new TimerTask() {
                    public void run() {
                        time--;
                        Message message = new Message();
                        message.what = 4;
                        mHandler.sendMessage(message);
                    }
                };
                timer = new Timer(true);
                timer.schedule(task, 1000, 1000);
            } else {
                tv_register_code.setEnabled(true);
            }
            Toast.makeText(Register.this, codeMessage, Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Register.this.setResult(1, getIntent());
            Register.this.finish();
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
