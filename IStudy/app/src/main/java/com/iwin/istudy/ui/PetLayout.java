package com.iwin.istudy.ui;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iwin.istudy.R;
import com.iwin.istudy.activity.MainActivity;

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
    private Context context;

    public PetLayout(Context context) {
        super(context);
        this.context = context;
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

    /**
     * 指定几种特别的动作
     */
    public static final int ACTION_PLEASE = BACKGROUND_RES_ID[0];
    public static final int ACTION_TITTER = BACKGROUND_RES_ID[11];
    public static final int ACTION_ZAN = BACKGROUND_RES_ID[14];
    public static final int ACTION_HAPPY = BACKGROUND_RES_ID[3];
    public static final int ACTION_PLAYING = BACKGROUND_RES_ID[5];
    public static final int ACTION_COQUETRY = BACKGROUND_RES_ID[15];

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
    public static final int DEFAULT = 0;
    public static final int STUDY = 1;
    public static final int REST = 2;
    public static final int WARNING = 3;
    public static final int RANDOM = 4;
    public static final int FINISH = 5;


    /**
     * 随机设置宠物动作,6s后返回默认的动作
     */
    private void setRandomBackground() {
        int res = getRandomBackgroundRes();
        setImgPetBackground(res);

        handle.removeMessages(DEFAULT);
        handle.sendEmptyMessageDelayed(DEFAULT,6000);
    }

    /**
     * 设置默认的宠物动作
     */
    private void setDeauftBackground() {
        setImgPetBackground(DEFAULT_BACKGROUND);
    }

    /**
     * 设置学习的动作
     */
    private void setStudyAction(){
        setImgPetBackground(ACTION_PLAYING);
        ((MainActivity)context).setNotifyLayoutVisiable(VISIBLE);
        ((MainActivity)context).setNotifyLayoutText("开始学习啦~~非系统App已经被俺禁用了,好好学习吧");
    }

    /**
     * 设置休息的动作
     */
    private void setRestAction(){
        setDeauftBackground();
        ((MainActivity)context).setNotifyLayoutVisiable(VISIBLE);
        ((MainActivity)context).setNotifyLayoutText("俺先睡会~~不要偷偷玩手机哦，俺会发现的");
    }

    /**
     * 设置温馨提示的动作
     */
    private void setWarningAction(){
        Random random = new Random();
        int action = random.nextInt(3);
        if (action == 0){
            setImgPetBackground(ACTION_PLEASE);
            ((MainActivity)context).setNotifyLayoutVisiable(VISIBLE);
            ((MainActivity)context).setNotifyLayoutText("不要玩手机啦~~好好学习嘛");
        }else if (action == 1){
            setImgPetBackground(ACTION_TITTER);
            ((MainActivity)context).setNotifyLayoutVisiable(VISIBLE);
            ((MainActivity)context).setNotifyLayoutText("xixi~~俺看着你，看你怎么玩手机");
        } else  if (action == 2){
            setImgPetBackground(ACTION_COQUETRY);
            ((MainActivity)context).setNotifyLayoutVisiable(VISIBLE);
            ((MainActivity)context).setNotifyLayoutText("不要再玩手机啦~~");
        }
    }

    /**
     * 设置学习完成的动作
     */
    private void setFinishAction(){
        Random random = new Random();
        if (random.nextInt(2) == 0){
            setImgPetBackground(ACTION_HAPPY);
            ((MainActivity)context).setNotifyLayoutVisiable(VISIBLE);
            ((MainActivity)context).setNotifyLayoutText("lala~~辛苦了，俺允许你玩手机了");
        }else {
            setImgPetBackground(ACTION_ZAN);
            ((MainActivity)context).setNotifyLayoutVisiable(VISIBLE);
            ((MainActivity)context).setNotifyLayoutText("zan~~俺越来越崇拜你了");
        }
    }

    /**
     * 设置动物的动作
     * @param action
     */
    public void setPetAction(int action){
        switch (action){
            case RANDOM:
                handle.sendEmptyMessage(RANDOM);
                break;

            case DEFAULT:
                handle.sendEmptyMessage(DEFAULT);
                break;

            case STUDY:
                handle.sendEmptyMessage(STUDY);
                handle.removeMessages(REST);
                handle.sendEmptyMessageDelayed(REST,6000);
                break;
            case REST:
                handle.sendEmptyMessage(REST);
                break;

            case WARNING:
                handle.sendEmptyMessage(WARNING);
                handle.removeMessages(REST);
                handle.sendEmptyMessageDelayed(REST,8000);
                break;

            case FINISH:
                handle.sendEmptyMessage(FINISH);
                handle.removeMessages(DEFAULT);
                handle.sendEmptyMessageDelayed(DEFAULT,10000);
                break;

            default:
                break;
        }
    }

    class MyHandle extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case RANDOM:
                    setRandomBackground();
                    break;

                case DEFAULT:
                    setDeauftBackground();
                    break;

                case STUDY:
                    setStudyAction();
                    break;

                case REST:
                    setRestAction();
                    break;

                case WARNING:
                    setWarningAction();
                    break;

                case FINISH:
                    setFinishAction();
                    break;

                default:
                    break;
            }
        }
    }
}
