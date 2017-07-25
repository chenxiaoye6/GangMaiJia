package com.example.administrator.gangmaijia.Model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/8.
 */

public class GoodBuniss implements Serializable{
    private int id;//":"120","
    private String city_address;
    private String phone_num;//":"233213323","
    private String name;//":"安鑫一家","
    private String major_business;//":"阿"
    private String detail;

    public GoodBuniss() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity_address() {
        return city_address;
    }

    public void setCity_address(String city_address) {
        this.city_address = city_address;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMajor_business() {
        return major_business;
    }

    public void setMajor_business(String major_business) {
        this.major_business = major_business;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public GoodBuniss(String name, String major_business) {
        this.name = name;
        this.major_business = major_business;
    }

    public GoodBuniss(int id, String city_address, String phone_num, String name, String major_business, String detail) {
        this.id = id;
        this.city_address = city_address;
        this.phone_num = phone_num;
        this.name = name;
        this.major_business = major_business;
        this.detail = detail;
    }
    public GoodBuniss( String city_address, String phone_num, String name, String major_business, String detail) {
        this.city_address = city_address;
        this.phone_num = phone_num;
        this.name = name;
        this.major_business = major_business;
        this.detail = detail;
    }


    @Override
    public String toString() {
        return "GoodBuniss{" +
                "id=" + id +
                ", city_address='" + city_address + '\'' +
                ", phone_num='" + phone_num + '\'' +
                ", name='" + name + '\'' +
                ", major_business='" + major_business + '\'' +
                ", detail='" + detail + '\'' +
                '}';
    }
}
