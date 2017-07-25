package com.example.administrator.gangmaijia.Model;

import java.io.Serializable;

/**
 * Created by wangliang on 2016/10/28.
 */

public class Consumption implements Serializable {

    private String d_week;
    private String phone_num;
    private String page;
    private String order_num;
    private String d_monthday;
    private int d_month;
    private int d_year;
    private int type;
    private String d_amount;
    private String time ;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getD_week() {
        return d_week;
    }

    public void setD_week(String d_week) {
        this.d_week = d_week;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getOrder_num() {
        return order_num;
    }

    public void setOrder_num(String order_num) {
        this.order_num = order_num;
    }

    public String getD_monthday() {
        return d_monthday;
    }

    public void setD_monthday(String d_monthday) {
        this.d_monthday = d_monthday;
    }

    public int getD_month() {
        return d_month;
    }

    public void setD_month(int d_month) {
        this.d_month = d_month;
    }

    public int getD_year() {
        return d_year;
    }

    public void setD_year(int d_year) {
        this.d_year = d_year;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getD_amount() {
        return d_amount;
    }

    public void setD_amount(String d_amount) {
        this.d_amount = d_amount;
    }
}
