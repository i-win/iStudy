package com.iwin.istudy.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iwin.istudy.R;

/**
 * Created by sxq on 2016/4/18.
 */
public class NotifyLayout extends LinearLayout {

    private TextView tvNotify;
    private LinearLayout notifyContainer;

    public NotifyLayout(Context context) {
        super(context);

        View view = LayoutInflater.from(context).inflate(R.layout.windows_notify, null);
        tvNotify = (TextView) view.findViewById(R.id.tv_notify);
        notifyContainer = (LinearLayout) view.findViewById(R.id.notify_container);
        this.addView(view);
    }

    /**
     * 设置通知气泡的内容
     * @param text 要通知的内容
     */
    public void setTvNotifyText(CharSequence text){
        tvNotify.setText(text);
    }

    /**
     * 设置通知气泡的显示
     * @param visiable {@value VISIBLE,GONE,INVISIBLE}
     */
    public void setNotifyVisiable(int visiable){
       /* TranslateAnimation animation;
        if (visiable == VISIBLE){
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF,
                    0.0f, Animation.RELATIVE_TO_SELF, -1.0f);
            animation.setDuration(500);
            notifyContainer.startAnimation(animation);

        } else if (visiable == GONE){
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                    0.0f, Animation.RELATIVE_TO_SELF, -1.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                    -1.0f);
            animation.setDuration(1000);
            notifyContainer.startAnimation(animation);
        }*/
        notifyContainer.setVisibility(visiable);
    }

    /**
     * 返回当前控件是否可见
     * @return visiable {@value VISIBLE,GONE,INVISIBLE}
     */
    public int getNotifyVisiable(){
        return notifyContainer.getVisibility();
    }

    public int getNotityWidth(){
        return notifyContainer.getWidth()==0 ? 350 : notifyContainer.getWidth();
    }

    public int getNotifyHeight(){
        return notifyContainer.getHeight()==0 ? 300 : notifyContainer.getWidth();
    }

    public void setBackground(int res){

        notifyContainer.setBackgroundResource(res);
    }
}
