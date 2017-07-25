package com.example.administrator.gangmaijia.Activity.UserInfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.gangmaijia.MyApplication;
import com.example.administrator.gangmaijia.R;
import com.example.administrator.gangmaijia.Util.HttpUtil;
import com.example.administrator.gangmaijia.Util.Internet;
import com.example.administrator.gangmaijia.View.CityExpandView;
import com.example.administrator.gangmaijia.View.LogisticsExpandView;
import com.example.administrator.gangmaijia.View.RoundRectImageView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import id.zelory.compressor.Compressor;

import static com.example.administrator.gangmaijia.R.id.et_registerchangjia_username;

/**
 * Created by Administrator on 2016/11/10.
 */

public class WuLiuRegister extends Activity implements View.OnClickListener {
    private RoundRectImageView iv_registerwuliu_zhizhao;
    private ImageView back;
    private LogisticsExpandView typeExpandView;
    private CityExpandView wuliucityExpandView;
    private EditText et_registerwuliu_companyName, et_registerwuliu_username, et_registerwuliu_companymajor;
    private TextView tv_registerwuliu_type, tv_registerwuliu_city, tv_registerwuliu_complete, logistics_expand_line1, logistics_expand_line2, logistics_expand_line3, logistics_expand_line4,
            city_expand_line1, city_expand_line2, city_expand_line3, city_expand_line4, city_expand_line5, city_expand_line6;
    private LinearLayout ly_registerwuliu_type, ly_registerwuliu_city, ly_registerwuliu_updataPhoto;
    String phone_num, company_name, user_name, major_business, company_type, city_address, business_license;
    int type = 1, type_num = 5;
    private Uri outputFileUri;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    submitResult(msg);
                    break;
            }
        }
    };

    private void submitResult(Message msg) {
        String s = msg.obj.toString();
        Log.e("s", s);
        try {
            JSONObject jsonObject = new JSONObject(s);
            Toast.makeText(WuLiuRegister.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
            //注册成功之后跳转到登录界面
            startActivity(new Intent(WuLiuRegister.this, Login.class));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_wuliu);
        MyApplication.getInstance().addActivity(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //得到电话号码
        phone_num = getIntent().getStringExtra("phone_num");
        initview();
    }

    private void initview() {
        city_expand_line1 = (TextView) findViewById(R.id.city_expand_line1);
        city_expand_line2 = (TextView) findViewById(R.id.city_expand_line2);
        city_expand_line3 = (TextView) findViewById(R.id.city_expand_line3);
        city_expand_line4 = (TextView) findViewById(R.id.city_expand_line4);
        city_expand_line5 = (TextView) findViewById(R.id.city_expand_line5);
        city_expand_line6 = (TextView) findViewById(R.id.city_expand_line6);
        logistics_expand_line1 = (TextView) findViewById(R.id.logistics_expand_line1);
        logistics_expand_line2 = (TextView) findViewById(R.id.logistics_expand_line2);
        logistics_expand_line3 = (TextView) findViewById(R.id.logistics_expand_line3);
        logistics_expand_line4 = (TextView) findViewById(R.id.logistics_expand_line4);

        iv_registerwuliu_zhizhao = (RoundRectImageView) findViewById(R.id.iv_registerwuliu_zhizhao);
        back = (ImageView) findViewById(R.id.back);
        typeExpandView = (LogisticsExpandView) findViewById(R.id.typeExpandView);
        wuliucityExpandView = (CityExpandView) findViewById(R.id.city_wiliu_ExpandView);
        et_registerwuliu_companyName = (EditText) findViewById(R.id.et_registerwuliu_companyName);
        et_registerwuliu_username = (EditText) findViewById(R.id.et_registerwuliu_username);
        et_registerwuliu_companymajor = (EditText) findViewById(R.id.et_registerwuliu_companymajor);
        tv_registerwuliu_type = (TextView) findViewById(R.id.tv_registerwuliu_company);
        tv_registerwuliu_city = (TextView) findViewById(R.id.tv_registerwuliu_city);
        tv_registerwuliu_complete = (TextView) findViewById(R.id.tv_registerwuliu_complete);
        ly_registerwuliu_type = (LinearLayout) findViewById(R.id.ly_registerwuliu_type);
        ly_registerwuliu_city = (LinearLayout) findViewById(R.id.ly_registerwuliu_city);
        ly_registerwuliu_updataPhoto = (LinearLayout) findViewById(R.id.ly_registerwuliu_updataPhoto);


        et_registerwuliu_companyName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });
        et_registerwuliu_username.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });
        et_registerwuliu_companymajor.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });
        //设置监听
        city_expand_line2.setOnClickListener(this);
        city_expand_line3.setOnClickListener(this);
        city_expand_line4.setOnClickListener(this);
        city_expand_line5.setOnClickListener(this);
        city_expand_line6.setOnClickListener(this);
        logistics_expand_line1.setOnClickListener(this);
        logistics_expand_line2.setOnClickListener(this);
        logistics_expand_line3.setOnClickListener(this);
        logistics_expand_line4.setOnClickListener(this);
        ly_registerwuliu_city.setOnClickListener(this);
        ly_registerwuliu_type.setOnClickListener(this);
        back.setOnClickListener(this);
        ly_registerwuliu_updataPhoto.setOnClickListener(this);
        tv_registerwuliu_complete.setOnClickListener(this);

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.ly_registerwuliu_type:

                if (typeExpandView.isExpand()) {
                    typeExpandView.collapse();
                } else {
                    typeExpandView.expand();
                }

                break;
            case R.id.ly_registerwuliu_city:

                if (wuliucityExpandView.isExpand()) {
                    wuliucityExpandView.collapse();
                } else {
                    city_expand_line1.setText("请选择城市");
                    wuliucityExpandView.expand();
                }
                break;
            case R.id.ly_registerwuliu_updataPhoto:
                showSelectDialog();
                break;
            case R.id.tv_registerwuliu_complete:
                submit();
                break;


            //修改
            case R.id.logistics_expand_line1:
                tv_registerwuliu_type.setText(logistics_expand_line1.getText().toString());
                typeExpandView.collapse();
                break;
            case R.id.logistics_expand_line2:
                tv_registerwuliu_type.setText(logistics_expand_line2.getText().toString());
                typeExpandView.collapse();
                break;
            case R.id.logistics_expand_line3:
                tv_registerwuliu_type.setText(logistics_expand_line3.getText().toString());
                typeExpandView.collapse();
                break;
            case R.id.logistics_expand_line4:
                tv_registerwuliu_type.setText(logistics_expand_line4.getText().toString());
                typeExpandView.collapse();
                break;


            case R.id.city_expand_line2:
                tv_registerwuliu_city.setText(city_expand_line2.getText().toString());
                wuliucityExpandView.collapse();
                break;
            case R.id.city_expand_line3:
                tv_registerwuliu_city.setText(city_expand_line3.getText().toString());
                wuliucityExpandView.collapse();
                break;
            case R.id.city_expand_line4:
                tv_registerwuliu_city.setText(city_expand_line4.getText().toString());
                wuliucityExpandView.collapse();
                break;
            case R.id.city_expand_line5:
                tv_registerwuliu_city.setText(city_expand_line5.getText().toString());
                wuliucityExpandView.collapse();
                break;
            case R.id.city_expand_line6:
                tv_registerwuliu_city.setText(city_expand_line6.getText().toString());
                wuliucityExpandView.collapse();
                break;
        }
    }

    //图片处理
    private void showSelectDialog() {
        System.out.println("---------------showSelectDialog-----------------");
        String[] items = getResources().getStringArray(
                R.array.picture_or_camera);
        new AlertDialog.Builder(WuLiuRegister.this)
                .setTitle(R.string.select_which_picture_title)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Intent intent = new Intent(
                                    Intent.ACTION_GET_CONTENT);
                            intent.addCategory(Intent.CATEGORY_OPENABLE);
                            intent.setType("image/*");
                            startActivityForResult(intent,
                                    // Intent.createChooser(intent, "选锟斤拷图片"),
                                    0);
                        } else if (which == 1) {
                            SimpleDateFormat dateFormat = new SimpleDateFormat(
                                    "yyyyMMdd_hhmmss", Locale.getDefault());
                            String str = dateFormat.format(new Date(System
                                    .currentTimeMillis()));
                            File file = new File(
                                    Environment
                                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                                    "/Camera/Img_" + str + ".jpg");
                            outputFileUri = Uri.fromFile(file);
                            System.out.println("---------------file------------" + file);
                            System.out.println("-----------------outputFileUri----------" + outputFileUri);
                            Toast.makeText(WuLiuRegister.this, "正在转码图片...", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(
                                    MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                    outputFileUri);
                            startActivityForResult(intent, 1);
                        }
                    }
                }).create().show();
    }

    private void bitmapFactory(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = Compressor.getDefault(this).compressToBitmap(new File(getRealFilePath(this, uri)));
        } catch (Exception e) {
            Bitmap image;
            Bitmap bitmap1 = null;
            try {
                image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 40, baos);
                ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
                BitmapFactory.Options newOpts = new BitmapFactory.Options();
                newOpts.inTempStorage = new byte[100 * 1024];
                // 设置位图颜色显示优化方式
                newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
                // 4.设置图片可以被回收，创建Bitmap用于存储Pixel的内存空间在系统内存不足时可以被回收
                // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
                newOpts.inJustDecodeBounds = true;
                bitmap1 = BitmapFactory.decodeStream(isBm, null, newOpts);
                newOpts.inJustDecodeBounds = false;
                // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
                int be = 1;// be=1表示不缩放
                newOpts.inSampleSize = be;// 设置缩放比例
                // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
                isBm = new ByteArrayInputStream(baos.toByteArray());
                bitmap1 = BitmapFactory.decodeStream(isBm, null, newOpts);
                isBm.reset();
                baos.reset();
                isBm.close();
                baos.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            bitmap = compressImage(bitmap1, 200);// 压缩好比例大小后再进行质量
            e.printStackTrace();
        }

        iv_registerwuliu_zhizhao.setVisibility(View.VISIBLE);
        iv_registerwuliu_zhizhao.setImageBitmap(bitmap);
        business_license = bitmapToBase64(bitmap);

    }

    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }


    private Bitmap compressImage(Bitmap bm, int size) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 25;
        if (baos.toByteArray().length / 1024 > 1000) {
            options = 5;
        }
        while (baos.toByteArray().length / 1024 > size) { // 循环判断如果压缩后图片是否大于50kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            bm.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中

            options -= 20;// 每次都减少10
            if (options < 0) {
                break;
            }
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }


    // 把Bitmap转换成Base64
    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

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
    //图片处理


    private void submit() {
//        company_name = et_registerchangjia_companyName.getText().toString();
//        user_name = et_registerchangjia_username.getText().toString();
//        major_business = et_registerchangjia_companymajor.getText().toString();
//        city_address = tv_register2_city.getText().toString();
//        company_type = tv_register2_company.getText().toString();
        company_name = et_registerwuliu_companyName.getText().toString();
        user_name = et_registerwuliu_username.getText().toString();
        major_business = tv_registerwuliu_type.getText().toString();
        company_type = et_registerwuliu_companymajor.getText().toString();
        city_address = tv_registerwuliu_city.getText().toString();
        if (company_name == null | company_name.equals("")) {
            Toast.makeText(WuLiuRegister.this, "请输入公司名称", Toast.LENGTH_SHORT).show();
            return;
        }
        if (user_name == null | user_name.equals("")) {
            Toast.makeText(WuLiuRegister.this, "请输入姓名", Toast.LENGTH_SHORT).show();
            return;
        }
        if (major_business == null | major_business.equals("")) {
            Toast.makeText(WuLiuRegister.this, "请选择运输方式", Toast.LENGTH_SHORT).show();
            return;
        }
        if (company_type == null | company_type.equals("")) {
            Toast.makeText(WuLiuRegister.this, "请输入运输城市", Toast.LENGTH_SHORT).show();
            return;
        }
        if (business_license == null | "".equals(business_license)) {
            Toast.makeText(WuLiuRegister.this, "请上传营业执照", Toast.LENGTH_SHORT).show();
            return;
        }
        JSONObject company = new JSONObject();
        try {
            company.put("phone_num", phone_num);
            company.put("company_name", company_name);
            company.put("user_name", user_name);
            company.put("newmajor_business", major_business);
            company.put("city_address", city_address);
            company.put("company_type", company_type);
            company.put("business_license", business_license);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        List<NameValuePair> nvps = new ArrayList<>();
//        nvps.add(new BasicNameValuePair("company",company.toString()));
        nvps.add(new BasicNameValuePair("phone_num", phone_num));
        nvps.add(new BasicNameValuePair("company_name", company_name));
        nvps.add(new BasicNameValuePair("user_name", user_name));
        nvps.add(new BasicNameValuePair("major_business", major_business));
        nvps.add(new BasicNameValuePair("city_address", city_address));
        nvps.add(new BasicNameValuePair("company_type", company_type));
        nvps.add(new BasicNameValuePair("type_num", type_num + ""));
        nvps.add(new BasicNameValuePair("business_license", business_license));
        nvps.add(new BasicNameValuePair("type", type + ""));
        new HttpUtil().post(mHandler, Internet.COMPANY_REGISTER_URL, 1, nvps);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            Uri uri = data.getData();
            bitmapFactory(uri);
        } else if (requestCode == 1) {
            bitmapFactory(outputFileUri);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            WuLiuRegister.this.setResult(1, getIntent());
            WuLiuRegister.this.finish();
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
