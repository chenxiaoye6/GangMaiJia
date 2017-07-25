package com.example.administrator.gangmaijia.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.administrator.gangmaijia.R;
import com.example.administrator.gangmaijia.Util.Internet;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

//import static com.igexin.push.core.g.R;

/**
 * Created by Administrator on 2016/9/6.
 */
public class ShowPhotoActivity extends Activity implements ViewPager.OnPageChangeListener {

    private ArrayList<View> listViews = null;
    private ArrayList<String> photo ;
    private ViewPager viewPager;
    private ArrayList<View> mImageViews;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showphtot);

        try {
            Intent intent = getIntent();
            int id = intent.getIntExtra("ID", 0);
            photo = intent.getStringArrayListExtra("photo");

            viewPager = (ViewPager) findViewById(R.id.viewPager);


            mImageViews = new ArrayList<>();
            for (int i = 0;i<photo.size();i++){
                PhotoView img = new PhotoView(this);// 构造textView对象
                try {
                    System.out.println(Internet.BASE_URL+photo.get(i));
                    com.orhanobut.logger.Logger.e(Internet.BASE_URL+photo.get(i));
                    Glide.with(ShowPhotoActivity.this).load(Internet.BASE_URL+photo.get(i)).into(img);
                    PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(img);
                    photoViewAttacher.update();
                    mImageViews.add(img);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //设置Adapter
            viewPager.setAdapter(new MyAdapter());
            //设置监听，主要是设置点点的背景
//        viewPager.setOnPageChangeListener(this);
            //设置ViewPager的默认项, 设置为长度的100倍，这样子开始就能往左滑动
            viewPager.setCurrentItem((mImageViews.size()) * 100);
            viewPager.setCurrentItem(id);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }





    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {// 滑动中。。。

    }

    @Override
    public void onPageSelected(int position) {// 页面选择响应函数

    }

    @Override
    public void onPageScrollStateChanged(int state) {/// 滑动状态改变
        System.out.println("-------------state-------------"+state);

    }

    public class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return photo.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager)container).removeView(mImageViews.get(position));

        }


        @Override
        public Object instantiateItem(View container, int position) {
            ((ViewPager)container).addView(mImageViews.get(position));
            return mImageViews.get(position);
        }



    }



}
