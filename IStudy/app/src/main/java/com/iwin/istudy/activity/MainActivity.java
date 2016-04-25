package com.iwin.istudy.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.iwin.istudy.R;
import com.iwin.istudy.engine.SquirrelPet;
import com.iwin.istudy.engine.TimerManager;
import com.iwin.istudy.engine.XbDogPet;
import com.iwin.istudy.receiver.CountDownFinishReceiver;
import com.iwin.istudy.receiver.NotifyUserReceiver;
import com.iwin.istudy.receiver.UpdateTimerReceiver;
import com.iwin.istudy.service.CountDownService;
import com.iwin.istudy.service.MonitorAppsService;
import com.iwin.istudy.ui.NotifyLayout;
import com.iwin.istudy.ui.PetLayout;
import com.iwin.istudy.ui.WheelView;
import com.iwin.istudy.util.ScreenUtils;
import com.kyleduo.switchbutton.SwitchButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";

    private UpdateTimerReceiver updateTimerReceiver;
    private NotifyUserReceiver notifyUserReceiver;
    private CountDownFinishReceiver countFinishReceiver;
    private WindowManager mWindowManager;

    private WindowManager.LayoutParams notifyParams;
    private WindowManager.LayoutParams petParams;
    private NotifyLayout notifyLayout;
    private PetLayout petLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        registerCountDownReceiver();
        registerCountFinishReceiver();
        registerNotifyUserReceiver();

        loadLocalData();

        overridePendingTransition(R.anim.activity_output, R.anim.activity_input);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
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
    private GifImageView imgPetPick;
    private ImageButton btnAlarm;
    private Button btnAlarmSet;
    private ScrollView scrollPlan;
    private TextView tvTotalTime;

    /**
     * 初始化控件，绑定事件
     */
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
                if (!isZeroTime()) {
                    stratMonitorService();
                    countServiceEnable();
                }
            }

        });

        btnLeftPlan = (ImageButton) findViewById(R.id.btn_left_plan);
        btnLeftPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchLeftPlan();
            }
        });

        imgPetPick = (GifImageView) findViewById(R.id.img_pet_pick);
        imgPetPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogPet();
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

        retrieveSystemWindowManager();
    }

    /**
     * 加载本地数据，如取出SharedPreferences、SQLite等数据
     */
    private void loadLocalData() {
        retrieveTotalTimeData();
    }

    /**
     * 取回本地SharedPreferences累计时间的数据
     */
    private void retrieveTotalTimeData() {
        SharedPreferences preferences = getSharedPreferences("myPref2", MODE_PRIVATE);  //当前程序才能读取
        int day = preferences.getInt("day", 0);
        int hour = preferences.getInt("hour", 0);
        int minute = preferences.getInt("minute", 0);
        int second = preferences.getInt("second", 0);
        String totalTime = day + "天" + hour + "小时" + minute + "分钟" + second + "秒";
        tvTotalTime = (TextView) findViewById(R.id.tv_total_time);
        tvTotalTime.setText(totalTime);
    }

    /**
     * 打开或关闭左侧任务计划弹窗
     */
    private void switchLeftPlan() {
        final SharedPreferences preferences = getSharedPreferences("myPref", MODE_PRIVATE);  //将学习计划信息存储起来
        final SharedPreferences.Editor editor = preferences.edit();
        final String plan = preferences.getString("plan", "");

        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);    //引入自定义布局
        View view_dialog_plan = inflater.inflate(R.layout.dialog_plan, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(view_dialog_plan);
        final AlertDialog dialog_plan = builder.create();  //创建一个dialog
        dialog_plan.show();

        Window dialogPlanWindow = dialog_plan.getWindow();
        dialogPlanWindow.setWindowAnimations(R.style.dialogPlanAnim);
        WindowManager.LayoutParams lp = dialogPlanWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogPlanWindow.setAttributes(lp);

        edtPlan = (EditText) view_dialog_plan.findViewById(R.id.edt_plan);
        edtPlan.setText(plan);

        btnEdit = (ImageButton) view_dialog_plan.findViewById(R.id.btn_edit);
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

    private SwitchButton swtbtn_pet;
    private GridView gridView_pet;
    private Button btnPetCommit;

    int selectPosition;
    /**
     * 初始化设置宠物下拉框属性
     */
    public void showDialogPet() {
        //初始化弹窗控件
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);    //引入自定义布局
        View view_pet_pick = inflater.inflate(R.layout.dialog_pet, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(view_pet_pick);
        final AlertDialog dialog_pet = builder.create();  //创建一个dialog
        dialog_pet.show();
        //初始化弹窗布局参数
        Window dialogPlanWindow = dialog_pet.getWindow();
        dialogPlanWindow.setWindowAnimations(R.style.dialogPetAnim);
        WindowManager.LayoutParams lp = dialogPlanWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogPlanWindow.setAttributes(lp);

        swtbtn_pet = (SwitchButton) view_pet_pick.findViewById(R.id.swtbtn_pet);
        swtbtn_pet.setThumbSize(40f,40f);
        swtbtn_pet.setChecked(isPetVisiable());
        swtbtn_pet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (petLayout == null){
                        petLayout = PetLayout.getInstance(MainActivity.this);
                    }
                    if (petParams == null){
                        initPetParams();
                    }
                    showPetWindow();
                } else {
                    closeWindowLayout(petLayout);
                }
            }
        });
        //设置弹窗数据
        gridView_pet = (GridView) dialog_pet.findViewById(R.id.gridview_pet);
        final List<Map<String,Object>> petList = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("image",R.drawable.xbdog1);
        petList.add(map);
        map = new HashMap<>();
        map.put("image",R.drawable.squirrel02);
        petList.add(map);
        SimpleAdapter adapter_pet = new SimpleAdapter(this, petList,
                R.layout.pet_pick, new String[]{"image"}, new int[]{R.id.image_pet});
        gridView_pet.getBackground().setAlpha(70);
        gridView_pet.setAdapter(adapter_pet);


        gridView_pet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectPosition = position;
