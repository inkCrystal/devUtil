package cn.dev.commons.datetime;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author : JasonMao
 * @version : 1.0
 * @date : 2023/3/17 14:49
 * @description :
 */
public class TimeMillisClock {

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
        private static final TimeMillisClock INSTANCE = new TimeMillisClock(1);
    }

    private static final TimeMillisClock getInstance(){
        return InstanceHolder.INSTANCE;
    }
    private TimeMillisClock(int period) {
        this.period = period;
        this.now = new AtomicLong(System.currentTimeMillis());
        this.start();
    }
    private vClock clock ;


    private static long lastClockFire = 0L;

    private void  start(){
        if (clock == null) {
            clock = new vClock();
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
                clock.reSet();
                lastClockFire = clock.getLastRest();
                long remainder = lastClockFire%1000;
                lastClockFire = lastClockFire -remainder;
            }else {
                long duration = mills - lastClockFire;
                while (duration >= 1000){
                    duration -= 1000;
                    clock.trySecondInc();
                }
                lastClockFire = mills - duration;
            }


        }, period, period, TimeUnit.MILLISECONDS);
    }

    public vClock getVClock(){
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







    class vClock{
        private int year, month , dayOfMonth , hour,minute ,second ;
        private long lastRest = 0L;
        private long waitActive = 1000 ;

        protected vClock() {
//            atomicInteger = new AtomicInteger(1000);
            LocalDateTime localDateTime = LocalDateTime.now();
            year = localDateTime.getYear();
            month = localDateTime.getMonthValue();
            dayOfMonth = localDateTime.getDayOfMonth();
            hour = localDateTime.getHour();
            minute = localDateTime.getMinute();
            second = localDateTime.getSecond();
        }


        private void trySecondInc(){
            second ++ ;
            if(second == 60){
                second = 0 ;
                this.minuteInc();
            }
        }

        private void minuteInc(){
            this.minute ++ ;
            /** 每五分钟重置一次   ---by jason @ 2023/3/27 14:16 */
            if(this.minute % 5 == 0){
                this.reSet();
            }
        }

        /**重置 虚拟时钟   ---by jason @ 2023/3/26 14:02 */
        private void reSet(){
            LocalDateTime localDateTime = LocalDateTime.now();
            year = localDateTime.getYear();
            month = localDateTime.getMonthValue();
            dayOfMonth = localDateTime.getDayOfMonth();
            hour = localDateTime.getHour();
            minute = localDateTime.getMinute();
            second = localDateTime.getSecond();
            lastRest = DateTimeUtil.toEpochMilli(localDateTime);
        }


        public long getLastRest() {
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
    }

    /// EOF ------------------
    public static void main(String[] args) throws InterruptedException {
        System.out.println("让我们开始吧 ");
        int errorCount = 0;
        int testCount = 0 ;
        getInstance();
        while (true){
            testCount ++ ;
            LocalDateTime now = LocalDateTime.now();
            final String localDateStr = now.format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss"));
            final String vClockStr = getInstance().getVClock().toString();
            if(!vClockStr.equals(localDateStr)){
                errorCount ++ ;
                System.out.println("ERROR " + errorCount + " times "   );
                System.out.println("-------------------------");
                System.out.println("Local DateTime " + now.format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss SSS")));
                System.out.println("VClockDateTime " + vClockStr);
                System.out.println("-------------------------");
            }else{
                System.out.print("\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b" +
                        "" + vClockStr );


            }
            if (testCount%100 == 0 ) {
                System.out.println("检测"+(testCount ) + "次 ， 目前出现偏差"+errorCount+"次");
            }

            Thread.sleep(100);

//                long testT =System.currentTimeMillis() %1000 ;
//                if (testT > 500 && testT < 600) {
//                    break;
//                }

        }

    }
}
