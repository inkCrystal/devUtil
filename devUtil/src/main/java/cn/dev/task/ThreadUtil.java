package cn.dev.task;

import java.util.concurrent.TimeUnit;

public class ThreadUtil {
    
    /**调用thread.sleep   ---by jason @ 2023/3/20 13:57 */
    public static void sleep(int time , TimeUnit timeUnit){
        try{
            timeUnit.sleep(time);
        }catch (Exception e){}
    }

    public static final long threadId(){
        return Thread.currentThread().getId();
    }

    public static String threadName(){
        return Thread.currentThread().getName();
    }

    public static void unSafeKill(){
    }
    
}
