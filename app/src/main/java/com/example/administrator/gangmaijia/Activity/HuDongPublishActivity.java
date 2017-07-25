package com.example.administrator.gangmaijia.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bigkoo.pickerview.OptionsPickerView;
import com.example.administrator.gangmaijia.DAO.UserDAO;
import com.example.administrator.gangmaijia.Model.AddressBean;
import com.example.administrator.gangmaijia.R;
import com.example.administrator.gangmaijia.Util.Internet;
import com.example.administrator.gangmaijia.Util.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class HuDongPublishActivity extends AppCompatActivity {

    @InjectView(R.id.tv_publish_choosecity)
    TextView tvPublishChoosecity;
    @InjectView(R.id.textView18)
    TextView textView18;
    @InjectView(R.id.tv_publish_choosetype)
    TextView tvPublishChoosetype;
    @InjectView(R.id.et_publish_content)
    EditText etPublishContent;
    @InjectView(R.id.ll)
    ConstraintLayout ll;
    private ArrayList<AddressBean> addressBean;
    private ArrayList<ArrayList<String>> cityList;
    private ArrayList<String> citys;
    ArrayList<String> typeList = new ArrayList<>();
    private String province = "";
    private String city = "";
    private String type = "";
    private UserDAO userDAO = new UserDAO(this);
    private String phone_num;
    public AMapLocationClient mLocationClient = null;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    private ArrayList<String> citym;
    private String p;
    private String c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hu_dong_publish);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        ButterKnife.inject(this);
        phone_num = userDAO.getUserInfo().getUserPhone();

        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        setUpMap();
        initCityAndType();

    }

    //初始化城市数据和类别数据
    private void initCityAndType() {
        String JsonData = Utils.getJson(this, "city.json");//获取assets目录下的json文件数据
        Gson gson = new Gson();
        addressBean = gson.fromJson(JsonData, new TypeToken<ArrayList<AddressBean>>() {
        }.getType());
        //海外放最后
        cityList = new ArrayList<>();
        for (AddressBean address : addressBean
                ) {
            citys = new ArrayList<>();
            for (AddressBean.CityBean city : address.getCity()
                    ) {
                citys.add(city.getName());
            }
            cityList.add(citys);
        }
        //招聘，求租 ，出租，求职，通知，出售，广告
        typeList.add("招聘");
        typeList.add("求租");
        typeList.add("出租");
        typeList.add("求职");
        typeList.add("通知");
        typeList.add("出售");
        typeList.add("广告");
    }

    @OnClick({R.id.back, R.id.tv_publish_choosecity, R.id.tv_publish_choosetype, R.id.btn_publish})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_publish_choosecity:
