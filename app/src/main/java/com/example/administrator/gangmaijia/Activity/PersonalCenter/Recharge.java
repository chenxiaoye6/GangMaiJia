package com.example.administrator.gangmaijia.Activity.PersonalCenter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.example.administrator.gangmaijia.MyApplication;
import com.example.administrator.gangmaijia.R;
import com.example.administrator.gangmaijia.View.DialogOrder;

/**
 * Created by Administrator on 2016/10/9.
 */

public class Recharge extends Activity {

    private EditText et_recharge_money ;
    private String photo = "022-86279266" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recharge);
        MyApplication.getInstance().addActivity(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        initview();


    }

    private void initview(){
//        et_recharge_money = (EditText) findViewById(R.id.et_recharge_money);
        findViewById(R.id.tv_recharge).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                final DialogOrder dialogCommon = new DialogOrder(Recharge.this,photo,"请拨打客服电话");
//                dialogCommon.show();
                try {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+photo));
                    Recharge.this.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
