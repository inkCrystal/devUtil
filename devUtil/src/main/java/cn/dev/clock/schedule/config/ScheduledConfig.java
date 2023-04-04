package cn.dev.clock.schedule.config;

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
    public boolean enableConfig(ITaskFunction taskFunction) throws ScheduleConfigException{
        this.tester().safeCheck();
        VerificationTool.isNotNull(taskFunction);
        this.scheduledTask = taskFunction;
        this.ableState =true;
        return true;
    }


    /**
     * 获取配置测试类
     * @return
     */
    public ScheduledConfigTester tester(){
        return new ScheduledConfigTester(this);
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

    // 触发 事件
    protected void fire(){
        this.fireCount ++ ;
        this.lastFireTime = CommonTimeClock.currentTimeMillis();
        //todo  需要完善实现
    }

    /**
     * 测试是否会在指定时间 触发
     * @param localDateTime
     * @return
     */
    public boolean testFire(LocalDateTime localDateTime){
        return testFire(localDateTime.getMonthValue(),localDateTime.getDayOfMonth(),localDateTime.getHour(),localDateTime.getMinute(),localDateTime.getSecond());
    }

    /**
     * 测试是否会触发
     * @param month
     * @param dayOfMonth
     * @param hour
     * @param minute
     * @param second
     * @return
     */
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

    /** 获取触发月份和日的配置
     * @return
     */
    public long getMothAndDayConfig() {
        return mothAndDayConfig;
    }

    /**
     * 获取触发时的配置
     * @return
     */
    public long getHourConfig() {
        return hourConfig;
    }

    /**
     * 获取触发分的配置
     * @return
     */
    public long getMinuteConfig() {
        return minuteConfig;
    }

    /**
     * 获取触发秒的配置
     * @return
     */
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


    /** setter 。   ---by jason @ 2023/4/4 11:15 */

    protected void setMothAndDayConfig(long mothAndDayConfig) {
        VerificationTool.isFalse(ableState,"配置已经启用，无法修改配置");
        this.mothAndDayConfig = mothAndDayConfig;
    }

    protected void setHourConfig(long hourConfig) {
        VerificationTool.isFalse(ableState,"配置已经启用，无法修改配置");
        this.hourConfig = hourConfig;
    }

    protected void setMinuteConfig(long minuteConfig) {
        VerificationTool.isFalse(ableState,"配置已经启用，无法修改配置");
        this.minuteConfig = minuteConfig;
    }

    protected void setSecondConfig(int secondConfig) {
        VerificationTool.isFalse(ableState,"配置已经启用，无法修改配置");
        this.secondConfig = secondConfig;
    }


}

