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
    private long countHour;
    private long countMinute;
    private long countSecond;

    public CountDownService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        countHour = intent.getLongExtra(getResources().getString(R.string.countHour), 0);
        countMinute = intent.getLongExtra(getResources().getString(R.string.countMinute), 0);
        countSecond = intent.getLongExtra(getResources().getString(R.string.countSecond), 0);
        Log.i("info", "!!!" + countHour + " " + countMinute + " " + countSecond);
        return countBinder;
    }

    public class CountDownBinder extends Binder {

        public String hour;
        public String minute;
        public String second;

        public void startCount() {
            long totalCount = (countHour * 3600 + countMinute * 60 + countSecond) * 1000;
            Log.w("totalCount", "totalCount" + totalCount);
            CountDownTimer timer = new CountDownTimer(totalCount, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    second = String.valueOf((millisUntilFinished / 1000) % 60);
                    int second_change = Integer.parseInt(second);
                    minute = String.valueOf((((millisUntilFinished / 1000) - second_change) / 60) % 60);
                    int minute_change = Integer.parseInt(minute);
                    hour = String.valueOf(((millisUntilFinished / 1000) - second_change - minute_change * 60) / 3600);
                    if (hour.length() == 1) {
                        hour = "0" + hour;
                    }
                    if (minute.length() == 1) {
                        minute = "0" + minute;
                    }
                    if (second.length() == 1) {
                        second = "0" + second;
                    }
                    Log.w("count", minute + ":" + second);
                    //发送更新倒计时广播给Activity
                    sendUptateTimerReceiver();
                }

                @Override
                public void onFinish() {
                    hour = "00";
                    minute = "00";
                    second = "00";
                    //发送更新倒计时广播给Activity
                    sendUptateTimerReceiver();
                    sendFinishTimerReceiver();
                }
            };
            timer.start();
        }

        /**
         * 倒计时完成时发送广播
         * 所有需要接收计时完成的广播均可注册广播，监听R.string.countFinishAction
         */
        private void sendFinishTimerReceiver() {
            Intent intent = new Intent();
            intent.setAction(getResources().getString(R.string.countFinishAction));
            sendBroadcast(intent);
        }


        /**
         * 倒计时广播，每计时1s，发送广播
         * 所有需要接收计时时时更新数据的均可注册广播，监听R.string.countDownAction
         */
        private void sendUptateTimerReceiver() {
            Intent intent = new Intent();
            intent.putExtra(getResources().getString(R.string.countHour), hour);
            intent.putExtra(getResources().getString(R.string.countMinute), minute);
            intent.putExtra(getResources().getString(R.string.countSecond), second);
            intent.setAction(getResources().getString(R.string.countDownAction));
            sendBroadcast(intent);
        }
    }
}
