package cn.dev.clock.schedule;

import cn.dev.clock.CommonTimeClock;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

public class ScheduleTaskRegister {
    private static final List<Queue<ScheduledConfig>> queueList = new ArrayList<>();
    private ScheduleTaskRegister() {
        initArrayList();
    }
    private static final ScheduleTaskRegister instance = new ScheduleTaskRegister();


    private volatile boolean initState = false;

    public static ScheduleTaskRegister getInstance() {
        return instance;
    }

    private synchronized void initArrayList(){
        if(queueList.size() == 0){
            for (int i = 0; i < 24; i++) {
                queueList.add(new ArrayBlockingQueue<>(512));
            }
        }
        this.initState =true;
    }


    public Queue<ScheduledConfig> getTaskQueue(int hour){
        return queueList.get(hour);
    }

    public Queue<ScheduledConfig> getTaskQueue(){
        int hour  = CommonTimeClock.getHour();
        return queueList.get(hour);
    }


    public void register(ScheduledConfig config){
        if(config.isConfigAvailable()){
            Queue<ScheduledConfig> taskQueue = getTaskQueue();
            taskQueue.add(config);
        }
    }






}
