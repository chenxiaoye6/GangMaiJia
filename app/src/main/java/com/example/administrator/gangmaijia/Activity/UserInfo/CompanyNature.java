package com.example.administrator.gangmaijia.Activity.UserInfo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.administrator.gangmaijia.Adapter.CityAdapter;
import com.example.administrator.gangmaijia.Model.Province;
import com.example.administrator.gangmaijia.MyApplication;
import com.example.administrator.gangmaijia.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/9.
 */

public class CompanyNature extends Activity {

    private ListView lv_nature ;
    private CityAdapter adapter ;
    private List<Province> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.companynature);
        MyApplication.getInstance().addActivity(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        initview();
        getList();
        adapter = new CityAdapter(this,list);
        lv_nature.setAdapter(adapter);


    }

    private void initview() {
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        lv_nature = (ListView) findViewById(R.id.lv_nature);
        lv_nature.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getIntent().putExtra("type",position+1);
                getIntent().putExtra("company_type",list.get(position).getProvinceName());
                CompanyNature.this.setResult(2,getIntent());
                CompanyNature.this.finish();
            }
        });
    }

    private void getList(){
        Province province1 = new Province();
        province1.setProvinceName("工厂");
        list.add(province1);
        Province province2 = new Province();
        province2.setProvinceName("现货商");
        list.add(province2);
        Province province3 = new Province();
        province3.setProvinceName("贸易商");
        list.add(province3);
        Province province4 = new Province();
        province4.setProvinceName("加工商");
        list.add(province4);
        Province province5 = new Province();
        province5.setProvinceName("物流公司");
        list.add(province5);
    }

}
