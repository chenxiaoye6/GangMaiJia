package com.example.administrator.gangmaijia.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.gangmaijia.Activity.InteractiveActivity;
import com.example.administrator.gangmaijia.Activity.PersonalCenter.AllOrder;
import com.example.administrator.gangmaijia.Activity.PersonalCenter.GoodBusinessAvtivity;
import com.example.administrator.gangmaijia.Activity.Purchase.PhotoOrder;
import com.example.administrator.gangmaijia.Activity.Purchase.WriteOrder;
import com.example.administrator.gangmaijia.Activity.Setting.setting;
import com.example.administrator.gangmaijia.Activity.WebActivity;
import com.example.administrator.gangmaijia.DAO.UserDAO;
import com.example.administrator.gangmaijia.Model.User;
import com.example.administrator.gangmaijia.R;

/**
 * Created by Administrator on 2016/9/28.
 */
public class ZhaoHuo extends Fragment implements View.OnClickListener {

    private View view;
    private ImageView iv_zhaohuo_setting;
    private TextView tv_zhaohuo_mydingdan, tv_zhaohuo_xiedan, tv_zhaohuo_paidan, tv_zhaohuo_goodbusiness;
    private RelativeLayout zhaohuo_xiedan_relativeLayout, zhaohuo_paidan_relativeLayout;
    private Intent it;
    private UserDAO userDAO = new UserDAO(getActivity());
    private User user;
    private TextView rl;
    private TextView xianhuo;
    private TextView textView11;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.zhaohuo, container, false);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        user = userDAO.getUserInfo();
        initview();
        if ("2".equals(user.getUserType())) {
            tv_zhaohuo_mydingdan.setText("我的订单");
        }
        return view;
    }

    private void initview() {
        rl = (TextView) view.findViewById(R.id.rl);
        tv_zhaohuo_goodbusiness = (TextView) view.findViewById(R.id.tv_zhaohuo_goodbusiness);
        iv_zhaohuo_setting = (ImageView) view.findViewById(R.id.iv_zhaohuo_setting);
        tv_zhaohuo_mydingdan = (TextView) view.findViewById(R.id.tv_zhaohuo_mydingdan);
        tv_zhaohuo_xiedan = (TextView) view.findViewById(R.id.tv_zhaohuo_xiedan);
        tv_zhaohuo_paidan = (TextView) view.findViewById(R.id.tv_zhaohuo_paidan);
        textView11 = (TextView) view.findViewById(R.id.textView11);
        zhaohuo_xiedan_relativeLayout = (RelativeLayout) view.findViewById(R.id.zhaohuo_xiedan_relativeLayout);
        zhaohuo_paidan_relativeLayout = (RelativeLayout) view.findViewById(R.id.zhaohuo_paidan_relativeLayout);
        xianhuo = (TextView) view.findViewById(R.id.xianhuo);
        rl.setOnClickListener(this);
        zhaohuo_paidan_relativeLayout.setOnClickListener(this);
        tv_zhaohuo_mydingdan.setOnClickListener(this);
        zhaohuo_xiedan_relativeLayout.setOnClickListener(this);
        iv_zhaohuo_setting.setOnClickListener(this);
        tv_zhaohuo_goodbusiness.setOnClickListener(this);
        xianhuo.setOnClickListener(this);
        textView11.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);
                //uri:统一资源标示符（更广）
                intent.setData(Uri.parse("tel:" + "02226224846"));
                //开启系统拨号器
                startActivity(intent);

                break;
            case R.id.iv_zhaohuo_setting:
                it = new Intent(getActivity(), setting.class);
                startActivity(it);
                break;
            case R.id.zhaohuo_xiedan_relativeLayout:
                it = new Intent(getActivity(), WriteOrder.class);
                startActivity(it);
                break;
            case R.id.zhaohuo_paidan_relativeLayout:
                it = new Intent(getActivity(), PhotoOrder.class);
                startActivity(it);
                break;
            case R.id.tv_zhaohuo_mydingdan:
//                it=new Intent(getActivity(), MyOrder.class);
                user = userDAO.getUserInfo();
                Bundle bundle = new Bundle();
                bundle.putString("type", user.getUserId());
                it = new Intent(getActivity(), AllOrder.class);
                it.putExtras(bundle);
                startActivity(it);
                break;
            //跳转到诚信企业界面
            case R.id.tv_zhaohuo_goodbusiness:
                it = new Intent(getActivity(), GoodBusinessAvtivity.class);
                startActivity(it);
                break;
            case R.id.xianhuo:

                it = new Intent(getActivity(), WebActivity.class);
                it.putExtra("url", "http://www.dqzgg.com/sjcx.aspx");
                startActivity(it);
                break;
            case R.id.textView11:
                it = new Intent(getActivity(), InteractiveActivity.class);
//                it.putExtra("url","http://www.dqzgg.com/minfo.aspx");
                startActivity(it);
                break;
        }
    }
}
