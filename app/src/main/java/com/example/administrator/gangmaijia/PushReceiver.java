package com.example.administrator.gangmaijia;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.example.administrator.gangmaijia.Activity.PersonalCenter.AllOrder;
import com.example.administrator.gangmaijia.DAO.NewsDao;
import com.example.administrator.gangmaijia.Model.News;
import com.igexin.sdk.PushConsts;

import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by hlj on 2016/10/30.
 */

public class PushReceiver extends BroadcastReceiver {


    private String url;

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        Log.e("123", "/*--*/-*/-*/-*/-*/-*/" + bundle.getInt(PushConsts.CMD_ACTION));
        switch (bundle.getInt(PushConsts.CMD_ACTION)) {
            case PushConsts.GET_CLIENTID:
                String cid = bundle.getString("clientid");
                // TODO:处理cid返回
                Log.e("cid", "cid=" + cid);
                break;
            case PushConsts.GET_MSG_DATA:
                Log.e("123", "/GET_MSG_DATA/-*/-*/-*/-*/-*/");
                System.out.println("------------GET_MSG_DATA------------");
                String taskid = bundle.getString("taskid");
                Log.e("taskid", "taskid=" + taskid);
//                String messageid = bundle.getString("messageid");
                byte[] payload = bundle.getByteArray("payload");
                Log.e("123", "/payload.length" + payload.length);

                if (payload != null) {
                    String data = new String(payload);
                    // TODO:接收处理透传（payload）数据
                    try {
                        Log.e("012", data + "===========================");
                        JSONObject jsonObject = new JSONObject(data);
                        url = jsonObject.getString("content");
                        String music = jsonObject.getString("sound");
                        Log.e("music", "铃声是=" + music);
                        Log.e("url", url);
//                            HttpUtil httpUtil = new HttpUtil();
//                            httpUtil.get(mHandler,url);
                        Log.e("123", "url:" + url.toString());
                        //之后
                        Intent mIntent = new Intent(context, AllOrder.class);
                        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        //用Bundle携带数据
                        Bundle bundle2 = new Bundle();

                        if (url.contains("purchase") || url.contains("custom")) {
                            bundle2.putString("type", "1");
                            //传递name参数为tinyphp
                            Log.e("789", bundle2.getString("type", "9"));
                            mIntent.putExtras(bundle2);
                            context.startActivity(mIntent);
                        } else if (url.contains("process")) {
                            bundle2.putString("type", "process");
                            //传递name参数为tinyphp
                            Log.e("789", bundle2.getString("type", "9"));
                            mIntent.putExtras(bundle2);
                            context.startActivity(mIntent);
                        } else if (url.contains("logistic")) {
                            bundle2.putString("type", "logistic");
                            //传递name参数为tinyphp
                            Log.e("789", bundle2.getString("type", "9"));
                            mIntent.putExtras(bundle2);
                            context.startActivity(mIntent);
                        } else {
                            Log.e("012", "1");
                            //数据库操作
                            //数据库操作

                            NewsDao newsDao = new NewsDao(context);
                            //数据库操作
                            News mNews = new News();
                            Calendar c = Calendar.getInstance();
                            String time = c.get(Calendar.YEAR) + "年" + c.get(Calendar.MONTH) + "月" + c.get(Calendar.DAY_OF_MONTH) + "日  " +
                                    c.get(Calendar.HOUR_OF_DAY) + ":" + ((c.get(Calendar.MINUTE)) > 10 ? ((c.get(Calendar.MINUTE))) : ("0" + (c.get(Calendar.MINUTE)))) +
                                    ":" + ((c.get(Calendar.SECOND)) > 10 ? ((c.get(Calendar.SECOND))) : ("0" + (c.get(Calendar.SECOND))));
                            String news = url;
                            int ms = c.get(Calendar.DAY_OF_MONTH);
                            Log.e("789", "ms=========" + ms);
                            SharedPreferences sp = context.getSharedPreferences("time", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putInt("ms", ms);
                            editor.commit();
                            mNews.setMs(ms);
                            mNews.setTime(time);
                            mNews.setNews(news);
                            newsDao.addNews(mNews);
                            //
                            showNormalDialog(context, url);
                        }
                        //之后
//                        if (url.contains("purchase")) {
                        //需要跳转到那个界面
                            /*
                            //之前
                            mIntent.setAction(Intent.ACTION_MAIN);
                            mIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                            mIntent.setComponent(new ComponentName(context.getPackageName(), PurchaseWrite_detail.class.getCanonicalName()));
                            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            Intent intent1 = new Intent(context, com.example.administrator.gangmaijia.Activity.Purchase.PurchaseWrite_detail.class);
                            mIntent.putExtra("type", 3);
                            mIntent.putExtra("url", url);
                            Log.e("123", "startActivity");
                            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                             context.startActivity(mIntent);
                            */
//                        }
//                        if (url.contains("process")) {
//                            //需要跳转到那个界面
//                            Intent intent1 = new Intent(context, com.example.administrator.gangmaijia.Activity.Purchase.Process_detail.class);
//                            intent1.putExtra("type", 3);
//                            //
//                            intent1.setAction(Intent.ACTION_MAIN);
//                            intent1.addCategory(Intent.CATEGORY_LAUNCHER);
//                            //
//                            intent1.putExtra("url", url);
//                            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            //模式
//                            context.startActivity(intent1);
//                        }
//                        if (url.contains("logistic")) {
//                            //需要跳转到那个界面
//                            Intent intent1 = new Intent(context, com.example.administrator.gangmaijia.Activity.Purchase.Logistic_detail.class);
//                            //
//                            intent1.setAction(Intent.ACTION_MAIN);
//                            intent1.addCategory(Intent.CATEGORY_LAUNCHER);
//                            //
//                            intent1.putExtra("type", 3);
//                            intent1.putExtra("url", url);
//                            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            //模式
//                            context.startActivity(intent1);
//                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }


    private void showNormalDialog(Context context, String message) {
        Log.e("012", "2");
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.mipmap.biao);
        builder.setTitle("通知");
        builder.setMessage(message);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.e("012", "3");
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alertDialog.show();
    }
}
