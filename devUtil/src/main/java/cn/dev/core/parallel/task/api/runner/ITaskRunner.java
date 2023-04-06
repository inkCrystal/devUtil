package cn.dev.core.parallel.task.api.runner;

import cn.dev.core.parallel.task.api.IFunction;
import cn.dev.core.parallel.task.api.ITaskFunction;
import cn.dev.core.parallel.task.runner.FunctionResult;
import cn.dev.core.parallel.task.runner.TaskFuture;

public interface ITaskRunner {

    /**
     *  运行 FUNCTION
     * @param d
     * @param function
     * @return
     * @param <D>
     * @param <V>
     */
    <D,V> FunctionResult<V> apply(D d, IFunction<D,V> function);


    /**
     * 运行任务
     * @param task
     * @return
     */
    TaskFuture execute(ITaskFunction task);




}
