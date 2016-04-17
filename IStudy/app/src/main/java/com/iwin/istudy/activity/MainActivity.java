package com.iwin.istudy.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.iwin.istudy.R;
import com.iwin.istudy.engine.TimerManager;
import com.iwin.istudy.receiver.CountDownFinishReceiver;
import com.iwin.istudy.receiver.NotifyUserReceiver;
import com.iwin.istudy.receiver.UpdateTimerReceiver;
import com.iwin.istudy.service.CountDownService;
import com.iwin.istudy.service.MonitorAppsService;
import com.iwin.istudy.ui.Desklayout;
import com.iwin.istudy.ui.WheelView;

import org.w3c.dom.Text;

import java.util.Calendar;

public class MainActivity extends BaseActivity {

    private UpdateTimerReceiver updateTimerReceiver;
    private NotifyUserReceiver notifyUserReceiver;
    private CountDownFinishReceiver countFinishReceiver;

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayout;
    private Desklayout mDesktopLayout;
    private boolean isSetedCountTime = false;
    private boolean isClickStart = false;
    private long startTime;
    // 声明屏幕的宽高
    float x, y;
    int top;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        registerCountDownReceiver();
        registerCountFinishReceiver();
        registerNotifyUserReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(updateTimerReceiver);
        unregisterReceiver(countFinishReceiver);
        unregisterReceiver(notifyUserReceiver);
    }

    private TextView tvHour;
    private TextView tvMinute;
    private TextView tvSecond;
    private LinearLayout linlayoutCountTime;
    private Button btnStartCount;
    private ImageButton btnLeftPlan;
    private PopupWindow popupWindow_right;
    private EditText edtPlan;
    private ImageButton btnEdit;
    private ImageButton btnSave;
    private ImageButton btnAlarm;
    private ScrollView scrollPlan;
    private Calendar calendar;
    private int hour;
    private int minute;
    private int hour_set;
    private int minute_set;
    private CountDownTimer alarm;
    private TextView tvTotalTime;

    private void initView() {
        tvHour = (TextView) findViewById(R.id.tv_hour);
        tvMinute = (TextView) findViewById(R.id.tv_minute);
        tvSecond = (TextView) findViewById(R.id.tv_second);

        linlayoutCountTime = (LinearLayout) findViewById(R.id.linlayout_count_time);
        linlayoutCountTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSetedCountTime) {
                    setCountTime();
                }
            }
        });

        btnStartCount = (Button) findViewById(R.id.btn_start_count);
        btnStartCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stratMonitorService();
                countServiceEnable();

            }

        });

        btnLeftPlan = (ImageButton) findViewById(R.id.btn_left_plan);
        btnLeftPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SharedPreferences preferences = getSharedPreferences("myPref", MODE_PRIVATE);  //将学习计划信息存储起来
                final SharedPreferences.Editor editor = preferences.edit();
                final String plan = preferences.getString("plan", "好的计划是成功的开始，快写下你的学习计划吧。");

                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);    //引入自定义布局
                View view_dialog_plan = inflater.inflate(R.layout.dialog_plan, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setView(view_dialog_plan);
                final AlertDialog dialog_plan = builder.create();  //创建一个dialog
                dialog_plan.show();

                Window dialogPlanWindow = dialog_plan.getWindow();
                dialogPlanWindow.setWindowAnimations(R.style.dialogWindowAnim);
                WindowManager.LayoutParams lp = dialogPlanWindow.getAttributes();
                lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialogPlanWindow.setAttributes(lp);

                edtPlan = (EditText) dialog_plan.findViewById(R.id.edt_plan);
                edtPlan.setText(plan);

                btnEdit = (ImageButton) dialog_plan.findViewById(R.id.btn_edit);
                btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        edtPlan.setEnabled(true);
                    }
                });

                btnSave = (ImageButton) view_dialog_plan.findViewById(R.id.btn_save);
                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        edtPlan.setEnabled(false);
                        editor.putString("plan", edtPlan.getText().toString());
                        editor.commit();
                        dialog_plan.dismiss();
                    }
                });

                scrollPlan = (ScrollView) view_dialog_plan.findViewById(R.id.scroll_plan);
                scrollPlan.getBackground().setAlpha(50);
            }
        });

        btnAlarm = (ImageButton) findViewById(R.id.btn_alarm);
        btnAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow_right != null && popupWindow_right.isShowing()) {
                    popupWindow_right.dismiss();
                    return;
                } else {
                    initmPopupWindowView_right();
                    popupWindow_right.showAsDropDown(v, 0, 5);
                }
            }
        });

        SharedPreferences preferences = getSharedPreferences("myPref2", MODE_PRIVATE);  //当前程序才能读取
        int day = preferences.getInt("day", 0);
        int hour = preferences.getInt("hour",0);
        int minute = preferences.getInt("minute",0);
        int second = preferences.getInt("second",0);
        String totalTime = day + "天"+hour+"小时"+minute+"分钟"+second+"秒";
        tvTotalTime = (TextView) findViewById(R.id.tv_total_time);
        tvTotalTime.setText(totalTime);
    }

    /**
     * 右侧下拉框属性
     */
    public void initmPopupWindowView_right() {
        final View customView_right = getLayoutInflater().inflate(R.layout.right_alarm, null, false);
        // 创建PopupWindow实例,200,150分别是宽度和高度
        popupWindow_right = new PopupWindow(customView_right, 160, RadioGroup.LayoutParams.WRAP_CONTENT);
        // 设置动画效果 [R.style.AnimationFade 是自己事先定义好的]
        popupWindow_right.setAnimationStyle(R.style.AnimationFade_right);

        calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        final NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(MainActivity.this);
        builder.setSmallIcon(R.mipmap.ic_launcher);   //设置通知图标
        builder.setTicker("IStudy有新的通知");   //手机状态栏的提示
        builder.setWhen(System.currentTimeMillis());   //设置通知的时间
        builder.setContentTitle("IStudy");      //设置通知的标题
        builder.setContentText("该学习了亲！");  //设置通知的内容
        builder.setWhen(System.currentTimeMillis() + 5000);
        Log.i("info", "!!!" + System.currentTimeMillis());
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivities(MainActivity.this, 0, new Intent[]{intent}, 0);
        builder.setContentIntent(pendingIntent);     //设置点击通知之后的跳转
        builder.setDefaults(Notification.DEFAULT_ALL);   //设置以上三种提示
        final Notification notification = builder.build();   //4.1以下用builder.getNotification()
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        manager.notify(0, notification);   //第一个参数为该通知的序号

        SharedPreferences preferences = getSharedPreferences("myPref1", MODE_PRIVATE);  //将学习计划信息存储起来
        final SharedPreferences.Editor editor1 = preferences.edit();
        String time_set = preferences.getString("time", "设置提醒");

        final Button btnAlarmSet = (Button) customView_right.findViewById(R.id.btn_alarm_set);
        btnAlarmSet.setText(time_set);
        btnAlarmSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (hourOfDay < 10 && minute < 10) {
                            String time = " 0" + hourOfDay + ":0" + minute + " ";
                            btnAlarmSet.setText(time);
                            editor1.putString("time", time);
                            editor1.commit();
                        } else if (hourOfDay >= 10 && minute < 10) {
                            String time = " " + hourOfDay + ":0" + minute + " ";
                            btnAlarmSet.setText(time);
                            editor1.putString("time", time);
                            editor1.commit();
                        } else if (hourOfDay < 10 && minute >= 10) {
                            String time = " 0" + hourOfDay + ":" + minute + " ";
                            btnAlarmSet.setText(time);
                            editor1.putString("time", time);
                            editor1.commit();
                        } else {
                            String time = " " + hourOfDay + ":" + minute + " ";
                            btnAlarmSet.setText(time);
                            editor1.putString("time", time);
                            editor1.commit();
                        }
                        hour_set = hourOfDay;
                        minute_set = minute;
                        int totalTime = (hour_set - hour) * 60 * 60 + (minute_set - minute) * 60;
                        Long total = Long.parseLong(String.valueOf(totalTime));
                        Log.i("info", "!!!" + total);
                        alarm = new CountDownTimer(total * 1000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                Log.i("info", "!!!" + millisUntilFinished / 1000);
                            }

                            @Override
                            public void onFinish() {

                            }
                        }.start();
                    }
                }, hour, minute, true).show();
            }
        });

        Button btnAlarmCancle = (Button) customView_right.findViewById(R.id.btn_alarm_cancle);
        btnAlarmCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnAlarmSet.setText("设置提醒");

            }
        });

    }

    /**
     * 注册监听用户打开的APP信息广播
     */
    private void registerNotifyUserReceiver() {
        notifyUserReceiver = new NotifyUserReceiver();
        IntentFilter filter = new IntentFilter(this.getString(R.string.notify_user_action));
        this.registerReceiver(notifyUserReceiver, filter);
    }

    /**
     * 注册监听倒计时更新的广播
     */
    private void registerCountDownReceiver() {
        updateTimerReceiver = new UpdateTimerReceiver();
        IntentFilter filter = new IntentFilter(this.getString(R.string.countDownAction));
        this.registerReceiver(updateTimerReceiver, filter);
    }

    /**
     * 注册倒计时完成广播
     */
    private void registerCountFinishReceiver() {
        countFinishReceiver = new CountDownFinishReceiver();
        IntentFilter filter = new IntentFilter(this.getString(R.string.countFinishAction));
        this.registerReceiver(countFinishReceiver, filter);
    }

    /**
     * 设置倒计时时间
     */
    private void setCountTime() {
        View wheelView = LayoutInflater.from(this).inflate(R.layout.dialog_wheel_view, null);
        final WheelView wvHour = (WheelView) wheelView.findViewById(R.id.wv_hour);
        final WheelView wvMinute = (WheelView) wheelView.findViewById(R.id.wv_minute);
        final WheelView wvSecond = (WheelView) wheelView.findViewById(R.id.wv_second);

        TimerManager timerManager = new TimerManager();
        wvHour.setItems(timerManager.getAllHour());
        wvMinute.setItems(timerManager.getAllMinute());
        wvSecond.setItems(timerManager.getAllSecond());
        //初始时间选择00:00:00
        wvHour.setSelection(0);
        wvMinute.setSelection(0);
        wvSecond.setSelection(0);


        new AlertDialog.Builder(this)
                .setTitle(this.getString(R.string.dialog_wv_set_count_time_title))
                .setView(wheelView)
                .setPositiveButton(this.getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tvHour.setText(wvHour.getSelectedItem());
                        tvMinute.setText(wvMinute.getSelectedItem());
                        tvSecond.setText(wvSecond.getSelectedItem());
                    }
                })
                .show();
    }

    /**
     * 启动后台监测正在运行的程序服务
     */
    public void stratMonitorService() {
        if (MonitorAppsService.isAccessibilitySettingsOn(this)) {
            MonitorAppsService.getInstance();
        } else {
            showNoPermission();
        }
    }

    private AlertDialog.Builder noPermissionDialog = null;

    /**
     * 提示并引导用户开启辅助权限
     */
    private void showNoPermission() {
        if (noPermissionDialog == null) {
            noPermissionDialog = new AlertDialog.Builder(this)
                    .setTitle(this.getString(R.string.no_permission))
                    .setMessage(this.getString(R.string.no_permission_advice))
                    .setPositiveButton(this.getString(R.string.go_open), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton(this.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
        }
        noPermissionDialog.show();
    }

    /**
     * 启动倒计时后台服务
     */
    public void startCountService() {
        Intent intent = new Intent(MainActivity.this, CountDownService.class);
        intent.putExtra(this.getString(R.string.countHour), Long.parseLong(tvHour.getText().toString()));
        intent.putExtra(this.getString(R.string.countMinute), Long.parseLong(tvMinute.getText().toString()));
        intent.putExtra(this.getString(R.string.countSecond), Long.parseLong(tvSecond.getText().toString()));
        //启动服务
        startService(intent);
        if (mDesktopLayout == null) {
            createWindowManager();
            createDesktopLayout();
            showDesk();
        }
    }

    /**
     * 先判断是否开启辅助权限，若已开启，则直接启动倒计时服务，若还没开启，等待开启完后再开启倒计时服务
     */
    private void countServiceEnable() {
        if (MonitorAppsService.isAccessibilitySettingsOn(MainActivity.this)) {
            startCountService();
        } else {
            setClickStart(true);
        }
    }

    /**
     * 创建悬浮窗体
     */
    private void createDesktopLayout() {
        mDesktopLayout = new Desklayout(this);
        mDesktopLayout.setOnTouchListener(new View.OnTouchListener() {
            float mTouchStartX;
            float mTouchStartY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 获取相对屏幕的坐标，即以屏幕左上角为原点
                x = event.getRawX();
                y = event.getRawY() - top; // 25是系统状态栏的高度
                Log.i("startP", "startX" + mTouchStartX + "====startY" + mTouchStartY);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // 获取相对View的坐标，即以此View左上角为原点
                        mTouchStartX = event.getX();
                        mTouchStartY = event.getY();
                        Log.i("startP", "startX" + mTouchStartX + "====startY" + mTouchStartY);
                        long end = System.currentTimeMillis() - startTime;
                        // 双击的间隔在 300ms以下
                        if (end < 300) {
                            Log.i("info", "!!!");
//                            closeDesk();
                        }
                        startTime = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // 更新浮动窗口位置参数
                        mLayout.x = (int) (x - mTouchStartX);
                        mLayout.y = (int) (y - mTouchStartY);
                        mWindowManager.updateViewLayout(v, mLayout);
                        break;
                    case MotionEvent.ACTION_UP:
                        // 更新浮动窗口位置参数
                        mLayout.x = (int) (x - mTouchStartX);
                        mLayout.y = (int) (y - mTouchStartY);
                        mWindowManager.updateViewLayout(v, mLayout);
                        // 可以在此记录最后一次的位置
                        mTouchStartX = mTouchStartY = 0;
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Rect rect = new Rect();
        // /取得整个视图部分,注意，如果你要设置标题样式，这个必须出现在标题样式之后，否则会出错
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        top = rect.top;//状态栏的高度，所以rect.height,rect.width分别是系统的高度的宽度
        Log.i("top", "" + top);
    }

    /**
     * 显示DesktopLayout
     */
    private void showDesk() {
        mWindowManager.addView(mDesktopLayout, mLayout);
    }

    /**
     * 关闭DesktopLayout
     */
    private void closeDesk() {
        mWindowManager.removeView(mDesktopLayout);
    }

    /**
     * 设置WindowManager
     */
    private void createWindowManager() {
        // 取得系统窗体
        mWindowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        // 窗体的布局样式
        mLayout = new WindowManager.LayoutParams();
        // 设置窗体显示类型——TYPE_SYSTEM_ALERT(系统提示)
        mLayout.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        // 设置窗体焦点及触摸：
        // FLAG_NOT_FOCUSABLE(不能获得按键输入焦点)
        mLayout.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // 设置显示的模式
        mLayout.format = PixelFormat.RGBA_8888;
        // 设置对齐的方法
        mLayout.gravity = Gravity.TOP | Gravity.LEFT;
        // 设置窗体宽度和高度
        mLayout.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mLayout.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //设置出现的位置
        mLayout.x = 200;
        mLayout.y = 300;
        //设置出现的动画
    }

    @Override    //左下角菜单
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.optionsmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.end) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override   //程序后台运行
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 设置TextView 小时的值
     *
     * @param text
     */
    public void setTvHour(String text) {
        if (tvHour != null) {
            tvHour.setText(text);
        }
    }

    /**
     * 设置TextView 分钟的值
     *
     * @param text
     */
    public void setTvMinute(String text) {
        if (tvMinute != null) {
            tvMinute.setText(text);
        }
    }

    /**
     * 设置TextView 秒的值
     *
     * @param text
     */

    public void setTvSecond(String text) {
        if (tvSecond != null) {
            tvSecond.setText(text);
        }
    }

    /**
     * 设置时间选择的使能
     *
     * @param set
     */
    public void setSetedCountTime(boolean set) {
        isSetedCountTime = set;
    }

    public boolean getIsSetedCountTime() {
        return isSetedCountTime;
    }

    /**
     * 设置悬浮窗倒计时的时间
     *
     * @param text
     * @see Desklayout#setCountTxt(String)
     */
    public void setDesklayoutCountText(String text) {
        if (mDesktopLayout != null) {
            mDesktopLayout.setCountTxt(text);
        }
    }

    /**
     * 设置悬浮窗弹出窗的可见性
     *
     * @param vis
     * @see Desklayout#setTvNotifyVisiable(int)
     */
    public void setDesklayoutNotifyVisiable(int vis) {
        if (mDesktopLayout != null) {
            mDesktopLayout.setTvNotifyVisiable(vis);
        }
    }

    /**
     * 设置悬浮窗的弹出窗的内容
     *
     * @param text
     * @see Desklayout#setTvNotifyText(CharSequence)
     */
    public void setDesklayoutNotifyText(CharSequence text) {
        if (mDesktopLayout != null) {
            mDesktopLayout.setTvNotifyText(text);
        }
    }

    public boolean isClickStart() {
        return isClickStart;
    }

    public void setClickStart(boolean clickStart) {
        isClickStart = clickStart;
    }

    /**
     * 设置了累计时间
     */
    public void setTvTotalTime(String text) {
        if (text != null) {
            tvTotalTime.setText(text);
        }
    }

}
