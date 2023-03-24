package cn.dev.parallel.task.runner;

import cn.dev.parallel.task.api.IFunction;
import cn.dev.parallel.task.api.ITaskFunction;
import cn.dev.parallel.task.api.runner.ITaskRunner;

public class SimpleTaskRunner implements ITaskRunner {

    @Override
    public <D, V> FunctionResult<V> apply(D d, IFunction<D, V> function) {
        return TaskRunTool.apply(d,function);
    }

    @Override
    public TaskFuture execute(ITaskFunction taskFunction) {
        return TaskRunTool.execute(taskFunction);
    }

    public TaskFuture execute(TaskFuture taskFuture, ITaskFunction taskFunction) {
        return TaskRunTool.execute(taskFuture,taskFunction);
    }
}
