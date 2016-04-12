package com.iwin.istudy.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.IBinder;

import com.iwin.istudy.R;
import com.iwin.istudy.engine.PackagesInfo;
import com.iwin.istudy.entity.AppInfo;
import com.jaredrummler.android.processes.ProcessManager;
import com.jaredrummler.android.processes.models.AndroidAppProcess;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MonitorAppsService extends Service {

    private final static String TAG = "MonitorAppsService";
    private boolean isOpenMonitor = true;
    private Context context = this;
    private List<AppInfo> preAppList;
    private List<AppInfo> curAppList;

    public MonitorAppsService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        final PackagesInfo allPackagesInfo = new PackagesInfo(context);
        final PackageManager pm = context.getPackageManager();

        preAppList = new ArrayList<>();

        //开启新线程后台时时监测所有运行的程序
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isOpenMonitor){
                    //获取当前正在运行的进程信息
                    List<AndroidAppProcess> runningProcess =
                            ProcessManager.getRunningForegroundApps(getApplicationContext());
                    Collections.sort(runningProcess, new Comparator<AndroidAppProcess>() {
                        @Override
                        public int compare(AndroidAppProcess lhs, AndroidAppProcess rhs) {
                            return lhs.getPackageName().compareTo(rhs.getPackageName());
                        }
                    });

                    ApplicationInfo appInfo;
                    curAppList = new ArrayList<>();
                    //统计当前正在运行的程序
                    for (AndroidAppProcess appProcess:runningProcess){
                        try {
                            //过滤掉系统应用以及后台进程的程序以及本程序
                            if ((appProcess.getPackageInfo(getApplicationContext(),0)
                                    .applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0
                                    && appProcess.foreground
                                    && !appProcess.getPackageName().contains(getPackageName())){

                                //通过进程获取的包名到包管理器中获取进程所在的程序对象
                                appInfo = allPackagesInfo.getInfo(appProcess.getPackageName());
                                //封装程序对象，自定义包名和程序名两个属性
                                AppInfo curApp = new AppInfo();
                                curApp.setAppName(appInfo.loadLabel(pm));
                                curApp.setAppPackage(appProcess.getPackageName());
                                curAppList.add(curApp);
                            }
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                    }

                    //根据以上统计得到的当前正在运行的程序与之前运行的程序比较，确定当前是否打开或关闭了某程序，以此
                    //达到监测程序的运行状态的目的
                    Map<CharSequence,Integer> runningApp = new HashMap<>();
                    for (AppInfo app:preAppList){
                        runningApp.put(app.getAppName(),1);
                    }
                    for (AppInfo app:curAppList){
                        Integer integer = runningApp.get(app.getAppName());
                        if (integer != null){
                            //当前程序状态无改变
                            runningApp.put(app.getAppName(),++integer);
                        }else {
                            //当前程序为新打开的程序
//                            Log.d(TAG, "你打开了:" + app.getAppName());
                            sendOpenNewAppReceiver(app.getAppName(),app.getAppPackage());
                        }
                    }
                    for (Map.Entry<CharSequence,Integer>entry : runningApp.entrySet()){
                        if (entry.getValue() == 1){
                            //关闭了**程序
//                            Log.d(TAG, "你关闭了:" + entry.getKey());
                            sendCloseAppReceiver(entry.getKey());
                        }
                    }
                    if (preAppList != null){
                        preAppList.clear();
                        preAppList.addAll(curAppList);
                    }else {
                        preAppList = new ArrayList<>();
                    }
                    if (curAppList != null){
                        curAppList.clear();
                    }
                }
            }
        }).start();
    }

    /**
     * 当监测到关闭了某应用时发送的广播
     * @param appName 关闭的应用的App名
     */
    private void sendCloseAppReceiver(CharSequence appName) {
        Intent intent = new Intent();
        intent.putExtra("appState","close");
        intent.putExtra(getApplicationContext().getString(R.string.app_info),appName);
        intent.setAction(getApplicationContext().getString(R.string.close_a_app));
        sendBroadcast(intent);
    }

    /**
     * 当监测到打开了某应用时发送的广播
     * @param appName 打开的应用的App名
     * @param appPackage 打开的应用的app包名
     */
    private void sendOpenNewAppReceiver(CharSequence appName, String appPackage) {
        Intent intent = new Intent();
        intent.putExtra("appState","open");
        intent.putExtra(getApplicationContext().getString(R.string.app_info),appName);
        intent.putExtra(getApplicationContext().getString(R.string.app_packageName_of_open),appPackage);
        intent.setAction(getApplicationContext().getString(R.string.open_a_app));
        sendBroadcast(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
