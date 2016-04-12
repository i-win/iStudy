package com.iwin.istudy.engine;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.List;

/**
 * Created by sxq on 2016/4/10.
 */
public class PackagesInfo {
    private static final String TAG = "PackagesInfo";
    public List<ApplicationInfo> appList;

    /**
     * 先通过包管理器获取手机所有已安装的应用信息
     * @param context
     */
    public PackagesInfo(Context context){
        PackageManager pm = context.getApplicationContext().getPackageManager();
        appList = pm.getInstalledApplications(PackageManager.GET_SHARED_LIBRARY_FILES);
        /*for (ApplicationInfo app:appList){
            Log.d("apps",app.loadLabel(pm).toString());
        }*/
    }

    /**
     * 根据APP包名返回对应的应用程序的信息。
     * @param appName 包名
     * @return 返回该包名所在的应用程序
     */
    public ApplicationInfo getInfo(String appName){
        if (appName == null){
            return null;
        }
        for (ApplicationInfo app:appList){
            if (app.processName.equals(appName)){
                return app;
            }
        }
        return null;
    }




}
