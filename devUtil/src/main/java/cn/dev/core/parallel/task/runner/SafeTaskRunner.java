package cn.dev.core.parallel.task.runner;

import cn.dev.core.parallel.task.api.IFunction;
import cn.dev.core.parallel.task.api.ITaskFunction;
import cn.dev.core.parallel.task.api.runner.IRunnerCallbackHelper;
import cn.dev.core.parallel.task.api.runner.ITaskRunner;
import cn.dev.core.parallel.task.api.runner.ITaskRunnerMonitor;

public class SafeTaskRunner  implements ITaskRunner, IRunnerCallbackHelper, ITaskRunnerMonitor {

    @Override
    public TaskFuture execute(TaskFuture taskFuture, ITaskFunction taskFunction) {
        return null;
    }

    @Override
    public <D, V> FunctionResult<V> apply(D d, IFunction<D, V> function) {
        return null;
    }

    @Override
    public TaskFuture execute(ITaskFunction task) {
        TaskExecutor.getRunner().execute(()->{
            try (executor executor = new executor()) {
                task.execute();
            }catch (Exception e){
                e.printStackTrace();
            }
        } );

        return null;
    }

    @Override
    public long getTaskCount() {
        return 0;
    }

    @Override
    public long getStarCount() {
        return 0;
    }

    @Override
    public long getCompleteCount() {
        return 0;
    }

    @Override
    public long getErrorCount() {
        return 0;
    }


    class executor implements AutoCloseable{
        @Override
        public void close() throws Exception {

        }



    }
}
