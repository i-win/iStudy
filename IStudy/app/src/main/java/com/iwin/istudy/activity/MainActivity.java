package com.iwin.istudy.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.IBinder;
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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
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

public class MainActivity extends BaseActivity {

    private UpdateTimerReceiver updateTimerReceiver;
    private NotifyUserReceiver notifyUserReceiver;
    private CountDownFinishReceiver countFinishReceiver;
    private CountDownService.CountDownBinder countBinder;

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayout;
    private Desklayout mDesktopLayout;
    private boolean isSetedCountTime = false;
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
    }

    private TextView tvHour;
    private TextView tvMinute;
    private TextView tvSecond;
    private LinearLayout linlayoutCountTime;
    private Button btnStartCount;
    private ImageButton btnChangeColor;
    private PopupWindow popupwindow;
    private View layout_main;
    private void initView() {
        tvHour = (TextView) findViewById(R.id.tv_hour);
        tvMinute = (TextView) findViewById(R.id.tv_minute);
        tvSecond = (TextView) findViewById(R.id.tv_second);

        linlayoutCountTime = (LinearLayout) findViewById(R.id.linlayout_count_time);
        linlayoutCountTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSetedCountTime){
                    setCountTime();
                }
            }
        });

        btnStartCount = (Button) findViewById(R.id.btn_start_count);
        btnStartCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCountService();
                stratMonitorService();

            }
        });

        btnChangeColor = (ImageButton)findViewById(R.id.btn_change_color);
        btnChangeColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupwindow != null&&popupwindow.isShowing()) {
                    popupwindow.dismiss();
                    return;
                } else {
                    initmPopupWindowView();
                    popupwindow.showAsDropDown(v, 0, 5);
                }
            }
        });
    }

    /**下拉框属性*/
    public void initmPopupWindowView() {
        // // 获取自定义布局文件pop.xml的视图
        View customView = getLayoutInflater().inflate(R.layout.popcolor_item, null, false);
        // 创建PopupWindow实例,200,150分别是宽度和高度
        popupwindow = new PopupWindow(customView, 150, 200);
        // 设置动画效果 [R.style.AnimationFade 是自己事先定义好的]
        popupwindow.setAnimationStyle(R.style.AnimationFade);
        // 自定义view添加触摸事件
        customView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (popupwindow != null && popupwindow.isShowing()) {
                    popupwindow.dismiss();
                    popupwindow = null;
                }
                return false;
            }
        });
        /** 在这里可以实现自定义视图的功能 */
        layout_main = findViewById(R.id.layout_main);
        Button btnColorRed = (Button)customView.findViewById(R.id.btn_color_red);
        btnColorRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_main.setBackgroundColor(Color.RED);
            }
        });
        Button btnColorYelllow = (Button)customView.findViewById(R.id.btn_color_yellow);
        btnColorYelllow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_main.setBackgroundColor(Color.YELLOW);
            }
        });
/*        Button btton2 = (Button) customView.findViewById(R.id.button2);
        Button btton3 = (Button) customView.findViewById(R.id.button3);
        Button btton4 = (Button) customView.findViewById(R.id.button4);*/
    }

    /**
     * 注册监听用户打开的APP信息广播
     */
    private void registerNotifyUserReceiver() {
        notifyUserReceiver = new NotifyUserReceiver();
        IntentFilter filter = new IntentFilter(this.getString(R.string.notify_user_action));
        this.registerReceiver(notifyUserReceiver,filter);
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
        this.registerReceiver(countFinishReceiver,filter);
    }

    /**
     * 设置倒计时时间
     */
    private void setCountTime() {
        View wheelView = LayoutInflater.from(this).inflate(R.layout.dialog_wheel_view,null);
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
     * 后台监测正在运行的程序服务
     */
    private void stratMonitorService() {
        Intent intent = new Intent(MainActivity.this,MonitorAppsService.class);
        startService(intent);
    }

    /**
     * 倒计时后台服务
     */
    private void startCountService() {
        Intent intent = new Intent(MainActivity.this, CountDownService.class);
        intent.putExtra(this.getString(R.string.countHour), Long.parseLong(tvHour.getText().toString()));
        intent.putExtra(this.getString(R.string.countMinute), Long.parseLong(tvMinute.getText().toString()));
        intent.putExtra(this.getString(R.string.countSecond), Long.parseLong(tvSecond.getText().toString()));
        ServiceConnection connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d("count", "更新");
                //服务连接成功后启动倒计时
                createWindowManager();
                createDesktopLayout();
                showDesk();
                countBinder = (CountDownService.CountDownBinder) service;
                countBinder.startCount();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        //启动服务
        bindService(intent, connection, BIND_AUTO_CREATE);

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
     * @param text
     */
    public void setTvHour(String text){
        if (tvHour != null){
            tvHour.setText(text);
        }
    }

    /**
     * 设置TextView 分钟的值
     * @param text
     */
    public void setTvMinute(String text) {
        if (tvMinute != null){
            tvMinute.setText(text);
        }
    }

    /**
     * 设置TextView 秒的值
     * @param text
     */

    public void setTvSecond(String text) {
        if (tvSecond != null){
            tvSecond.setText(text);
        }
    }

    /**
     * 设置时间选择的使能
     * @param set
     */
    public void setSetedCountTime(boolean set) {
        isSetedCountTime = set;
    }

    /**
     * 设置悬浮窗倒计时的时间
     * @see Desklayout#setCountTxt(String)
     * @param text
     */
    public void setDesklayoutCountText(String text){
        if (mDesktopLayout != null){
            mDesktopLayout.setCountTxt(text);
        }
    }

    /**
     * 设置悬浮窗弹出窗的可见性
     * @see Desklayout#setTvNotifyVisiable(int)
     * @param vis
     */
    public void setDesklayoutNotifyVisiable(int vis){
        if (mDesktopLayout != null){
            mDesktopLayout.setTvNotifyVisiable(vis);
        }
    }

    /**
     * 设置悬浮窗的弹出窗的内容
     * @see Desklayout#setTvNotifyText(CharSequence)
     * @param text
     */
    public void setDesklayoutNotifyText(CharSequence text){
        if (mDesktopLayout != null){
            mDesktopLayout.setTvNotifyText(text);
        }
    }
}
