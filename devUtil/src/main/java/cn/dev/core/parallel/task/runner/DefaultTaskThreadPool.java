package cn.dev.core.parallel.task.runner;


import cn.dev.commons.log.DLog;

import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author : JasonMao
 * @version : 1.0
 * @date : 2022/10/26 21:09
 * @description : saiL 核心 线程池
 */

public class DefaultTaskThreadPool {

    private static final DefaulthreadPoolMonitor monitor;
    private static int queueCapacity = 128;

    /**
     * 核心线程数量，不会被回收
     */
    private static final int corePoolSize = 2  ;
    /**
     *最大线程数量，超过核心数量当空闲时候会被回收
     */
    private static final int maximumPoolSize = Runtime.getRuntime().availableProcessors() ;
    /**
     * 拒绝策略采用  ，尝试 加queue 三次，如果 失败，让生产线程 自己跑去 ！
     */
    private static final ThreadPoolExecutor executorService;

    static {
        ArrayBlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(queueCapacity){
            @Override
            public Runnable poll() {
                Runnable r =super.poll();
                whileQueuePoll();
                return r;
            }

            @Override
            public Runnable poll(long timeout, TimeUnit unit) throws InterruptedException {
                Runnable r = super.poll(timeout, unit);
                whileQueuePoll();
                return r;
            }

        };
        DefThreadFactory threadFactory = new DefThreadFactory();
        DefRejectedExecutionHandler handler = new DefRejectedExecutionHandler();
        executorService = new ThreadPoolExecutor(
                    corePoolSize,maximumPoolSize,45, TimeUnit.SECONDS,
                workQueue, threadFactory, handler);

        monitor = new DefaulthreadPoolMonitor(executorService , "TASK-THREAD-POOL");

    }


    /**
     * 当阻塞对了 poll 时候 发生
     */
    private static void whileQueuePoll(){
        if (DLog.isTraceEnabled()) {

        }
        int size = executorService.getQueue().size();
        DLog.trace("目前依然有"+ size +"个任务在排队，等待线程池处理。");
    }


    /**
     * 自定义得 ThreadFactory ，用于创建线程，给线程标记可识别得名称
     */
    static class DefThreadFactory implements ThreadFactory {
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        DefThreadFactory() {
            group = Thread.currentThread().getThreadGroup();
            namePrefix = "TASK-THREAD-";
        }

        @Override
        public Thread newThread(Runnable r) {
            int number = threadNumber.getAndIncrement();
            Thread t = new Thread(group, r,
                    namePrefix +  (number>9?number:"0"+number),
                    0);
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }

    /**
     *  拒绝策略
     */
    static class DefRejectedExecutionHandler implements RejectedExecutionHandler{
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            boolean offerResult = false;
            int tryCount = 3 ;
            while (!offerResult && tryCount >0 ){
                tryCount -- ;
                offerResult = tryOffer(r,executor.getQueue());
                if(offerResult == false) {
                    try {
                        Thread.sleep(1);
                    } catch (Exception e) {
                    }
                }
            }

            if(!offerResult ) {
                try{
                    // 阻塞pu
                    DLog.warn("【1】异步线程过于繁忙才提示得警告,多次异步排队未成功，尝试塞生产线程排队，强制等待任务进入队列。----当前异步线程缓冲队列容量["+queueCapacity+"]") ;
                    //阻塞生产线程 put 进去
                    executor.getQueue().put(r);
                }catch (Exception ex){
                    //实在没办法了，让生产线程自己玩吧。这边一般不可能进入
                    if (!executor.isShutdown()) {
                        DLog.warn("【2】异步线程远远超过了系统预期，触发最严格得容错机制，将直接使用生产线程执行该任务！");
                        r.run();
                    }
                }
            }
        }
        private boolean tryOffer(Runnable r , Queue<Runnable> queue){
            try {
                Thread.sleep(1);
            }catch (Exception e){}
            return queue.offer(r);
        }
    }


    protected static Executor getExecutor() {
        return executorService;
    }

    protected static ExecutorService getExecutorService(){
        return executorService;
    }

    protected static final DefaulthreadPoolMonitor getMonitor(){
        return monitor;
    }








}
