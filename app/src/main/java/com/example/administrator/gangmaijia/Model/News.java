package com.example.administrator.gangmaijia.Model;

/**
 * Created by Administrator on 2017/2/25.
 */

public class News {
    private int ms;
    private String time;
    private String news;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNews() {
        return news;
    }

    public void setNews(String news) {
        this.news = news;
    }

    public int getMs() {
        return ms;
    }

    public void setMs(int ms) {
        this.ms = ms;
    }

    @Override
    public String toString() {
        return "News{" +
                "ms=" + ms +
                ", time='" + time + '\'' +
                ", news='" + news + '\'' +
                '}';
    }
}
