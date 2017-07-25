package com.example.administrator.gangmaijia;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.gangmaijia.DAO.UserDAO;
import com.example.administrator.gangmaijia.Fragment.CheYuan;
import com.example.administrator.gangmaijia.Fragment.DingDan;
import com.example.administrator.gangmaijia.Fragment.JiaGong;
import com.example.administrator.gangmaijia.Fragment.WoDe;
import com.example.administrator.gangmaijia.Fragment.ZhaoHuo;
import com.example.administrator.gangmaijia.Model.MessageEvent;
import com.example.administrator.gangmaijia.Model.User;
import com.example.administrator.gangmaijia.Util.HttpUtil;
import com.example.administrator.gangmaijia.Util.Internet;
import com.example.administrator.gangmaijia.View.DialogCommon;

import org.apache.http.NameValuePair;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目的主Activity，所有的Fragment都嵌入在这里。
 *
 * @author guolin
 */
public class MainActivity extends Activity implements OnClickListener {

    private ZhaoHuo zhaohuo;

    private JiaGong jiagong;

    private CheYuan cheyuan;

    private DingDan dingdan;

    private WoDe wode;

    private View main_zhaohuo;

    private View main_jiagong;

    private View main_cheyuan;

    private View main_dingdan;

    private ImageView ivmain_zhaohuo;

    private ImageView ivmain_jiagong;

    private ImageView ivmain_cheyuan;

    private ImageView ivmain_dingdan;

    private TextView tvmain_zhaohuo;

    private TextView tvmain_jiagong;

    private TextView tvmain_cheyuan;

    private TextView tvmain_dingdan;

    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

