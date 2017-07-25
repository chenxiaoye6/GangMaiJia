package com.example.administrator.gangmaijia.Model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/19.
 */

public class JiaGongOrder implements Serializable {

    private String show1;
    private String phone_num;
    private String page;
    private String process_type;
    private String order_num;
    private String product_name;
    private String state;
    private String ton_amount;
    private String gener_time;
    private String comment;
    private String process_content;
    private boolean showup;
    private  String target_city;

    public boolean isShowup() {
        return showup;
    }

    public void setShowup(boolean showup) {
        this.showup = showup;
    }


    @Override
    public String toString() {
        return "JiaGongOrder{" +
                "show1='" + show1 + '\'' +
                ", phone_num='" + phone_num + '\'' +
                ", page='" + page + '\'' +
                ", process_type='" + process_type + '\'' +
                ", order_num='" + order_num + '\'' +
                ", product_name='" + product_name + '\'' +
                ", state='" + state + '\'' +
                ", ton_amount='" + ton_amount + '\'' +
                ", gener_time='" + gener_time + '\'' +
                ", comment='" + comment + '\'' +
                ", process_content='" + process_content + '\'' +
                ", target_city='" + target_city + '\'' +
                '}';
    }

    public String getShow1() {
        return show1;
    }

    public void setShow1(String show1) {
        this.show1 = show1;
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

    public String getProcess_type() {
        return process_type;
    }

    public void setProcess_type(String process_type) {
        this.process_type = process_type;
    }

    public String getOrder_num() {
        return order_num;
    }

    public void setOrder_num(String order_num) {
        this.order_num = order_num;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTon_amount() {
        return ton_amount;
    }

    public void setTon_amount(String ton_amount) {
        this.ton_amount = ton_amount;
    }

    public String getGener_time() {
        return gener_time;
    }

    public void setGener_time(String gener_time) {
        this.gener_time = gener_time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getProcess_content() {
        return process_content;
    }

    public void setProcess_content(String process_content) {
        this.process_content = process_content;
    }

    public String getTarget_city() {
        return target_city;
    }

    public void setTarget_city(String target_city) {
        this.target_city = target_city;
    }
}
