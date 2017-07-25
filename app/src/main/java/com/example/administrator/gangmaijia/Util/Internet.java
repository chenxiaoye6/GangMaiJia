package com.example.administrator.gangmaijia.Util;

/**
 * Created by Administrator on 2016/10/12.
 */

public class Internet {


//        public static final String BASE_URL = "http://www.gangmaijiaw.com/GangMaiJia/";
    public static final String BASE_URL = "http://axej.viphk.ngrok.org/GangMaiJia/";
    public static final String CODE_SEND_URL = BASE_URL + "send.action";
    public static final String CHANGE_PHONE_CODE_SEND = BASE_URL + "update_send.action";
    public static final String LOGIN_URL = BASE_URL + "login.action";
    public static final String CHANGE_PHONE_URL = BASE_URL + "update_phone.action";
    public static final String COMPANY_REGISTER_URL = BASE_URL + "sign_up.action";
    public static final String WRITE_ORDER_URL = BASE_URL + "insert_purchase.action";
    public static final String INSERT_PHOTO_URL = BASE_URL + "insert_photo.action";
    public static final String INSERT_PROCESS_URL = BASE_URL + "insert_process.action";
    public static final String INSERT_LOGISTIC_URL = BASE_URL + "insert_logistic.action";
    public static final String SHOW_PROCESS_URL = BASE_URL + "select_process.action";
    public static final String SHOW_LGOISTIC_URL = BASE_URL + "select_logistic.action";
    public static final String SHOW_PURCHASE_URL = BASE_URL + "select_purchase.action";
    //    public static final String SHOW_PROCESS_URL = "http://51bigod.ngrok.cc/GangMaiJia/" + "select_process.action";
//    public static final String SHOW_LGOISTIC_URL = "http://51bigod.ngrok.cc/GangMaiJia/" + "select_logistic.action";
//    public static final String SHOW_PURCHASE_URL = "http://51bigod.ngrok.cc/GangMaiJia/" + "select_purchase.action";
    public static final String SHOW_PHOTO_URL = BASE_URL + "select_photo.action";
    public static final String DELETE_PROCESS_URL = BASE_URL + "process_show.action";
    public static final String DELETE_PURCHASE_URL = BASE_URL + "purchase_show.action";
    public static final String DELETE_PHOTO_URL = BASE_URL + "photo_show.action";
    public static final String DELETE_LGOISTIC_URL = BASE_URL + "logistic_show.action";
    public static final String ALL_PURCHASE_URL = BASE_URL + "relation_purchase.action";
    public static final String ALL_PROCESS_URL = BASE_URL + "relation_process.action";
    public static final String ALL_LOGISTIC_URL = BASE_URL + "relation_logistic.action";
    //    public static final String QIANG_PURCHASE_URL = BASE_URL + "purchase_state.action";
//    public static final String QIANG_PROCESS_URL = BASE_URL + "process_state.action";
//    public static final String QIANG_LOGISTIC_URL = BASE_URL + "logistic_state.action";
//    public static final String INSERT_RELATION_URL = BASE_URL + "insert_relation.action";
    public static final String SELECT_BALANCE_URL = BASE_URL + "select_balance.action";
    public static final String SELECT_DETAILS_URL = BASE_URL + "select_details.action";
    public static final String UPDATE_SOUND_URL = BASE_URL + "update_sound.action";
    public static final String INSERTRELATIONPURCHASE = BASE_URL + "insert_relation_purchase.action";
    public static final String INSERTRELATIONPROCESS = BASE_URL + "insert_relation_process.action";
    public static final String INSERTRELATIONLOGISTIC = BASE_URL + "insert_relation_logistic.action";
    public static final String SIGNLOGIN = BASE_URL + "sign_login.action";
    public static final String SELECTVERSION = BASE_URL + "select_version.action";
    public static final String UPDATECID = BASE_URL + "update_cid.action";
    //删除完成单
    public static final String DELETECOMPLETEORDER = BASE_URL + "relation_show.action";
    //删除未抢单
    public static final String DELETENOORDER = BASE_URL + "show_order.action";
    //    //删除加工单
    public static final String DELETEPROCESS = BASE_URL + "process_show.action";
    //    //删除采购单
    public static final String DELETEPURCHASE = BASE_URL + "purchase_show.action";
    // 互动数据请求
    public static final String HUDONG = BASE_URL + "like_liuyan.action";
    // 互动发布
    public static final String HUDONGPUBLISH = BASE_URL + "insert_liuyan.action";
    public String internet() {
        return BASE_URL;
    }
}
