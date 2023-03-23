package cn.dev.clock;

import cn.dev.commons.datetime.DateTimeUtil;
import cn.dev.task.api.ITaskFunction;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;

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

    record timeCnf(int[] monthMatch,int[]hourMatch ,int[]  minuteMatch ,int[] secondMatch){
        public boolean isMatch(LocalDateTime localDateTime){
            int M = localDateTime.getMonthValue();
            int D = localDateTime.getDayOfMonth();
            int H = localDateTime.getHour();
            int m = localDateTime.getMinute();
            int s = localDateTime.getSecond();
            if(D>28){
                int maxM = DateTimeUtil.endOfMonth(localDateTime).getMonthValue();
            }


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

        }

    }

}