    private int textColor_grey = Color.parseColor("#82858b");
    private int textColor_blue = Color.parseColor("#18b4ed");
    private int type = 2;
    private UserDAO userDAO = new UserDAO(this);
    private String string = "";

    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Object obj = msg.obj;
            Log.e("result", "结果是" + obj);
            try {
                //强制更新
                JSONObject jsonObject1 = new JSONObject(obj.toString());
                JSONObject jsonObject2 = jsonObject1.getJSONObject("data");
                Log.e("123", "obj=++++" + obj.toString());
                //版本的编号,跟后台对比
                if ("1".equals(jsonObject2.getInt("force_update") + "")) {
                    String i = "12";
                    //设置更新
                    if (i.equals(jsonObject2.getString("system_version"))) {

                    } else {

                        final DialogCommon dialogCommon = new DialogCommon(MainActivity.this, "钢买家有新版本了,是否更新?", "是", "否");
                        dialogCommon.show();
                        dialogCommon.setClicklistener(new DialogCommon.ClickListenerInterface() {

                            @Override
                            public void doConfirm() {
                                Uri uri = Uri.parse("http://m.gangmaijiaw.com/demo/demo.html");
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                //建立Intent对象，传入uri
                                startActivity(intent);

                                dialogCommon.dismiss();
                            }

                            @Override
                            public void doCancel() {
                                dialogCommon.dismiss();
                            }
                        });
                    }
                } else {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("123", "onPause=============");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("123", "onStart==============");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("123", "onStop==============");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("123", "onRestart=============");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("123", "onResume=================================");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        List<NameValuePair> nvps = new ArrayList<>();
        new HttpUtil().post(mhandler, Internet.SELECTVERSION, 1, nvps);

        //
        Intent intent = new Intent(this, MyNotificationService.class);
        PendingIntent sender = PendingIntent
                .getService(this, 0, intent, 0);
        AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 5 * 1000, sender);
        //
        Log.e("123", "onCreate=============");
        string = Settings.Secure.getString(getContentResolver(),
                "enabled_notification_listeners") + "";
        Log.e("string", "string=" + string);
        // 数据库中保存的格式：包名/服务名:包名/服务名，如：
        // com.example.notification/com.example.notification.NotificationService
        // :com.example.smartface/com.example.smartface.notification.SmartFaceListenerService
        if (!string.contains(MyNotificationService.class.getName())) {
            startActivity(new Intent(
                    "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
        }
        MyApplication.getInstance().addActivity(this);
//        EventBus.getDefault().register(this);
        type = Integer.parseInt(userDAO.getUserInfo().getUserType());
        // 初始化布局元素
        initViews();
        fragmentManager = getFragmentManager();
        // 第一次启动时选中第0个tab
        User user = userDAO.getUserInfo();
        Log.e("789", "user.tostring====" + user.toString());
//            Log.e("789", "1");
        setTabSelection(0);
    }

    /**
     * 在这里获取到每个需要用到的控件的实例，并给它们设置好必要的点击事件。
     */
    private void initViews() {
        main_zhaohuo = findViewById(R.id.main_zhaohuo);
        main_jiagong = findViewById(R.id.main_jiagong);
        main_cheyuan = findViewById(R.id.main_cheyuan);
        main_dingdan = findViewById(R.id.main_dingdan);
        ivmain_zhaohuo = (ImageView) findViewById(R.id.ivmain_zhaohuo);
        ivmain_jiagong = (ImageView) findViewById(R.id.ivmain_jiagong);
        ivmain_cheyuan = (ImageView) findViewById(R.id.ivmain_cheyuan);
        ivmain_dingdan = (ImageView) findViewById(R.id.ivmain_dingdan);
        tvmain_zhaohuo = (TextView) findViewById(R.id.tvmain_zhaohuo);
        tvmain_jiagong = (TextView) findViewById(R.id.tvmain_jiagong);
        tvmain_cheyuan = (TextView) findViewById(R.id.tvmain_cheyuan);
        tvmain_dingdan = (TextView) findViewById(R.id.tvmain_dingdan);
        if (type == 2) {
            ivmain_dingdan.setImageResource(R.mipmap.wode);
            tvmain_dingdan.setText("我的");
        }
        main_zhaohuo.setOnClickListener(this);
        main_jiagong.setOnClickListener(this);
        main_cheyuan.setOnClickListener(this);
        main_dingdan.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_zhaohuo:
                // 当点击了消息tab时，选中第1个tab
                setTabSelection(0);
                break;
            case R.id.main_jiagong:
                // 当点击了联系人tab时，选中第2个tab
                setTabSelection(1);
                break;
            case R.id.main_cheyuan:
                // 当点击了动态tab时，选中第3个tab
                setTabSelection(2);
                break;
            case R.id.main_dingdan:
                // 当点击了设置tab时，选中第4个tab
                setTabSelection(3);
                break;
        }
    }

    /**
     * 根据传入的index参数来设置选中的tab页。
     *
     * @param index 每个tab页对应的下标。0表示消息，1表示联系人，2表示动态，3表示设置。
     */
    private void setTabSelection(int index) {
        // 每次选中之前先清楚掉上次的选中状态
        clearSelection();
        // 开启一个Fragment事务
        transaction = fragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (index) {
            case 0:
                // 当点击了消息tab时，改变控件的图片和文字颜色
                ivmain_zhaohuo.setImageResource(R.mipmap.zhaohuo2);
                tvmain_zhaohuo.setTextColor(textColor_blue);
                if (zhaohuo == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    zhaohuo = new ZhaoHuo();
                    transaction.add(R.id.content, zhaohuo);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(zhaohuo);
                }
                break;
            case 1:
                // 当点击了联系人tab时，改变控件的图片和文字颜色
                ivmain_jiagong.setImageResource(R.mipmap.jiagong2);
                tvmain_jiagong.setTextColor(textColor_blue);
                if (jiagong == null) {
                    // 如果ContactsFragment为空，则创建一个并添加到界面上
                    jiagong = new JiaGong();
                    transaction.add(R.id.content, jiagong);
                } else {
                    // 如果ContactsFragment不为空，则直接将它显示出来
                    transaction.show(jiagong);
                }
                break;
            case 2:
                // 当点击了动态tab时，改变控件的图片和文字颜色
                ivmain_cheyuan.setImageResource(R.mipmap.zhaoche2);
                tvmain_cheyuan.setTextColor(textColor_blue);
                if (cheyuan == null) {
                    // 如果NewsFragment为空，则创建一个并添加到界面上
                    cheyuan = new CheYuan();
                    transaction.add(R.id.content, cheyuan);
                } else {
                    // 如果NewsFragment不为空，则直接将它显示出来
                    transaction.show(cheyuan);
                }
                break;
            case 3:

                if (type == 2) {

                    // 当点击了设置tab时，改变控件的图片和文字颜色
                    ivmain_dingdan.setImageResource(R.mipmap.dingdan2);
                    tvmain_dingdan.setTextColor(textColor_blue);
                    if (dingdan == null) {
                        // 如果SettingFragment为空，则创建一个并添加到界面上
                        dingdan = new DingDan();
                        transaction.add(R.id.content, dingdan);
                    } else {
                        // 如果SettingFragment不为空，则直接将它显示出来
                        dingdan.onDestroy();
                        dingdan = new DingDan();
                        transaction.add(R.id.content, dingdan);
//                    transaction.show(dingdan);
                    }

                } else if (type == 1) {
                    ivmain_dingdan.setImageResource(R.mipmap.wode2);
                    tvmain_dingdan.setTextColor(textColor_blue);
                    if (wode == null) {
                        // 如果SettingFragment为空，则创建一个并添加到界面上
                        wode = new WoDe();
                        transaction.add(R.id.content, wode);
                    } else {
                        // 如果SettingFragment不为空，则直接将它显示出来
                        transaction.show(wode);
                    }
                }
                break;
            default:
                break;
        }
        transaction.commit();
    }

    /**
     * 清除掉所有的选中状态。
     */
    private void clearSelection() {
        ivmain_zhaohuo.setImageResource(R.mipmap.zhaohuo1);
        tvmain_zhaohuo.setTextColor(textColor_grey);
        ivmain_jiagong.setImageResource(R.mipmap.jiagong);
        tvmain_jiagong.setTextColor(textColor_grey);
        ivmain_cheyuan.setImageResource(R.mipmap.zhaoche);
        tvmain_cheyuan.setTextColor(textColor_grey);
        if (type == 2) {
            ivmain_dingdan.setImageResource(R.mipmap.dingdn);
        } else if (type == 1) {
            ivmain_dingdan.setImageResource(R.mipmap.wode);
        }
        tvmain_dingdan.setTextColor(textColor_grey);
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction 用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (zhaohuo != null) {
            transaction.hide(zhaohuo);
        }
        if (jiagong != null) {
            transaction.hide(jiagong);
        }
        if (cheyuan != null) {
            transaction.hide(cheyuan);
        }
        if (dingdan != null) {
            transaction.hide(dingdan);
        }
        if (wode != null) {
            transaction.hide(wode);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("123", "onDestroy==============");
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        System.out.println("----------onMessageEvent----------");
        if (event.getType().equals("fragment")) {
            transaction = fragmentManager.beginTransaction();
            if (event.getMsg().equals("jiagong")) {
                transaction.remove(jiagong);
                jiagong = new JiaGong();
                transaction.add(R.id.content, jiagong);
            } else if (event.getMsg().equals("cheyuan")) {
                transaction.remove(cheyuan);
                cheyuan = new CheYuan();

                transaction.add(R.id.content, cheyuan);
            }
            transaction.commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            MyApplication.getInstance().exit();
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
