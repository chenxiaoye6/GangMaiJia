package com.example.administrator.gangmaijia.Activity.UserInfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.gangmaijia.R;
import com.example.administrator.gangmaijia.Util.Internet;
import com.example.administrator.gangmaijia.Util.OkHttpUtils;
import com.squareup.okhttp.FormEncodingBuilder;

/**
 * Created by Administrator on 2016/11/10.
 */

public class RegisterType extends Activity implements View.OnClickListener {

    private TextView tv_registertype_next;
    private LinearLayout ly_register_back;
    private RadioButton rb_registertype_person, rb_registertype_caigou, rb_registertype_jiagong, rb_registertype_wuliu;
    private String phone_num;
    private int type = 2;
    private Intent it;
    Bundle bundle2 = new Bundle();
    private String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_type);
        Bundle bundle = getIntent().getExtras();
        phone_num = bundle.getString("phone_num");
        Log.e("phone_num", phone_num);
        initView();
    }

    private void initView() {
        tv_registertype_next = (TextView) findViewById(R.id.tv_registertype_next);
        rb_registertype_person = (RadioButton) findViewById(R.id.rb_registertype_person);
        rb_registertype_caigou = (RadioButton) findViewById(R.id.rb_registertype_caigou);
        rb_registertype_jiagong = (RadioButton) findViewById(R.id.rb_registertype_jiagong);
        rb_registertype_wuliu = (RadioButton) findViewById(R.id.rb_registertype_wuliu);
        ly_register_back = (LinearLayout) findViewById(R.id.ly_register_back);
        //设置监听
        tv_registertype_next.setOnClickListener(this);
        ly_register_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        //bundle中放入号码
        bundle2.putString("phone_num", phone_num);
        switch (view.getId()) {
            case R.id.tv_registertype_next:
                if (rb_registertype_person.isChecked()) {
                    Log.e("phone_num", phone_num);
                    //个人注册
                    registerPerson();
                }
                if (rb_registertype_caigou.isChecked()) {
                    it = new Intent(this, ChangJiaRegister.class);
                    it.putExtras(bundle2);
                    startActivity(it);
                }
                if (rb_registertype_jiagong.isChecked()) {
                    it = new Intent(this, JiaGongRegister.class);
                    it.putExtras(bundle2);
                    startActivity(it);
                }
                if (rb_registertype_wuliu.isChecked()) {
                    it = new Intent(this, WuLiuRegister.class);
                    it.putExtras(bundle2);
                    startActivity(it);
                } else {
                    Toast.makeText(this, "请选择类型", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ly_register_back:
                finish();
                break;
            default:
                break;

        }
    }

    private void registerPerson() {
        url = Internet.COMPANY_REGISTER_URL;
        final FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("phone_num", phone_num);
        builder.add("type", type + "");
        builder.add("company_name", "");
        builder.add("user_name", "");
        builder.add("major_business", "");
        builder.add("company_type", "");
        builder.add("city_address", "");
        builder.add("business_license", "");
        builder.add("type_num", "");
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String s = OkHttpUtils.postKeyValue(mHandler, url, builder);
                            Log.e("s", s);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).start();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    //注册个人请求成功
                    Toast.makeText(RegisterType.this, "注册成功,请登录", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterType.this, Login.class));
                    break;
                //请求失败
                case -1:
                    Toast.makeText(RegisterType.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    //一键注销
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            RegisterType.this.setResult(1, getIntent());
            RegisterType.this.finish();
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
