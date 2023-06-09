package cn.dev.clock;

import cn.dev.clock.listener.ITimeClockListener;
import cn.dev.commons.datetime.DateTimeUtil;
import cn.dev.commons.log.DLog;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author : JasonMao
 * @version : 1.0
 * @date : 2023/3/17 14:49
 * @description :
 */
public class CommonTimeClock {

    private final int period;

    private final AtomicLong now;

    private ScheduledThreadPoolExecutor scheduler ;

    /**是否使用  System.currentTimeMillis() 来获取毫秒时间戳---by JasonMao @ 2023/3/17 17:25 */
    private static boolean usingSystemCurrentMillis = false;

    /**
     * 设置是否使用 System.currentTimeMillis() 获得毫秒时间戳
     * @param b
     */
    public final void setUsingSystemMillis(boolean b){
        usingSystemCurrentMillis = b;
        if(b){
            if (scheduler != null && scheduler.getActiveCount() > 0 ) {
                scheduler.shutdownNow();
            }
        }

    }

    private static class InstanceHolder {
        private static final CommonTimeClock INSTANCE = new CommonTimeClock(1);
    }

    private static final CommonTimeClock getInstance(){
        return InstanceHolder.INSTANCE;
    }
    private CommonTimeClock(int period) {
        this.period = period;
        this.now = new AtomicLong(System.currentTimeMillis());
        this.start();
    }
    private static VTimeClock clock ;


    private static long lastClockFire = 0L;

    private void  start(){
        if (clock == null) {
            clock = new VTimeClock();
        }
        scheduler = new ScheduledThreadPoolExecutor(1, r -> {
            Thread thread = new Thread(r, "MILLIS-CLOCK");
            thread.setDaemon(true);
            return thread;
        });

        scheduler.scheduleAtFixedRate(() -> {
            long mills = System.currentTimeMillis();
            now.set( mills);
            if(lastClockFire == 0 ){
                clock.syncByLocalDateTime();
                lastClockFire = clock.getLastRest();
                long remainder = lastClockFire%1000;
                lastClockFire = lastClockFire -remainder;
            }else {
                long duration = mills - lastClockFire;
                while (duration >= 1000){
                    duration -= 1000;
                    //秒钟 ++
                    clock.setSecondOrInc(0,true);
                }
                lastClockFire = mills - duration;
            }


        }, period, period, TimeUnit.MILLISECONDS);
    }

    public VTimeClock getVClock(){
        return clock;
    }
    /**
     * 只有当 始终启动成功后才会返回！
     * @return
     */
    public static final long currentMillis(){
        return getInstance().now.get();
    }


    /**same as   currentMillis---by JasonMao @ 2023/1/30 19:00 */
    public static final long currentTimeMillis(){
        return currentMillis();
    }


    public static VTimeClock getClock() {
        return getInstance().getVClock();
    }


    public static int getHour(){
        return getClock().getHour();
    }

    public static int getMinute(){
        return getClock().getMinute();
    }

    public static int getSecond(){
        return getClock().getSecond();
    }

    public static int getYear(){
        return getClock().getYear();
    }

    public static int getMonth(){
        return getClock().getMonth();
    }

    public static int getDayOfMonth(){
        return getClock().getDayOfMonth();
    }

    public static LocalTime getLocalTime(){
        return LocalTime.of(getHour(), getMinute(), getSecond());
    }

    public static LocalDateTime getLocalDateTime(){
        return LocalDateTime.of(getYear(), getMonth(), getDayOfMonth(), getHour(), getMinute(), getSecond());
    }



    class VTimeClock {
        private int year, month , dayOfMonth , hour,minute ,second =-1;
        private long lastRest = 0L;

        private List<ITimeClockListener> listeners =new ArrayList<>();


        protected VTimeClock() {

//            atomicInteger = new AtomicInteger(1000);
            LocalDateTime localDateTime = LocalDateTime.now();
            year = localDateTime.getYear();
            month = localDateTime.getMonthValue();
            dayOfMonth = localDateTime.getDayOfMonth();
            hour = localDateTime.getHour();
            minute = localDateTime.getMinute();
            second = localDateTime.getSecond();
        }

        public void bindListener(ITimeClockListener listener){
            if(listener !=null) {
                boolean contains = false;
                for (ITimeClockListener t : this.listeners) {
                    if (t.listenerInfo().equals(listener.listenerInfo())) {
                        contains =true;
                        break;
                    }
                }
                if(contains!=true){
                    this.listeners.add(listener);
                }
            }
        }




        /**同步虚拟时钟   ---by jason @ 2023/3/26 14:02 */
        private void syncByLocalDateTime(){
            LocalDateTime localDateTime = LocalDateTime.now();
            this.setYear(localDateTime.getYear());
            this.setMonth(localDateTime.getMonthValue());
            this.setDayOfMonth(localDateTime.getDayOfMonth());
            this.setHour(localDateTime.getHour());
            this.setMinuteOrInc(localDateTime.getMinute(),false);
            this.setSecondOrInc(localDateTime.getSecond(),false);
            lastRest = DateTimeUtil.toEpochMilli(localDateTime);
        }




        protected long getLastRest() {
            return lastRest;
        }

        public int getYear() {
            return year;
        }

        public int getMonth() {
            return month;
        }

        public int getDayOfMonth() {
            return dayOfMonth;
        }

        public int getHour() {
            return hour;
        }

