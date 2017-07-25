package com.example.administrator.gangmaijia.Model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/19.
 */

public class Logistic implements Serializable {

    private String delivery_time;
    private String delivery_place;
    private String show1;
    private String phone_num;
    private String page;
    private String order_num;
    private String product_name;
    private String state;
    private String length;
    private String ton_amount;
    private String gener_time;
    private String transport_way;
    private String special_demand;
    private String destination;
    private boolean showup;
    private String target_city;

    public boolean isShowup() {
        return showup;
    }

    public void setShowup(boolean showup) {
        this.showup = showup;
    }

    @Override
    public String toString() {
        return "Logistic{" +
                "delivery_time='" + delivery_time + '\'' +
                ", delivery_place='" + delivery_place + '\'' +
                ", show1='" + show1 + '\'' +
                ", phone_num='" + phone_num + '\'' +
                ", page='" + page + '\'' +
                ", order_num='" + order_num + '\'' +
                ", product_name='" + product_name + '\'' +
                ", state='" + state + '\'' +
                ", length='" + length + '\'' +
                ", ton_amount='" + ton_amount + '\'' +
                ", gener_time='" + gener_time + '\'' +
                ", transport_way='" + transport_way + '\'' +
                ", special_demand='" + special_demand + '\'' +
                ", destination='" + destination + '\'' +
                ", showup=" + showup +
                ", target_city='" + target_city + '\'' +
                '}';
    }

    public String getDelivery_time() {
        return delivery_time;
    }

    public void setDelivery_time(String delivery_time) {
        this.delivery_time = delivery_time;
    }

    public String getDelivery_place() {
        return delivery_place;
    }

    public void setDelivery_place(String delivery_place) {
        this.delivery_place = delivery_place;
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

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
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

    public String getTransport_way() {
        return transport_way;
    }

    public void setTransport_way(String transport_way) {
        this.transport_way = transport_way;
    }

    public String getSpecial_demand() {
        return special_demand;
    }

    public void setSpecial_demand(String special_demand) {
        this.special_demand = special_demand;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getTarget_city() {
        return target_city;
    }

    public void setTarget_city(String target_city) {
        this.target_city = target_city;
    }
}
