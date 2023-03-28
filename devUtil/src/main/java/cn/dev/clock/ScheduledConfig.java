package cn.dev.clock;

import cn.dev.commons.datetime.DateTimeUtil;
import cn.dev.parallel.task.api.ITaskFunction;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

/**
 * 定时配置
 */
public class ScheduledConfig implements Serializable{
    @Serial
    private static final long serialVersionUID = -6693991069626527197L;
    /** 配置 ：是否有 运行次数限制  ---by jason @ 2023/3/23 14:43 */
    private boolean cnfTimeLimit ;
    /** 配置 ：最多运行次数   ---by jason @ 2023/3/23 14:43 */
    private long cnfMaxTimes;

    //**运行次数  ---by jason @ 2023/3/23 14:42 */
    private long runTimes;
    /**最后触发时间  ---by jason @ 2023/3/23 14:43 */
    private long lastFireTime;
    
    /**下次触发时间，一般 运行30 ms的误差   ---by jason @ 2023/3/23 14:44 */
    private long nextFireTime;

    /**需要执行的任务   ---by jason @ 2023/3/23 14:46 */
    private ITaskFunction scheduledTask;


    /**
     * 配置信息
     */
    class cnfInfo {
        int[] monthMatch,dayMatch,hourMatch ,minuteMatch ,secondMatch;



//        public static cnfInfo delay(long time , TimeUnit timeUnit){
//
//
//        }



        public cnfInfo setMonthConfig(int... month){
            if(month.length >0){
                Arrays.stream(month).distinct().filter(x->(x>1));

                this.monthMatch = month;
            }
            return this;
        }

        public static cnfInfo everyYear(int month , int dayOfMonth){
            return everyYear(month,dayOfMonth,0);
        }
        public static cnfInfo everyYear(int month , int dayOfMonth, int hour){
            return everyYear(month,dayOfMonth,hour,0);
        }
        public static cnfInfo everyYear(int month , int dayOfMonth, int hour, int minute ){
            return everyYear(month,dayOfMonth,hour,minute,0);
        }
        public static cnfInfo everyYear(int month , int dayOfMonth, int hour , int minute, int second){

            return null;
        }
        public static cnfInfo everyMonth(int dayOfMonth, int hour, int minute , int second){
            return everyYear(0,dayOfMonth,hour,minute,second);
        }

        public static cnfInfo everyMonth(int dayOfMonth, int hour , int minute ){
            return everyMonth(dayOfMonth,hour,minute,0);
        }

        public static cnfInfo everyMonth(int dayOfMonth, int hour){
            return everyMonth(dayOfMonth,hour,0);
        }
        public static cnfInfo everyMonth(int dayOfMonth){
            return everyMonth(dayOfMonth,0);
        }

        public static cnfInfo everyDay(int hour, int minute , int second){
            return everyMonth(0,hour,minute,second);
        }

        public static cnfInfo everyDay(int hour, int minute){
            return everyDay(hour,minute,0);
        }
        public static final cnfInfo everyDay(int hour){
            return everyDay(hour,0);
        }

        public static final cnfInfo everyHour(int minute , int second){
            return everyDay(0,minute,second);
        }



        public static cnfInfo everyHour(int minute){
            return everyHour(minute,0);
        }

        public static cnfInfo everyMinute(int second){
            return everyHour(0,second);
        }

        public long nextFire(){
            LocalDateTime now = LocalDateTime.now();
//            int nextMonth =


            return -1L;
        }


        private boolean containsZeroOrTarget(int target , int maxValue, int[] array){
            if(target > maxValue){
                target = maxValue;
            }
            if (array == null) {
                return true;
            }
            for (int i : array) {
                if( i == target){
                    return true;
                }
            }
            return false;
        }

        public boolean testMatch(int month ,int dayOfMonth ,int  hour ,int minute ,int second ) {
            if (this.containsZeroOrTarget(month, 12, monthMatch)) {
                int maxDayOfMoth = DateTimeUtil.endOfMonth().getMonthValue();
                if (this.containsZeroOrTarget(dayOfMonth, maxDayOfMoth, dayMatch)) {
                    if (this.containsZeroOrTarget(hour, 23, hourMatch)) {
                        if (this.containsZeroOrTarget(minute, 59, minuteMatch)) {
                            return this.containsZeroOrTarget(second, 59, secondMatch);
                        }
                    }
                }
            }
            return false;
        }



    }

    record scheduledConfig(Predicate<LocalDateTime> localDateTimePredicate){
        public boolean test(LocalDateTime localDateTime){
            return localDateTimePredicate.test(localDateTime);
        }
    }

}
