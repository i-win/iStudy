package com.iwin.istudy.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iwin.istudy.R;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by sxq on 2016/4/18.
 */
public class PetLayout extends LinearLayout {
    private GifImageView imgPet;
    private TextView tvPetTime;

    public PetLayout(Context context) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.window_pet,null);
        imgPet = (GifImageView) view.findViewById(R.id.img_pet);
        tvPetTime = (TextView) view.findViewById(R.id.tv_pet_time);

        this.addView(view);
    }

    /**
     * 设置桌面宠物的倒计时显示的时间
     * @param time
     */
    public void setTvPetTime(String time){
        if (tvPetTime != null){
            tvPetTime.setText(time);
        }
    }

    /**
     * 设置桌面宠物的动作
     * @param res
     */
    public void setImgPetBackground(int res){
        if (imgPet != null){
            imgPet.setBackgroundResource(res);
        }
    }
}
