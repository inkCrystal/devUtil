package cn.dev.clock.schedule;

import cn.dev.clock.DateTimeEntry;
import cn.dev.clock.TimeMillisClock;
import cn.dev.commons.ArrayUtil;
import cn.dev.commons.BinaryTool;
import cn.dev.commons.RandomUtil;
import cn.dev.commons.datetime.DateTimeUtil;
import cn.dev.commons.verification.VerificationTool;
import cn.dev.exception.ScheduleConfigException;
import cn.dev.parallel.task.api.ITaskFunction;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * 本配置 不支持每秒 执行1次的配置，因为这 可能应用场景并不可取 ，
 * 且可能在项目应用会造成过度的系统负载
 */
public class ScheduledConfig {

    /**
     * 月份和 日 的配置 。
     * 0位空缺
     * 1-12位表示 月份配置 ，offset =0
     * 13-34位表示 日 配置，offset =12
     */
    private long mothAndDayConfig =  0;
    private long hourConfig;
    private long minuteConfig;
    private int secondConfig;

    /**
     * 失效时间 ，设置失效时间后，将会在 到达指定事件后失效
     */
    private long expireTime = Long.MAX_VALUE;

    /**
     * 最大允许执行次数，-1表示 不限制
     */
    private int maxFireCount =-1 ;
    /**
     * 触发次数
     */
    private long fireCount = 0 ;

    /**
     * 下次触发的时间戳 （变化的 ）
     */
    private DateTimeEntry nextFireTime;

    /**
     * 最后触发时间
     */
    private long lastFireTime = 0;


    /**
     * 有效状态
     */
    private boolean ableState = false;

    private ITaskFunction scheduledTask = null;


    /**
     * 启用配置
     * @param taskFunction
     * @return
     * @throws RuntimeException
     */
    protected boolean enable(ITaskFunction taskFunction) throws ScheduleConfigException{
        this.safeCheck();
        VerificationTool.isNotNull(taskFunction);
        this.scheduledTask = taskFunction;
        this.ableState =true;
        return true;
    }

    private void configSafeCheck(String typeConfigName, long value , int startIndex ) throws ScheduleConfigException {
        boolean[] arr = BinaryTool.toBoolArray(value);
        int count = 0;
        for (int i = startIndex; i < arr.length; i++) {
            if(arr[i]){
                count ++ ;
            }
        }
        if(count == 0){
            throw new ScheduleConfigException(typeConfigName + "缺失可以匹配的明确时间点");
        }
    }
    private void safeCheck() throws ScheduleConfigException {
        this.configSafeCheck("配置的可执行月：",getMonthConfig(),1);
        this.configSafeCheck("配置的可执行日：",getDayOfMonthConfig(),1);
        this.configSafeCheck("配置的可执行时：",this.hourConfig,0);
        this.configSafeCheck("配置的可执行分：",this.hourConfig,0);

    }



    /**
     * 是否有效
     * @return
     */
    public boolean isAvailable(){
        if(ableState &&  TimeMillisClock.currentMillis() < this.expireTime){
            if(maxFireCount > 0 ){
                return fireCount < maxFireCount;
            }
            return true;
        }
        return false;
    }



    public void setExpireTime(long expireTime) {
        if(expireTime > TimeMillisClock.currentTimeMillis()) {
            this.expireTime = expireTime;
        }
    }

    /**
     * 设置最大允许执行次数
     * @param maxTime
     */
    public void setMaxRuntimes(int maxTime){
        if(maxFireCount <0 && maxTime > 0) {
            this.maxFireCount = maxTime;
        }
    }



    private long set(long source , int offSet , int minValue , int maxValue , Predicate<Integer> predicate){
        for (int i = minValue; i <= maxValue ; i++) {
            if (predicate.test(i)) {
                long t =1;
                for (int j = 0; j <i+offSet; j++) {
                    t = t<<1;
                }
                source = source|t;
            }
        }
        return source;
    }
    public ScheduledConfig setDayConfigPredicate(Predicate<Integer> predicate){
        this.mothAndDayConfig = set(this.mothAndDayConfig,12,1,31,predicate);
        //System.out.println(BinaryTool.toHorizontalString(this.mothAndDayConfig));

        return this;
    }

