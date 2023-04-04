package cn.dev.core.parallel.task.runner;

import cn.dev.core.parallel.task.api.IFunction;
import cn.dev.core.parallel.task.api.ITaskFunction;
import cn.dev.core.parallel.task.api.runner.ITaskRunner;

public class TaskSimpleRunner implements ITaskRunner {

    @Override
    public <D, V> FunctionResult<V> apply(D d, IFunction<D, V> function) {
        return AbstractTaskRunTool.apply(d,function);
    }

    @Override
    public TaskFuture execute(ITaskFunction taskFunction) {
        return AbstractTaskRunTool.execute(taskFunction);
    }

    public TaskFuture execute(TaskFuture taskFuture, ITaskFunction taskFunction) {
        return AbstractTaskRunTool.execute(taskFuture,taskFunction);
    }
}
