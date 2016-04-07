package com.iwin.istudy.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import com.iwin.istudy.R;

public class CountDownService extends Service {

    private CountDownBinder countBinder = new CountDownBinder();
    private long countMinute;
    private long countSecond;

    public CountDownService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        countMinute = intent.getLongExtra(getResources().getString(R.string.countMinute),0);
        countSecond = intent.getLongExtra(getResources().getString(R.string.countSecond),0);
        Log.w("count", "countMinute:"+countMinute+"===="+"countSecond"+countSecond);
        return countBinder;
    }

    public class CountDownBinder extends Binder {

        public String minute;
        public String second;

        public void startCount() {
            long totalCount = (countMinute*60 + countSecond) * 1000;
            Log.w("count", "totalCount" + totalCount);
            CountDownTimer timer = new CountDownTimer(totalCount,1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    minute = String.valueOf((millisUntilFinished/1000) / 60);
                    second = String.valueOf((millisUntilFinished/1000) % 60);

                    if (minute.length()==1){
                        minute = "0" + minute;
                    }
                    if (second.length()==1){
                        second = "0" + second;
                    }
                    Log.w("count",minute+":"+second);
                    //发送更新倒计时广播给Activity
                    sendUptateTimerReceiver();
                }

                @Override
                public void onFinish() {
                    minute = "00";
                    second = "00";
                    //发送更新倒计时广播给Activity
                    sendUptateTimerReceiver();
                }
            };
            timer.start();
        }

        private void sendUptateTimerReceiver() {
            Intent intent = new Intent();
            intent.putExtra(getResources().getString(R.string.countMinute),minute);
            intent.putExtra(getResources().getString(R.string.countSecond),second);
            intent.setAction(getResources().getString(R.string.countDownAction));
            sendBroadcast(intent);
        }
    }
}
