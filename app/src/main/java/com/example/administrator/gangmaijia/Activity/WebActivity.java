package com.example.administrator.gangmaijia.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.gangmaijia.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class WebActivity extends AppCompatActivity {

    @InjectView(R.id.web)
    WebView web;
    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_title)
    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().startSync();
        CookieManager.getInstance().removeSessionCookie();
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        ButterKnife.inject(this);
        String url = getIntent().getStringExtra("url");
        if (url.contains("sjcx")) {
            tvTitle.setText("现货查询");
        } else {
            tvTitle.setText("互动平台");
        }
        web.loadUrl(url);
        WebSettings settings = web.getSettings();
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setDisplayZoomControls(false);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);//设置js可以直接打开窗口，如window.open()，默认为false
        settings.setJavaScriptEnabled(true);// 是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
        settings.setSupportZoom(true);// 是否可以缩放，默认true
        settings.setBuiltInZoomControls(true);// 是否显示缩放按钮，默认false
        settings.setUseWideViewPort(true);// 设置此属性，可任意比例缩放。大视图模式
        settings.setLoadWithOverviewMode(true);// 和setUseWideViewPort(true)一起解决网页自适应问题
        settings.setAppCacheEnabled(true);// 是否使用缓存
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);//不使用缓存
        settings.setDomStorageEnabled(true);// DOM Storage
        settings.setUserAgentString("User-Agent:Android");//设置用户代理，一般不用
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }

    public final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void showSource(String html) {
            System.out.println("====>html=" + html);
        }

        @JavascriptInterface
        public void showDescription(String str) {
            Log.e("789", "aaa + ");
            System.out.println("====>html=" + str);
            Log.e("789", str);
        }
    }
}
