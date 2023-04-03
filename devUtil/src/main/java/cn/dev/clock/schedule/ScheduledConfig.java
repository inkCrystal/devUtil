package cn.dev.clock.schedule;

import cn.dev.clock.DateTimeEntry;
import cn.dev.clock.CommonTimeClock;
import cn.dev.commons.BinaryTool;
import cn.dev.commons.RandomUtil;
import cn.dev.commons.verification.VerificationTool;
import cn.dev.exception.ScheduleConfigException;
import cn.dev.parallel.task.api.ITaskFunction;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
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
    //触发小时 的配置
    private long hourConfig;
    //触发分钟 的配置
    private long minuteConfig;
    //触发秒 的配置
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
    //任务函数
    private ITaskFunction scheduledTask = null;


    /**
     * 启用配置
     * @param taskFunction
     * @return
     * @throws RuntimeException
     */
    protected boolean enableConfig(ITaskFunction taskFunction) throws ScheduleConfigException{
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
    //检查 配置参数的合法性， 会直接抛出 异常 ScheduleConfigException
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
    public boolean isConfigAvailable(){
        if(ableState &&  CommonTimeClock.currentMillis() < this.expireTime){
            if(maxFireCount > 0 ){
                return fireCount < maxFireCount;
            }
            return true;
        }
        return false;
    }


    /**
     * 设置配置 失效时间
     * @param expireTime
     */
    public void setExpireTime(long expireTime) {
        if(expireTime > CommonTimeClock.currentTimeMillis()) {
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
                var t= BinaryTool.leftMove(1,offSet+i);
                source = source| t;
            }
        }
        return source;
    }

    //设置月份配置
    public ScheduledConfig configSetDayConfigPredicate(Predicate<Integer> predicate){
        this.mothAndDayConfig = set(this.mothAndDayConfig,12,1,31,predicate);
        return this;
    }

    public ScheduledConfig configSetMonthConfigPredicate(Predicate<Integer> predicate){
        this.mothAndDayConfig = set(this.mothAndDayConfig,0,1,12,predicate);
        return this;
    }

    public ScheduledConfig configSetHourConfigPredicate(Predicate<Integer> predicate){
        this.hourConfig = set(this.hourConfig,0,0,23,predicate);
        return this;
    }

    public ScheduledConfig configSetMinuteConfigPredicate(Predicate<Integer> predicate){
        this.minuteConfig = set(this.minuteConfig,0,0,59,predicate);
        return this;
    }

    public ScheduledConfig configSetSecondConfigPredicate(int secondConfig){
        this.secondConfig = secondConfig;
        return this;
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
        System.out.println(month  + " " + dayOfMonth );
        return new ScheduledConfig()
                .configSetMonthConfigPredicate(m->m==month||month<0)
                .configSetDayConfigPredicate(d->d==dayOfMonth||dayOfMonth<0)
                .configSetHourConfigPredicate(h->h==hour||hour<0)
                .configSetMinuteConfigPredicate(m-> m == minute || minute < 0)
                .configSetSecondConfigPredicate(second);
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


    // 设置 多少时间后执行 1次
    public ScheduledConfig configRunDelay(int time , TimeUnit unit){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime fireTime = now.plusSeconds(unit.toSeconds(time));
        this.configSetMonthConfigPredicate(m->m==fireTime.getMonthValue());
        this.configSetDayConfigPredicate(d->d==fireTime.getDayOfMonth());
        this.configSetHourConfigPredicate(h->h==fireTime.getHour());
        this.configSetMinuteConfigPredicate(m->m==fireTime.getMinute());
        this.configSetSecondConfigPredicate(fireTime.getSecond());
        this.setMaxRuntimes(1);
        return this;
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


    protected void fire(){
        this.fireCount ++ ;
        this.lastFireTime = CommonTimeClock.currentTimeMillis();
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



    /**
     * 获取触发日的配置
     * @return
     */
    public long getDayOfMonthConfig(){
        boolean[] booleans = BinaryTool.toBoolArray(this.mothAndDayConfig);
        int offSet =12 ;
        boolean[] dConfig = new boolean[64];
        for (int i = 1; i <= 31; i++) {
            dConfig[i] = booleans[i+offSet];
        }
        return BinaryTool.valueOfBooleanArray(dConfig);
    }

    /**
     * 获取触发月份的配置
     * @return
     */
    public long getMonthConfig(){
        boolean[] booleans = BinaryTool.toBoolArray(this.mothAndDayConfig);
        boolean[] mConfig = new boolean[64];
        for (int i = 1; i <= 12; i++) {
            mConfig[i] = booleans[i];
        }
        return BinaryTool.valueOfBooleanArray(mConfig);
    }

    public long getHourConfig() {
        return hourConfig;
    }

    public long getMinuteConfig() {
        return minuteConfig;
    }

    public int getSecondConfig() {
        return secondConfig;
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
                ScheduledConfig.configEveryDay(3, 12).randomSecondIfZero();
        System.out.println(config.disPlayTable());
    }

}

