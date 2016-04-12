package com.iwin.istudy.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.iwin.istudy.R;
import com.iwin.istudy.adapter.MyPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sqh on 2016/3/31.
 */
public class Desklayout extends LinearLayout {
    private ViewFlipper flipper;
    private int[] resId;
    private List<View> viewList;
    private ViewPager pager;
    private PagerTitleStrip tab;
    private List<String> titlelist;
    private TextView count_txt;
    private TextView tvNotify;


    public Desklayout(final Context context) {
        super(context);

        setOrientation(LinearLayout.VERTICAL);// 水平排列
        //设置宽高
        this.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        View view = LayoutInflater.from(context).inflate(R.layout.windows_layout, null);

        tvNotify = (TextView) view.findViewById(R.id.tv_notify);
        tvNotify.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setTvNotifyVisiable(View.INVISIBLE);
            }
        });
        flipper = (ViewFlipper) view.findViewById(R.id.flipper);
        //动态导入的方式为ViewFlipper加入子View
        resId = new int[]{R.drawable.clean_a1, R.drawable.clean_a1, R.drawable.clean_a1,
                R.drawable.clean_a1, R.drawable.clean_a1, R.drawable.clean_a1, R.drawable.clean_a1,
                R.drawable.clean_a1, R.drawable.clean_a1, R.drawable.clean_a1, R.drawable.clean_a1,
                R.drawable.clean_a1, R.drawable.clean_a1, R.drawable.clean_a1, R.drawable.clean_a1,
                R.drawable.clean_a1, R.drawable.clean_a1, R.drawable.clean_a1, R.drawable.clean_a1,
                R.drawable.clean_c, R.drawable.clean_a1};
        for (int i = 0; i < resId.length; i++) {
            flipper.addView(getImageView(resId[i]));
        }
        flipper.setAutoStart(true);
        flipper.setFlipInterval(70);//自动切换
        flipper.startFlipping();

        viewList = new ArrayList<>();
        View view1 = View.inflate(context, R.layout.pager_view1, null);//讲layout布局文件装换成view对象
        View view2 = View.inflate(context, R.layout.pager_view2, null);
        View view3 = View.inflate(context, R.layout.pager_view3, null);
        View view4 = View.inflate(context, R.layout.pager_view4, null);
        View view5 = View.inflate(context, R.layout.pager_view5, null);
        viewList.add(view1);
        viewList.add(view2);
        viewList.add(view3);
        viewList.add(view4);
        viewList.add(view5);
        //为ViewPager设置标题
        titlelist = new ArrayList<String>();
        titlelist.add("");
        titlelist.add("");
        titlelist.add("");
        titlelist.add("");
        titlelist.add("");

        count_txt = (TextView)view1.findViewById(R.id.count_txt);


        view2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                flipper.stopFlipping();
                flipper.setAlpha(0.3f);
                flipper.startFlipping();
            }
        });
        view3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                flipper.stopFlipping();
                flipper.setAlpha(1f);
                flipper.startFlipping();
            }
        });
        view4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < resId.length; i++) {
                    flipper.removeViewAt(0);
                }
                resId = new int[]{R.drawable.dog_feed_a, R.drawable.dog_feed_a,
                        R.drawable.dog_feed_b, R.drawable.dog_feed_b,
                        R.drawable.dog_feed_c, R.drawable.dog_feed_c,
                        R.drawable.dog_feed_d, R.drawable.dog_feed_d,
                        R.drawable.dog_feed_d, R.drawable.dog_feed_d};
                for (int i = 0; i < resId.length; i++) {
                    flipper.addView(getImageView(resId[i]));
                }
                flipper.startFlipping();
                new CountDownTimer(700, 70) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                    }

                    @Override
                    public void onFinish() {
                        for (int i = 0; i < resId.length; i++) {
                            flipper.removeViewAt(0);
                        }
                        resId = new int[]{R.drawable.clean_a1, R.drawable.clean_a1, R.drawable.clean_a1,
                                R.drawable.clean_a1, R.drawable.clean_a1, R.drawable.clean_a1, R.drawable.clean_a1,
                                R.drawable.clean_a1, R.drawable.clean_a1, R.drawable.clean_a1, R.drawable.clean_a1,
                                R.drawable.clean_a1, R.drawable.clean_a1, R.drawable.clean_a1, R.drawable.clean_a1,
                                R.drawable.clean_a1, R.drawable.clean_a1, R.drawable.clean_a1, R.drawable.clean_a1,
                                R.drawable.clean_c};
                        for (int i = 0; i < resId.length; i++) {
                            flipper.addView(getImageView(resId[i]));
                        }
                        flipper.startFlipping();
                    }
                }.start();
            }
        });
        view5.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < resId.length; i++) {
                    flipper.removeViewAt(0);
                }
                resId = new int[]{R.drawable.dog_bath_a, R.drawable.dog_bath_b, R.drawable.dog_bath_c,
                        R.drawable.dog_bath_d, R.drawable.dog_bath_e, R.drawable.dog_bath_f,
                        R.drawable.dog_bath_g, R.drawable.dog_bath_h, R.drawable.dog_bath_i,
                        R.drawable.dog_bath_j, R.drawable.dog_bath_k, R.drawable.dog_bath_l,
                        R.drawable.dog_bath_m, R.drawable.dog_bath_n, R.drawable.dog_bath_o,
                        R.drawable.dog_bath_p, R.drawable.dog_bath_q, R.drawable.dog_bath_q
                        , R.drawable.dog_bath_q, R.drawable.dog_bath_q, R.drawable.dog_bath_q
                        , R.drawable.dog_bath_q, R.drawable.dog_bath_q, R.drawable.dog_bath_q};
                for (int i = 0; i < resId.length; i++) {
                    flipper.addView(getImageView(resId[i]));
                }
                flipper.startFlipping();
                new CountDownTimer(1600, 70) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                    }
                    @Override
                    public void onFinish() {
                        for (int i = 0; i < resId.length; i++) {
                            flipper.removeViewAt(0);
                        }
                        resId = new int[]{R.drawable.clean_a1, R.drawable.clean_a1, R.drawable.clean_a1,
                                R.drawable.clean_a1, R.drawable.clean_a1, R.drawable.clean_a1, R.drawable.clean_a1,
                                R.drawable.clean_a1, R.drawable.clean_a1, R.drawable.clean_a1, R.drawable.clean_a1,
                                R.drawable.clean_a1, R.drawable.clean_a1, R.drawable.clean_a1, R.drawable.clean_a1,
                                R.drawable.clean_a1, R.drawable.clean_a1, R.drawable.clean_a1, R.drawable.clean_a1,
                                R.drawable.clean_c};
                        for (int i = 0; i < resId.length; i++) {
                            flipper.addView(getImageView(resId[i]));
                        }
                        flipper.startFlipping();
                    }
                }.start();
            }
        });

        //为PagerTabStrip设置一些属性
        tab = (PagerTitleStrip) view.findViewById(R.id.title);
        tab.setTextColor(Color.BLACK);
        tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, 0);
        //初始化ViewPager
        pager = (ViewPager) view.findViewById(R.id.pager);
        //创建适配器pagerAdapter
        MyPagerAdapter adapter = new MyPagerAdapter(viewList, titlelist);
        //viewPager加载适配器
        pager.setAdapter(adapter);
        this.addView(view);
    }

    private ImageView getImageView(int resId) {
        ImageView image = new ImageView(getContext());
        image.setImageResource(resId);
//        image.setBackgroundResource(resId);  将图片铺满屏幕
        return image;
    }

    /**
     * 设置宠物狗的倒计时时间
     * @param text 显示的时间
     */
    public void setCountTxt(String text){
        count_txt.setText(text);
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
    public void setTvNotifyVisiable(int visiable){
        tvNotify.setVisibility(visiable);
    }
}
