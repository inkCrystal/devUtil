package cn.dev.clock;

import cn.dev.commons.datetime.DateTimeUtil;
import cn.dev.parallel.task.api.ITaskFunction;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.function.Predicate;

/**
 * 定时配置
 */
public class ScheduledConfig implements Serializable {
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




    class cnf{
        int[] monthMatch,dayMatch,hourMatch ,minuteMatch ,secondMatch;

        public static cnf everyYear(int month ,int dayOfMonth){
            return everyYear(month,dayOfMonth,0);
        }
        public static cnf everyYear(int month ,int dayOfMonth,int hour){
            return everyYear(month,dayOfMonth,hour,0);
        }
        public static cnf everyYear(int month ,int dayOfMonth,int hour,int minute ){
            return everyYear(month,dayOfMonth,hour,minute,0);
        }
        public static cnf everyYear(int month ,int dayOfMonth,int hour ,int minute,int second){
            return null;
        }
        public static cnf everyMonth(int dayOfMonth,int hour,int minute ,int second){
            return everyYear(0,dayOfMonth,hour,minute,second);
        }

        public static cnf everyMonth(int dayOfMonth,int hour ,int minute ){
            return everyMonth(dayOfMonth,hour,minute,0);
        }

        public static cnf everyMonth(int dayOfMonth,int hour){
            return everyMonth(dayOfMonth,hour,0);
        }
        public static cnf everyMonth(int dayOfMonth){
            return everyMonth(dayOfMonth,0);
        }

        public static cnf everyDay(int hour, int minute ,int second){
            return everyMonth(0,hour,minute,second);
        }

        public static cnf everyDay(int hour,int minute){
            return everyDay(hour,minute,0);
        }
        public static final cnf everyDay(int hour){
            return everyDay(hour,0);
        }

        public static final cnf everyHour(int minute ,int second){
            return everyDay(0,minute,second);
        }

        public static cnf everyHour(int minute){
            return everyHour(minute,0);
        }

        public static cnf everyMinute(int second){
            return everyHour(0,second);
        }

    }

    record scheduledConfig(Predicate<LocalDateTime> localDateTimePredicate){

        public boolean test(LocalDateTime localDateTime){
            return localDateTimePredicate.test(localDateTime);
        }
    }

    record timeCnf(int[] monthMatch,int[] dayMatch,int[]hourMatch ,int[]  minuteMatch ,int[] secondMatch){
        public boolean isMatch(LocalDateTime localDateTime){
            int M = localDateTime.getMonthValue();
            int D = localDateTime.getDayOfMonth();
            int H = localDateTime.getHour();
            int m = localDateTime.getMinute();
            int s = localDateTime.getSecond();
            if(D>28){
                int maxM = DateTimeUtil.endOfMonth(localDateTime).getMonthValue();
            }
            return true;
        }


        private static boolean containsZeroOrTarget(int target , int maxValue, int[] array){
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

    }

}
