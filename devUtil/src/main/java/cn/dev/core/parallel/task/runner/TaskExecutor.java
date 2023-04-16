package cn.dev.core.parallel.task.runner;

import cn.dev.clock.schedule.ScheduleManger;
import cn.dev.clock.schedule.ScheduleTaskRegister;
import cn.dev.commons.log.DLog;
import cn.dev.core.parallel.share.LocalTaskShareHelper;
import cn.dev.core.parallel.share.LocalTaskShareListener;
import cn.dev.core.parallel.share.TaskShareData;
import cn.dev.core.parallel.task.api.ITaskFunction;
import cn.dev.core.parallel.task.api.runner.IRunnerCallbackHelper;
import cn.dev.core.parallel.task.api.runner.ITaskRunner;
import cn.dev.core.parallel.task.api.runner.ITaskRunnerMonitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskExecutor {

    /**
     * 线程间数据传递的 支持配置 ，默认启用
     */
    protected static boolean threadLocalShareSupport = true;

    public static void configThreadLocalShareSupport(boolean threadLocalShareSupport) {
        TaskExecutor.threadLocalShareSupport = threadLocalShareSupport;
    }



    private static final ExecutorService executorService =DefaultTaskThreadPool.getExecutorService();

//    public static void main(String[] args) {
//        List<ITaskRunner> list = new ArrayList<>();
//        for(int i = 0 ; i < 512 ; i++){
//            getRunner().execute(()->{
//                Thread.sleep(10);
//                System.out.println( Thread.currentThread().getName() );
//            });
//        }
//        System.out.println("hohoho");
//    }


    public static TaskFuture execute(ITaskFunction taskFunction){
        return getRunner().execute(taskFunction);
    }


    public static ITaskRunner getRunner(){
        return DefaultTaskRunner.newBuild(executorService);
    }

    public static ITaskRunnerMonitor getMonitor(){
        return (ITaskRunnerMonitor) getRunner();
    }

    public static IRunnerCallbackHelper getCallbackHelper(){
        return (IRunnerCallbackHelper) getRunner();
    }




}
class threadSafeExecutor implements AutoCloseable{

    private TaskShareData shareData ;


    public threadSafeExecutor() {
        if(TaskExecutor.threadLocalShareSupport){
            if (LocalTaskShareHelper.isSharerSet()) {
                shareData = LocalTaskShareHelper.getSharer().toShareData();
            }
        }
    }







    @Override
    public void close() throws Exception {

    }
}