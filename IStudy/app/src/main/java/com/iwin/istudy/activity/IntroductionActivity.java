package com.iwin.istudy.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iwin.istudy.R;
import com.iwin.istudy.adapter.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class IntroductionActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener,View.OnClickListener{

    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private List<View> views;
    private ImageView[] dots ;
    private int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);
        overridePendingTransition(R.anim.activity_output, R.anim.activity_input);
        views = new ArrayList<View>();
        View view1 = View.inflate(this, R.layout.introduction_view1, null);//讲layout布局文件装换成view对象
        View view2 = View.inflate(this, R.layout.introduction_view2, null);
        View view3 = View.inflate(this, R.layout.introduction_view3, null);
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        views.add(view1);
        views.add(view2);
        views.add(view3);
        TextView tvBegin = (TextView)view3.findViewById(R.id.tv_begin);
        tvBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntroductionActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        //初始化Adapter
        viewPagerAdapter = new ViewPagerAdapter(views);
        viewPager.setAdapter(viewPagerAdapter);
        //绑定回调
        viewPager.setOnPageChangeListener(this);
        //初始化底部小点
        initDots();
        overridePendingTransition(R.anim.activity_output, R.anim.activity_input);// 淡出淡入动画效果
    }
    private void initDots() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.linearlayout);
        dots = new ImageView[4];
        //循环取得小点图片
        for (int i = 0; i < 3; i++) {
            dots[i] = (ImageView) ll.getChildAt(i);
            dots[i].setEnabled(true);//都设为灰色
            dots[i].setOnClickListener(this);
            dots[i].setTag(i);//设置位置tag，方便取出与当前位置对应
        }
        currentIndex = 0;
        dots[currentIndex].setEnabled(false);//设置为白色，即选中状态
    }

    /**
     *设置当前的引导页
     */
    private void setCurView(int position)
    {
        if (position < 0 || position >= 3) {
            return;
        }
        viewPager.setCurrentItem(position);
    }

    /**
     *这只当前引导小点的选中
     */
    private void setCurDot(int positon)
    {
        if (positon < 0 || positon > 3 - 1 || currentIndex == positon) {
            return;
        }
        dots[positon].setEnabled(false);
        dots[currentIndex].setEnabled(true);
        currentIndex = positon;
    }

    //当滑动状态改变时调用
    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub
    }

    //当当前页面被滑动时调用
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub
    }

    //当新的页面被选中时调用
    @Override
    public void onPageSelected(int arg0) {
        //设置底部小点选中状态
        setCurDot(arg0);
    }

    @Override
    public void onClick(View v) {
        int position = (Integer)v.getTag();
        setCurView(position);
        setCurDot(position);
    }
}
