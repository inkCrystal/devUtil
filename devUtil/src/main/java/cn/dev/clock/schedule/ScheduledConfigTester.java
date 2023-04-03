package cn.dev.clock.schedule;

import cn.dev.clock.CommonTimeClock;
import cn.dev.commons.BinaryTool;

import java.time.LocalDate;

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


    public int nextFireMinute(){
        return nextFireMinute(CommonTimeClock.getMinute());
    }
    public int nextFireMinute(int currentMinute){
        final boolean[] minuteConfigArray
                = BinaryTool.toBoolArray(config.getMinuteConfig());
        for (int i = currentMinute; i < 60; i++) {
            if(minuteConfigArray[i]){
                return i;
            }
        }
        for (int i = 0; i < currentMinute; i++) {
            if(minuteConfigArray[i]){
                return i;
            }
        }
        return -1;
    }

    public int nextfireHour(){
        return nextFireHour(CommonTimeClock.getHour());
    }

    public int nextFireHour(int currentHour){
        final boolean[] hourConfigArray
                = BinaryTool.toBoolArray(config.getHourConfig());
        for (int i = currentHour; i < 24; i++) {
            if(hourConfigArray[i]){
                return i;
            }
        }
        for (int i = 0; i < currentHour; i++) {
            if(hourConfigArray[i]){
                return i;
            }
        }
        return -1;
    }

//
//    public int nextFireDate(LocalDate currentDate){
//        final boolean[] dateConfigArray
//                = BinaryTool.toBoolArray(config.getDateConfig());
//        for (int i = currentDate.getDayOfMonth(); i < 31; i++) {
//            if(dateConfigArray[i]){
//                return i;
//            }
//        }
//        for (int i = 1; i < currentDate.getDayOfMonth(); i++) {
//            if(dateConfigArray[i]){
//                return i;
//            }
//        }
//        return -1;
//
//
//    }



}
