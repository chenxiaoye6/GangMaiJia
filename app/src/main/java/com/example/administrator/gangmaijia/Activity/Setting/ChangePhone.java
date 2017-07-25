package com.example.administrator.gangmaijia.Activity.Setting;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/10/9.
 */

public class ChangePhone extends Activity implements View.OnClickListener{

    private EditText et_changephone_phone,et_changephone_code ;
    private TextView tv_changephone_sendcode,tv_changephone_complete ;
    private ImageView back ;
    private String phone = "",code = "" ;
//    private int type = 1 ;
    private HttpUtil httpUtil ;
    private TimerTask task ;
    private Timer timer ;
    private int time = 60;
    private UserDAO userDAO = new UserDAO(this);
    private User user ;


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    sendCodeResult(msg);
                    break;
                case 2:
                    changePhoneResult(msg);
                    break;
                case 4:
                    if (time==0){
                        tv_changephone_sendcode.setText("发送验证码");
                        tv_changephone_sendcode.setEnabled(true);
                        task.cancel();
                        time = 60;
                    }else {
                        tv_changephone_sendcode.setText(time+"s后重发");
                    }
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changephone);
        MyApplication.getInstance().addActivity(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        initview();
        httpUtil = new HttpUtil();
        user = userDAO.getUserInfo();

    }

    private void initview(){
        et_changephone_phone = (EditText) findViewById(R.id.et_changephone_phone);
        et_changephone_code = (EditText) findViewById(R.id.et_changephone_code);
        tv_changephone_sendcode = (TextView) findViewById(R.id.tv_changephone_sendcode);
        tv_changephone_complete = (TextView) findViewById(R.id.tv_changephone_complete);
        back = (ImageView) findViewById(R.id.back);

        back.setOnClickListener(this);
        tv_changephone_sendcode.setOnClickListener(this);
        tv_changephone_complete.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.tv_changephone_sendcode:
                sendCode();
                break;
            case R.id.tv_changephone_complete:
                changePhone();
                break;
        }
    }

    private void sendCode() {
        phone = et_changephone_phone.getText().toString();
        if (phone==null|phone.equals("")){
            Toast.makeText(this,"请输入手机号码",Toast.LENGTH_SHORT).show();
        }else {
            tv_changephone_sendcode.setEnabled(false);
            task = new TimerTask(){
                public void run() {
                    time--;
                    Message message = new Message();
                    message.what = 4;
                    mHandler.sendMessage(message);
                }
            };
            timer = new Timer(true);
            timer.schedule(task,1000, 1000);
            String url = Internet.CHANGE_PHONE_CODE_SEND;
//            JSONObject jsonParameter = new JSONObject();
//            try {
//                jsonParameter.put("phone_num",user.getUserPhone());
//                jsonParameter.put("phone_num1",phone);
//                jsonParameter.put("type",user.getUserType());
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
            List<NameValuePair> nvps = new ArrayList<>();
//            nvps.add(new BasicNameValuePair("jsonParameter",jsonParameter.toString()));
            nvps.add(new BasicNameValuePair("phone_num",user.getUserPhone()));
            nvps.add(new BasicNameValuePair("phone_num1",phone));
            httpUtil.post(mHandler,url,1,nvps);
        }
    }

    private void sendCodeResult(Message msg){
        String str = (String) msg.obj;
        try {
            JSONObject jsonObject = new JSONObject(str);
            String codeMessage = jsonObject.getString("msg");
            code = jsonObject.getString("data");

            if (codeMessage.contains("成功")) {
                tv_changephone_sendcode.setEnabled(false);
            }
            Toast.makeText(ChangePhone.this, codeMessage, Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void changePhone(){
        phone = et_changephone_phone.getText().toString();
        code = et_changephone_code.getText().toString();
        if (phone==null|phone.equals("")){
            Toast.makeText(this,"请输入手机号码",Toast.LENGTH_SHORT).show();
            return;
        }
        if (code==null|code.equals("")){
            Toast.makeText(this,"请输入验证码",Toast.LENGTH_SHORT).show();
            return;
        }
//        JSONObject jsonParameter = new JSONObject();
//        try {
//            jsonParameter.put("phone_num",user.getUserPhone());
//            jsonParameter.put("phone_num1",phone);
//            jsonParameter.put("type",user.getUserType());
//            jsonParameter.put("validat_num",code);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        List<NameValuePair> nvps = new ArrayList<>();
//        nvps.add(new BasicNameValuePair("jsonParameter",jsonParameter.toString()));
        nvps.add(new BasicNameValuePair("phone_num",user.getUserPhone()));
        nvps.add(new BasicNameValuePair("phone_num1",phone));
        nvps.add(new BasicNameValuePair("validat_num",code));
        String url = Internet.CHANGE_PHONE_URL+"?phone_num="+user.getUserPhone()+
                "&phone_num1="+phone+"&validat_num="+code;
        httpUtil.get(mHandler,url,2);
//        httpUtil.post(mHandler,Internet.CHANGE_PHONE_URL,2,nvps);
    }

    private void changePhoneResult(Message msg){
        String str = (String) msg.obj;
        try {
            JSONObject jsonObject = new JSONObject(str);
            String codeMessage = jsonObject.getString("msg");
            if (jsonObject.getString("result").equals("0")){
                Toast.makeText(ChangePhone.this, "修改成功", Toast.LENGTH_SHORT).show();
                user.setUserPhone(phone);
                userDAO.updateUser(user);
            }else
            Toast.makeText(ChangePhone.this, codeMessage, Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
