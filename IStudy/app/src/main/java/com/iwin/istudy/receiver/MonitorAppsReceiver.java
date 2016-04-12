package com.iwin.istudy.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.iwin.istudy.R;
import com.iwin.istudy.activity.MainActivity;

public class MonitorAppsReceiver extends BroadcastReceiver {
    private static final String TAG = "MonitorAppsReceiver";
    public MonitorAppsReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String appState = intent.getStringExtra("appState");
        CharSequence appName = intent.getCharSequenceExtra(context.getString(R.string.app_info));
        if (appState.equals("open")){
            String appPackageName = intent.getStringExtra(context.getString(R.string.app_packageName_of_open));
            Log.d(TAG,"你打开了："+appName+";包名："+appPackageName);

            //发送打开一个app广播
            sendOpenAppReceiver(context,appName);

        } else if (appState.equals("close")){
            Log.d(TAG,"你关闭了："+appName);
        }else {
            Log.d(TAG,"没有接到intent");
        }
    }

    /**
     * 当收到打开APP的广播时，转发该广播给更新倒计时的广播，在那边一起对悬浮窗进行修改
     * @param context context
     * @param appName 打开的App的名字
     */
    private void sendOpenAppReceiver(Context context, CharSequence appName) {
        Intent intent = new Intent();
        intent.putExtra(context.getString(R.string.is_open_a_app),true);
        intent.putExtra(context.getString(R.string.app_name_of_open),appName);
        intent.setAction(context.getString(R.string.notify_user_action));
        context.sendBroadcast(intent);
    }
}
