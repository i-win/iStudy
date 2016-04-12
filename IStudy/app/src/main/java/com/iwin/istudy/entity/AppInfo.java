package com.iwin.istudy.entity;

/**
 * Created by sxq on 2016/4/10.
 * 实体app的信息，属性有包名，app名。
 */
public class AppInfo {
    private CharSequence appName;
    private String appPackage;

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
}
