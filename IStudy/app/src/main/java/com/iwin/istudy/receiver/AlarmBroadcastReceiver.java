package com.iwin.istudy.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.iwin.istudy.R;
import com.iwin.istudy.activity.MainActivity;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"闹钟广播接收到了");
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(R.drawable.app_icon);   //设置通知图标
        builder.setTicker("IStudy有新的通知");   //手机状态栏的提示
        builder.setWhen(System.currentTimeMillis());   //设置通知的时间
        builder.setContentTitle("IStudy");      //设置通知的标题
        builder.setContentText("时间到了，该学习了亲！");  //设置通知的内容
        intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivities(context, 0, new Intent[]{intent}, 0);
        builder.setContentIntent(pendingIntent);     //设置点击通知之后的跳转
        builder.setDefaults(Notification.DEFAULT_ALL);   //设置以上三种提示
        final Notification notification = builder.build();   //4.1以下用builder.getNotification()
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        manager.notify(0, notification);   //第一个参数为该通知的序号
    }
}
