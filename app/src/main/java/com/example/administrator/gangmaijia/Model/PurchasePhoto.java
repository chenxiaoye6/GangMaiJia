package com.example.administrator.gangmaijia.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/21.
 */

public class PurchasePhoto implements Serializable{

    private String acceptor ;
    private String show1 ;
    private String phone_num ;
    private String page ;
    private String order_num ;
    private String state ;
    private List<String> photo_path = new ArrayList<>();

    @Override
    public String toString() {
        return "PurchasePhoto{" +
                "acceptor='" + acceptor + '\'' +
                ", show1='" + show1 + '\'' +
                ", phone_num='" + phone_num + '\'' +
                ", page='" + page + '\'' +
                ", order_num='" + order_num + '\'' +
                ", state='" + state + '\'' +
                ", photo_path=" + photo_path +
                '}';
    }

    public String getAcceptor() {
        return acceptor;
    }

    public void setAcceptor(String acceptor) {
        this.acceptor = acceptor;
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

    public String getOrder_num() {
        return order_num;
    }

    public void setOrder_num(String order_num) {
        this.order_num = order_num;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<String> getPhoto_path() {
        return photo_path;
    }

    public void setPhoto_path(List<String> photo_path) {
        this.photo_path = photo_path;
    }
}
