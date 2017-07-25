package com.example.administrator.gangmaijia.Fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.gangmaijia.DAO.UserDAO;
import com.example.administrator.gangmaijia.Model.MessageEvent;
import com.example.administrator.gangmaijia.R;
import com.example.administrator.gangmaijia.Util.HttpUtil;
import com.example.administrator.gangmaijia.Util.Internet;
import com.example.administrator.gangmaijia.View.CityExpandView;
import com.example.administrator.gangmaijia.View.DialogCommon;
import com.example.administrator.gangmaijia.View.LogisticsExpandView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016/9/28.
 */
public class CheYuan extends Fragment implements View.OnClickListener {

    private View view;
    private ImageView iv_logistics_city1, iv_logistics_type1;
    private EditText et_cheyuan_changdu, et_cheyuan_changpin, et_cheyuan_date, et_cheyuan_dunwei, et_cheyuan_fahuo, et_cheyuan_shouhuo, et_cheyuan_yaoqiu;
    private TextView tv_cheyuan_add, tv_cheyuan_upload, et_cheyuan_type, logistics_expand_line1, logistics_expand_line2, logistics_expand_line3, logistics_expand_line4,
            city_expand_line1, city_expand_line2, city_expand_line3, city_expand_line4, city_expand_line5, city_expand_line6, logistics_city_tv;
    private LinearLayout ly_chayuan, logistics_city_linearLayout;
    private int t = 1, tmax = 5;
    private CityExpandView logistics_city_expandView;
    private LogisticsExpandView logistics_type_expandView;
    private EditText[] cheyuan_changdu = new EditText[tmax];
    private EditText[] cheyuan_changpin = new EditText[tmax];
    private EditText[] cheyuan_date = new EditText[tmax];
    private EditText[] cheyuan_dunwei = new EditText[tmax];
    private EditText[] cheyuan_fahuo = new EditText[tmax];
    private EditText[] cheyuan_shouhuo = new EditText[tmax];
    private TextView[] cheyuan_type = new TextView[tmax];
    private EditText[] cheyuan_yaoqiu = new EditText[tmax];
    private TextView[] target_city = new TextView[tmax];
    private ProgressDialog progressDialog;
    private UserDAO userDAO = new UserDAO(getActivity());
    private HttpUtil httpUtil = new HttpUtil();
    private int type = 1;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    String s = (String) msg.obj;
                    try {
                        progressDialog.dismiss();
                        JSONObject jsonObject = new JSONObject(s);
                        String msg1 = jsonObject.getString("msg");
                        Toast.makeText(getActivity(), msg1, Toast.LENGTH_SHORT).show();
                        if (jsonObject.getString("result").equals("0")) {
                            if (type == 2) {
                                add();
                                t++;
                                if (t == tmax) {
                                    tv_cheyuan_add.setVisibility(View.GONE);
                                }
                            } else {
                                MessageEvent messageEvent = new MessageEvent();
                                messageEvent.setType("fragment");
                                messageEvent.setMsg("cheyuan");
                                EventBus.getDefault().post(messageEvent);
//                                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//                                fragmentTransaction.replace(R.id.content, new CheYuan());
//                                fragmentTransaction.commit();
                                //初始化界面
//                                et_cheyuan_changdu = (EditText) view.findViewById(R.id.et_cheyuan_changdu);
//                                et_cheyuan_changpin = (EditText) view.findViewById(R.id.et_cheyuan_changpin);
//                                et_cheyuan_date = (EditText) view.findViewById(R.id.et_cheyuan_date);
//                                et_cheyuan_dunwei = (EditText) view.findViewById(R.id.et_cheyuan_dunwei);
//                                et_cheyuan_fahuo = (EditText) view.findViewById(R.id.et_cheyuan_fahuo);
//                                et_cheyuan_shouhuo = (EditText) view.findViewById(R.id.et_cheyuan_shouhuo);
//                                et_cheyuan_type = (TextView) view.findViewById(R.id.et_cheyuan_type);
//                                et_cheyuan_yaoqiu = (EditText) view.findViewById(R.id.et_cheyuan_yaoqiu);
                                et_cheyuan_changdu.setText("");
                                et_cheyuan_changpin.setText("");
                                et_cheyuan_date.setText("");
                                et_cheyuan_fahuo.setText("");
                                et_cheyuan_shouhuo.setText("");
                                et_cheyuan_type.setText("");
                                et_cheyuan_yaoqiu.setText("");
                                et_cheyuan_dunwei.setText("");
                                logistics_city_tv.setText("");
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.cheyuan, container, false);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        initview();
        return view;
    }

    private void initview() {

        logistics_expand_line1 = (TextView) view.findViewById(R.id.logistics_expand_line1);
        logistics_expand_line2 = (TextView) view.findViewById(R.id.logistics_expand_line2);
        logistics_expand_line3 = (TextView) view.findViewById(R.id.logistics_expand_line3);
        logistics_expand_line4 = (TextView) view.findViewById(R.id.logistics_expand_line4);
        city_expand_line1 = (TextView) view.findViewById(R.id.city_expand_line1);
        city_expand_line2 = (TextView) view.findViewById(R.id.city_expand_line2);
        city_expand_line3 = (TextView) view.findViewById(R.id.city_expand_line3);
        city_expand_line4 = (TextView) view.findViewById(R.id.city_expand_line4);
        city_expand_line5 = (TextView) view.findViewById(R.id.city_expand_line5);
        city_expand_line6 = (TextView) view.findViewById(R.id.city_expand_line6);
        iv_logistics_type1 = (ImageView) view.findViewById(R.id.iv_logistics_type1);
        logistics_type_expandView = (LogisticsExpandView) view.findViewById(R.id.logistics_type_expandView);
        logistics_city_expandView = (CityExpandView) view.findViewById(R.id.logistics_city_expandView);
        iv_logistics_city1 = (ImageView) view.findViewById(R.id.iv_logistics_city1);
        et_cheyuan_changdu = (EditText) view.findViewById(R.id.et_cheyuan_changdu);
        et_cheyuan_changpin = (EditText) view.findViewById(R.id.et_cheyuan_changpin);
        et_cheyuan_date = (EditText) view.findViewById(R.id.et_cheyuan_date);
        et_cheyuan_dunwei = (EditText) view.findViewById(R.id.et_cheyuan_dunwei);
        et_cheyuan_fahuo = (EditText) view.findViewById(R.id.et_cheyuan_fahuo);
        et_cheyuan_shouhuo = (EditText) view.findViewById(R.id.et_cheyuan_shouhuo);
        et_cheyuan_type = (TextView) view.findViewById(R.id.et_cheyuan_type);
        et_cheyuan_yaoqiu = (EditText) view.findViewById(R.id.et_cheyuan_yaoqiu);
        tv_cheyuan_add = (TextView) view.findViewById(R.id.tv_cheyuan_add);
        tv_cheyuan_upload = (TextView) view.findViewById(R.id.tv_cheyuan_upload);
        logistics_city_tv = (TextView) view.findViewById(R.id.logistics_city_tv);
        ly_chayuan = (LinearLayout) view.findViewById(R.id.ly_chayuan);

        cheyuan_changdu[0] = et_cheyuan_changdu;
        cheyuan_changpin[0] = et_cheyuan_changpin;
        cheyuan_date[0] = et_cheyuan_date;
        cheyuan_dunwei[0] = et_cheyuan_dunwei;
        cheyuan_fahuo[0] = et_cheyuan_fahuo;
        cheyuan_shouhuo[0] = et_cheyuan_shouhuo;
        cheyuan_type[0] = et_cheyuan_type;
        target_city[0] = logistics_city_tv;
        cheyuan_yaoqiu[0] = et_cheyuan_yaoqiu;

        //设置页面点击监听
        tv_cheyuan_add.setOnClickListener(this);
        tv_cheyuan_upload.setOnClickListener(this);

        logistics_expand_line1.setOnClickListener(this);
        logistics_expand_line2.setOnClickListener(this);
        logistics_expand_line3.setOnClickListener(this);
        logistics_expand_line4.setOnClickListener(this);
        city_expand_line1.setOnClickListener(this);
        city_expand_line2.setOnClickListener(this);
        city_expand_line3.setOnClickListener(this);
        city_expand_line4.setOnClickListener(this);
        city_expand_line5.setOnClickListener(this);
        city_expand_line6.setOnClickListener(this);
        iv_logistics_city1.setOnClickListener(this);
        iv_logistics_type1.setOnClickListener(this);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("正在提交订单...");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cheyuan_add:
                type = 2;
                showdialog();
                break;
            case R.id.tv_cheyuan_upload:
                type = 1;
                submit();
                break;
            case R.id.iv_logistics_type1:
                if (logistics_type_expandView.isExpand()) {
                    logistics_type_expandView.collapse();
                } else {
                    logistics_type_expandView.expand();
                }
                break;
            case R.id.iv_logistics_city1:
                if (logistics_city_expandView.isExpand()) {
                    logistics_city_expandView.collapse();
                } else {
                    logistics_city_expandView.expand();
                }
                break;
            case R.id.logistics_expand_line1:
                et_cheyuan_type.setText(logistics_expand_line1.getText());
                logistics_type_expandView.collapse();
                break;
            case R.id.logistics_expand_line2:
                et_cheyuan_type.setText(logistics_expand_line2.getText());
                logistics_type_expandView.collapse();
                break;
            case R.id.logistics_expand_line3:
                et_cheyuan_type.setText(logistics_expand_line3.getText());
                logistics_type_expandView.collapse();
                break;
            case R.id.logistics_expand_line4:
                et_cheyuan_type.setText(logistics_expand_line4.getText());
                logistics_type_expandView.collapse();
                break;
            case R.id.city_expand_line1:
                logistics_city_tv.setText(city_expand_line1.getText());
                logistics_city_expandView.collapse();
                break;
            case R.id.city_expand_line2:
                logistics_city_tv.setText(city_expand_line2.getText());
                logistics_city_expandView.collapse();
                break;
            case R.id.city_expand_line3:
                logistics_city_tv.setText(city_expand_line3.getText());
                logistics_city_expandView.collapse();
                break;
            case R.id.city_expand_line4:
                logistics_city_tv.setText(city_expand_line4.getText());
                logistics_city_expandView.collapse();
                break;
            case R.id.city_expand_line5:
                logistics_city_tv.setText(city_expand_line5.getText());
                logistics_city_expandView.collapse();
                break;
            case R.id.city_expand_line6:
                logistics_city_tv.setText(city_expand_line6.getText());
                logistics_city_expandView.collapse();
                break;
            default:
                break;
        }
    }

