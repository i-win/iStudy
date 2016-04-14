package com.iwin.istudy.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;

import com.iwin.istudy.R;
import com.iwin.istudy.activity.MainActivity;
import com.iwin.istudy.engine.PackagesInfo;
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
            String appPackageName = intent.getStringExtra(context.getString(R.string.app_packageName_of_open));
            AppInfo app = getAppInfo(context,appPackageName);
            ((MainActivity) context).setDesklayoutNotifyVisiable(View.VISIBLE);
            ((MainActivity) context).setDesklayoutNotifyText("你打开了："+app.getAppName());
            Log.d(TAG,"appName:"+app.getAppName());
        }else if (isClickStart){
            ((MainActivity)context).startCountService();
            ((MainActivity)context).setClickStart(false);
        }
    }

    /**
     *
     * @param context
     * @param packageName
     * @return 返回开启的APP信息，包括APP名，和App包名
     */
    private AppInfo getAppInfo(Context context,String packageName){
        PackagesInfo allPackagesInfo = new PackagesInfo(context);
        PackageManager pm = context.getPackageManager();

        ApplicationInfo appInfo = allPackagesInfo.getInfo(packageName);
        AppInfo app = new AppInfo();
        app.setAppName(appInfo.loadLabel(pm));
        app.setAppPackage(packageName);

        return app;
    }
}
