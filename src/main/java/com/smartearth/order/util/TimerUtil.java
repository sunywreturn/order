package com.smartearth.order.util;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

public class TimerUtil {

    private static Timer timer = new Timer();

    public static Map<String, TimerTask> timerMap = new ConcurrentHashMap<>();

    /**
     * 创建一个新的定时任务，并将其添加到定时任务映射中。
     *
     * @param key 定时任务的唯一标识
     * @param date 定时任务的执行时间，格式为日期字符串
     * @param timerTask 要执行的定时任务
     */
    public static void newTimerTask(String key,String date,TimerTask timerTask){
        //创建 `TimeUtil` 实例以处理时间相关操作。
        TimeUtil timeUtil = new TimeUtil();
        //使用 `TimeUtil` 将日期字符串转换为 `Date` 对象，并安排定时任务。
        timer.schedule(timerTask,timeUtil.getDate(date));
        timerMap.put(key,timerTask);
    }

}
