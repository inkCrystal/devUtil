package cn.dev.clock.schedule.config;

import cn.dev.clock.DateTimeEntry;
import cn.dev.commons.BinaryTool;
import cn.dev.exception.ScheduleConfigException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *  日程配置测试类
 * @author : JasonMao
 * @author : JasonMao
 */
public class ScheduledConfigTester {

    private ScheduledConfig config;

    public ScheduledConfigTester(ScheduledConfig config) {
        this.config = config;
    }

    private boolean testMinute(int minute){
        return BinaryTool.checkPositionIsTrue(config.getMinuteConfig(), minute);
    }

    private boolean testHour(int hour){
        return BinaryTool.checkPositionIsTrue(config.getHourConfig(), hour);
    }

    private boolean testDayOfMonth(int day){
        return BinaryTool.checkPositionIsTrue(config.getDayOfMonthConfig(), day);
    }

    private boolean testMonth(int month){
        return BinaryTool.checkPositionIsTrue(config.getMonthConfig(), month);
    }

    /**获得 可匹配的 小时数组   ---by jason @ 2023/4/4 13:07 */
    private Integer[] hourFireArray() {
        List<Integer> list = new ArrayList<>();
        long hourConfig = config.getHourConfig();
        for (int i = 0; i < 24; i++) {
            if (BinaryTool.checkPositionIsTrue(hourConfig, i)) {
                list.add(i);
            }
        }
        return list.toArray(new Integer[0]);
    }

    /**获得 可匹配的 分钟 数组   ---by jason @ 2023/4/4 13:07 */
    private Integer[] minuteFireArray(){
        List<Integer> list =new ArrayList<>();
        long minuteConfig = config.getMinuteConfig();
        for (int i = 0; i < 60; i++) {
            if(BinaryTool.checkPositionIsTrue(minuteConfig,i)){
                list.add(i);
            }
        }
        return list.toArray(new Integer[0]);
    }


    // 1. 从当前时间开始，计算下一次执行时间
    public DateTimeEntry nextFireTime(){
        return nextFireTime(LocalDateTime.now());
    }

    // 2. 从指定时间开始，计算下一次执行时间
    public DateTimeEntry nextFireTime(LocalDateTime localDateTime){
        return  new DateTimeEntry(tryNextFireTimeByDayPlus(localDateTime));
    }


    private LocalDateTime tryNextFireTimeByDayPlus(LocalDateTime localDateTime){
        Integer[] minuteFireArray = minuteFireArray();
        Integer[] hourFireArray = hourFireArray();
        if (minuteFireArray.length ==1 && hourFireArray.length ==1) {
            LocalDateTime t = LocalDateTime.of(
                    localDateTime.getYear(), localDateTime.getMonth(), localDateTime.getDayOfMonth(),
                    hourFireArray[0], minuteFireArray[0], config.getSecondConfig());
            while (!t.isAfter(localDateTime)) {
                t = t.plusDays(1);
            }
            localDateTime =t ;
            while (!config.testFire(localDateTime)){
                localDateTime = localDateTime.plusDays(1);
            }
            return localDateTime;
        }
        return tryNextFireTimeByHourPlus(localDateTime);
    }

    private LocalDateTime tryNextFireTimeByHourPlus(LocalDateTime localDateTime){
        Integer[] minuteFireArray = minuteFireArray();
        if (minuteFireArray.length ==1 ) {
            LocalDateTime t = LocalDateTime.of(
                    localDateTime.getYear(), localDateTime.getMonth(), localDateTime.getDayOfMonth(),
                    localDateTime.getHour(), minuteFireArray[0], config.getSecondConfig());
            while (!t.isAfter(localDateTime)) {
                t = t.plusHours(1);
            }
            localDateTime = t;
            while (!config.testFire(localDateTime)){
                localDateTime = localDateTime.plusHours(1);
            }
            return localDateTime;
        }
        return tryNextFireTimeByMinutePLus(localDateTime);
    }

    private LocalDateTime tryNextFireTimeByMinutePLus(LocalDateTime localDateTime){
        LocalDateTime t = LocalDateTime.of(
                localDateTime.getYear(), localDateTime.getMonth(), localDateTime.getDayOfMonth(),
                localDateTime.getHour(), localDateTime.getMinute(), config.getSecondConfig());
        while (!t.isAfter(localDateTime)) {
            t = t.plusMinutes(1);
        }
        localDateTime = t;
        while (!config.testFire(localDateTime)) {
            localDateTime = localDateTime.plusMinutes(1);
        }
        return localDateTime;
    }



    /**仅当 分钟匹配的 值 是固定唯一时候 执行该方法  ---by jason @ 2023/4/4 13:01 */
    private LocalDateTime nextFileTimeWhileMinuteFireLenIsOne(LocalDateTime localDateTime ){
        while (!config.testFire(localDateTime)){
            localDateTime = localDateTime.plusHours(1);
        }
        return localDateTime;
    }


    private void configSafeCheck(String typeConfigName, long value , int startIndex ) throws ScheduleConfigException {
        boolean[] arr = BinaryTool.toBoolArray(value);
        int count = 0;
        for (int i = startIndex; i < arr.length; i++) {
            if(arr[i]){
                count ++ ;
            }
        }
        if(count == 0){
            throw new ScheduleConfigException(typeConfigName + "缺失可以匹配的明确时间点");
        }
    }
    //检查 配置参数的合法性， 会直接抛出 异常 ScheduleConfigException
    public void safeCheck() throws ScheduleConfigException {
        this.configSafeCheck("配置的可执行月：",config.getMonthConfig(),1);
        this.configSafeCheck("配置的可执行日：",config.getDayOfMonthConfig(),1);
        this.configSafeCheck("配置的可执行时：", config.getHourConfig(),0);
        this.configSafeCheck("配置的可执行分：",config.getMinuteConfig(),0);
        if(config.getSecondConfig()<0 || config.getSecondConfig()>59){
            throw new ScheduleConfigException("配置的可执行秒：必须在 0-59 之间");
        }
    }





}
