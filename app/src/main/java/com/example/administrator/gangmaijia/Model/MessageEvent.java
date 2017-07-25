package com.example.administrator.gangmaijia.Model;

/**
 * Created by Administrator on 2016/10/21.
 */

public class MessageEvent {

    private String type;
    private String msg;


    public MessageEvent() {

    }

    public MessageEvent(String msg,String type) {
        this.msg = msg;
        this.type = type;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
