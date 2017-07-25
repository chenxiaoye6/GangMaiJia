package com.example.administrator.gangmaijia.Activity.PersonalCenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.administrator.gangmaijia.Model.GoodBuniss;
import com.example.administrator.gangmaijia.R;
import com.example.administrator.gangmaijia.Util.Internet;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/8.
 */

public class GoodBusinessAvtivity extends Activity {
    private EditText etSearch;
    private ListView lv_majorBusiness;
    private ImageView back;
    Handler myhandler = new Handler();
    List<GoodBuniss> listBusiness = new ArrayList<GoodBuniss>();
    Map<String, Object> map;
    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    List<Map<String, Object>> mlist = new ArrayList<Map<String, Object>>();
    SimpleAdapter adapter;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String s = (String) msg.obj;
            //com.orhanobut.logger.Logger.e(s);
            try {
                JSONObject jsonObject1 = new JSONObject(s);
                JSONObject data = jsonObject1.getJSONObject("data");
                Log.e("012", data.toString());
                for (int z = 1; z <= data.length(); z++) {
                    JSONObject news = data.getJSONObject("new" + z);
                    listBusiness.add(new GoodBuniss(news.getString("city_address"), news.getString("phone_num"), news.getString("name"), news.getString("major_business"), news.getString("introduction")));
                }
                Log.e("aaa", listBusiness.toString());
                for (int i = 0; i < listBusiness.size(); i++) {
                    map = new HashMap<String, Object>();
                    map.put("name", listBusiness.get(i).getName());
                    Log.e("012", "1");
                    map.put("major_business", listBusiness.get(i).getMajor_business());
                    map.put("phone_num", listBusiness.get(i).getPhone_num());
                    map.put("city_address", listBusiness.get(i).getCity_address());
                    map.put("introduction", listBusiness.get(i).getDetail());
                    list.add(map);
                    mlist = list;
                }
                //com.orhanobut.logger.Logger.e(list.toString());

                adapter = new SimpleAdapter(GoodBusinessAvtivity.this, list, R.layout.list_goodbusiness, new String[]{"name", "major_business"},
                        new int[]{R.id.majorbusiness_name, R.id.majorbusiness_yewu});
                lv_majorBusiness.setAdapter(adapter);
                //设置listview点击详情
                lv_majorBusiness.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        Intent intent = new Intent();
                        intent.setClass(GoodBusinessAvtivity.this, GoodBusinessDetail.class);
                        //create a bundle, then put it into intent
                        Bundle bundle = new Bundle();
                        Log.e("123", "list=" + list.get(position).toString() + "======================");
                        bundle.putString("name", list.get(position).get("name") + "");
                        bundle.putString("major_business", list.get(position).get("major_business").toString());
                        bundle.putString("phone_num", list.get(position).get("phone_num").toString());
                        bundle.putString("city_address", list.get(position).get("city_address") + "");
                        bundle.putString("introduction", list.get(position).get("introduction") + "");
                        intent.putExtras(bundle);
                        startActivity(intent);
                        //设置跳转

//                        bundle.putString("mainPageInfo", "Hello Activity1"); // 压入数据
//                        intent.putExtras(bundle);
//
//                        //use intent to define the page direction
//                        startActivity(intent);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    Thread thread = new Thread() {
        @Override
        public void run() {
            super.run();
            OkHttpClient mOkHttpClient = new OkHttpClient();
//创建一个Request
            final Request request = new Request.Builder()
                    .url(Internet.BASE_URL + "select_integrity.action")
                    .build();
//new call
            Call call = mOkHttpClient.newCall(request);
//请求加入调度
            call.enqueue(new com.squareup.okhttp.Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    String result = response.body().string();
                    com.orhanobut.logger.Logger.e(result);
                    Message message = Message.obtain();
                    message.obj = result;
                    handler.sendMessage(message);
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goodbusiness);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        lv_majorBusiness = (ListView) findViewById(R.id.lv_majorBusiness);
        etSearch = (EditText) findViewById(R.id.etSearch);
        back = (ImageView) findViewById(R.id.back);
        thread.start();
        //设置搜索框的文本更改时的监听器
        set_eSearch_TextChanged();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoodBusinessAvtivity.this.finish();
            }
        });
    }

    /**
     * 设置搜索框的文本更改时的监听器
     */
    private void set_eSearch_TextChanged() {
        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
                //这个应该是在改变的时候会做的动作吧，具体还没用到过。
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub
                //这是文本框改变之前会执行的动作
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                String mdate = etSearch.getText().toString();
                com.orhanobut.logger.Logger.e(mdate);
                com.orhanobut.logger.Logger.e(list.toString());
                list.clear();//先要清空，不然会叠加
                getlistSub(list, mdate);//获取更新数据
                com.orhanobut.logger.Logger.e(list.toString());
                adapter.notifyDataSetChanged();//更新
            }
        });

    }

    /**
     * 获得根据搜索框的数据data来从元数据筛选，筛选出来的数据放入mDataSubs里
     */

    private void getlistSub(List<Map<String, Object>> mlistSubs, String text) {
        int length = listBusiness.size();
        for (int i = 0; i < length; ++i) {
            if (listBusiness.get(i).getMajor_business().contains(text)) {

                Map<String, Object> item = new HashMap<String, Object>();
                item.put("name", listBusiness.get(i).getName());
                item.put("major_business", listBusiness.get(i).getMajor_business());
                item.put("phone_num", listBusiness.get(i).getPhone_num());
                item.put("city_address", listBusiness.get(i).getCity_address());
                item.put("introduction", listBusiness.get(i).getDetail());
                mlistSubs.add(item);
            }
        }
    }
}
