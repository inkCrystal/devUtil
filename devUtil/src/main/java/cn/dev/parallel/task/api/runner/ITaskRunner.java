package cn.dev.parallel.task.api.runner;

import cn.dev.parallel.task.api.IFunction;
import cn.dev.parallel.task.api.ITaskFunction;
import cn.dev.parallel.task.runner.FunctionResult;
import cn.dev.parallel.task.runner.TaskFuture;

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
     * @param taskFunction
     * @return
     */
    TaskFuture execute(ITaskFunction taskFunction);

//    /**
//     * 运行任务 ，返回采用 传递的 对象
//     * @param taskFuture
//     * @param taskFunction
//     * @return
//     */
//    TaskFuture execute(TaskFuture taskFuture , ITaskFunction taskFunction);


}
