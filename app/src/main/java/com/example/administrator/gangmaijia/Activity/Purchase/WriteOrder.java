package com.example.administrator.gangmaijia.Activity.Purchase;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.gangmaijia.DAO.UserDAO;
import com.example.administrator.gangmaijia.MyApplication;
import com.example.administrator.gangmaijia.R;
import com.example.administrator.gangmaijia.Util.HttpUtil;
import com.example.administrator.gangmaijia.Util.Internet;
import com.example.administrator.gangmaijia.View.CityExpandView;
import com.example.administrator.gangmaijia.View.DialogCommon;
import com.example.administrator.gangmaijia.View.ExpandView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016/10/9.
 */

public class WriteOrder extends Activity implements View.OnClickListener {

    private EditText et_writedang_beizhu, et_writedang_caizhi, et_writedang_dunwei, et_writedang_guige, et_writedang_pinming;
    private TextView tv_writedang_add, tv_writedang_type, tv_writedang_upload, city_expand_line1, city_expand_line2, city_expand_line3, city_expand_line4,
            city_expand_line5, city_expand_line6, tv_writedang_city;
    private TextView expand_line1, expand_line2, expand_line3, expand_line4, add_again;
    private ImageView iv_writedang_type, back, iv_writedang_city;
    private LinearLayout linearLayout, ll_add;
    private ExpandView mExpandView;
    private CityExpandView cExpandView;
    private EditText[] writedang_pinming = new EditText[10];

    private EditText[] writedang_guige = new EditText[10];
    private EditText[] writedang_caizhi = new EditText[10];
    private EditText[] writedang_dunwei = new EditText[10];
    private EditText[] writedang_beizhu = new EditText[10];
    private TextView[] writedang_type = new TextView[10];
    private TextView[] writedang_city = new TextView[10];
    private TextView[] add_again_array = new TextView[10];
    private LinearLayout[] ll_add_array = new LinearLayout[10];
    private int t = 0;
    private ProgressDialog progressDialog;
    private UserDAO userDAO = new UserDAO(this);
    private HttpUtil httpUtil = new HttpUtil();
    private int type = 1;
    //多规格的属性
    private String mguige = "";
    private String mcaizhi = "";
    private String mshuliang = "";
    int m = 0;

