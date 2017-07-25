package com.example.administrator.gangmaijia.Util;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2016/10/12.
 */

public class HttpUtil {

    public void get(final Handler handler, final String url, final int i) {
        Log.e("789", "url============" + url);
        String result = null;
        new Thread(new Runnable() {
            String result = null;

            @Override
            public void run() {
                try {
                    System.out.println(url);
                    DefaultHttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpGet = new HttpGet(url);
                    Log.e("url", "get的url=" + url);
                    HttpResponse response = httpClient.execute(httpGet);
                    result = EntityUtils.toString(response.getEntity(), "utf-8");
                    Log.e("url", "get的result=" + result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.obj = result;
                msg.what = i;
                handler.sendMessage(msg);
            }
        }).start();
    }

    public void get(final Handler handler, final String url) {
        Log.e("789", "url============" + url);
        String result = null;
        new Thread(new Runnable() {
            String result = null;

            @Override
            public void run() {
                try {
                    System.out.println(url);
                    Log.e("url", "get的url=" + url);
                    DefaultHttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpGet = new HttpGet(url);
                    HttpResponse response = httpClient.execute(httpGet);
                    result = EntityUtils.toString(response.getEntity(), "utf-8");
                    Log.e("url", "get的result=" + result);
//                    com.orhanobut.logger.Logger.e(url+"\n"+result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.obj = result;
                handler.sendMessage(msg);
            }
        }).start();
    }

//    public void post(final Handler handler, final String url, final int i, final JSONObject jsonObject){
//        new Thread(new Runnable() {
//            String result = null;
//            @Override
//            public void run() {
//                try {
//                    String encoderJson = null;
//
//                    DefaultHttpClient httpClient = new DefaultHttpClient();
//                    HttpPost httpPost = new HttpPost(url);
//                    encoderJson = URLEncoder.encode(jsonObject.toString(), HTTP.UTF_8);
////                    StringEntity se = new StringEntity(encoderJson);
////                    se.setContentType("text/json");
////                    se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json; charset=utf-8"));
////                    httpPost.setEntity(se);
//                    httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json");
//
//                    StringEntity se = new StringEntity(encoderJson);
//                    se.setContentType("text/json");
//                    se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
//                    System.out.println("---------se-------"+se);
//                    httpPost.setEntity(se);
//                    HttpResponse response = httpClient.execute(httpPost);
//                    System.out.println("response.getStatusLine().getStatusCode()======================="+response.getStatusLine().getStatusCode());
//                    System.out.println("response.getStatusLine().getStatusCode()======================="+response.getStatusLine().getReasonPhrase());
//                    System.out.println("response.getStatusLine().getStatusCode()======================="+response.getStatusLine().getProtocolVersion().toString());
//                    result = EntityUtils.toString(response.getEntity(),"utf-8");// 返回json格式：
//                    com.orhanobut.logger.Logger.e(result);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                Message msg = new Message();
//                msg.obj = result;
//                msg.what = i;
//                handler.sendMessage(msg);
//            }
//        }).start();
//    }


    public void post(final Handler handler, final String url, final int i, final List<NameValuePair> nvps) {
        Log.e("789", "url============" + url + "========nvps.tostring=======" + nvps.toString());
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = null;
                try {
                    String encoderJson = null;
                    DefaultHttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(url);
                    Log.e("aaa", "nvps============" + nvps.toString());
                    Log.e("aaa", "url============" + url);
                    UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                    httpPost.setEntity(formEntity);
                    HttpResponse response = httpClient.execute(httpPost);
                    httpClient.getConnectionManager().shutdown();
                    System.out.println("response.getStatusLine().getStatusCode()=======================" + response.getStatusLine().getStatusCode());
                    HttpEntity entity = response.getEntity();
                    result = EntityUtils.toString(entity, "utf-8");
                    Log.e("url", "post的result=" + result);
                } catch (IOException e) {
                    e.printStackTrace();
                    result = "{\"msg\":\"链接异常\",\"data\":{},\"result\":-1}";
                }
                Message msg = new Message();
                msg.obj = result.toString();
                msg.what = i;
                Log.e("what", i + "");
                handler.sendMessage(msg);
            }
        }).start();
    }

}
