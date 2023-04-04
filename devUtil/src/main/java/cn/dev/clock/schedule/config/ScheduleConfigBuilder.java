package cn.dev.clock.schedule.config;

import cn.dev.commons.BinaryTool;
import cn.dev.commons.verification.VerificationTool;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

/**
 * 定时配置构建器
 */
public class ScheduleConfigBuilder {

    protected static long calSetValue(long source , int offSet , int minValue , int maxValue , Predicate<Integer> predicate){
        for (int i = minValue; i <= maxValue ; i++) {
            source = predicate.test(i)?source|BinaryTool.leftMove(1,offSet+i):source;
        }
        return source;
    }


    public static ScheduledConfig configSetMonthConfigPredicate(ScheduledConfig config, Predicate<Integer> predicate){
        VerificationTool.isNotNull(config,"配置对象不能为空");
        config.setMothAndDayConfig(
                ScheduleConfigBuilder.calSetValue(config.getMothAndDayConfig(),0,1,12,predicate)
        );
        return config;
    }

    public static ScheduledConfig configSetDayConfigPredicate(ScheduledConfig config, Predicate<Integer> predicate){
        VerificationTool.isNotNull(config,"配置对象不能为空");
        config.setMothAndDayConfig(
                ScheduleConfigBuilder.calSetValue(config.getMothAndDayConfig(),12,1,31,predicate)
        );
        return config;
    }

    public static ScheduledConfig configSetHourConfigPredicate(ScheduledConfig config, Predicate<Integer> predicate){
        VerificationTool.isNotNull(config,"配置对象不能为空");
        config.setHourConfig(
                ScheduleConfigBuilder.calSetValue(config.getHourConfig(),0,0,23,predicate)
        );
        return config;
    }

    public static ScheduledConfig configSetMinuteConfigPredicate(ScheduledConfig config, Predicate<Integer> predicate){
        VerificationTool.isNotNull(config,"配置对象不能为空");
        config.setMinuteConfig(
                ScheduleConfigBuilder.calSetValue(config.getMinuteConfig(),0,0,59,predicate)
        );
        return config;
    }

    public static ScheduledConfig configSetSecondConfigPredicate(ScheduledConfig config, int secondConfig){
        VerificationTool.isNotNull(config,"配置对象不能为空");
        config.setSecondConfig( secondConfig) ;
        return config;
    }


    // 设置 多少时间后执行 1次
    public ScheduledConfig configRunDelay(int time , TimeUnit unit){
        ScheduledConfig config =new ScheduledConfig();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime fireTime = now.plusSeconds(unit.toSeconds(time));
        configSetMonthConfigPredicate(config,m->m==fireTime.getMonthValue());
        configSetDayConfigPredicate(config,d->d==fireTime.getDayOfMonth());
        configSetHourConfigPredicate(config,h->h==fireTime.getHour());
        configSetMinuteConfigPredicate(config,m->m==fireTime.getMinute());
        configSetSecondConfigPredicate(config,fireTime.getSecond());
        config.setMaxRuntimes(1);
        return config;
    }




    public static ScheduledConfig configEveryYear(int month , int dayOfMonth){
        return configEveryYear(month,dayOfMonth,0);
    }
    public static ScheduledConfig configEveryYear(int month , int dayOfMonth, int hour){
        return configEveryYear(month,dayOfMonth,hour,0);
    }
    public static ScheduledConfig configEveryYear(int month , int dayOfMonth, int hour, int minute ){
        return configEveryYear(month,dayOfMonth,hour,minute,0);
    }
    public static ScheduledConfig configEveryYear(int month , int dayOfMonth, int hour , int minute, int second){
        final ScheduledConfig config = new ScheduledConfig();
        configSetMonthConfigPredicate(config,m->m==month||month<0);
        configSetDayConfigPredicate(config,d->d==dayOfMonth||dayOfMonth<0);
        configSetHourConfigPredicate(config,h->h==hour||hour<0);
        configSetMinuteConfigPredicate(config,m-> m == minute || minute < 0);
        configSetSecondConfigPredicate(config,second);
        return config;
    }
    public static ScheduledConfig configEveryMonth(int dayOfMonth, int hour, int minute , int second){
        return configEveryYear(-1,dayOfMonth,hour,minute,second);
    }

    public static ScheduledConfig configEveryMonth(int dayOfMonth, int hour , int minute ){
        return configEveryMonth(dayOfMonth,hour,minute,0);
    }

    public static ScheduledConfig configEveryMonth(int dayOfMonth, int hour){
        return configEveryMonth(dayOfMonth,hour,0);
    }
    public static ScheduledConfig configEveryMonth(int dayOfMonth){
        return configEveryMonth(dayOfMonth,0);
    }

    public static ScheduledConfig configEveryDay(int hour, int minute , int second){
        return configEveryMonth(-1,hour,minute,second);
    }

    public static ScheduledConfig configEveryDay(int hour, int minute){
        return configEveryDay(hour,minute,0);
    }
    public static final ScheduledConfig configEveryDay(int hour){
        return configEveryDay(hour,0);
    }

    public static final ScheduledConfig configEveryHour(int minute , int second){
        return configEveryDay(-1,minute,second);
    }


    public static ScheduledConfig configEveryHour(int minute){
        return configEveryHour(minute,0);
    }

    public static ScheduledConfig configEveryMinute(int second){
        return configEveryHour(-1,second);
    }



}
