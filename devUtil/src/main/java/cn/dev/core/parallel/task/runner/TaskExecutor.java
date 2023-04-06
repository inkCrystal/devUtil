package cn.dev.core.parallel.task.runner;

import cn.dev.core.parallel.task.api.runner.IRunnerCallbackHelper;
import cn.dev.core.parallel.task.api.runner.ITaskRunner;
import cn.dev.core.parallel.task.api.runner.ITaskRunnerMonitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TaskExecutor {

    private static final Executor executor = Executors.newFixedThreadPool(12);

    private static final Queue<DefaultTaskRunner> activeQueue =new ArrayBlockingQueue<>(10);

    static {
        // auto check
    }

    private static void autoCheck(){

    }




    public static ITaskRunner getRunner(){
        return DefaultTaskRunner.newBuild();
    }


    public static ITaskRunnerMonitor getMonitor(){
        return DefaultTaskRunner.newBuild();
    }

    public static IRunnerCallbackHelper getCallbackHelper(){
        return DefaultTaskRunner.newBuild();
    }


}