//                Toast.makeText(MainActivity.this, "选择了宠物" + position, Toast.LENGTH_SHORT).show();
            }
        });

        btnPetCommit = (Button) dialog_pet.findViewById(R.id.btn_pet_commit);
        btnPetCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (petLayout == null){
                    petLayout = PetLayout.getInstance(MainActivity.this);
                }
                if (selectPosition == 0){
                    petLayout.setPetAttr(new XbDogPet());
                    imgPetPick.setImageResource(R.drawable.xbdog7);
                }else if (selectPosition == 1){
                    petLayout.setPetAttr(new SquirrelPet());
                    imgPetPick.setImageResource(R.drawable.squirrel26);
                }
                dialog_pet.cancel();
            }
        });
    }

    private AlarmManager alarmManager;
    private Calendar calendar = Calendar.getInstance(Locale.CHINESE);

    /**
     * 初始化右侧设置闹钟下拉框属性
     */
    public void initmPopupWindowView_right() {
        final View customView_right = getLayoutInflater().inflate(R.layout.right_alarm, null, false);
        // 创建PopupWindow实例,200,150分别是宽度和高度
        customView_right.getBackground().setAlpha(200);
        popupWindow_right = new PopupWindow(customView_right, RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
        // 设置动画效果 [R.style.AnimationFade 是自己事先定义好的]
        popupWindow_right.setAnimationStyle(R.style.AnimationFade_right);

        //点击空白处窗口退出
        popupWindow_right.setFocusable(true);
        popupWindow_right.setBackgroundDrawable(new BitmapDrawable());

        SharedPreferences preferences = getSharedPreferences("myPref1", MODE_PRIVATE);  //将学习计划信息存储起来
        final SharedPreferences.Editor editor1 = preferences.edit();
        String time_set = preferences.getString("time", "设置提醒");

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent();
        intent.setAction(this.getString(R.string.alarm_goes_off));
        final PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        btnAlarmSet = (Button) customView_right.findViewById(R.id.btn_alarm_set);
        btnAlarmSet.setText(time_set);
        btnAlarmSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date();
                calendar.setTime(date);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Toast toast = Toast.makeText(MainActivity.this, "设置提醒成功", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
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
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        calendar.set(Calendar.SECOND, 0);
                        Log.d(TAG, "当前时间:" + calendar.getTime() + "||" + calendar.getTimeInMillis());
                        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                    }
                }, hour, minute, true).show();
            }
        });

        Button btnAlarmCancle = (Button) customView_right.findViewById(R.id.btn_alarm_cancle);
        btnAlarmCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnAlarmSet.setText("设置提醒");
                editor1.putString("time", "设置提醒");
                editor1.commit();
                alarmManager.cancel(pendingIntent);
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
     * 弹出时间选择器，设置倒计时时间
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
        if (petLayout == null) {
            petLayout = PetLayout.getInstance(MainActivity.this);
        }
        if (petParams == null){
            initPetParams();
        }
        showPetWindow();
        petLayout.setPetAction(PetLayout.STUDY);
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

    private int top;

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
     * 首次点击屏幕的时间，用来判断双击使用
     */
    private long startTime;

    /**
     * x:屏幕的宽
     * y：屏幕的高
     */
    float x, y;

    /**
     * 取得系统窗体
     */
    private void retrieveSystemWindowManager() {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        }
    }

    /**
     * 显示气泡弹窗
     */
    private void showNotifyWindow() {
        notifyParams = new WindowManager.LayoutParams();
        notifyParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        notifyParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        notifyParams.format = PixelFormat.RGBA_8888;
        notifyParams.gravity = Gravity.TOP | Gravity.LEFT;
        notifyParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        notifyParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        //petlayout控件的坐标点
        int[] location = new int[2];
        petLayout.getLocationOnScreen(location);
        //petlayout控件所在的区域(屏幕分成3行2列）
        int[] area = ScreenUtils.inAreaOfScreen(MainActivity.this,
                (location[0] + (petLayout.getWidth() / 2)), location[1], 2, 3);
        Log.d(TAG, "宠物位置:" + location[0] + "  " + location[1]);
        Log.d(TAG, "宠物位置在第" + area[0] + "行，第" + area[1] + "列；");
        Log.d(TAG, "宠物大小:" + petLayout.getWidth() + " * " + petLayout.getHeight());

        //设置出现的位置
        if (area[1] == 1) {
            //petlayout在第一列时
            if (area[0] > 1) {
                //petlayout在第一行以下时
                notifyLayout.setBackground(R.drawable.dialog_righttop);
                notifyParams.x = location[0] + petLayout.getWidth();
                notifyParams.y = location[1] - petLayout.getHeight();
            } else {
                //petlayout在第一行时
                notifyLayout.setBackground(R.drawable.dialog_rightbottom);
                notifyParams.x = location[0] + petLayout.getWidth();
                notifyParams.y = location[1] + petLayout.getHeight() / 2;
            }
        } else {
            //petlayout在第二列时
            if (area[0] > 1) {
                //petlayout在第一行以下时
                notifyLayout.setBackground(R.drawable.dialog_lefttop);
                notifyParams.x = location[0] - notifyLayout.getNotityWidth();
                notifyParams.y = location[1] - petLayout.getHeight();
            } else {
                //petlayout在第一行时
                notifyLayout.setBackground(R.drawable.dialog_leftbottom);
                notifyParams.x = location[0] - notifyLayout.getNotityWidth();
                notifyParams.y = location[1] + petLayout.getHeight() / 2;
            }
        }
        Log.d(TAG, "弹窗位置:" + notifyParams.x + "  " + notifyParams.y);
        Log.d(TAG, "弹窗大小" + notifyLayout.getNotityWidth() + " * " + notifyLayout.getNotifyHeight());
        retrieveSystemWindowManager();
        try {
            mWindowManager.addView(notifyLayout, notifyParams);
        }catch (IllegalStateException e){
            e.printStackTrace();
        }
    }

    /**
     * 初始化桌面宠物的布局参数
     */
    public void initPetParams() {
        petParams = new WindowManager.LayoutParams();
        petParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        petParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        petParams.format = PixelFormat.RGBA_8888;
        petParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        petParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //设置坐标原点
        petParams.gravity = Gravity.TOP | Gravity.LEFT;
        petParams.x = ScreenUtils.getScreenWidth(MainActivity.this) / 2;
        petParams.y = ScreenUtils.getScreenHeight(MainActivity.this) / 2;
    }

    /**
     * 显示桌面宠物
     */
    public void showPetWindow() {
        petLayout.setOnTouchListener(new View.OnTouchListener() {
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
                        setNotifyLayoutVisiable(View.GONE);
                        mTouchStartX = event.getX();
                        mTouchStartY = event.getY();
                        Log.i("startP", "startX" + mTouchStartX + "====startY" + mTouchStartY);
                        long end = System.currentTimeMillis() - startTime;
                        // 双击的间隔在 500ms以下
                        if (end < 500) {
                            Log.i("info", "!!!");
                            setPetAction(PetLayout.RANDOM);
                        }
                        startTime = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // 更新浮动窗口位置参数
                        petParams.x = (int) (x - mTouchStartX);
                        petParams.y = (int) (y - mTouchStartY);
                        mWindowManager.updateViewLayout(v, petParams);
                        break;
                    case MotionEvent.ACTION_UP:
                        // 更新浮动窗口位置参数
                        petParams.x = (int) (x - mTouchStartX);
                        petParams.y = (int) (y - mTouchStartY);
                        mWindowManager.updateViewLayout(v, petParams);
                        // 可以在此记录最后一次的位置
                        mTouchStartX = mTouchStartY = 0;
                        break;
                }
                return true;
            }
        });

        retrieveSystemWindowManager();
        try {
            mWindowManager.addView(petLayout, petParams);
        }catch (IllegalStateException e){
            e.printStackTrace();
        }
    }

    public void closeWindowLayout(View view) {
        retrieveSystemWindowManager();
        try {
            mWindowManager.removeView(view);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "关闭悬浮窗失败");
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.end) {
            setNotifyLayoutVisiable(View.GONE);
            closeWindowLayout(petLayout);
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

    private boolean isSetedCountTime = false;

    /**
     * 设置时间选择的使能
     *
     * @param set
     */
    public void setSetedCountTime(boolean set) {
        isSetedCountTime = set;
    }

    /**
     * 返回时间选择的使能
     *
     * @return 若已设置完时间返回True，否则返回False
     */
    public boolean getIsSetedCountTime() {
        return isSetedCountTime;
    }

    /**
     * 设置悬浮窗倒计时的时间
     *
     * @param text
     * @see PetLayout#setTvPetTime(String)
     */
    public void setPetTimeText(String text) {
        if (petLayout != null) {
            petLayout.setTvPetTime(text);
        }
    }

    /**
     * 判断桌面宠物是否可见
     * @return
     */
    public boolean isPetVisiable(){
        if (petLayout != null && petParams!= null){
            try {
                mWindowManager.addView(petLayout,petParams);
                mWindowManager.removeView(petLayout);
                return false;
            }catch (IllegalStateException e){
                e.printStackTrace();
                return true;
            }
        }else {
            return false;
        }
    }

    /**
     * 设置悬浮窗弹出窗的可见性
     *
     * @param vis
     * @see NotifyLayout#setNotifyVisiable(int)
     */
    public void setNotifyLayoutVisiable(int vis) {
        if (notifyLayout == null) {
            notifyLayout = new NotifyLayout(MainActivity.this);
            notifyLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setNotifyLayoutVisiable(View.GONE);
                }
            });
        }
        if (vis == View.GONE) {
            if (notifyLayout.getNotifyVisiable() == View.VISIBLE) {
                try {
                    mWindowManager.removeView(notifyLayout);
                }catch (IllegalStateException e){
                    e.printStackTrace();
                }
                notifyLayout.setNotifyVisiable(View.GONE);
            }
        } else if (vis == View.VISIBLE) {
            Log.d(TAG, "设置对话框显示" + notifyLayout.getVisibility());
            if (notifyLayout.getNotifyVisiable() == View.GONE && isPetVisiable()) {
                notifyLayout.setNotifyVisiable(View.VISIBLE);
                showNotifyWindow();
            }
        }
    }

    /**
     * 设置悬浮窗的弹出窗的内容
     *
     * @param text
     * @see NotifyLayout#setTvNotifyText(CharSequence)
     */
    public void setNotifyLayoutText(CharSequence text) {
        if (notifyLayout != null) {
            notifyLayout.setTvNotifyText(text);
        }
    }

    public void setPetAction(int action) {
        if (petLayout != null) {
            petLayout.setPetAction(action);
        }
    }

    /**
     * 判断设置的倒计时时间是否为0
     *
     * @return 为0返回true
     */
    public boolean isZeroTime() {
        String time = tvHour.getText().toString()
                + tvMinute.getText().toString() + tvSecond.getText().toString();
        if (time.equals("000000")) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isClickStart = false;

    /**
     * 返回是否点击启动
     *
     * @return 若点击启动，返回true,否则返回false
     */
    public boolean isClickStart() {
        return isClickStart;
    }

    /**
     * 设置是否点击启动状态
     *
     * @param clickStart
     */
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

    /**
     * 关闭其他应用，实际上并没有关闭应用，只是模拟点击Home键，将屏幕退回到主界面，以此模拟实现关闭效果
     *
     * @param packageName 要关闭的应用的包名
     */
    public void closeApp(String packageName) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        (getApplicationContext()).startActivity(intent);
        Log.d(TAG, "关闭应用:" + packageName);
    }
}
