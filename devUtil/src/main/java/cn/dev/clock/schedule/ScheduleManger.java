package cn.dev.clock.schedule;

import cn.dev.clock.schedule.config.ScheduledConfig;

import java.util.*;

public class ScheduleManger {
    private final Map<String, List<ScheduledConfig>> taskMap ;

    private ScheduleManger() {
        taskMap = new HashMap<>();
    }

    private static ScheduleManger instance = null ;
    public static synchronized ScheduleManger getInstance(){
        if(instance != null){
            return instance;
        }
        instance = new ScheduleManger();
        return instance;
    }

    /**
     * 读取或初始化 配置列表
     * @param k
     */
    private List<ScheduledConfig> getOrInitConfigListByKey(String k){
        if (!taskMap.containsKey(k)) {
            taskMap.put(k,new ArrayList<>());
        }
        return taskMap.get(k);
    }

    /**
     * 添加定时任务配置
     * @param config
     */
    private void putConfig(  ScheduledConfig config){
        List<ScheduledConfig> configList = getOrInitConfigListByKey("key");
        configList.add(config);
    }






}
