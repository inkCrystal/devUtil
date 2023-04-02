package cn.dev.commons.id;

import cn.dev.commons.RandomUtil;
import cn.dev.commons.datetime.DateTimeUtil;
import cn.dev.clock.CommonTimeClock;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author : JasonMao
 * @version : 1.0
 * @date : 2023/3/16 13:49
 * @description :
 */
public class IdHelper {

    private static IdHelper instance = null;


    private static final long serialVersionUID = -91763634792618211L;
    /**构建机器id  ---by JasonMao @ 2022/12/15*/
    private int workerId = 0;

    private long currentIndex = 0 ;

    private final long maxIndexLimit ;

    /**最后更新  ---by JasonMao @ 2023/2/1 15:29 */
    private long lastTimestamp = -1L;

    /**  索引长度 决定了每毫秒的  单机 id 容量
     *  默认10，每毫秒 单机 1024个Id容量
     *   10 代表 单机 1024个容量
     *   11 代表 单机 2048
     *   12 代表 待机 4096
     * ---by JasonMao @ 2023/2/1 15:36
     * */
    private long LEN_INDEX = 11 ;

    private IdHelper(long LEN_INDEX, int LEN_WORKER_ID) {
        this.LEN_INDEX = LEN_INDEX;
        this.LEN_WORKER_ID = LEN_WORKER_ID;
        this.maxIndexLimit = (1L << this.LEN_INDEX) - 1L;
        this.init();
    }

    private IdHelper() {
        this.maxIndexLimit = (1<<this.LEN_INDEX) -1 ;
        this.init();
    }

    private static long getMillis() {

        return CommonTimeClock.currentTimeMillis();
    }


    public void setWorkerId(int workerId) {
        if(workerId >= (1<<LEN_WORKER_ID)){
            throw new RuntimeException("workId不合法，请小于"+ (1<<LEN_WORKER_ID));
        }
        if(workerId < 0){
            throw new RuntimeException("workId不合法，请大于等于0");
        }
        this.workerId = workerId;
    }

    public int getWorkerId() {
        return workerId;
    }

    private synchronized void init(){
        if(instance != null){
            throw new RuntimeException("无法构建多个实例");
        }
        this.LEN_MILLIS = 63-(LEN_INDEX + LEN_WORKER_ID);
        instance = this;
        this.setWorkerId(RandomUtil.nextInt());

    }

    public static IdHelper getInstance(){
        if (instance ==null) {
            new IdHelper();
        }
        return instance;
    }

    public static IdHelper buildInstance(long LEN_INDEX, int LEN_WORKER_ID){
        if(instance == null) {
            new IdHelper(LEN_INDEX, LEN_WORKER_ID);
        }
        return getInstance();
    }


    /**
     * 机器码长度 决定了 最多可以 有多少台机器 可以共同协同
     *  默认：10 ----1024台机器容量
     * 1 代表  2 台
     * 2 代表  4台
     * ...
     * 8 代表 256台
     * 9 代表 512台
     * 10 代表 1024台
     * ---by JasonMao @ 2023/2/1 15:36 */
    private int LEN_WORKER_ID = 9;

    private long LEN_MILLIS = 43;


    public String info(){
        StringBuilder sb = new StringBuilder("\n");
        sb.append("\nWORKER_ID = ").append(this.workerId)
                .append("\nINDEX_LMT = ").append(this.maxIndexLimit);
        return sb.toString();
    }

    //**时间戳 开始时间 ，我们从 1990-1-1 0点开始计算
    // ---by JasonMao @ 2023/2/1 15:44 */
    private static final long MIN_MILLIS = DateTimeUtil.toEpochMilli(
            LocalDateTime.of(1990,1,1,0,0,0,0)
    );


    private long computeLongId(long time , long workerId , long index){
        return (time - MIN_MILLIS)<<(LEN_INDEX + LEN_WORKER_ID) | (index << LEN_WORKER_ID) | workerId;
    }

    private synchronized long getCurrentComputeAbleTime(){
        long time = getMillis();
        if(time< lastTimestamp){
            time = lastTimestamp;
        }
        if(time == lastTimestamp){
            currentIndex ++ ;
            if(currentIndex >= maxIndexLimit){
                if(currentIndex-maxIndexLimit > 10){
                    this.currentIndex = 0;
                    this.lastTimestamp ++ ;
                    return this.lastTimestamp;
                }
                return getCurrentComputeAbleTime();
            }
        }else{
            this.currentIndex = 0;
            this.lastTimestamp = time;
        }
        return time;
    }



    /**
     * 得到一个 long类型的id
     * @return
     */
    public final long getNextId(){
        long time = getCurrentComputeAbleTime();
        return computeLongId(time,workerId,this.currentIndex);
    }

    /**
     * 得到一个16进制的id
     * @return
     */
    public final String getNextHexId(){
        return Long.toHexString(getNextId()).toUpperCase();
    }

    /**
     * 得到一个36进制表示法的id
     * @return
     */
    public final String getNextBase36Id(){
        return Long.toString(getNextId(),36).toUpperCase();
    }

    /**
     * 得到一个64进制 表示法的Id
     * @return
     */
    public final String getNextBase64Id(){
        return Long.toString(getNextId(),64).toUpperCase();
    }


    /**
     * 通过id 计算出 id生成时间
     * @param id
     * @return
     */
    public long getTimeMillisById(long id){
        return (id>>(LEN_INDEX+LEN_WORKER_ID))+ MIN_MILLIS;
    }

    /**
     * 获取指定毫秒时间戳的最小id
     * @param time
     * @return
     */
    public long getFirstIdOfTime(long time){
        return computeLongId(time,workerId,0);
    }


    /**
     * 获取指定时间的最小id
     * @param date
     * @return
     */
    public long getFirstIdOfTime(Date date){
        return getFirstIdOfTime(date.getTime());
    }

    /**
     * 获取指定时间的 最小id ！
     * @param localDateTime
     * @return
     */
    public long getFirstIdOfTime(LocalDateTime localDateTime){
        long time = DateTimeUtil.toEpochSecond(localDateTime);
        return getFirstIdOfTime(time);
    }

    /**
     * 模拟指定时间的id ！
     * @param time
     * @return
     */
    public long getFakeIdByTime(long time){
        return computeLongId(time,workerId, maxIndexLimit /2 + RandomUtil.nextInt());
    }


    public static final long  nextId(){
        return getInstance().getNextId();
    }



    public static final String nextHexId(){
        return getInstance().getNextHexId();
    }


    public static final String nextBase64Id(){
        return getInstance().getNextBase64Id();
    }

    public static final String nextBase36Id(){
        return getInstance().getNextBase36Id();
    }



}
