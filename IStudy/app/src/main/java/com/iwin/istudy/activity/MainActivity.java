package com.iwin.istudy.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.iwin.istudy.R;
import com.iwin.istudy.receiver.UpdateTimerReceiver;
import com.iwin.istudy.service.CountDownService;
import com.iwin.istudy.ui.Desklayout;

public class MainActivity extends BaseActivity {

    public TextView tvMinute;
    public TextView tvSecond;
    private Button btnStartCount;
    private CountDownService.CountDownBinder countBinder;
    private UpdateTimerReceiver updateTimerReceiver;

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayout;
    public Desklayout mDesktopLayout;
    private long startTime;
    // 声明屏幕的宽高
    float x, y;
    int top;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initView();

        //注册监听倒计时更新的广播
        registerCountDownReceiver();


    }

    private void registerCountDownReceiver() {
        updateTimerReceiver = new UpdateTimerReceiver();
        IntentFilter filter = new IntentFilter(this.getString(R.string.countDownAction));
        this.registerReceiver(updateTimerReceiver, filter);
    }

    private void initView() {
        tvMinute = (TextView) findViewById(R.id.tv_minute);
        tvSecond = (TextView) findViewById(R.id.tv_second);
        btnStartCount = (Button) findViewById(R.id.btn_start_count);

        btnStartCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startCountService();

            }
        });
    }

    private void startCountService() {
        Intent intent = new Intent(MainActivity.this,CountDownService.class);
        intent.putExtra(this.getString(R.string.countMinute),Long.parseLong(tvMinute.getText().toString()));
        intent.putExtra(this.getString(R.string.countSecond),Long.parseLong(tvSecond.getText().toString()));

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(updateTimerReceiver);
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
}
