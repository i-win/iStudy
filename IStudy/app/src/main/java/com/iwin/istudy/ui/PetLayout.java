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
import com.iwin.istudy.engine.XbDogPet;
import com.iwin.istudy.entity.Pet;
import com.iwin.istudy.interfaces.PetAttributes;

import java.util.Random;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by sxq on 2016/4/18.
 */
public class PetLayout extends LinearLayout {

    private GifImageView imgPet;
    private TextView tvPetTime;
    private static Context context;
    private static PetLayout instance;
    private PetAttributes petAttr;

    private PetLayout(Context context) {
        super(context);
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.window_pet, null);
        imgPet = (GifImageView) view.findViewById(R.id.img_pet);
        tvPetTime = (TextView) view.findViewById(R.id.tv_pet_time);

        this.addView(view);
    }

    public static PetLayout getInstance(Context context){
        if (instance == null){
            instance = new PetLayout(context);
        }
        return instance;
    }

    private static final PetAttributes DEFAULT_PETATTR = new XbDogPet();
    /**
     * 选择宠物种类
     * @param petAttr
     */
    public void setPetAttr(PetAttributes petAttr){
        this.petAttr = petAttr;
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
    private void setRandomAction() {
        int res = petAttr.getRandomAction();
        setImgPetBackground(res);

        handle.removeMessages(DEFAULT);
        handle.sendEmptyMessageDelayed(DEFAULT,6000);
    }

    /**
     * 设置默认的宠物动作
     */
    private void setDeauftAction() {
        Pet pet = petAttr.getDeauft();
        setImgPetBackground(pet.getAction());
    }

    /**
     * 设置学习的动作
     */
    private void setStudyAction(){
        Pet pet = petAttr.getStudy();
        setImgPetBackground(pet.getAction());
        ((MainActivity)context).setNotifyLayoutVisiable(VISIBLE);
        ((MainActivity)context).setNotifyLayoutText(pet.getSaying());
    }

    /**
     * 设置休息的动作
     */
    private void setRestAction(){
        Pet pet = petAttr.getRest();
        setImgPetBackground(pet.getAction());
        ((MainActivity)context).setNotifyLayoutVisiable(VISIBLE);
        ((MainActivity)context).setNotifyLayoutText(pet.getSaying());
    }

    /**
     * 设置温馨提示的动作
     */
    private void setWarningAction(){
        Pet pet = petAttr.getWarning();
        setImgPetBackground(pet.getAction());
        ((MainActivity)context).setNotifyLayoutVisiable(VISIBLE);
        ((MainActivity)context).setNotifyLayoutText(pet.getSaying());
    }

    /**
     * 设置学习完成的动作
     */
    private void setFinishAction(){
        Pet pet = petAttr.getFinish();
        setImgPetBackground(pet.getAction());
        ((MainActivity)context).setNotifyLayoutVisiable(VISIBLE);
        ((MainActivity)context).setNotifyLayoutText(pet.getSaying());
    }

    /**
     * 设置动物的动作
     * @param action
     */
    public void setPetAction(int action){
        if (petAttr == null){
            petAttr = DEFAULT_PETATTR;
        }
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
                handle.sendEmptyMessageDelayed(REST,8000);
                break;
            case REST:
                handle.sendEmptyMessage(REST);
                break;

            case WARNING:
                handle.sendEmptyMessage(WARNING);
                handle.removeMessages(REST);
                handle.sendEmptyMessageDelayed(REST,10000);
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
                    setRandomAction();
                    break;

                case DEFAULT:
                    setDeauftAction();
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
