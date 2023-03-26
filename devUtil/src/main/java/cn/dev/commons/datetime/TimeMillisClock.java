package cn.dev.commons.datetime;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
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

    ScheduledThreadPoolExecutor scheduler ;

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


    private void  start(){
        if (clock == null) {
            clock = new vClock();
        }else{
            clock.reSet();
        }
        scheduler = new ScheduledThreadPoolExecutor(1, r -> {
            Thread thread = new Thread(r, "MILLIS-CLOCK");
            thread.setDaemon(true);
            return thread;
        });

        scheduler.scheduleAtFixedRate(() -> {
            now.set(System.currentTimeMillis());
            clock.trySecondInc();
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



    public static void main(String[] args) throws InterruptedException {




        getInstance();
        while (true){

            System.out.print(LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYY-MM-DD HH:mm:ss")));
            System.out.print( "   ");
            System.out.println(getInstance().getVClock().toString());
            Thread.sleep(1000);

        }

    }

    class vClock{
        private int year, month , dayOfMonth , hour,minute ,second ;
        private static AtomicInteger atomicInteger ;

        protected vClock() {
            atomicInteger = new AtomicInteger(1000);
            LocalDateTime localDateTime = LocalDateTime.now();
            year = localDateTime.getYear();
            month = localDateTime.getMonthValue();
            dayOfMonth = localDateTime.getDayOfMonth();
            hour = localDateTime.getHour();
            minute = localDateTime.getMinute();
            second = localDateTime.getSecond();
        }


        protected void trySecondInc(){
            if (atomicInteger.decrementAndGet() == 0) {
                atomicInteger.set(1000);
                this.second ++ ;
                if(this.second == 60){
                    minuteInc();
                }
            }

        }

        private void minuteInc(){
            this.minute ++ ;
            if(this.minute % 15 == 0){

            }
        }

        /**重置 虚拟时钟   ---by jason @ 2023/3/26 14:02 */
        public void reSet(){
            LocalDateTime localDateTime = LocalDateTime.now();
            year = localDateTime.getYear();
            month = localDateTime.getMonthValue();
            dayOfMonth = localDateTime.getDayOfMonth();
            hour = localDateTime.getHour();
            minute = localDateTime.getMinute();
            second = localDateTime.getSecond();
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

}
