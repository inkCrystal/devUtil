package com.dev.commons.id;

import com.dev.commons.RandomUtil;
import com.dev.commons.datetime.DateTimeUtil;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author : JasonMao
 * @version : 1.0
 * @date : 2023/3/16 13:49
 * @description :
 */
public class IdUtil {
//    private static DefaultIdGenerator instance = null;
//    private static final long serialVersionUID = -91763634792618211L;
//    /**构建机器id  ---by JasonMao @ 2022/12/15*/
//    private int workerId = 0;
//
//    private long currentIndex = 0 ;
//
//    private final long maxIndexLimit ;
//
//    /**最后更新  ---by JasonMao @ 2023/2/1 15:29 */
//    private long lastTimestamp = -1L;
//
//    /**  索引长度 决定了每毫秒的  单机 id 容量
//     *  默认10，每毫秒 单机 1024个Id容量
//     *   10 代表 单机 1024个容量
//     *   11 代表 单机 2048
//     *   12 代表 待机 4096
//     * ---by JasonMao @ 2023/2/1 15:36
//     * */
//    private long LEN_INDEX = 11 ;
//
//    private DefaultIdGenerator(long LEN_INDEX, int LEN_WORKER_ID) {
//        this.LEN_INDEX = LEN_INDEX;
//        this.LEN_WORKER_ID = LEN_WORKER_ID;
//        this.maxIndexLimit = (1 << this.LEN_INDEX) - 1;
//        this.init();
//    }
//
//    private DefaultIdGenerator() {
//        this.maxIndexLimit = (1<<this.LEN_INDEX) -1 ;
//        this.init();
//
//    }
//
//    public void setWorkerId(int workerId) {
//        if(workerId >= (1<<LEN_WORKER_ID)){
//            throw new RuntimeException("workId不合法，请小于"+ (1<<LEN_WORKER_ID));
//        }
//        if(workerId < 0){
//            throw new RuntimeException("workId不合法，请大于等于0");
//        }
//        this.workerId = workerId;
//    }
//
//    public int getWorkerId() {
//        return workerId;
//    }
//
//    private synchronized void init(){
//        if(instance != null){
//            throw new RuntimeException("无法构建多个实例");
//        }
//        this.LEN_MILLIS = 63-(LEN_INDEX + LEN_WORKER_ID);
//        instance = this;
//        Long x =  RandomUtil.nextLong();//System.nanoTime()%(1<<LEN_WORKER_ID);
//        this.setWorkerId(x.intValue());
//
//    }
//
//    public static final DefaultIdGenerator getInstance(){
//        if (instance ==null) {
//            new DefaultIdGenerator();
//        }
//        return instance;
//    }
//
//    public static final DefaultIdGenerator buildInstance(long LEN_INDEX, int LEN_WORKER_ID){
//        if(instance == null) {
//            new DefaultIdGenerator(LEN_INDEX, LEN_WORKER_ID);
//        }
//        return getInstance();
//    }
//
//
//    /**
//     * 机器码长度 决定了 最多可以 有多少台机器 可以共同协同
//     *  默认：10 ----1024台机器容量
//     * 1 代表  2 台
//     * 2 代表  4台
//     * ...
//     * 8 代表 256台
//     * 9 代表 512台
//     * 10 代表 1024台
//     * ---by JasonMao @ 2023/2/1 15:36 */
//    private int LEN_WORKER_ID = 9;
//
//    private long LEN_MILLIS = 43;
//
//
//    public String info(){
//        StringBuilder sb = new StringBuilder("\n");
//        sb
//                .append("\nWORKER_ID = ").append(this.workerId)
//                .append("\nINDEX_LMT = ").append(this.maxIndexLimit);
//        return sb.toString();
//    }
//
//    //**时间戳 开始时间 ，我们从 1990-1-1 0点开始计算
//    // ---by JasonMao @ 2023/2/1 15:44 */
//    private static final long MIN_MILLIS =
//            DateTimeUtil.toEpochMilli(
//                    LocalDateTime.of(1990,1,1,0,0,0,0)
//            );
//
//
//    private final long doCompute(long time ,long workerId ,long index){
//        return (time - MIN_MILLIS)<<(LEN_INDEX + LEN_WORKER_ID) | (index << LEN_WORKER_ID) | workerId;
//    }
//
//    private void doIndexInc(){
//        this.currentIndex ++ ;
//        if(currentIndex == maxIndexLimit){
//            this.currentIndex = 0;
//        }
//    }
//
//
//    private synchronized long getCurrentComputeAbleTime(){
//        long time = TimeMillisClock.currentTimeMillis();
//        if(time< lastTimestamp){
//            time = lastTimestamp;
//        }
//        if(time == lastTimestamp){
//            currentIndex ++ ;
//            if(currentIndex >= maxIndexLimit){
//                if(currentIndex-maxIndexLimit > 10){
//                    this.currentIndex = 0;
//                    this.lastTimestamp ++ ;
//                    return this.lastTimestamp;
//                }
//                return getCurrentComputeAbleTime();
//
////                try{Thread.sleep(1);}catch (Exception e){}
////                return getCurrentComputeAbleTime();
//            }
//        }else{
//            this.currentIndex = 0;
//            this.lastTimestamp = time;
//        }
//        return time;
//    }
//
//
//    public final long nextId(){
//        long time = getCurrentComputeAbleTime();
//        return doCompute(time,workerId,this.currentIndex);
//    }
//
//    public final String nextHexId(){
//        return Long.toHexString(nextId()).toUpperCase();
//    }
//
//    public final String nextBase36Id(){
//        return Long.toString(nextId(),36).toUpperCase();
//    }
//
//    public final String nextBase64Id(){
//        return Long.toString(nextId(),64).toUpperCase();
//    }
//
////   public IdAnalysis getAnalysis(){
////       if (idAnalysis == null) {
////           idAnalysis = new IdAnalysis();
////       }
////       return idAnalysis;
////   }
//    //---------------------------------
//
//    public long getTimeMillisById(long id){
////        log.info("通过id {} 获取时间戳 {} ",id , (LEN_INDEX_BIT+LEN_MACHINE_BIT) );
//        return (id>>(LEN_INDEX+LEN_WORKER_ID))+ MIN_MILLIS;
//    }
//
//    public long firstIdOfTime(long time){
//        return doCompute(time,workerId,0);
//    }
//
//    public long firstIdOfTime(Date date){
//        return firstIdOfTime(date.getTime());
//    }
//
//    public long firstIdOfTime(LocalDateTime localDateTime){
//        long time = DateTimeUtil.toEpochSecond(localDateTime);
//        return firstIdOfTime(time);
//    }
//
//    public long fakeIdByTime(long time){
//        return doCompute(time,workerId, maxIndexLimit /2 + RandomUtil.nextInt());
//    }


}
