package com.iwin.istudy.engine;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sxq on 2016/4/12.
 */
public class TimerManager {

    /**
     * @return 返回00~12
     */
    public List<String> getAllHour(){
        List<String> hour = new ArrayList<>();
        for (int i=0;i<10;i++){
            hour.add("0"+i);
        }
        for (int i=10;i<=12;i++){
            hour.add(""+i);
        }
        return  hour;
    }

    /**
     *
     * @return 返回00~60
     */
    public List<String> getAllMinute(){
        List<String> minute = new ArrayList<>();
        for (int i=0;i<10;i++){
            minute.add("0"+i);
        }
        for (int i=10;i<60;i++){
            minute.add(""+i);
        }
        return  minute;
    }

    /**
     *
     * @return 返回00~60
     */
    public List<String> getAllSecond(){
        List<String> second = new ArrayList<>();
        for (int i=0;i<10;i++){
            second.add("0"+i);
        }
        for (int i=10;i<60;i++){
            second.add(""+i);
        }
        return  second;
    }
}
