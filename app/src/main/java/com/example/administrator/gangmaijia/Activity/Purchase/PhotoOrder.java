package com.example.administrator.gangmaijia.Activity.Purchase;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.widget.MultiPickResultView;

/**
 * Created by Administrator on 2016/10/9.
 */

public class PhotoOrder extends Activity implements View.OnClickListener {

    private MultiPickResultView recyclerView;
    private ImageView iv_photoorder_type, iv_photoorder_city;
    private TextView tv_photoorder_add, tv_photoorder_upload, tv_photoorder_type, tv_photoorder_city, city_expand_line1, city_expand_line2, city_expand_line3, city_expand_line4, city_expand_line5, city_expand_line6;
    private ExpandView mExpandView;
    private CityExpandView cExpandView;
    private TextView expand_line1, expand_line2, expand_line3, expand_line4;
    private LinearLayout ly_photoorder;
    private int t = 0, tmax = 5, p = 0;
    private MultiPickResultView[] recycler = new MultiPickResultView[tmax];
    private TextView[] photoorder_type = new TextView[tmax];
    private TextView[] photoorder_city = new TextView[tmax];
    private UserDAO userDAO = new UserDAO(this);
    private HttpUtil httpUtil = new HttpUtil();
    private int type = 1;
    private ProgressDialog progressDialog;

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
                        Log.e("123", jsonObject.toString() + "=========================");
                        Toast.makeText(PhotoOrder.this, msg1, Toast.LENGTH_SHORT).show();
                        if (jsonObject.getString("result").equals("0")) {
                            if (type == 2) {
                                recycler[t].setEnabled(false);
                                recycler[t].setFocusable(false);
                                t++;
                                findViewById(R.id.view1).setVisibility(View.GONE);
                                findViewById(R.id.view2).setVisibility(View.GONE);
                                add();
                            } else {
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photoorder);
        MyApplication.getInstance().addActivity(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        initview();


    }

    private void initview() {
        recyclerView = (MultiPickResultView) findViewById(R.id.recycler_view);
        recyclerView.init(this, MultiPickResultView.ACTION_SELECT, null);
        tv_photoorder_add = (TextView) findViewById(R.id.tv_photoorder_add);
        tv_photoorder_upload = (TextView) findViewById(R.id.tv_photoorder_upload);
        tv_photoorder_type = (TextView) findViewById(R.id.tv_photoorder_type);
        tv_photoorder_city = (TextView) findViewById(R.id.tv_photoorder_city);
        iv_photoorder_type = (ImageView) findViewById(R.id.iv_photoorder_type);
        mExpandView = (ExpandView) findViewById(R.id.expandView);
        cExpandView = (CityExpandView) findViewById(R.id.cExpandView);
        expand_line1 = (TextView) findViewById(R.id.expand_line1);
        expand_line2 = (TextView) findViewById(R.id.expand_line2);
        expand_line3 = (TextView) findViewById(R.id.expand_line3);
        expand_line4 = (TextView) findViewById(R.id.expand_line4);
        city_expand_line1 = (TextView) findViewById(R.id.city_expand_line1);
        city_expand_line2 = (TextView) findViewById(R.id.city_expand_line2);
        city_expand_line3 = (TextView) findViewById(R.id.city_expand_line3);
        city_expand_line4 = (TextView) findViewById(R.id.city_expand_line4);
        city_expand_line5 = (TextView) findViewById(R.id.city_expand_line5);
        city_expand_line6 = (TextView) findViewById(R.id.city_expand_line6);
        ly_photoorder = (LinearLayout) findViewById(R.id.ly_photoorder);
        iv_photoorder_city = (ImageView) findViewById(R.id.iv_photoorder_city);

        recycler[0] = recyclerView;
        photoorder_type[0] = tv_photoorder_type;
        photoorder_city[0] = tv_photoorder_city;


        tv_photoorder_add.setOnClickListener(this);
        iv_photoorder_city.setOnClickListener(this);
        iv_photoorder_type.setOnClickListener(this);
        tv_photoorder_upload.setOnClickListener(this);
        expand(expand_line1, expand_line2, expand_line3, expand_line4, tv_photoorder_type, mExpandView);
        expand(city_expand_line1, city_expand_line2, city_expand_line3, city_expand_line4, city_expand_line5, city_expand_line6, tv_photoorder_city, cExpandView);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在提交订单...");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        recycler[t].onActivityResult(requestCode, resultCode, data);
        if (t == 0) {
            int size = recyclerView.getPhotos().size();
            if (size >= 4) {
                findViewById(R.id.view1).setVisibility(View.GONE);
            } else {
                findViewById(R.id.view1).setVisibility(View.VISIBLE);
            }
            if (size >= 8) {
                findViewById(R.id.view2).setVisibility(View.GONE);
            } else {
                findViewById(R.id.view2).setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_photoorder_type:
                if (mExpandView.isExpand()) {
                    mExpandView.collapse();
                } else {
                    mExpandView.expand();
                }
                break;
            case R.id.iv_photoorder_city:
                if (cExpandView.isExpand()) {
                    cExpandView.collapse();
                } else {
                    cExpandView.expand();
                }
                break;
            case R.id.tv_photoorder_add:
                type = 2;
                showdialog();

                break;
            case R.id.tv_photoorder_upload:
                type = 1;
                submit();
                break;
        }
    }

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

    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(result);
        return result;
    }

    private void add() {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(PhotoOrder.this).inflate(R.layout.photoorder_item, null);
        MultiPickResultView recyclerView = (MultiPickResultView) layout.findViewById(R.id.recycler_view);
        recyclerView.init(this, MultiPickResultView.ACTION_SELECT, null);
        recycler[t] = recyclerView;
        TextView expand_line1 = (TextView) layout.findViewById(R.id.expand_line1);
        TextView expand_line2 = (TextView) layout.findViewById(R.id.expand_line2);
        TextView expand_line3 = (TextView) layout.findViewById(R.id.expand_line3);
        TextView expand_line4 = (TextView) layout.findViewById(R.id.expand_line4);
        TextView city_expand_line1 = (TextView) findViewById(R.id.city_expand_line1);
        TextView city_expand_line2 = (TextView) findViewById(R.id.city_expand_line2);
        TextView city_expand_line3 = (TextView) findViewById(R.id.city_expand_line3);
        TextView city_expand_line4 = (TextView) findViewById(R.id.city_expand_line4);
        TextView city_expand_line5 = (TextView) findViewById(R.id.city_expand_line5);
        TextView city_expand_line6 = (TextView) findViewById(R.id.city_expand_line6);
        photoorder_type[t] = (TextView) layout.findViewById(R.id.tv_photoorder_type);
        photoorder_city[t] = (TextView) layout.findViewById(R.id.tv_photoorder_city);
        final ExpandView mExpandView = (ExpandView) layout.findViewById(R.id.expandView);
        final CityExpandView cExpandView = (CityExpandView) layout.findViewById(R.id.cExpandView);
        ImageView iv_photoorder_type = (ImageView) layout.findViewById(R.id.iv_photoorder_type);
        ImageView iv_photoorder_city = (ImageView) layout.findViewById(R.id.iv_photoorder_city);
        iv_photoorder_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mExpandView.isExpand()) {
                    mExpandView.collapse();
                } else {
                    mExpandView.expand();
                }
            }
        });
        iv_photoorder_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cExpandView.isExpand()) {
                    cExpandView.collapse();
                } else {
                    cExpandView.expand();
                }
            }
        });

        ly_photoorder.addView(layout);
        expand(expand_line1, expand_line2, expand_line3, expand_line4, photoorder_type[t], mExpandView);
        expand(city_expand_line1, city_expand_line2, city_expand_line3, city_expand_line4, city_expand_line5, city_expand_line6, photoorder_city[t], cExpandView);
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

    private void expand(final TextView tv1, final TextView tv2, final TextView tv3, final TextView tv4, final TextView tv5, final TextView tv6, final TextView tv, final CityExpandView mExpandView) {
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
        tv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setText(tv5.getText());
                mExpandView.collapse();
            }
        });
        tv6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setText(tv6.getText());
                mExpandView.collapse();
            }
        });
    }

    private void submit() {
        ArrayList<String> photos = recycler[t].getPhotos();
        if (photos.size() == 0) {
            Toast.makeText(PhotoOrder.this, "请添加图片", Toast.LENGTH_SHORT).show();
            return;
        }
        String acceptor = photoorder_type[t].getText().toString();
        String target_city = photoorder_city[t].getText().toString();
        Log.e("123", "city=+===========" + target_city);
        if (target_city.equals("全部城市")) {
            target_city = "null";
        }
        if ("".equals(acceptor)) {
            Toast.makeText(this, "请选择接收对象", Toast.LENGTH_SHORT).show();
            return;
        }
        if ("".equals(target_city)) {
            Toast.makeText(this, "请选择接收城市", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.show();
        JSONObject photo_path = new JSONObject();

        for (int i = 0; i < photos.size(); i++) {
            com.orhanobut.logger.Logger.e(photos.get(i));
            try {
                Bitmap bitmap = BitmapFactory.decodeFile(photos.get(i));
                String s1 = bitmapToBase64(bitmap);
                photo_path.put(i + "", s1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("phone_num", userDAO.getUserInfo().getUserPhone()));
        nvps.add(new BasicNameValuePair("acceptor", acceptor));
        nvps.add(new BasicNameValuePair("type_num", getTypeNum(acceptor) + ""));
        nvps.add(new BasicNameValuePair("photo_path", photo_path.toString()));
        nvps.add(new BasicNameValuePair("target_city", target_city));
        httpUtil.post(mHandler, Internet.INSERT_PHOTO_URL, 1, nvps);

    }

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
