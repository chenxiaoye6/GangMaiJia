package com.example.administrator.gangmaijia.DAO;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.administrator.gangmaijia.DBHelper;
import com.example.administrator.gangmaijia.Model.News;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/25.
 */

public class NewsDao {
    private int id;
    private String time;
    private String news;
    private Context context;

    public NewsDao(Context context) {
        this.context = context;
    }

    //添加新消息
    private String ADDNEWS = "insert into News_table(ms,time,news) values(?,?,?)";

    public boolean addNews(News news) {
        boolean flag = false;
        SQLiteDatabase db = DBHelper.getInstance(context).getWritableDatabase();
        Log.e("789", "database=========1");
        db.execSQL(ADDNEWS, new Object[]{
                news.getMs(),
                news.getTime(),
                news.getNews()
        });
        Log.e("789", "sqlnews=======" + news.toString());
        db.close();
        Log.e("789", "database=========2");
        flag = true;
        return flag;
    }

    //查询消息
    private String GETNEWS = "select * from News_table";

    public List<News> getNews() {
        List<News> list = new ArrayList<News>();
        SQLiteDatabase db = DBHelper.getInstance(context).getReadableDatabase();
        Cursor cursor = db.rawQuery(GETNEWS, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            News mNews = new News();
            int ms = cursor.getInt(cursor.getColumnIndex("ms"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            Log.e("789", "time============" + time);
            String news = cursor.getString(cursor.getColumnIndex("news"));
            Log.e("789", "news============" + news);
            mNews.setMs(ms);
            mNews.setTime(time);
            mNews.setNews(news);
            list.add(mNews);
        }
        cursor.close();
        Log.e("789", "list===================" + list.toString());
        return list;
    }

    //删除消息
    public boolean deleteNews() {
        SharedPreferences sp = context.getSharedPreferences("time", Context.MODE_PRIVATE);
        int ims = sp.getInt("ms", 0);
        Log.e("789", "ims=======" + ims);
        boolean flag = false;
        SQLiteDatabase db = DBHelper.getInstance(context).getWritableDatabase();
        db.execSQL("delete from News_table where ms<>" + ims);
        db.close();
        flag = true;
        return flag;
    }
}
