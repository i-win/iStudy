package com.iwin.istudy.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.iwin.istudy.R;
import com.iwin.istudy.activity.MainActivity;

public class NotifyUserReceiver extends BroadcastReceiver {
    private String TAG = "NotifyUserReceiver";
    public NotifyUserReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isNotifyOpenApp =  intent.getBooleanExtra(context.getString(R.string.is_open_a_app),false);
        if (isNotifyOpenApp){
            //显示通知
            CharSequence appName = intent.getCharSequenceExtra(context.getString(R.string.app_name_of_open));
            ((MainActivity) context).mDesktopLayout.setTvNotifyVisiable(View.VISIBLE);
            ((MainActivity) context).mDesktopLayout.setTvNotifyText(appName);
            Log.d(TAG,"appName:"+appName);
        }
    }
}
