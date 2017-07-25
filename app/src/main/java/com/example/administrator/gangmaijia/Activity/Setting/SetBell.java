package com.example.administrator.gangmaijia.Activity.Setting;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.gangmaijia.Adapter.BellAdapter;
import com.example.administrator.gangmaijia.DAO.UserDAO;
import com.example.administrator.gangmaijia.Model.Bell;
import com.example.administrator.gangmaijia.Model.User;
import com.example.administrator.gangmaijia.MyApplication;
import com.example.administrator.gangmaijia.R;
import com.example.administrator.gangmaijia.Util.HttpUtil;
import com.example.administrator.gangmaijia.Util.Internet;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/9.
 */

public class SetBell extends Activity {

    private ListView lv_setbell;
    private List<Bell> list = new ArrayList<>();
    private BellAdapter adapter;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private UserDAO userDao = new UserDAO(this);
    private HttpUtil httpUtil = new HttpUtil();
    private User user;
    String sound = null;
    private int id;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e("返回信息是=", msg.toString());
            switch (msg.what) {
                case 1:
                    String s = (String) msg.obj;
                    Log.e("s", "结果=" + s);
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        String msg1 = jsonObject.getString("msg");
                        Toast.makeText(SetBell.this, msg1, Toast.LENGTH_SHORT).show();
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    String str = (String) msg.obj;

                    try {
                        JSONObject jsonObject = new JSONObject(str);
                        JSONObject data = jsonObject.getJSONObject("data");
                        sound = data.getString("sound");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    getList(sound);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setbell);
        MyApplication.getInstance().addActivity(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        initview();
        user = userDao.getUserInfo();
        String url = Internet.SELECT_BALANCE_URL + "?phone_num=" + user.getUserPhone();
        httpUtil.get(mHandler, url, 2);
        adapter = new BellAdapter(this, list);
        lv_setbell.setAdapter(adapter);


    }

    private void initview() {
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                finish();
            }
        });
        findViewById(R.id.tv_setbell_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("铃声", "铃声");
                mediaPlayer.stop();
                for (Bell b : list) {
                    if (b.getIsCheck().equals("1")) {
                        String url = Internet.UPDATE_SOUND_URL + "?phone_num=" + user.getUserPhone() + "&sound=" + b.getBellPath();
                        user.setUserBell(b.getBellPath());
                        Log.e("铃声", user.getUserBell());
                        httpUtil.get(mHandler, url, 1);
                        //保存铃声
                        SharedPreferences settings = getSharedPreferences("settings", Context.MODE_MULTI_PROCESS);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("bellPath", user.getUserBell());
                        editor.commit();
                        userDao.updateUser(user);
                        Log.e("user", user.toString());
                        com.orhanobut.logger.Logger.e(userDao.getUserInfo().toString());
                        Toast.makeText(SetBell.this, "保存成功", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }
        });
        lv_setbell = (ListView) findViewById(R.id.lv_setbell);
        lv_setbell.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {

                    for (Bell a : list) {
                        a.setIsCheck("0");
                    }
                    list.get(position).setIsCheck("1");
                    adapter.notifyDataSetChanged();
                    startAlarm(list.get(position).getBellPath());
                    Log.e("设置铃声", "设置铃声");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void getList(String sound) {
        Bell bell = new Bell();
        bell.setBellName("铃声1");
        bell.setBellPath("Cannon.m4a");
        bell.setIsCheck("0");
        list.add(bell);
        Bell bell1 = new Bell();
        bell1.setBellName("铃声2");
        bell1.setBellPath("bell1.m4a");
        bell1.setIsCheck("0");
        list.add(bell1);
        Bell b2 = new Bell();
        b2.setBellName("铃声3");
        b2.setBellPath("b2.m4a");
        b2.setIsCheck("0");
        list.add(b2);
        Bell b3 = new Bell();
        b3.setBellName("铃声4");
        b3.setBellPath("b3.m4a");
        b3.setIsCheck("0");
        list.add(b3);
        Bell b4 = new Bell();
        b4.setBellName("铃声5");
        b4.setBellPath("b4.m4a");
        b4.setIsCheck("0");
        list.add(b4);
        Bell b5 = new Bell();
        b5.setBellName("铃声6");
        b5.setBellPath("b5.m4a");
        b5.setIsCheck("0");
        list.add(b5);
        Bell b6 = new Bell();
        b6.setBellName("铃声7");
        b6.setBellPath("b6.m4a");
        b6.setIsCheck("0");
        list.add(b6);
        Bell b7 = new Bell();
        b7.setBellName("铃声8");
        b7.setBellPath("b7.m4a");
        b7.setIsCheck("0");
        list.add(b7);
        Bell b8 = new Bell();
        b8.setBellName("铃声9");
        b8.setBellPath("b8.m4a");
        b8.setIsCheck("0");
        list.add(b8);
        Bell b9 = new Bell();
        b9.setBellName("铃声10");
        b9.setBellPath("b9.m4a");
        b9.setIsCheck("0");
        list.add(b9);
        Bell b10 = new Bell();
        b10.setBellName("铃声11");
        b10.setBellPath("b10.m4a");
        b10.setIsCheck("0");
        list.add(b10);
        Bell b11 = new Bell();
        b11.setBellName("铃声12");
        b11.setBellPath("b11.m4a");
        b11.setIsCheck("0");
        list.add(b11);
        Bell b12 = new Bell();
        b12.setBellName("铃声13");
        b12.setBellPath("b12.m4a");
        b12.setIsCheck("0");
        list.add(b12);

        if (sound != null) {
            for (Bell b : list) {
                if (b.getBellPath().equals(sound)) {
                    b.setIsCheck("1");
                    break;
                }
            }
        } else {
            list.get(0).setIsCheck("1");
        }

        adapter.notifyDataSetChanged();
    }

    private void startAlarm(String bellpath) throws IOException {

        mediaPlayer.stop();
        mediaPlayer.reset();
        AssetFileDescriptor fileDescriptor = getAssets().openFd(bellpath);
        mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),
                fileDescriptor.getStartOffset(),
                fileDescriptor.getLength());
        mediaPlayer.prepare();
        mediaPlayer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            User userInfo = new UserDAO(this).getUserInfo();
            for (Bell b : list) {
                if (b.getIsCheck().equals("1")) {
                    if (!b.getBellPath().equals(userInfo.getUserBell())) {

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
