package com.example.administrator.gangmaijia.Model;

import java.util.List;

/**
 * Created by Administrator on 2017/7/24.
 */

public class HuDongBean {


    @Override
    public String toString() {
        return "HuDongBean{" +
                "result=" + result +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    /**
     * result : 0
     * data : [{"content":"1234445566fdhsgfjhcvbdfhdhgbvbcbhghsdfhghsfgfhghfgfcccddffgdfhdfh","id":"1500860536287","category":"求租","timess":"2017-07-24 09:42:16","area":"北京市","phone_num":"15122947309","ly_city":"北京市","states":"1","types":"已审核"},{"content":"123","id":"1500713659367","category":"招聘","timess":"2017-07-22 16:54:19","area":"天津市","phone_num":"15122947309","ly_city":"天津市","states":"1","types":"已审核"},{"content":"123","id":"1500713609568","category":"招聘","timess":"2017-07-22 16:53:29","area":"天津市","phone_num":"15122947309","ly_city":"天津市","states":"1","types":"已审核"},{"content":"123","id":"1500708651319","category":"招聘","timess":"2017-07-22 15:30:51","area":"天津市","phone_num":"15122947309","ly_city":"天津市","states":"1","types":"已审核"},{"content":"123","id":"1500708636003","category":"出租","timess":"2017-07-22 15:30:36","area":"天津市","phone_num":"15122947309","ly_city":"天津市","states":"1","types":"已审核"},{"content":"123","id":"1500708027754","category":"招聘","timess":"2017-07-22 15:20:27","area":"天津市","phone_num":"15122947309","ly_city":"天津市","states":"1","types":"已审核"},{"content":"内容编辑","id":"1500707716525","category":"招聘","timess":"2017-07-22 15:15:16","area":"天津市","phone_num":"15122947309","ly_city":"天津市","states":"1","types":"已审核"},{"content":"内容编辑","id":"1500707366380","category":"招聘","timess":"2017-07-22 15:09:26","area":"天津市","phone_num":"15122947309","ly_city":"天津市","states":"1","types":"已审核"},{"content":"内容编辑","id":"1500706861897","category":"求租","timess":"2017-07-22 15:01:01","area":"天津市","phone_num":"15122947309","ly_city":"天津市","states":"1","types":"已审核"},{"content":"123","id":"1498806282309","category":"招聘","timess":"2017-06-30 15:04:42","area":"天津市","phone_num":"15122947309","ly_city":"天津市","states":"1","types":"已审核"},{"content":"123","id":"1498805357289","category":"招聘","timess":"2017-06-30 14:49:17","area":"天津市","phone_num":"15122947309","ly_city":"天津市","states":"1","types":"已审核"},{"content":"123","id":"1498805241831","category":"招聘","timess":"2017-06-30 14:47:21","area":"天津市","phone_num":"15122947309","ly_city":"天津市","states":"1","types":"已审核"},{"content":"123","id":"1498805174408","category":"招聘","timess":"2017-06-30 14:46:14","area":"天津市","phone_num":"15122947309","ly_city":"天津市","states":"1","types":"已审核"},{"content":"Java是一门面向对象编程语言，不仅吸收了C++语言的各种优点，还摒弃了C++里难以理解的多继承、指针等概念，因此Java语言具有功能强大和简单易用两个特征。Java语言作为静态面向对象编程语言的代表，极好地实现了面向对象理论，允许程序员以优雅的思维方式进行复杂的编程","id":"1498805031255","category":"招聘","timess":"2017-06-30 14:43:51","area":"天津市","phone_num":"15122947309","ly_city":"天津市","states":"1","types":"已审核"}]
     * msg : 获取成功
     */


    private int result;
    private String msg;
    private List<DataBean> data;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        @Override
        public String toString() {
            return "DataBean{" +
                    "content='" + content + '\'' +
                    ", id='" + id + '\'' +
                    ", category='" + category + '\'' +
                    ", timess='" + timess + '\'' +
                    ", area='" + area + '\'' +
                    ", phone_num='" + phone_num + '\'' +
                    ", ly_city='" + ly_city + '\'' +
                    ", states='" + states + '\'' +
                    ", types='" + types + '\'' +
                    '}';
        }

        /**
         * content : 1234445566fdhsgfjhcvbdfhdhgbvbcbhghsdfhghsfgfhghfgfcccddffgdfhdfh
         * id : 1500860536287
         * category : 求租
         * timess : 2017-07-24 09:42:16
         * area : 北京市
         * phone_num : 15122947309
         * ly_city : 北京市
         * states : 1
         * types : 已审核
         */

        private String content;
        private String id;
        private String category;
        private String timess;
        private String area;
        private String phone_num;
        private String ly_city;
        private String states;
        private String types;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getTimess() {
            return timess;
        }

        public void setTimess(String timess) {
            this.timess = timess;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getPhone_num() {
            return phone_num;
        }

        public void setPhone_num(String phone_num) {
            this.phone_num = phone_num;
        }

        public String getLy_city() {
            return ly_city;
        }

        public void setLy_city(String ly_city) {
            this.ly_city = ly_city;
        }

        public String getStates() {
            return states;
        }

        public void setStates(String states) {
            this.states = states;
        }

        public String getTypes() {
            return types;
        }

        public void setTypes(String types) {
            this.types = types;
        }
    }
}
