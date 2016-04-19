package com.iwin.istudy.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Chronometer;

import com.iwin.istudy.R;
import com.iwin.istudy.activity.MainActivity;

public class UpdateTimerReceiver extends BroadcastReceiver {
    private String TAG = "UpdateTimerReceiver";
    private int totalDay;
    private int totalHour;
    private int totalMinute;
    private int totalSecond;

    public UpdateTimerReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //context:注册该广播的context
        //intent:发送该广播携带的intent
        //更新倒计时
        String hour = intent.getStringExtra(context.getString(R.string.countHour));
        String minute = intent.getStringExtra(context.getString(R.string.countMinute));
        String second = intent.getStringExtra(context.getString(R.string.countSecond));
        ((MainActivity) context).setTvHour(hour);
        ((MainActivity) context).setTvMinute(minute);
        ((MainActivity) context).setTvSecond(second);
        ((MainActivity) context).setSetedCountTime(true);
        ((MainActivity) context).setPetTimeText(hour + ":" + minute + ":" + second);

        SharedPreferences preferences = context.getSharedPreferences("myPref2", Context.MODE_PRIVATE);  //当前程序才能读取
        SharedPreferences.Editor editor = preferences.edit();
        totalSecond = preferences.getInt("second", 0);
        totalMinute = preferences.getInt("minute", 0);
        totalHour = preferences.getInt("hour", 0);
        totalDay = preferences.getInt("day", 0);
        totalSecond++;
        if (totalSecond == 60) {
            totalMinute++;
            totalSecond = 0;
            if (totalMinute == 60) {
                totalHour++;
                totalMinute = 0;
                if (totalHour == 24) {
                    totalDay++;
                    totalHour = 0;
                }
            }
        }
        String totalTime = totalDay + "天"+ totalHour+"小时" +totalMinute+ "分钟" + totalSecond + "秒";
        ((MainActivity) context).setTvTotalTime(totalTime);
        editor.putInt("second", totalSecond);
        editor.putInt("minute", totalMinute);
        editor.putInt("hour", totalHour);
        editor.putInt("day", totalDay);
        editor.commit();
    }
}
