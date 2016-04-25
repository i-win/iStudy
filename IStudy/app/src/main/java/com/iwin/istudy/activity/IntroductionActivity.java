package com.iwin.istudy.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.iwin.istudy.R;
import com.iwin.istudy.adapter.MyPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class IntroductionActivity extends AppCompatActivity {
    private List<View> viewList;
    private ViewPager pager;
    private PagerTitleStrip title_bottom;
    private List<String> titlelist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);
        overridePendingTransition(R.anim.activity_output, R.anim.activity_input);
        viewList = new ArrayList<>();
        View view1 = View.inflate(this, R.layout.introduction_view1, null);//讲layout布局文件装换成view对象
        View view2 = View.inflate(this, R.layout.introduction_view2, null);
        View view3 = View.inflate(this, R.layout.introduction_view3, null);
        View view4 = View.inflate(this, R.layout.introduction_view4, null);
        View view5 = View.inflate(this, R.layout.introduction_view5, null);
        View view6 = View.inflate(this, R.layout.introduction_view6, null);
        View view7 = View.inflate(this, R.layout.introduction_view7, null);
        viewList.add(view1);
        viewList.add(view2);
        viewList.add(view3);
        viewList.add(view4);
        viewList.add(view5);
        viewList.add(view6);
        viewList.add(view7);
        //为ViewPager设置标题
        titlelist = new ArrayList<String>();
        titlelist.add("。");
        titlelist.add("。");
        titlelist.add("。");
        titlelist.add("。");
        titlelist.add("。");
        titlelist.add("。");
        titlelist.add("。");
        title_bottom = (PagerTitleStrip) findViewById(R.id.title_bottom);
        title_bottom.setTextColor(Color.BLACK);
        title_bottom.setTextSize(TypedValue.COMPLEX_UNIT_PX, 30);
        //初始化ViewPager
        pager = (ViewPager) findViewById(R.id.pager);
        //创建适配器pagerAdapter
        MyPagerAdapter adapter = new MyPagerAdapter(viewList, titlelist);
        //viewPager加载适配器
        pager.setAdapter(adapter);
        TextView tvStartActivity = (TextView) view7.findViewById(R.id.tv_start_activity);
        tvStartActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntroductionActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
