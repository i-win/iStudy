package com.iwin.istudy.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.iwin.istudy.R;
import com.iwin.istudy.activity.MainActivity;
import com.iwin.istudy.engine.PackagesInfo;

public class MonitorAppsReceiver extends BroadcastReceiver {
    private static final String TAG = "MonitorAppsReceiver";
    public MonitorAppsReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String appState = intent.getStringExtra("appState");
        if (appState.equals("open")){
            String appPackageName = intent.getStringExtra(context.getString(R.string.app_packageName_of_open));
            Log.d(TAG,"你打开了："+appPackageName);
            //发送打开一个app广播
            sendOpenAppReceiver(context,appPackageName);
        }else {
            Log.d(TAG,"没有接到intent");
        }
    }

    /**
     * 当收到打开APP的广播时，转发该广播给更新倒计时的广播，在那边一起对悬浮窗进行修改
     * @param context context
     * @param appPackageName 打开的App的名字
     */
    private void sendOpenAppReceiver(Context context, String appPackageName) {
        Intent intent = new Intent();
        intent.putExtra(context.getString(R.string.is_open_a_app),true);
        intent.putExtra(context.getString(R.string.app_packageName_of_open),appPackageName);
        intent.setAction(context.getString(R.string.notify_user_action));
        context.sendBroadcast(intent);
    }
}
