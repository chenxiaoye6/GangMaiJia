package com.example.administrator.gangmaijia.Fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
import com.example.administrator.gangmaijia.View.JiagongTypeExpandView;

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
public class JiaGong extends Fragment implements View.OnClickListener {

    private View view;
    private Context context;
    private JiagongTypeExpandView jiagongtype_expandView;
    private CityExpandView jiagongcity_expandView;
    private EditText et_jiagongdan_beizhu, et_jiagongdan_content, et_jiagongdan_dunwei, et_jiagongdan_name;
    private TextView et_jiagongdan_type, tv_jiagong_city, tv_jiagongdan_add, tv_jiagongdan_upload, tv_jiagong_type, city_expand_line1, city_expand_line2, city_expand_line3, city_expand_line4,
            city_expand_line5, city_expand_line6, type_expand_line1, type_expand_line2, type_expand_line3, type_expand_line4, tv_jiaong_city;
    private ImageView iv_jiagong_type, iv_jiagong_city;
    private LinearLayout ly_jiagong, jiaongtype_linearLayout, jiagongcity_linearLayout;
    private int t = 1, tmax = 5;
    //
    private TextView[] jiagongdan_type = new TextView[tmax];
    private EditText[] jiagongdan_beizhu = new EditText[tmax];
    private EditText[] jiagongdan_content = new EditText[tmax];
    private EditText[] jiagongdan_dunwei = new EditText[tmax];
    private EditText[] iagongdan_name = new EditText[tmax];
    //
    private TextView[] jiaong_city = new TextView[tmax];
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
                                    tv_jiagongdan_add.setVisibility(View.GONE);
                                }
                            } else {
                                MessageEvent messageEvent = new MessageEvent();
                                messageEvent.setType("fragment");
                                messageEvent.setMsg("jiagong");
                                EventBus.getDefault().post(messageEvent);
//                                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//                                fragmentTransaction.replace(R.id.content,new JiaGong());
//                                fragmentTransaction.commit();
                                //初始化界面
                                et_jiagongdan_beizhu.setText("");
                                et_jiagongdan_content.setText("");
                                et_jiagongdan_dunwei.setText("");
                                et_jiagongdan_name.setText("");
                                et_jiagongdan_type.setText("");
                                tv_jiagong_city.setText("");

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
        view = inflater.inflate(R.layout.jiagong, container, false);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        initview();
        return view;
    }

    private void initview() {

        jiagongtype_expandView = (JiagongTypeExpandView) view.findViewById(R.id.jiagongtype_expandView);
        jiagongcity_expandView = (CityExpandView) view.findViewById(R.id.jiagongcity_expandView);
        et_jiagongdan_beizhu = (EditText) view.findViewById(R.id.et_jiagongdan_beizhu);
        et_jiagongdan_content = (EditText) view.findViewById(R.id.et_jiagongdan_content);
        et_jiagongdan_dunwei = (EditText) view.findViewById(R.id.et_jiagongdan_dunwei);
        et_jiagongdan_name = (EditText) view.findViewById(R.id.et_jiagongdan_name);
        et_jiagongdan_type = (TextView) view.findViewById(R.id.tv_jiagong_type);
        tv_jiagong_city = (TextView) view.findViewById(R.id.tv_jiaong_city);
        tv_jiagongdan_add = (TextView) view.findViewById(R.id.tv_jiagongdan_add);
        tv_jiagongdan_upload = (TextView) view.findViewById(R.id.tv_jiagongdan_upload);
        tv_jiagong_type = (TextView) view.findViewById(R.id.tv_jiagong_type);
        iv_jiagong_type = (ImageView) view.findViewById(R.id.iv_jiagong_type);
        iv_jiagong_city = (ImageView) view.findViewById(R.id.iv_jiagong_city);
        city_expand_line1 = (TextView) view.findViewById(R.id.city_expand_line1);
        city_expand_line2 = (TextView) view.findViewById(R.id.city_expand_line2);
        city_expand_line3 = (TextView) view.findViewById(R.id.city_expand_line3);
        city_expand_line4 = (TextView) view.findViewById(R.id.city_expand_line4);
        city_expand_line5 = (TextView) view.findViewById(R.id.city_expand_line5);
        city_expand_line6 = (TextView) view.findViewById(R.id.city_expand_line6);
        type_expand_line1 = (TextView) view.findViewById(R.id.type_expand_line1);
        type_expand_line2 = (TextView) view.findViewById(R.id.type_expand_line2);
        type_expand_line3 = (TextView) view.findViewById(R.id.type_expand_line3);
        type_expand_line4 = (TextView) view.findViewById(R.id.type_expand_line4);
        ly_jiagong = (LinearLayout) view.findViewById(R.id.ly_jiagong);//

        jiagongdan_type[0] = et_jiagongdan_type;//
        iagongdan_name[0] = et_jiagongdan_name;

        jiaong_city[0] = tv_jiagong_city;//
        jiagongdan_content[0] = et_jiagongdan_content;
        jiagongdan_dunwei[0] = et_jiagongdan_dunwei;
        jiagongdan_beizhu[0] = et_jiagongdan_beizhu;

        tv_jiagongdan_add.setOnClickListener(this);
        tv_jiagongdan_upload.setOnClickListener(this);
        iv_jiagong_type.setOnClickListener(this);
        iv_jiagong_city.setOnClickListener(this);
        tv_jiagong_type.setOnClickListener(this);
        city_expand_line1.setOnClickListener(this);
        city_expand_line2.setOnClickListener(this);
        city_expand_line3.setOnClickListener(this);
        city_expand_line4.setOnClickListener(this);
        city_expand_line5.setOnClickListener(this);
        city_expand_line6.setOnClickListener(this);
        type_expand_line1.setOnClickListener(this);
        type_expand_line2.setOnClickListener(this);
        type_expand_line3.setOnClickListener(this);
        type_expand_line4.setOnClickListener(this);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("正在提交订单...");

    }

    //设置这个页面各个的点击事件,包括点击下拉
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_jiagongdan_add:
                type = 2;
                showdialog();
                break;
            case R.id.tv_jiagongdan_upload:
                type = 1;
                submit();
                break;
            //加工列表展开
            case R.id.iv_jiagong_type:
                if (jiagongtype_expandView.isExpand()) {
                    jiagongtype_expandView.collapse();
                } else {
                    jiagongtype_expandView.expand();
                }
                break;
            //city列表展开
            case R.id.iv_jiagong_city:
                if (jiagongcity_expandView.isExpand()) {
                    jiagongcity_expandView.collapse();
                } else {
                    jiagongcity_expandView.expand();
                }
                break;
            case R.id.type_expand_line1:
                tv_jiagong_type.setText("镀锌加工");
                jiagongtype_expandView.collapse();
                break;
            case R.id.type_expand_line2:
                tv_jiagong_type.setText("钢管加工");
                jiagongtype_expandView.collapse();
                break;
            case R.id.type_expand_line3:
                tv_jiagong_type.setText("板材加工");
                jiagongtype_expandView.collapse();
                break;
            case R.id.type_expand_line4:
                tv_jiagong_type.setText("其他加工");
                jiagongtype_expandView.collapse();
                break;
            case R.id.city_expand_line1:
                tv_jiagong_city.setText("全部城市");
                jiagongcity_expandView.collapse();
                break;
            case R.id.city_expand_line2:
                tv_jiagong_city.setText("天津市");
                jiagongcity_expandView.collapse();
                break;
            case R.id.city_expand_line3:
                tv_jiagong_city.setText("唐山市");
                jiagongcity_expandView.collapse();
                break;
            case R.id.city_expand_line4:
                tv_jiagong_city.setText("沧州市");
                jiagongcity_expandView.collapse();
                break;
            case R.id.city_expand_line5:
                tv_jiagong_city.setText("胜芳镇");
                jiagongcity_expandView.collapse();
                break;
            case R.id.city_expand_line6:
                tv_jiagong_city.setText("聊城市");
                jiagongcity_expandView.collapse();
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
        //设置加工的继续填写之后的布局情况
        LinearLayout layout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.jiagong_item, null);
        //
        jiagongcity_expandView = (CityExpandView) layout.findViewById(R.id.jiagongcity_expandView);
        jiagongtype_expandView = (JiagongTypeExpandView) layout.findViewById(R.id.jiagongtype_expandView);

        jiagongdan_type[t] = (TextView) layout.findViewById(R.id.tv_jiagong_type);
        iagongdan_name[t] = (EditText) layout.findViewById(R.id.et_jiagongdan_name);
        jiaong_city[t] = (TextView) layout.findViewById(R.id.tv_jiaong_city);
        jiagongdan_content[t] = (EditText) layout.findViewById(R.id.et_jiagongdan_content);
        jiagongdan_dunwei[t] = (EditText) layout.findViewById(R.id.et_jiagongdan_dunwei);
        jiagongdan_beizhu[t] = (EditText) layout.findViewById(R.id.et_jiagongdan_beizhu);
        //
        type_expand_line1 = (TextView) layout.findViewById(R.id.type_expand_line1);
        type_expand_line2 = (TextView) layout.findViewById(R.id.type_expand_line2);
        type_expand_line3 = (TextView) layout.findViewById(R.id.type_expand_line3);
        type_expand_line4 = (TextView) layout.findViewById(R.id.type_expand_line4);
        city_expand_line1 = (TextView) layout.findViewById(R.id.city_expand_line1);
        city_expand_line2 = (TextView) layout.findViewById(R.id.city_expand_line2);
        city_expand_line3 = (TextView) layout.findViewById(R.id.city_expand_line3);
        city_expand_line4 = (TextView) layout.findViewById(R.id.city_expand_line4);
        city_expand_line5 = (TextView) layout.findViewById(R.id.city_expand_line5);
        city_expand_line6 = (TextView) layout.findViewById(R.id.city_expand_line6);
        ImageView iv_jiagong_type2 = (ImageView) layout.findViewById(R.id.iv_jiagong_type2);
        ImageView iv_jiagong_city2 = (ImageView) layout.findViewById(R.id.iv_jiagong_city2);

        iv_jiagong_type2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (jiagongtype_expandView.isExpand()) {
                    jiagongtype_expandView.collapse();
                } else {
                    jiagongtype_expandView.expand();
                }
            }
        });
        iv_jiagong_city2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (jiagongcity_expandView.isExpand()) {
                    jiagongcity_expandView.collapse();
                } else {
                    jiagongcity_expandView.expand();
                }
            }
        });

        ly_jiagong.addView(layout);
        expand(type_expand_line1, type_expand_line2, type_expand_line3, type_expand_line4, jiagongdan_type[t], jiagongtype_expandView);
        expand(city_expand_line1, city_expand_line2, city_expand_line3, city_expand_line4, city_expand_line5, city_expand_line6, jiaong_city[t], jiagongcity_expandView);
    }

    //设置加工的页面内容
    private void expand(TextView type_expand_line1, TextView type_expand_line2, TextView type_expand_line3, TextView type_expand_line4, final TextView textView, final JiagongTypeExpandView jiagongtype_expandView) {
        type_expand_line1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("镀锌加工");
                jiagongtype_expandView.collapse();
            }
        });
        type_expand_line2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("钢管加工");
                jiagongtype_expandView.collapse();
            }
        });
        type_expand_line3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("板材加工");
                jiagongtype_expandView.collapse();
            }
        });
        type_expand_line4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("其他加工");
                jiagongtype_expandView.collapse();
            }
        });
    }

    private void expand(TextView city_expand_line1, TextView city_expand_line2, TextView city_expand_line3, TextView city_expand_line4, TextView city_expand_line5, TextView city_expand_line6, final TextView textView, final CityExpandView jiagongcity_expandView) {
        city_expand_line1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("全部城市");
                jiagongcity_expandView.collapse();
            }
        });
        city_expand_line2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("天津市");
                jiagongcity_expandView.collapse();
            }
        });
        city_expand_line3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("唐山市");
                jiagongcity_expandView.collapse();
            }
        });
        city_expand_line4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("沧州市");
                jiagongcity_expandView.collapse();
            }
        });
        city_expand_line5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("胜芳镇");
                jiagongcity_expandView.collapse();
            }
        });
        city_expand_line6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("聊城市");
                jiagongcity_expandView.collapse();
            }
        });
    }


    private void submit() {
        String beizhu = jiagongdan_beizhu[t - 1].getText().toString();
        String content = jiagongdan_content[t - 1].getText().toString();
        String dunwei = jiagongdan_dunwei[t - 1].getText().toString();
        String type = jiagongdan_type[t - 1].getText().toString();
        Log.e("type", type);
        String name = iagongdan_name[t - 1].getText().toString();
        //
        String target_city = jiaong_city[t - 1].getText().toString();

        if (target_city.equals("全部城市")) {
            target_city = "null";
        }
        if (content == null | content.equals("") | dunwei == null | dunwei.equals("") | type == null | type.equals("") | name == null | name.equals("")) {
            Toast.makeText(getActivity(), "请填写完整信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if ("".equals(type)) {
            Toast.makeText(getActivity(), "请选择加工类型", Toast.LENGTH_SHORT).show();
            return;
        }
        if ("".equals(target_city)) {
            Toast.makeText(getActivity(), "请选择接收城市", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.show();
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("phone_num", userDAO.getUserInfo().getUserPhone()));
        nvps.add(new BasicNameValuePair("product_name", name));
        nvps.add(new BasicNameValuePair("process_type", type));
        nvps.add(new BasicNameValuePair("process_content", content));
        nvps.add(new BasicNameValuePair("ton_amount", dunwei));
        nvps.add(new BasicNameValuePair("comment", beizhu));
        nvps.add(new BasicNameValuePair("target_city", target_city));
        Log.e("012", nvps.toString());
        httpUtil.post(mHandler, Internet.INSERT_PROCESS_URL, 1, nvps);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("-------onDestroy--jiagong-------");
    }
}
