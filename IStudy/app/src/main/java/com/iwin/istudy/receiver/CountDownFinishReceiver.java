package com.iwin.istudy.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.iwin.istudy.activity.MainActivity;
import com.iwin.istudy.ui.PetLayout;

public class CountDownFinishReceiver extends BroadcastReceiver {
    private static final String TAG = "CountDownFinishReceiver";
    public CountDownFinishReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ((MainActivity)context).setSetedCountTime(false);
        ((MainActivity)context).setPetAction(PetLayout.FINISH);
    }
}
