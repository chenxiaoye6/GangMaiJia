package com.example.administrator.gangmaijia.Activity.PersonalCenter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.gangmaijia.R;

/**
 * Created by Administrator on 2016/11/8.
 */

public class GoodBusinessDetail extends Activity {
    private TextView goodbusiness_name, goodbusiness_major_business, goodbusiness_phone_num, goodbusiness_city_address, tv_goodbusiness_detail;
    private ImageView back;
    private String phone_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goodbusinessshow);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        initView();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoodBusinessDetail.this.finish();
            }
        });
    }

    private void initView() {
        back = (ImageView) findViewById(R.id.back);
        goodbusiness_name = (TextView) findViewById(R.id.goodbusiness_name);
        goodbusiness_major_business = (TextView) findViewById(R.id.goodbusiness_major_business);
        goodbusiness_phone_num = (TextView) findViewById(R.id.goodbusiness_phone_num);
        goodbusiness_city_address = (TextView) findViewById(R.id.goodbusiness_city_address);
        tv_goodbusiness_detail = (TextView) findViewById(R.id.tv_goodbusiness_detail);
        //bundle 接收值
        Bundle mBundle = this.getIntent().getExtras();
        String name = mBundle.getString("name");
        String major_business = mBundle.getString("major_business");
        phone_num = mBundle.getString("phone_num");
        String city_address = mBundle.getString("city_address");
        String detail = mBundle.getString("introduction");
        Log.e("123", mBundle.toString());

        goodbusiness_name.setText(name);
        goodbusiness_major_business.setText(major_business);
        goodbusiness_phone_num.setText(phone_num);
        goodbusiness_city_address.setText(city_address);
        tv_goodbusiness_detail.setText(detail);
        goodbusiness_phone_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + phone_num));
                startActivity(intent);
            }
        });

    }
}