    public ScheduledConfig setMonthConfigPredicate(Predicate<Integer> predicate){
        this.mothAndDayConfig = set(this.mothAndDayConfig,0,1,12,predicate);
        return this;
    }

    public ScheduledConfig setHourConfigPredicate(Predicate<Integer> predicate){
        this.hourConfig = set(this.hourConfig,0,0,23,predicate);
        return this;
    }

    public ScheduledConfig setMinuteConfigPredicate(Predicate<Integer> predicate){
        this.minuteConfig = set(this.minuteConfig,0,0,59,predicate);
        return this;
    }

    public ScheduledConfig setSecondConfigPredicate(int secondConfig){
//        this.secondConfig = set(this.secondConfig,0,0,59,predicate);
        this.secondConfig = secondConfig;
        return this;
    }


//    public ScheduledConfig setDayConfig(int... days){
//
//    }

    public static ScheduledConfig everyYear(int month , int dayOfMonth){
        return everyYear(month,dayOfMonth,0);
    }
    public static ScheduledConfig everyYear(int month , int dayOfMonth, int hour){
        return everyYear(month,dayOfMonth,hour,0);
    }
    public static ScheduledConfig everyYear(int month , int dayOfMonth, int hour, int minute ){
        return everyYear(month,dayOfMonth,hour,minute,0);
    }
    public static ScheduledConfig everyYear(int month , int dayOfMonth, int hour , int minute, int second){
        System.out.println(month  + " " + dayOfMonth );
        return new ScheduledConfig()
                .setMonthConfigPredicate(m->m==month||month<0)
                .setDayConfigPredicate(d->d==dayOfMonth||dayOfMonth<0)
                .setHourConfigPredicate(h->h==hour||hour<0)
                .setMinuteConfigPredicate(m-> m == minute || minute < 0)
                .setSecondConfigPredicate(second);
    }
    public static ScheduledConfig everyMonth(int dayOfMonth, int hour, int minute , int second){
        return everyYear(-1,dayOfMonth,hour,minute,second);
    }

    public static ScheduledConfig everyMonth(int dayOfMonth, int hour , int minute ){
        return everyMonth(dayOfMonth,hour,minute,0);
    }

    public static ScheduledConfig everyMonth(int dayOfMonth, int hour){
        return everyMonth(dayOfMonth,hour,0);
    }
    public static ScheduledConfig everyMonth(int dayOfMonth){
        return everyMonth(dayOfMonth,0);
    }

    public static ScheduledConfig everyDay(int hour, int minute , int second){
        return everyMonth(-1,hour,minute,second);
    }

    public static ScheduledConfig everyDay(int hour, int minute){
        return everyDay(hour,minute,0);
    }
    public static final ScheduledConfig everyDay(int hour){
        return everyDay(hour,0);
    }

    public static final ScheduledConfig everyHour(int minute , int second){
        return everyDay(-1,minute,second);
    }


    public static ScheduledConfig everyHour(int minute){
        return everyHour(minute,0);
    }

    public static ScheduledConfig everyMinute(int second){
        return everyHour(-1,second);
    }

    /**
     * 当 运行 触发 秒 = 0 的时候，如果调用此配置，
     * 触发秒会被 重置为 一个 0-59的随机数
     * @return
     */
    public ScheduledConfig randomSecondIfZero(){
        if(this.secondConfig == 0){
            int newS = RandomUtil.nextInt()%60;
            System.out.println(secondConfig + ">" + newS + ">" + newS%60);
            this.secondConfig = newS;
        }
        return this;
    }

//
//    /**
//     * 下次触发 的 时间 ，不对外开放
//     * @return
//     */
//    private Optional<LocalDateTime> computeNextFireLocalDateTime(){
//        if (!this.isAvailable()) {
//            return Optional.ofNullable(null);
//        }
//
//        LocalDateTime dt =DateTimeUtil.now();
//        int minute = dt.getMinute();
//        int hour = dt.getHour();
//        int dayOfMonth = dt.getDayOfMonth();
//        int year = dt.getYear();
//        int second = dt.getSecond();
//
//
//        int fireSecond = this.secondConfig;
//        if(fireSecond > second){
//            second = fireSecond;
//        }else{
//
//        }
//
//
//
//        if(fireSecond > currentSecond){
//            dt = dt.plusSeconds(fireSecond -currentSecond);
//        }else if(currentSecond > fireSecond){
//            dt = dt.minusSeconds( currentSecond - fireSecond);
//        }
//        if(dt.isAfter(LocalDateTime.now())){
//            dt = dt.plusMinutes(1);
//        }
//        while (!testFire(dt)){
//            dt = dt.plusMinutes(1);
//        }
//        return Optional.ofNullable(dt);
//    }

//    public long nextFireTimeMills(){
//        if(nextFireTimeMills > 0 && nextFireTimeMills > TimeMillisClock.currentTimeMillis()){
//            return nextFireTimeMills;
//        }
//        final Optional<LocalDateTime> optional = computeNextFireLocalDateTime();
//        if (optional.isPresent()) {
//            this.nextFireTimeMills = DateTimeUtil.toEpochMilli(optional.get());
//            return this.nextFireTimeMills;
//        }else {
//            this.ableState = false;
//        }
//        return -1;
//
//    }


