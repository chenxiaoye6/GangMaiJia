package com.example.administrator.gangmaijia.Model;

import com.bigkoo.pickerview.model.IPickerViewData;

import java.util.List;

/**
 * Created by Administrator on 2017/5/19.
 */

public class AddressBean implements IPickerViewData {


    /**
     * name : 全境
     * city : [{"name":"全境"}]
     */


    private String name;
    private List<CityBean> city;

    @Override
    public String toString() {
        return "AddressBean{" +
                "name='" + name + '\'' +
                ", city=" + city +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CityBean> getCity() {
        return city;
    }

    public void setCity(List<CityBean> city) {
        this.city = city;
    }

    @Override
    public String getPickerViewText() {
        return this.name;
    }

    public static class CityBean {
        @Override
        public String toString() {
            return "CityBean{" +
                    "name='" + name + '\'' +
                    '}';
        }

        /**
         * name : 全境
         */


        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
