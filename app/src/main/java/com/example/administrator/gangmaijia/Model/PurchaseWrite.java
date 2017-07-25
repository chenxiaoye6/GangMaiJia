package com.example.administrator.gangmaijia.Model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/21.
 */

public class PurchaseWrite implements Serializable {

    private String acceptor ;//"acceptor":"全部接收"
    private String show1 ;//show1":1
    private String state ;//state
    private String back_stage ;//back_stage":0
    private String standard ;//"standard":"2"
    private String phone_num ;//phone_num":"100","
    private String page ;//page":0
    private String order_num ;//order_num":"20161112150704"
    private String product_name ;//product_name
    private String gener_time ;//"gener_time":"2016-11-12 15:07:04"
    private String ton_amount ;//"ton_amount":2,
    private String comment ;//,"comment":"2"
    private String type_num ;//"type_num":6,
    private String material ;//material":"2"
    private boolean showup;


    public PurchaseWrite(String acceptor, String show1, String state, String back_stage, String standard, String phone_num, String page, String order_num, String product_name, String gener_time, String ton_amount, String comment, String type_num, String material, boolean showup) {
        this.acceptor = acceptor;
        this.show1 = show1;
        this.state = state;
        this.back_stage = back_stage;
        this.standard = standard;
        this.phone_num = phone_num;
        this.page = page;
        this.order_num = order_num;
        this.product_name = product_name;
        this.gener_time = gener_time;
        this.ton_amount = ton_amount;
        this.comment = comment;
        this.type_num = type_num;
        this.material = material;
        this.showup = showup;
    }

    public PurchaseWrite() {
    }

    @Override
    public String toString() {
        return "PurchaseWrite{" +
                "acceptor='" + acceptor + '\'' +
                ", show1='" + show1 + '\'' +
                ", state='" + state + '\'' +
                ", back_stage='" + back_stage + '\'' +
                ", standard='" + standard + '\'' +
                ", phone_num='" + phone_num + '\'' +
                ", page='" + page + '\'' +
                ", order_num='" + order_num + '\'' +
                ", product_name='" + product_name + '\'' +
                ", gener_time='" + gener_time + '\'' +
                ", ton_amount='" + ton_amount + '\'' +
                ", comment='" + comment + '\'' +
                ", type_num='" + type_num + '\'' +
                ", material='" + material + '\'' +
                ", showup=" + showup +
                '}';
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public String getBack_stage() {
        return back_stage;
    }

    public void setBack_stage(String back_stage) {
        this.back_stage = back_stage;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
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

    public String getGener_time() {
        return gener_time;
    }

    public void setGener_time(String gener_time) {
        this.gener_time = gener_time;
    }

    public String getTon_amount() {
        return ton_amount;
    }

    public void setTon_amount(String ton_amount) {
        this.ton_amount = ton_amount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getType_num() {
        return type_num;
    }

    public void setType_num(String type_num) {
        this.type_num = type_num;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public boolean isShowup() {
        return showup;
    }

    public void setShowup(boolean showup) {
        this.showup = showup;
    }
}
