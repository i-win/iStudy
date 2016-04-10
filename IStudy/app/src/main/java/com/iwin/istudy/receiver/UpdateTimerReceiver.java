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
        String minute = intent.getStringExtra(context.getString(R.string.countMinute));
        String second = intent.getStringExtra(context.getString(R.string.countSecond));
        ((MainActivity) context).tvMinute.setText(minute);
        ((MainActivity) context).tvSecond.setText(second);
        ((MainActivity) context).mDesktopLayout.setCountTxt("   "+minute+":"+second);
    }
}
