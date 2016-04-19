package com.iwin.istudy.ui;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iwin.istudy.R;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by sxq on 2016/4/18.
 */
public class PetLayout extends LinearLayout {
    private GifImageView imgPet;
    private TextView tvPetTime;

    public PetLayout(Context context) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.window_pet, null);
        imgPet = (GifImageView) view.findViewById(R.id.img_pet);
        tvPetTime = (TextView) view.findViewById(R.id.tv_pet_time);

        this.addView(view);
    }

    /**
     * 设置桌面宠物的倒计时显示的时间
     *
     * @param time
     */
    public void setTvPetTime(String time) {
        if (tvPetTime != null) {
            tvPetTime.setText(time);
        }
    }

    /**
     * 所有辛巴狗的动作
     */
    private static final int[] BACKGROUND_RES_ID = {
            //拜托               奔跑嗨              崇拜              高兴转圈
            R.drawable.xbdog0, R.drawable.xbdog1, R.drawable.xbdog2, R.drawable.xbdog3,
            //流口水               弹琴              挥刀无奈            微笑
            R.drawable.xbdog4, R.drawable.xbdog5, R.drawable.xbdog6, R.drawable.xbdog7,
            //哭死                  人呢              被雷打             偷笑
            R.drawable.xbdog8, R.drawable.xbdog9, R.drawable.xbdog10, R.drawable.xbdog11,
            //睡觉                吃药              赞                   撒娇
            R.drawable.xbdog12, R.drawable.xbdog13, R.drawable.xbdog14, R.drawable.xbdog15,
    };

    private static final int DEFAULT_BACKGROUND = R.drawable.xbdog12;

    public static final int ACTION_PLEASE = BACKGROUND_RES_ID[0];
    public static final int ACTION_TITTER = BACKGROUND_RES_ID[11];
    public static final int ACTION_ZAN = BACKGROUND_RES_ID[14];
    public static final int ACTION_HAPPY = BACKGROUND_RES_ID[3];

    /**
     * 获得随机的辛巴狗动作
     *
     * @return 返回辛巴狗动作的资源ID
     */
    public int getRandomBackgroundRes() {
        Random random = new Random();
        int res = random.nextInt(16);
        return BACKGROUND_RES_ID[res];
    }

    /**
     * 设置桌面宠物的动作
     *
     * @param res
     */
    public void setImgPetBackground(int res) {
        if (imgPet != null) {
            imgPet.setBackgroundResource(res);
        }
    }

    private MyHandle handle = new MyHandle();

    /**
     * 随机设置宠物动作,6s后返回默认的动作
     */
    public void setRandomBackground() {
        int res = getRandomBackgroundRes();
        setImgPetBackground(res);

        handle.removeMessages(0);
        handle.sendEmptyMessageDelayed(0,6000);
    }

    /**
     * 设置默认的宠物动作
     */
    public void setDeauftBackground() {
        setImgPetBackground(DEFAULT_BACKGROUND);
    }

    class MyHandle extends Handler{
        @Override
        public void handleMessage(Message msg) {
            setDeauftBackground();
        }
    }
}
