package com.iwin.istudy.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.iwin.istudy.R;
import com.iwin.istudy.activity.MainActivity;

public class UpdateTimerReceiver extends BroadcastReceiver {
    private String TAG = "UpdateTimerReceiver";

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
        ((MainActivity) context).setDesklayoutCountText(hour + ":" + minute + ":" + second);

    }
}
