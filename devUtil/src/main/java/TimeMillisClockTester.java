import cn.dev.commons.datetime.DateTimeUtil;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author : JasonMao
 * @version : 1.0
 * @date : 2023/3/17 14:49
 * @description :
 */
public class TimeMillisClockTester {

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
        private static final TimeMillisClockTester INSTANCE = new TimeMillisClockTester(1);
    }

    private static final TimeMillisClockTester getInstance(){
        return InstanceHolder.INSTANCE;
    }
    private TimeMillisClockTester(int period) {
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
            long currentTimeMillis = System.currentTimeMillis();
            now.set(currentTimeMillis);
            clock.trySecondInc(currentTimeMillis);
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
        System.out.println("让我们开始吧 ");
        int errorCount = 0;
        int testCount = 0 ;
        getInstance();
        while (true){
            testCount ++ ;
            final String localDateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss"));
            final String vClockStr = getInstance().getVClock().toString();
            if(!vClockStr.equals(localDateStr)){
                errorCount ++ ;
                System.out.println("ERROR " + errorCount + " times "   );
                System.out.println("-------------------------");
                System.out.println("Local DateTime " + localDateStr);
                System.out.println("VClockDateTime " + vClockStr);
                System.out.println("-------------------------");
            }else{
                System.out.print("\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b" +
                        "" + vClockStr );


            }
            if (testCount%60 == 0 ) {
                System.out.println("检测"+(testCount/60) + "分钟， 目前出现偏差"+errorCount+"次");
            }

            while (true) {
                Thread.sleep(100);
                long testT =System.currentTimeMillis() %1000 ;
                if (testT > 500 && testT < 600) {
                    break;
                }
            }

        }

    }

    class vClock{
        private int year, month , dayOfMonth , hour,minute ,second ;
        //private static AtomicInteger atomicInteger ;
        static int checkSeq = 330;

        protected vClock() {
//            atomicInteger = new AtomicInteger(checkSeq);
            //this.reSet();
        }

        private static long lastCallMillis = 0;

        private int checkSecondPlus(long currentMillis){
            if(lastCallMillis == 0){
                lastCallMillis = currentMillis;
                this.reSet();
                return 0;
            }
            if(currentMillis - lastCallMillis > 100){
                long tms = currentMillis % 1000;
                if(tms > 980 || tms < 20){
//                    System.out.printf("@" + tms + " :");
                    lastCallMillis = currentMillis;
                    return 1 ;
                }
            }
            return 0;
        }

        protected void trySecondInc(long currentMillis ){
            int i = checkSecondPlus(currentMillis);
            if(i>0){
                if(second <59) {
                    second += i;
                }else{
                    this.minuteInc();
                }
            }


        }

        private void minuteInc(){
            this.minute ++ ;
            if(minute%3==0) {
                reSet();
            }
        }

        /**重置 虚拟时钟   ---by jason @ 2023/3/26 14:02 */
        public void reSet(){
            //
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
