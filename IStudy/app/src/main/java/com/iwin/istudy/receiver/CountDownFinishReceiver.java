package com.iwin.istudy.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.iwin.istudy.activity.MainActivity;

public class CountDownFinishReceiver extends BroadcastReceiver {
    public CountDownFinishReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ((MainActivity)context).setSetedCountTime(false);
    }
}
