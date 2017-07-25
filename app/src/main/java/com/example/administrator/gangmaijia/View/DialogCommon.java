package com.example.administrator.gangmaijia.View;

/**
 * Created by Administrator on 2016/8/8.
 */
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.administrator.gangmaijia.R;

//import static com.igexin.push.core.g.R;

public class DialogCommon extends Dialog {

    private Context context;
    private String title;
    private String confirmButtonText;
    private String cacelButtonText;
    private ClickListenerInterface clickListenerInterface;
    private TextView tvTitle;
    private TextView tvConfirm;
    private TextView tvCancel;

               public interface ClickListenerInterface {

                        public void doConfirm();

                        public void doCancel();
            }

                public DialogCommon(Context context, String title, String confirmButtonText, String cacelButtonText) {
                super(context, R.style.Dialog);
                this.context = context;
                this.title = title;
                this.confirmButtonText = confirmButtonText;
                this.cacelButtonText = cacelButtonText;
            }

                @Override
        protected void onCreate(Bundle savedInstanceState) {
                // TODO Auto-generated method stub
               super.onCreate(savedInstanceState);

                init();
            }

                public void init() {
                LayoutInflater inflater = LayoutInflater.from(context);
                View view = inflater.inflate(R.layout.dialog_common, null);
                setContentView(view);

                tvTitle = (TextView) view.findViewById(R.id.dialog_common_title);
                tvConfirm = (TextView) view.findViewById(R.id.dialog_common_true);
                tvCancel = (TextView) view.findViewById(R.id.dialog_common_false);

                tvTitle.setText(title);
                tvConfirm.setText(confirmButtonText);
                tvCancel.setText(cacelButtonText);

                tvConfirm.setOnClickListener(new clickListener());
                tvCancel.setOnClickListener(new clickListener());

                Window dialogWindow = getWindow();
                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
                lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
                dialogWindow.setAttributes(lp);
            }

                public void setClicklistener(ClickListenerInterface clickListenerInterface) {
                this.clickListenerInterface = clickListenerInterface;
            }

                private class clickListener implements View.OnClickListener {
               @Override
                public void onClick(View v) {
                        // TODO Auto-generated method stub
                        int id = v.getId();
                        switch (id) {
                            case R.id.dialog_common_true:
                                    clickListenerInterface.doConfirm();
                                    break;
                            case R.id.dialog_common_false:
                                tvConfirm.setBackgroundResource(R.color.white);
                                tvConfirm.setTextColor(context.getResources().getColor(R.color.gray));
                                tvCancel.setBackgroundResource(R.drawable.backgroundcase_blue);
                                tvCancel.setTextColor(context.getResources().getColor(R.color.white));
                                clickListenerInterface.doCancel();
                                    break;
                            }
                    }

                    };

            }