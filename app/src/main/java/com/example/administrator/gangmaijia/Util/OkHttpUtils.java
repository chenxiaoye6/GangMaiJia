package com.example.administrator.gangmaijia.Util;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.IOException;


public class OkHttpUtils {


    private static String result;
    //post请求
    public static String postKeyValue(Handler mHandler,String url, FormEncodingBuilder builder) throws Exception {
        Log.e("789","url=========="+url);
        OkHttpClient okHttpClient=new OkHttpClient();
        RequestBody formBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        if (response.isSuccessful()){
            result = response.body().string();
            Log.e("msg.obj",result.toString());
            //解析得到msg 和 result
            JSONObject jsonObject=new JSONObject(result);
            Message msg=new Message();
            msg.obj=jsonObject.getString("msg");
            Log.e("msg.obj",msg.obj.toString());
            msg.what=(Integer.parseInt(jsonObject.getString("result")));
            Log.e("msg.what",msg.what+"");
            mHandler.sendMessage(msg);

        }else {
            throw new IOException("Unexpected code " + response);

        }
        return result;
    }
    public static String getKeyValue( String url) throws IOException {
        Log.e("789","url=========="+url);
        OkHttpClient okHttpClient=new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        if (response.isSuccessful()){
           return (response.body().string());
        }else {
            throw new IOException("Unexpected code " + response);
        }
    }

}
