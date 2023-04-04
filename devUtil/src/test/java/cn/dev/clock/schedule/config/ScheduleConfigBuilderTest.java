package cn.dev.clock.schedule.config;

import cn.dev.exception.ScheduleConfigException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class ScheduleConfigBuilderTest {

    private void print(LocalDateTime dateTime){
        System.out.print(" "+dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }


    @Test
    void configEveryMinute() throws ScheduleConfigException {
        LocalDateTime now = LocalDateTime.now();
        print(now);
        final ScheduledConfig config = ScheduleConfigBuilder.configEveryMinute(32);

        runtest(config,"每分钟的第32秒执行");

        runtest(ScheduleConfigBuilder.configEveryHour(12,11),"没小时的第12分11秒执行");

        runtest(ScheduleConfigBuilder.configEveryDay(12,7,1),"每天的第12时7分1秒执行");

        runtest(ScheduleConfigBuilder.configEveryMonth(12,7,1,1),"每月的第12日7:1:1秒执行");

        runtest(ScheduleConfigBuilder.configEveryYear(12,7,1,1,1),"每年的第12月7日执行");


    }


    public void runtest(ScheduledConfig config ,String title ){
        LocalDateTime now = LocalDateTime.now();
        System.out.println("\n测试 " + title + ">>");
        ScheduledConfigTester tester = config.tester();
        long start = System.currentTimeMillis();
        for(int i = 0; i < 10; i++) {
            now = tester.nextFireTime(now).toLocalDateTime();
            print(now);
        }
        long end = System.currentTimeMillis();
        System.out.println("\n耗时 " + (end - start) + " ms");
    }






}