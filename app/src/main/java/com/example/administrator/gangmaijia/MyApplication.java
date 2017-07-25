package com.example.administrator.gangmaijia;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;

import com.igexin.sdk.PushManager;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/13.
 */

public class MyApplication extends Application {

    private List<Activity> mList = new LinkedList();
    private static MyApplication instance;
    IntentFilter filter = new IntentFilter(Intent.ACTION_TIME_TICK);

    private MyApplication() {

    }

    public synchronized static MyApplication getInstance() {
        if (null == instance) {
            instance = new MyApplication();
        }
        return instance;
    }

    // add Activity
    public void addActivity(Activity activity) {
        mList.add(activity);
    }

    public void exit() {
        try {
            for (Activity activity : mList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            System.exit(0);
        }
    }

    @Override
    public void onCreate() {

        super.onCreate();
        System.out.println("-----------PushManager.getInstance().initialize(this.getApplicationContext());----------");
        PushManager.getInstance().initialize(this.getApplicationContext());
    }
//
//    public void onLowMemory() {
//        super.onLowMemory();
//        System.gc();
//    }

}
