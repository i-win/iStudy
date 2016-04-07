package com.iwin.istudy.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.iwin.istudy.R;
import com.iwin.istudy.receiver.UpdateTimerReceiver;
import com.iwin.istudy.service.CountDownService;

public class MainActivity extends BaseActivity {

    public TextView tvMinute;
    public TextView tvSecond;
    private Button btnStartCount;
    private CountDownService.CountDownBinder countBinder;
    private UpdateTimerReceiver updateTimerReceiver;

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
        this.registerReceiver(updateTimerReceiver,filter);
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
                countBinder = (CountDownService.CountDownBinder) service;
                countBinder.startCount();

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        //启动服务
        bindService(intent,connection,BIND_AUTO_CREATE);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(updateTimerReceiver);
    }
}
