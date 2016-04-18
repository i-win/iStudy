package com.iwin.istudy.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.iwin.istudy.R;

public class WelcomeActivity extends Activity {
    Handler handler = new Handler();
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        handler.postDelayed(new Runnable() {  //使用handler的postDelayed实现延时跳转
            public void run() {
                SharedPreferences preferences = getSharedPreferences("welcome", MODE_PRIVATE);  //当前程序才能读取
                SharedPreferences.Editor editor = preferences.edit();
                int flag = preferences.getInt("flag", 0);
                if (flag == 0) {
                    Intent intent = new Intent(WelcomeActivity.this, IntroductionActivity.class);
                    startActivity(intent);
                    editor.putInt("flag", 1);
                    editor.commit();
                    finish();
                } else {
                    Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 2000);//5秒后跳转至应用主界面MainActivity
        overridePendingTransition(R.anim.activity_output, R.anim.activity_input);// 淡出淡入动画效果
    }
}