    TextView txadd_again;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    String s = (String) msg.obj;
                    try {
                        Log.e("456", s);
                        progressDialog.dismiss();
                        JSONObject jsonObject = new JSONObject(s);
                        String msg1 = jsonObject.getString("msg");
                        Toast.makeText(WriteOrder.this, msg1, Toast.LENGTH_SHORT).show();
                        if (jsonObject.getString("result").equals("1")) {
                            //成功 再添加一行相同的布局
                            if (type == 2) {
                                t++;
                                add();
                                Log.e("456", "add");
                            } else {
                                Log.e("456", "finish");
                                finish();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
    private EditText et_add_guige;
    private EditText et_add_caizhi;
    private EditText et_add_dunwei;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.writedang);
        MyApplication.getInstance().addActivity(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        initview();
    }

    private void initview() {
        et_writedang_beizhu = (EditText) findViewById(R.id.et_writedang_beizhu);
        et_writedang_caizhi = (EditText) findViewById(R.id.et_writedang_caizhi);
        et_writedang_dunwei = (EditText) findViewById(R.id.et_writedang_dunwei);
        et_writedang_guige = (EditText) findViewById(R.id.et_writedang_guige);
        et_writedang_pinming = (EditText) findViewById(R.id.et_writedang_pinming);
        tv_writedang_add = (TextView) findViewById(R.id.tv_writedang_add);
        tv_writedang_type = (TextView) findViewById(R.id.tv_writedang_type);
        tv_writedang_upload = (TextView) findViewById(R.id.tv_writedang_upload);
        iv_writedang_type = (ImageView) findViewById(R.id.iv_writedang_type);
        tv_writedang_city = (TextView) findViewById(R.id.tv_writedang_city);
        mExpandView = (ExpandView) findViewById(R.id.expandView);
        cExpandView = (CityExpandView) findViewById(R.id.cExpandView);
        city_expand_line1 = (TextView) findViewById(R.id.city_expand_line1);
        city_expand_line2 = (TextView) findViewById(R.id.city_expand_line2);
        city_expand_line3 = (TextView) findViewById(R.id.city_expand_line3);
        city_expand_line4 = (TextView) findViewById(R.id.city_expand_line4);
        city_expand_line5 = (TextView) findViewById(R.id.city_expand_line5);
        city_expand_line6 = (TextView) findViewById(R.id.city_expand_line6);
        expand_line1 = (TextView) findViewById(R.id.expand_line1);
        expand_line2 = (TextView) findViewById(R.id.expand_line2);
        expand_line3 = (TextView) findViewById(R.id.expand_line3);
        expand_line4 = (TextView) findViewById(R.id.expand_line4);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        back = (ImageView) findViewById(R.id.back);
        iv_writedang_city = (ImageView) findViewById(R.id.iv_writedang_city);
        //继续添加设置监听事件
        add_again = (TextView) findViewById(R.id.add_again);
        ll_add = (LinearLayout) findViewById(R.id.ll_add);
        //找到各个text添加的值


        add_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (m == 0) {
                    mguige = et_writedang_guige.getText().toString();
                    mcaizhi = et_writedang_caizhi.getText().toString() + "";
                    mshuliang = et_writedang_dunwei.getText().toString();
                    if ("".equals(mguige) || "".equals(mshuliang)) {
                        Toast.makeText(WriteOrder.this, "规格、数量不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    if ("".equals(et_add_guige.getText().toString()) || "".equals(et_add_dunwei.getText().toString())) {
                        Toast.makeText(WriteOrder.this, "规格、数量不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mguige = mguige + ";" + et_add_guige.getText().toString();
                    mcaizhi = mcaizhi + ";" + et_add_caizhi.getText().toString() + "";
                    mshuliang = mshuliang + ";" + et_add_dunwei.getText().toString();
                }

                View view = LayoutInflater.from(WriteOrder.this).inflate(R.layout.add, null);
                et_add_guige = (EditText) view.findViewById(R.id.et_add_guige);
                et_add_caizhi = (EditText) view.findViewById(R.id.et_add_caizhi);
                et_add_dunwei = (EditText) view.findViewById(R.id.et_add_dunwei);
                ll_add.addView(view);
                m++;
            }
        });


        writedang_pinming[0] = et_writedang_pinming;
        writedang_guige[0] = et_writedang_guige;
        writedang_caizhi[0] = et_writedang_caizhi;
        writedang_dunwei[0] = et_writedang_dunwei;
        writedang_beizhu[0] = et_writedang_beizhu;
        writedang_type[0] = tv_writedang_type;
        writedang_city[0] = tv_writedang_city;
        add_again_array[0] = add_again;
        ll_add_array[0] = ll_add;


        back.setOnClickListener(this);
        tv_writedang_add.setOnClickListener(this);
        tv_writedang_upload.setOnClickListener(this);
        iv_writedang_type.setOnClickListener(this);
        expand_line1.setOnClickListener(this);
        expand_line2.setOnClickListener(this);
        expand_line3.setOnClickListener(this);
        expand_line4.setOnClickListener(this);
        city_expand_line1.setOnClickListener(this);
        city_expand_line2.setOnClickListener(this);
        city_expand_line3.setOnClickListener(this);
        city_expand_line4.setOnClickListener(this);
        city_expand_line5.setOnClickListener(this);
        city_expand_line6.setOnClickListener(this);
        iv_writedang_city.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在提交订单...");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_writedang_add:
                //继续填写
                type = 2;
                //
                showdialog();
                break;
            case R.id.tv_writedang_upload:
//                上传
                try {
                    type = 1;
                    submit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.expand_line1:
                tv_writedang_type.setText(expand_line1.getText().toString());
                mExpandView.collapse();
                break;
            case R.id.expand_line2:
                tv_writedang_type.setText(expand_line2.getText().toString());
                mExpandView.collapse();
                break;
            case R.id.expand_line3:
                tv_writedang_type.setText(expand_line3.getText().toString());
                mExpandView.collapse();
                break;
            case R.id.expand_line4:
                tv_writedang_type.setText(expand_line4.getText().toString());
                mExpandView.collapse();
                break;
            //展开对象列表
            case R.id.iv_writedang_type:
                if (mExpandView.isExpand()) {
                    mExpandView.collapse();
                } else {
                    mExpandView.expand();
                }
                break;
            //展开city列表
            case R.id.iv_writedang_city:
                if (cExpandView.isExpand()) {
                    cExpandView.collapse();
                } else {
                    cExpandView.expand();
                }
                break;
            //设置城市监听
            case R.id.city_expand_line1:
                tv_writedang_city.setText(city_expand_line1.getText());
                cExpandView.collapse();
                break;
            case R.id.city_expand_line2:
                tv_writedang_city.setText(city_expand_line2.getText());
                cExpandView.collapse();
                break;
            case R.id.city_expand_line3:
                tv_writedang_city.setText(city_expand_line3.getText());
                cExpandView.collapse();
                break;
            case R.id.city_expand_line4:
                tv_writedang_city.setText(city_expand_line4.getText());
                cExpandView.collapse();
                break;
            case R.id.city_expand_line5:
                tv_writedang_city.setText(city_expand_line5.getText());
                cExpandView.collapse();
                break;
            case R.id.city_expand_line6:
                tv_writedang_city.setText(city_expand_line6.getText());
                cExpandView.collapse();
                break;
        }
    }

    //发起提交请求
    private void showdialog() {
        final DialogCommon dialogCommon = new DialogCommon(this, "确认提交此订单，\n并添加新订单？", "是", "否");
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


    //提交成功 调用 添加布局
    private void add() {
        mguige = "";
        m = 0;
        LinearLayout layout = (LinearLayout) LayoutInflater.from(WriteOrder.this).inflate(R.layout.purchase_write, null);
        final ExpandView mExpandView = (ExpandView) layout.findViewById(R.id.expandView);
        final CityExpandView cExpandView = (CityExpandView) layout.findViewById(R.id.cExpandView);
        TextView expand_line1 = (TextView) layout.findViewById(R.id.expand_line1);
        TextView expand_line2 = (TextView) layout.findViewById(R.id.expand_line2);
        TextView expand_line3 = (TextView) layout.findViewById(R.id.expand_line3);
        TextView expand_line4 = (TextView) layout.findViewById(R.id.expand_line4);
        city_expand_line1 = (TextView) layout.findViewById(R.id.city_expand_line1);
        city_expand_line2 = (TextView) layout.findViewById(R.id.city_expand_line2);
        city_expand_line3 = (TextView) layout.findViewById(R.id.city_expand_line3);
        city_expand_line4 = (TextView) layout.findViewById(R.id.city_expand_line4);
        city_expand_line5 = (TextView) layout.findViewById(R.id.city_expand_line5);
        city_expand_line6 = (TextView) layout.findViewById(R.id.city_expand_line6);
        writedang_pinming[t] = (EditText) layout.findViewById(R.id.et_writedang_pinming);
        writedang_guige[t] = (EditText) layout.findViewById(R.id.et_writedang_guige);
        writedang_caizhi[t] = (EditText) layout.findViewById(R.id.et_writedang_caizhi);
        writedang_dunwei[t] = (EditText) layout.findViewById(R.id.et_writedang_dunwei);
        writedang_beizhu[t] = (EditText) layout.findViewById(R.id.et_writedang_beizhu);
        writedang_type[t] = (TextView) layout.findViewById(R.id.tv_writedang_type);
        writedang_city[t] = (TextView) layout.findViewById(R.id.tv_writedang_city);

        //添加规格
        add_again_array[t] = (TextView) layout.findViewById(R.id.add_again);
        ll_add_array[t] = (LinearLayout) layout.findViewById(R.id.ll_add);
        //找到各个text添加的值

        add_again_array[t].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (m == 0) {
                    mguige = writedang_guige[t].getText().toString();
                    mcaizhi = writedang_caizhi[t].getText().toString() + "";
                    mshuliang = writedang_dunwei[t].getText().toString();
                } else {
                    mguige = mguige + ";" + et_add_guige.getText().toString();
                    mcaizhi = mcaizhi + ";" + et_add_caizhi.getText().toString() + "";
                    mshuliang = mshuliang + ";" + et_add_dunwei.getText().toString();
                }

                View view = LayoutInflater.from(WriteOrder.this).inflate(R.layout.add, null);
                et_add_guige = (EditText) view.findViewById(R.id.et_add_guige);
                et_add_caizhi = (EditText) view.findViewById(R.id.et_add_caizhi);
                et_add_dunwei = (EditText) view.findViewById(R.id.et_add_dunwei);
                ll_add_array[t].addView(view);
                m++;
            }
        });


        ImageView iv_writedang_type2 = (ImageView) layout.findViewById(R.id.iv_writedang_type2);
        ImageView iv_writedang_city2 = (ImageView) layout.findViewById(R.id.iv_writedang_city2);
        iv_writedang_city2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cExpandView.isExpand()) {
                    cExpandView.collapse();
                } else {
                    cExpandView.expand();
                }
            }
        });
        iv_writedang_type2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mExpandView.isExpand()) {
                    mExpandView.collapse();
                } else {
                    mExpandView.expand();
                }
            }
        });
        linearLayout.addView(layout);
        expand(expand_line1, expand_line2, expand_line3, expand_line4, writedang_type[t], mExpandView);
        expand(city_expand_line1, city_expand_line2, city_expand_line3, city_expand_line4, city_expand_line5, city_expand_line6, writedang_city[t], cExpandView);
    }

    private void expand(TextView city_expand_line1, TextView city_expand_line2, TextView city_expand_line3, TextView city_expand_line4, TextView city_expand_line5, TextView city_expand_line6, final TextView textView, final CityExpandView cExpandView) {
        city_expand_line1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("全部城市");
                cExpandView.collapse();
            }
        });
        city_expand_line2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("天津市");
                cExpandView.collapse();
            }
        });
        city_expand_line3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("唐山市");
                cExpandView.collapse();
            }
        });
        city_expand_line4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("沧州市");
                cExpandView.collapse();
            }
        });
        city_expand_line5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("胜芳镇");
                cExpandView.collapse();
            }
        });
        city_expand_line6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("聊城市");
                cExpandView.collapse();
            }
        });
    }

    private void expand(final TextView tv1, final TextView tv2, final TextView tv3, final TextView tv4, final TextView tv, final ExpandView mExpandView) {
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setText(tv1.getText());
                mExpandView.collapse();
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setText(tv2.getText());
                mExpandView.collapse();
            }
        });
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setText(tv3.getText());
                mExpandView.collapse();
            }
        });
        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setText(tv4.getText());
                mExpandView.collapse();
            }
        });
    }

    //提交并进行网络请求
    private void submit() {
        //获取多规格多材质数量
        String guige, caizhi, dunwei;
        if (m == 0) {
            guige = writedang_guige[t].getText().toString();
            caizhi = writedang_caizhi[t].getText().toString() + " ";
            dunwei = writedang_dunwei[t].getText().toString();
        } else {
            guige = mguige + ";" + et_add_guige.getText().toString();
            caizhi = mcaizhi + ";" + et_add_caizhi.getText().toString() + " ";
            dunwei = mshuliang + ";" + et_add_dunwei.getText().toString();
        }
//        String guige=mguige+";"+et_add_guige.getText().toString();
//        Log.e("789","guige+===="+guige);
//        String caizhi=mcaizhi+";"+et_add_caizhi.getText().toString();
//        Log.e("789","caizhi+===="+caizhi);
//        String dunwei=mshuliang+";"+et_add_dunwei.getText().toString();
//        Log.e("789","dunwei+===="+dunwei);
        String pinming = writedang_pinming[t].getText().toString();
//        String guige = writedang_guige[t].getText().toString();
//        String caizhi = writedang_caizhi[t].getText().toString();
//        String dunwei = writedang_dunwei[t].getText().toString();
        String beizhu = writedang_beizhu[t].getText().toString();
        String type = writedang_type[t].getText().toString();
        String target_city = writedang_city[t].getText().toString();
        if (target_city.equals("全部城市")) {
            target_city = "null";
        }

        if (pinming == null | pinming.equals("") | guige == null | guige.equals("") | dunwei == null | dunwei.equals("") | type == null | type.equals("")) {
            Toast.makeText(WriteOrder.this, "请填写完整信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if ("".equals(type)) {
            Toast.makeText(this, "请选择接收对象", Toast.LENGTH_SHORT).show();
            return;
        }
        if ("".equals(target_city)) {
            Toast.makeText(this, "请选择接收城市", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.show();
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("phone_num", userDAO.getUserInfo().getUserPhone()));
        nvps.add(new BasicNameValuePair("product_name", pinming));
        nvps.add(new BasicNameValuePair("standard", guige));
        nvps.add(new BasicNameValuePair("material", caizhi));
        nvps.add(new BasicNameValuePair("ton_amount", dunwei));
        nvps.add(new BasicNameValuePair("comment", beizhu));
        nvps.add(new BasicNameValuePair("acceptor", type));
        nvps.add(new BasicNameValuePair("type_num", getTypeNum(type) + ""));
        nvps.add(new BasicNameValuePair("target_city", target_city));
        Log.e("target_city", "target_city=" + target_city);
        Log.e("012", "nvps==================" + nvps.toString());
        httpUtil.post(mHandler, Internet.WRITE_ORDER_URL, 1, nvps);
    }

    //提交型号分类
    private int getTypeNum(String type) {
        int type_num = 1;
        switch (type) {
            case "工厂接收":
                type_num = 1;
                break;
            case "现货商接收":
                type_num = 2;
                break;
            case "贸易商接收":
                type_num = 3;
                break;
            case "全部接收":
                type_num = 6;
                break;
            default:
                type_num = 1;
                break;

        }
        return type_num;
    }

}
