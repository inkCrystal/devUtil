package com.dev.commons.datetime;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author : JasonMao
 * @version : 1.0
 * @date : 2023/3/17 14:49
 * @description :
 */
public class TimeMillisClock {

    private final int period;

    private final AtomicLong now;

    ScheduledThreadPoolExecutor scheduler ;

    /**是否使用  System.currentTimeMillis() 来获取毫秒时间戳---by JasonMao @ 2023/3/17 17:25 */
    private static boolean usingSystemCurrentMillis = false;

    /**
     * 设置是否使用 System.currentTimeMillis() 获得毫秒时间戳
     * @param b
     */
    public final void setUsingSystemMillis(boolean b){
        usingSystemCurrentMillis = b;
        if(b){
            if (scheduler != null && scheduler.getActiveCount() > 0 ) {
                scheduler.shutdownNow();
            }
        }

    }

    private static class InstanceHolder {
        private static final TimeMillisClock INSTANCE = new TimeMillisClock(1);
    }

    private static final TimeMillisClock getInstance(){
        return InstanceHolder.INSTANCE;
    }
    private TimeMillisClock(int period) {
        this.period = period;
        this.now = new AtomicLong(System.currentTimeMillis());
        this.start();
    }


    private void  start(){
        scheduler = new ScheduledThreadPoolExecutor(1, r -> {
            Thread thread = new Thread(r, "MILLIS-CLOCK");
            thread.setDaemon(true);
            return thread;
        });
        scheduler.scheduleAtFixedRate(() -> {
            now.set(System.currentTimeMillis());
        }, period, period, TimeUnit.MILLISECONDS);
    }

    /**
     * 只有当 始终启动成功后才会返回！
     * @return
     */
    public static final long currentMillis(){
        return getInstance().now.get();
    }


    /**same as   currentMillis---by JasonMao @ 2023/1/30 19:00 */
    public static final long currentTimeMillis(){
        return currentMillis();
    }


    public static void main(String[] args) {

    }

}
