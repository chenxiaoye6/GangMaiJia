package com.example.administrator.gangmaijia;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

/**
 * Created by Administrator on 2016/11/21.
 */

public class MyNotificationService extends NotificationListenerService {
    private MyHandler handler = new MyHandler();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("px", "Srevice is open" + "-----");
//        return super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.i("px", "Get Message" + "-----" + sbn.getNotification().tickerText.toString());
        Message message = handler.obtainMessage();
        message.what = 1;
        message.obj = sbn;
        handler.sendMessage(message);
    }

    @Override
    public void onDestroy() {
        Intent localIntent = new Intent();
        localIntent.setClass(this, MyNotificationService.class); //销毁时重新启动Service
        this.startService(localIntent);
    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        if (msg.obj.toString().contains("gangmaijia")) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        MediaPlayer mediaPlayer = new MediaPlayer();
                                        SharedPreferences settings = getSharedPreferences("settings", Context.MODE_MULTI_PROCESS);
                                        String bellpath = settings.getString("bellPath", "a");
                                        AssetFileDescriptor fileDescriptor = getAssets().openFd(bellpath);
                                        mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),
                                                fileDescriptor.getStartOffset(),
                                                fileDescriptor.getLength());
                                        mediaPlayer.prepare();
                                        mediaPlayer.start();
                                        Thread.sleep(3000);
                                        mediaPlayer.stop();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
        }
    }
}
