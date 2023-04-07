package cn.dev.core.parallel.task.runner;


import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author : JasonMao
 * @version : 1.0
 * @date : 2023/2/24 11:14
 * @description :
 */
public class DefaulthreadPoolMonitor {

    private ThreadPoolExecutor pool;
    private final String name ;

    protected DefaulthreadPoolMonitor(ThreadPoolExecutor executorService, String name ) {
        this.pool = executorService;
        this.name = name.intern();
    }



    
    /**活跃的线程数  ---by JasonMao @ 2023/2/24 17:10 */
    public int activeThreadTaskCount(){
        return this.pool.getActiveCount();
    }

    /**复杂率>0.7 视为繁忙  ---by JasonMao @ 2023/2/24 17:09 */
    public double loadRate(){
        return this.activeThreadTaskCount() * 1.0 / this.maxPoolSize();
    }
    

    /**当前线程池大小  ---by JasonMao @ 2023/2/24 17:11 */
    public int poolSize(){
        return this.pool.getPoolSize();
    }

    /**最大的线程数  ---by JasonMao @ 2023/2/24 17:10 */
    public int maxPoolSize(){
        return this.pool.getMaximumPoolSize();
    }






}