    private void showdialog() {
        final DialogCommon dialogCommon = new DialogCommon(getActivity(), "确认提交此订单，\n并添加新订单？", "是", "否");
        dialogCommon.show();
        dialogCommon.setClicklistener(new DialogCommon.ClickListenerInterface() {

            @Override
            public void doConfirm() {
                submit();
                dialogCommon.dismiss();
            }

            @Override
            public void doCancel() {
                dialogCommon.dismiss();
            }
        });
    }

    private void add() {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.cheyuan_item, null);
        logistics_type_expandView = (LogisticsExpandView) layout.findViewById(R.id.logistics_type_expandView);
        logistics_city_expandView = (CityExpandView) layout.findViewById(R.id.logistics_city_expandView);
        logistics_expand_line1 = (TextView) layout.findViewById(R.id.logistics_expand_line1);
        logistics_expand_line2 = (TextView) layout.findViewById(R.id.logistics_expand_line2);
        logistics_expand_line3 = (TextView) layout.findViewById(R.id.logistics_expand_line3);
        logistics_expand_line4 = (TextView) layout.findViewById(R.id.logistics_expand_line4);
        city_expand_line1 = (TextView) layout.findViewById(R.id.city_expand_line1);
        city_expand_line2 = (TextView) layout.findViewById(R.id.city_expand_line2);
        city_expand_line3 = (TextView) layout.findViewById(R.id.city_expand_line3);
        city_expand_line4 = (TextView) layout.findViewById(R.id.city_expand_line4);
        city_expand_line5 = (TextView) layout.findViewById(R.id.city_expand_line5);
        city_expand_line6 = (TextView) layout.findViewById(R.id.city_expand_line6);
        cheyuan_changdu[t] = (EditText) layout.findViewById(R.id.et_cheyuan_changdu);
        cheyuan_changpin[t] = (EditText) layout.findViewById(R.id.et_cheyuan_changpin);
        cheyuan_date[t] = (EditText) layout.findViewById(R.id.et_cheyuan_date);
        cheyuan_dunwei[t] = (EditText) layout.findViewById(R.id.et_cheyuan_dunwei);
        cheyuan_fahuo[t] = (EditText) layout.findViewById(R.id.et_cheyuan_fahuo);
        cheyuan_shouhuo[t] = (EditText) layout.findViewById(R.id.et_cheyuan_shouhuo);
        cheyuan_type[t] = (TextView) layout.findViewById(R.id.et_cheyuan_type);
        target_city[t] = (TextView) layout.findViewById(R.id.logistics_city_tv);
        cheyuan_yaoqiu[t] = (EditText) layout.findViewById(R.id.et_cheyuan_yaoqiu);
        ImageView iv_logistics_type2 = (ImageView) layout.findViewById(R.id.iv_logistics_type2);
        ImageView iv_logistics_city2 = (ImageView) layout.findViewById(R.id.iv_logistics_ciy2);
        iv_logistics_type2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (logistics_type_expandView.isExpand()) {
                    logistics_type_expandView.collapse();
                } else {
                    logistics_type_expandView.expand();
                }
            }
        });
        iv_logistics_city2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (logistics_city_expandView.isExpand()) {
                    logistics_city_expandView.collapse();
                } else {
                    logistics_city_expandView.expand();
                }
            }
        });

        ly_chayuan.addView(layout);
        expand(logistics_expand_line1, logistics_expand_line2, logistics_expand_line3, logistics_expand_line4, cheyuan_type[t], logistics_type_expandView);
        expand(city_expand_line1, city_expand_line2, city_expand_line3, city_expand_line4, city_expand_line5, city_expand_line6, target_city[t], logistics_city_expandView);
    }

    private void expand(TextView logistics_expand_line1, TextView logistics_expand_line2, TextView logistics_expand_line3, TextView logistics_expand_line4, final TextView textView, final LogisticsExpandView logistics_type_expandView) {
        logistics_expand_line1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("汽车");
                logistics_type_expandView.collapse();
            }
        });
        logistics_expand_line2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("火车");
                logistics_type_expandView.collapse();
            }
        });
        logistics_expand_line3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("海运");
                logistics_type_expandView.collapse();
            }
        });
        logistics_expand_line4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("倒短车");
                logistics_type_expandView.collapse();
            }
        });
    }

    private void expand(TextView city_expand_line1, TextView city_expand_line2, TextView city_expand_line3, TextView city_expand_line4, TextView city_expand_line5, TextView city_expand_line6, final TextView textView, final CityExpandView logistics_city_expandView) {
        city_expand_line1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("全部城市");
                logistics_city_expandView.collapse();
            }
        });
        city_expand_line2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("天津市");
                logistics_city_expandView.collapse();
            }
        });
        city_expand_line3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("唐山市");
                logistics_city_expandView.collapse();
            }
        });
        city_expand_line4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("沧州市");
                logistics_city_expandView.collapse();
            }
        });
        city_expand_line5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("胜芳镇");
                logistics_city_expandView.collapse();
            }
        });
        city_expand_line6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("聊城市");
                logistics_city_expandView.collapse();
            }
        });
    }

    private void submit() {
        String length = cheyuan_changdu[t - 1].getText().toString();
        String product_name = cheyuan_changpin[t - 1].getText().toString();
        String delivery_time = cheyuan_date[t - 1].getText().toString();
        String ton_amount = cheyuan_dunwei[t - 1].getText().toString();
        String delivery_place = cheyuan_fahuo[t - 1].getText().toString();
        String destination = cheyuan_shouhuo[t - 1].getText().toString();
        String transport_way = cheyuan_type[t - 1].getText().toString();
        String special_demand = cheyuan_yaoqiu[t - 1].getText().toString();
        String target_cityshow = target_city[t - 1].getText().toString();
        ///
        if (target_cityshow.equals("全部城市")) {
            target_cityshow = "null";
        }
        if (product_name == null | product_name.equals("") | ton_amount == null | "".equals(ton_amount) | delivery_time == null | delivery_time.equals("") | ton_amount == null | ton_amount.equals("") |
                delivery_place == null | delivery_place.equals("") | destination == null | destination.equals("") | transport_way == null | transport_way.equals("")) {
            Toast.makeText(getActivity(), "请填写完整信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if ("".equals(transport_way)) {
            Toast.makeText(getActivity(), "请选择运输方式", Toast.LENGTH_SHORT).show();
            return;
        }
//        if ("".equals(target_cityshow)) {
//            Toast.makeText(getActivity(), "请选择装货城市", Toast.LENGTH_SHORT).show();
//        }
        progressDialog.show();
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("phone_num", userDAO.getUserInfo().getUserPhone()));
        nvps.add(new BasicNameValuePair("product_name", product_name));
        nvps.add(new BasicNameValuePair("transport_way", transport_way));
        nvps.add(new BasicNameValuePair("delivery_place", delivery_place));
        nvps.add(new BasicNameValuePair("destination", destination));
        nvps.add(new BasicNameValuePair("ton_amount", ton_amount));
        nvps.add(new BasicNameValuePair("length", length));
        nvps.add(new BasicNameValuePair("delivery_time", delivery_time));
        nvps.add(new BasicNameValuePair("special_demand", special_demand));
        nvps.add(new BasicNameValuePair("target_city", "null"));
        httpUtil.post(mHandler, Internet.INSERT_LOGISTIC_URL, 1, nvps);
    }

    public void onDestroy() {
        super.onDestroy();
        System.out.println("-------onDestroy--cheyuan-------");
    }
}