    protected void fire(){
        this.fireCount ++ ;
        this.lastFireTime = TimeMillisClock.currentTimeMillis();
//        this.nextFireTimeMills();
    }

    public boolean testFire(LocalDateTime localDateTime){
        return testFire(localDateTime.getMonthValue(),localDateTime.getDayOfMonth(),localDateTime.getHour(),localDateTime.getMinute(),localDateTime.getSecond());
    }
    public boolean testFire( int month, int dayOfMonth, int hour, int minute, int second){
        if(second == this.secondConfig){
            int offSet = 0 ;
            if (isCheckPositionIsTrue(this.minuteConfig , minute, offSet)) {
                if(isCheckPositionIsTrue(this.hourConfig,hour , offSet)){
                    offSet =12;
                    if(isCheckPositionIsTrue(this.mothAndDayConfig,dayOfMonth , offSet)){
                        offSet =0 ;
                        return isCheckPositionIsTrue(this.mothAndDayConfig, month , offSet);
                    }
                }
            }
        }
        return false;
    }

    private boolean isCheckPositionIsTrue(long source ,int target, int offSet) {
        return BinaryTool.checkPositionIsTrue(source, target + offSet);
    }

    public static String dateString(){
        return  LocalDateTime.of(2023, 12, 33, 1, 1, 1).toString();
    }


    public long getDayOfMonthConfig(){
        boolean[] booleans = BinaryTool.toBoolArray(this.mothAndDayConfig);
        int offSet =12 ;
        boolean[] dConfig = new boolean[64];
        for (int i = 1; i <= 31; i++) {
            dConfig[i] = booleans[i+offSet];
        }
        return BinaryTool.valueOfBooleanArray(dConfig);
    }
    public long getMonthConfig(){
        boolean[] booleans = BinaryTool.toBoolArray(this.mothAndDayConfig);
        boolean[] mConfig = new boolean[64];
        for (int i = 1; i <= 12; i++) {
            mConfig[i] = booleans[i];
        }
        return BinaryTool.valueOfBooleanArray(mConfig);
    }


    public String disPlayTable(){
        StringBuilder sb =new StringBuilder("配置图表");
        sb.append(BinaryTool.toHorizontalString(this.mothAndDayConfig));
        sb.append("\n运行匹配月份配置:\n");
        String s = BinaryTool.toHorizontalString(getMonthConfig()).toString();
        sb.append(s);
        sb.append("\n运行匹配日配置:\n");
        s = BinaryTool.toHorizontalString(getDayOfMonthConfig()).toString();
        sb.append(s);
        sb.append("\n运行匹配小时配置:\n");
        sb.append(BinaryTool.toHorizontalString(this.hourConfig));
        sb.append("\n运行匹配分配置:\n");
        sb.append(BinaryTool.toHorizontalString(this.minuteConfig));
        sb.append("\n运行秒 = ").append(this.secondConfig);
        return sb.toString();

    }

//
//    private int[] getConfigMonthArray(){
//        final long monthConfig = getMonthConfig();
//        final boolean[] booleans = BinaryTool.toBoolArray(monthConfig);
//
//    }


    public static void main(String[] args) {
        ScheduledConfig config =
                ScheduledConfig.everyDay(3, 12).randomSecondIfZero();
        System.out.println(config.disPlayTable());
    }

}