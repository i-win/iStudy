package com.iwin.istudy.entity;

import java.io.Serializable;

/**
 * Created by sxq on 2016/4/10.
 * 实体app的信息，属性有包名，app名。
 */
public class AppInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private CharSequence appName;
    private String appPackage;
    private boolean isSystemApp;
    private boolean isMyApp;

    public CharSequence getAppName() {
        return appName;
    }

    public void setAppName(CharSequence appName) {
        this.appName = appName;
    }

    public String getAppPackage() {
        return appPackage;
    }

    public void setAppPackage(String appPackage) {
        this.appPackage = appPackage;
    }

    public boolean isSystemApp() {
        return isSystemApp;
    }

    public void setSystemApp(boolean systemApp) {
        isSystemApp = systemApp;
    }

    public boolean isMyApp() {
        return isMyApp;
    }

    public void setMyApp(boolean myApp) {
        isMyApp = myApp;
    }
}
