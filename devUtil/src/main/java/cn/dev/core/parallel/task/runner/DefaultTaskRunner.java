package cn.dev.core.parallel.task.runner;

import cn.dev.core.parallel.task.api.IFunction;
import cn.dev.core.parallel.task.api.runner.IRunnerCallbackHelper;
import cn.dev.core.parallel.task.api.ITaskFunction;
import cn.dev.core.parallel.task.api.runner.ITaskRunner;
import cn.dev.core.parallel.task.api.runner.ITaskRunnerMonitor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 抽象任务 Runner
 */
public class DefaultTaskRunner implements  ITaskRunner , IRunnerCallbackHelper ,ITaskRunnerMonitor{
//    private static final ExecutorService executor = Executors.newFixedThreadPool(8);

    private ExecutorService executorService;
    protected final static DefaultTaskRunner newBuild(ExecutorService executorService){
        return new DefaultTaskRunner(executorService);
    }

    private DefaultTaskRunner(ExecutorService executorService){
        this.executorService = executorService;
    }


    private AtomicLong totalTaskCount = new AtomicLong(0);
    private AtomicLong totalErrorCount = new AtomicLong(0);
    private AtomicLong startTaskCount = new AtomicLong(0);


    private FunctionTaskListenerManager listenerManager = FunctionTaskListenerManager.getManager();

    final void toFireListenerEvent(short event ){
        if(event == FunctionTaskListenerManager.START) {
            totalTaskCount.incrementAndGet();
            startTaskCount.incrementAndGet();
        }else if(event == FunctionTaskListenerManager.ERROR || event == FunctionTaskListenerManager.COMPLETE){
            if(event == FunctionTaskListenerManager.ERROR){
                startTaskCount.decrementAndGet();
                totalErrorCount.incrementAndGet();
            }else if(event == FunctionTaskListenerManager.COMPLETE){
                startTaskCount.decrementAndGet();
            }
        }
        listenerManager.callFire(event);
    }



    @Override
    public <D, V> FunctionResult<V> apply(D d, IFunction<D, V> function) {
        FunctionResult<V> r = new FunctionResult<>();
        r.fireAccept();
        executorService.execute(()->{
            toFireListenerEvent(FunctionTaskListenerManager.START);
            try {
                r.fireStart();
                V v = function.apply(d);
                toFireListenerEvent(FunctionTaskListenerManager.COMPLETE);
                r.setResult(v, FunctionState.SUCCESS);
                r.fireSuccess();
            }catch (Exception e){
                r.fireError(e);
                toFireListenerEvent(FunctionTaskListenerManager.ERROR);
            }
        });
        return r;
    }

    @Override
    public TaskFuture execute(ITaskFunction task) {
        return execute(new TaskFuture(),task);
    }

    @Override
    public TaskFuture execute(TaskFuture taskFuture, ITaskFunction task) {
        taskFuture.fireAccept();
        executorService.execute(()->{
            toFireListenerEvent(FunctionTaskListenerManager.START);
            taskFuture.fireStart();
            try {
                task.execute();
                toFireListenerEvent(FunctionTaskListenerManager.COMPLETE);
                taskFuture.fireSuccess();
            }catch (Exception e){
                toFireListenerEvent(FunctionTaskListenerManager.ERROR);
                taskFuture.fireError(e);
                e.printStackTrace();
            }
        });
        return taskFuture;
    }

    @Override
    public long getTaskCount() {
        return totalTaskCount.get();
    }

    @Override
    public long getStarCount() {
        return startTaskCount.get();
    }

    @Override
    public long getCompleteCount() {
        return totalTaskCount.get() - startTaskCount.get() - totalErrorCount.get();
    }

    @Override
    public long getErrorCount() {
        return totalErrorCount.get();
    }


    public ITaskRunner getRunner(){
        return this;
    }

    public ITaskRunnerMonitor getMonitor(){
        return this;
    }

    public IRunnerCallbackHelper getCallbackHelper(){
        return this;
    }
}

