package com.example.administrator.gangmaijia.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.administrator.gangmaijia.R;
import com.example.administrator.gangmaijia.Util.Internet;

import java.util.List;

/**
 * Created by 张宝 on 2016/8/27.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
//    private Internet inter = new Internet();

    public ImageAdapter(Context c, List<String> photoUrlList) {
        mContext = c;
        this.photoUrlList = photoUrlList;
    }

    public int getCount() {
        return photoUrlList.size();
    }

    public Object getItem(int position) {
        return photoUrlList.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_image_view, null);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);

//            viewHolder.imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            viewHolder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            viewHolder.imageView.setPadding(8, 8, 8, 8);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Glide.with(mContext).load(Internet.BASE_URL + photoUrlList.get(position)).into(viewHolder.imageView);

//        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                System.out.println("------------View------------"+v.getId());
//            }
//        });

        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
    }

    // references to our images
    private List<String> photoUrlList;
}
