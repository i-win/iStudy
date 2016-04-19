package com.iwin.istudy.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.iwin.istudy.R;
import com.iwin.istudy.activity.MainActivity;
import com.iwin.istudy.entity.AppInfo;

public class NotifyUserReceiver extends BroadcastReceiver {
    private String TAG = "NotifyUserReceiver";
    public NotifyUserReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isNotifyOpenApp =  intent.getBooleanExtra(context.getString(R.string.is_open_a_app),false);
        boolean isCountStart = ((MainActivity)context).getIsSetedCountTime();
        boolean isClickStart = ((MainActivity)context).isClickStart();
        Log.d(TAG,"是否在计时:"+isCountStart);
        Log.d(TAG,"是否点击开启:"+isClickStart);
        if (isNotifyOpenApp && isCountStart){
            //显示通知
            AppInfo app = (AppInfo) intent.getSerializableExtra(context.getString(R.string.app_of_open));
            Log.d(TAG,"App:"+app.getAppName()+" 系统："+app.isSystemApp()+"自己:"+app.isMyApp());

            if ((!app.isMyApp()) && (!app.isSystemApp()) && (app.getAppName() != null) ){
                Log.d(TAG,"调用Home");
                ((MainActivity) context).closeApp(app.getAppPackage());
                ((MainActivity) context).setNotifyLayoutVisiable(View.VISIBLE);
                ((MainActivity) context).setNotifyLayoutText("你打开了："+app.getAppName());
            }
            Log.d(TAG,"appName:"+app.getAppName());
        }else if (isClickStart){
            ((MainActivity)context).startCountService();
            ((MainActivity)context).setClickStart(false);
        }
    }
}
