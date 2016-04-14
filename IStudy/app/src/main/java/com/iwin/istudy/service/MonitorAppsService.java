package com.iwin.istudy.service;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.iwin.istudy.R;

public class MonitorAppsService extends AccessibilityService {

    private final static String TAG = "MonitorAppsService";
    private static MonitorAppsService mInstance = null;

    public MonitorAppsService(){

    }

    public static MonitorAppsService getInstance(){

        if (mInstance == null){
            synchronized (MonitorAppsService.class){
                if (mInstance == null){
                    mInstance = new MonitorAppsService();
                }
            }
        }
        return mInstance;
    }

    /**
     * 监听窗口焦点,并且获取焦点窗口的包名
     * @param event
     */
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED ){
            Log.d(TAG,"你打开了："+event.getPackageName());
            sendOpenNewAppReceiver(event.getPackageName().toString());
        }
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * 当监测到打开了某应用时发送的广播
     *
     * @param appPackage 打开的应用的app包名
     */
    private void sendOpenNewAppReceiver(String appPackage) {
        Intent intent = new Intent();
        intent.putExtra("appState", "open");
        intent.putExtra(getApplicationContext().getString(R.string.app_packageName_of_open), appPackage);
        intent.setAction(getApplicationContext().getString(R.string.open_a_app));
        sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 此方法用来判断当前应用的辅助功能服务是否开启
     * @param context
     * @return
     */
    public static boolean isAccessibilitySettingsOn(Context context){
        int accessibilityEnabled = 0;
        try {
            accessibilityEnabled = Settings.Secure.getInt(context.getContentResolver(),
                    Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        if (accessibilityEnabled == 1){
            String services = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (services != null){
                return services.toLowerCase().contains(context.getPackageName().toLowerCase());
            }
        }
        return false;
    }
}
