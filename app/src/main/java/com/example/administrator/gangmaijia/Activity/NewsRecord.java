package com.example.administrator.gangmaijia.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.administrator.gangmaijia.DAO.NewsDao;
import com.example.administrator.gangmaijia.Model.News;
import com.example.administrator.gangmaijia.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/2/27.
 */

public class NewsRecord extends Activity {

    private ImageView iv_tusong_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news);
        initListView();
    }

    private void initListView() {
        ListView lv = (ListView) findViewById(R.id.lv_news);
        iv_tusong_back = (ImageView) findViewById(R.id.iv_tusong_back);
        iv_tusong_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        List<News> list = new ArrayList<>();
        NewsDao newsDao = new NewsDao(this);
        newsDao.deleteNews();
        list = newsDao.getNews();
        //将list进行倒叙排列
        Collections.reverse(list);
        Log.e("list", list.toString());
        List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
        for (News news : list) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ms", news.getMs());
            map.put("time", news.getTime());
            map.put("news", news.getNews());
            data.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.newsitem, new String[]{"time", "news"},
                new int[]{R.id.tv_newitem_time, R.id.tv_newitem_text});
        lv.setAdapter(adapter);
    }
}