//                ShowPickerView();
                p = province;
                c = city;
                ProvincePopWindow provincePopWindow = new ProvincePopWindow(this);
                provincePopWindow.showAtLocation(ll, Gravity.CENTER, 0, 0);
                break;
            case R.id.tv_publish_choosetype:
                ShowPickerView2();
                break;
            case R.id.btn_publish:
                Log.e("aaa",
                        "(HuDongPublishActivity.java:141)" + province + "===" + city);
                final ProgressDialog p = new ProgressDialog(this);
                p.show();
                p.setMessage("正在上传");
                if (TextUtils.isEmpty(etPublishContent.getText().toString())) {
                    Toast.makeText(this, "请填写完整信息", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (tvPublishChoosetype.getText().equals("请选择")) {
                    Toast.makeText(this, "请填写", Toast.LENGTH_SHORT).show();
                }
                OkHttpUtils.post()
                        .url(Internet.HUDONGPUBLISH)
                        .addParams("phone_num", phone_num)
                        .addParams("content", etPublishContent.getText().toString())
                        .addParams("ly_city", province)
                        .addParams("category", type)
                        .addParams("area", city)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Request request, Exception e) {

                            }

                            @Override
                            public void onResponse(String response) {
                                Log.e("aaa",
                                        "(HuDongPublishActivity.java:124)" + response);
                                try {
                                    JSONObject json = new JSONObject(response);
                                    Toast.makeText(HuDongPublishActivity.this, json.getString("msg"), Toast.LENGTH_SHORT).show();
                                    if (response.contains("成功")) {
                                        finish();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                break;
        }
    }
//
//    private void ShowPickerView() {// 弹出选择器
//
//        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
//
//            @Override
//            public void onOptionsSelect(int options1, int options2, int options3, View v) {
//                //返回的分别是三个级别的选中位置
//                province = addressBean.get(options1).getPickerViewText();
//                city = cityList.get(options1).get(options2);
//                tvPublishChoosecity.setText(city);
//                if ("全境".equals(province)) {
//                    province = "";
//                }
//                if ("全境".equals(city)) {
//                    city = "";
//                }
//
//            }
//        })
//
//                .setTitleText("城市选择")
//                .setDividerColor(Color.BLACK)
//                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
//                .setContentTextSize(20)
//                .setOutSideCancelable(false)// default is true
//                .build();
//        pvOptions.setPicker(addressBean, cityList);//二级选择器
//        pvOptions.show();
//    }

    private void ShowPickerView2() {// 弹出选择器

        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                type = typeList.get(options1);
                tvPublishChoosetype.setText(type);
            }
        })
                .setTitleText("类别选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .setOutSideCancelable(false)// default is true
                .build();
        pvOptions.setPicker(typeList);//一级选择器
        pvOptions.show();
    }


    /**
     * 配置定位参数
     */
    private void setUpMap() {
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
//        mLocationOption.setInterval(0);
        mLocationOption.setOnceLocation(true);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }


    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //定位成功回调信息，设置相关消息
                    amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                    amapLocation.getLatitude();//获取纬度
                    amapLocation.getLongitude();//获取经度
                    amapLocation.getAccuracy();//获取精度信息
                    amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                    amapLocation.getCountry();//国家信息
                    amapLocation.getProvince();//省信息
                    amapLocation.getCity();//城市信息
                    amapLocation.getDistrict();//城区信息
                    amapLocation.getStreet();//街道信息
                    amapLocation.getStreetNum();//街道门牌号信息
                    amapLocation.getCityCode();//城市编码
                    amapLocation.getAdCode();//地区编码
                    amapLocation.getAoiName();//获取当前定位点的AOI信息
                    province = amapLocation.getProvince();
                    city = amapLocation.getCity();
                    Log.e("aaa",
                            "(InteractiveActivity.java:221)" + amapLocation.getCity());
                    tvPublishChoosecity.setText(city);
                } else {
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    Log.e("aaa",
                            "(InteractiveActivity.java:253)" + amapLocation.getErrorCode() + "====" + amapLocation.getErrorInfo());
                }
            }
        }
    };


    class ProvincePopWindow extends PopupWindow {
        private View conentView;
        private CityAdapter cityAdapter;

        public ProvincePopWindow(final Activity context) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            conentView = inflater.inflate(R.layout.city_choose_item, null);
            ListView provincem = (ListView) conentView.findViewById(R.id.lv_province);
            final ListView cityz = (ListView) conentView.findViewById(R.id.lv_city);

            ProvinceAdapter madapter = new ProvinceAdapter();
            provincem.setAdapter(madapter);

            citym = cityList.get(0);
            cityAdapter = new CityAdapter();
            cityz.setAdapter(cityAdapter);
            provincem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    c = "";
                    citym = cityList.get(position);
                    if ("全境".equals(addressBean.get(position).getName())) {
                        p = "";
                    } else {
                        p = addressBean.get(position).getName();
                    }
                    cityAdapter = new CityAdapter();
                    cityz.setAdapter(cityAdapter);
                    cityz.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if ("全境".equals(citym.get(position))) {
                                c = "";
                            } else {
                                c = citym.get(position);
                            }
                        }
                    });
                }
            });
            //确定和取消的监听事件
            conentView.findViewById(R.id.btn_congif).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    province = p;
                    city = c;
                    tvPublishChoosecity.setText(city);
                    dismiss();
                }
            });
            conentView.findViewById(R.id.btn_cancle).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            int h = context.getWindowManager().getDefaultDisplay().getHeight();
            int w = context.getWindowManager().getDefaultDisplay().getWidth();
            // 设置SelectPicPopupWindow的View
            this.setContentView(conentView);
            // 设置SelectPicPopupWindow弹出窗体的宽
            this.setWidth(w - 200);
            // 设置SelectPicPopupWindow弹出窗体的高
            this.setHeight(h - 200);
            // 设置SelectPicPopupWindow弹出窗体可点击
            this.setFocusable(true);
            this.setOutsideTouchable(true);
            // 刷新状态
            this.update();
            // 实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0000000000);
            // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
            this.setBackgroundDrawable(dw);
            // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
            // 设置SelectPicPopupWindow弹出窗体动画效果
            this.setAnimationStyle(R.style.AnimationPreview);
        }
    }

    class ProvinceAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return addressBean.size();
        }

        @Override
        public Object getItem(int position) {
            return addressBean.get(position).getName();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = View.inflate(HuDongPublishActivity.this, R.layout.provinceitem, null);
            final TextView tv = (TextView) convertView.findViewById(R.id.tv);
            tv.setText(addressBean.get(position).getName());
            return convertView;
        }
    }

    class CityAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return citym.size();
        }

        @Override
        public Object getItem(int position) {
            return citym.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = View.inflate(HuDongPublishActivity.this, R.layout.cityitem, null);
            final TextView tv = (TextView) convertView.findViewById(R.id.tv);
            tv.setText(citym.get(position));
            return convertView;
        }
    }
}
