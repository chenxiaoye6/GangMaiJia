package com.example.administrator.gangmaijia.View;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.administrator.gangmaijia.R;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Administrator on 2016/10/10.
 */

public class ImageShow extends Activity {

    private PhotoView imageshow ;
    private Intent intent ;
    private PhotoViewAttacher mAttacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imageshow);

        imageshow = (PhotoView) findViewById(R.id.imageshow);
        intent = getIntent();
        String url = intent.getStringExtra("url");
        Glide.with(this).load(url).into(imageshow);
        mAttacher = new PhotoViewAttacher(imageshow);
        mAttacher.update();


    }
}