        public int getMinute() {
            return minute;
        }

        public int getSecond() {
            return second;
        }

        private void setYear(int year) {
            if(this.year != year) {
                this.year = year;
                this.fireListener(dateTimePropType.YEAR);
            }
        }

        private void setMonth(int month) {
            if(this.month != month){
                this.month = month;
                this.fireListener(dateTimePropType.MONTH);
            }
        }

        private void setDayOfMonth(int dayOfMonth) {
            if(this.dayOfMonth != dayOfMonth){
                this.dayOfMonth = dayOfMonth;
                this.fireListener(dateTimePropType.DAY_OF_MOTH);
            }
        }

        private void setHour(int hour) {
            if(this.hour != hour){
                this.hour = hour;
                this.fireListener(dateTimePropType.HOUR);
            }
        }

        private void setMinuteOrInc(int m ,boolean inc ) {

            if(inc){
                this.minute ++ ;
                /** 每五分钟校对 一次   ---by jason @ 2023/3/27 14:16 */
                if(this.minute % 5 == 0){
                    this.syncByLocalDateTime();
                }
                this.fireListener(dateTimePropType.MONTH);
            } else if(this.minute!=m ){
                this.minute = m;
                this.fireListener(dateTimePropType.MONTH);
            }
        }


        private void setSecondOrInc(int s ,boolean inc ) {
            if (inc){
                this.second ++ ;
                if(this.second == 60){
                    this.second = 0 ;
                    this.setMinuteOrInc(0,true);
                    this.fireListener(dateTimePropType.SECOND);
                }
            }else {
                if (this.second!=s) {
                    this.second = s ;
                    this.fireListener(dateTimePropType.SECOND);
                }
            }
        }




        private void fireListener(dateTimePropType dateTimePropType){
            switch (dateTimePropType){
                case YEAR -> listeners.stream().forEach(t->t.onYearChange(this.getYear()));
                case MONTH -> listeners.stream().forEach(t->t.onMonthChange(this.getYear(),this.getMonth()));
                case DAY_OF_MOTH -> listeners.stream().forEach(t->t.onDayOfMonthChange(this.getYear(),this.getMonth(),this.getDayOfMonth()));
                case HOUR -> listeners.stream().forEach(t->t.onHourChange(this.getYear(),this.getMonth(),this.getDayOfMonth(),this.getHour()));
                case MINUTE -> listeners.stream().forEach(t->t.onMinuteChange(this.getYear(),this.getMonth(),this.getDayOfMonth(),this.getHour(),this.getMinute()));
                case SECOND -> listeners.stream().forEach(t-> t.onSecondChange(this.getYear(),this.getMonth(),this.getDayOfMonth(),this.getHour(),this.getMinute(),this.getSecond()));
                default -> {
                }
            }
        }



        public String toString(){
            var delimiter ='-';
            return toString(delimiter);

        }
        public String toString(char delimiter){
            StringBuilder clockString  = new StringBuilder()
                    .append(year)
                    .append(delimiter).append(month<10?'0':"").append(month)
                    .append(delimiter).append(dayOfMonth<10?'0':"").append(dayOfMonth)
                    .append(" ")
                    .append(hour<10?'0':"").append(hour)
                    .append(":").append(minute<10?'0':"").append(minute)
                    .append(":").append(second<10?'0':"").append(second);
            return clockString.toString();
        }

        public String toTimeString(){
            StringBuilder clockString  = new StringBuilder()
                    .append(hour<10?'0':"").append(hour)
                    .append(":").append(minute<10?'0':"").append(minute)
                    .append(":").append(second<10?'0':"").append(second);
            return clockString.toString();
        }

        public String toDateString(char delimiter){
            StringBuilder clockString  = new StringBuilder()
                    .append(year)
                    .append(delimiter).append(month<10?'0':"").append(month)
                    .append(delimiter).append(dayOfMonth<10?'0':"").append(dayOfMonth);
            return clockString.toString();
        }
        public String toDateString(){
            return toDateString('-');
        }





        /**
         * 返回 年 月 日 时 分 秒 的int数组
         * @return
         */
        public int[] getTimeArray(){
            return new int[]{this.year,this.month,this.dayOfMonth,this.hour,this.minute,this.second};
        }
    }

    enum dateTimePropType{
        YEAR ,MONTH ,DAY_OF_MOTH,HOUR,MINUTE,SECOND
    }

    /// EOF ------------------
    public static void main(String[] args) throws InterruptedException {


        if(1>0){
            return;
        }
        DLog.info("让我们开始吧 ");
        int errorCount = 0;
        int testCount = 0 ;

        while (true){
            testCount ++ ;
            LocalDateTime now = LocalDateTime.now();
            final String localDateStr = now.format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss"));
            final String vClockStr = getInstance().getVClock().toString();
            if(!vClockStr.equals(localDateStr)){
                errorCount ++ ;
                DLog.info("ERROR " + errorCount + " times "   );
                DLog.info("-------------------------");
                DLog.info("Local DateTime " + now.format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss SSS")));
                DLog.info("VClockDateTime " + vClockStr);
                DLog.info("-------------------------");
            }else{
                DLog.info("\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b" +
                        "" + vClockStr );


            }
            if (testCount%100 == 0 ) {
                DLog.info("检测"+(testCount ) + "次 ， 目前出现偏差"+errorCount+"次");
            }

            Thread.sleep(100);


        }

    }


}
