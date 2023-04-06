package cn.dev.core.parallel.task.api.runner;

import cn.dev.core.parallel.task.api.ITaskFunction;
import cn.dev.core.parallel.task.runner.TaskFuture;

/**
 * 一个抽象的接口 用于执行后续的任务
 */
public interface IRunnerCallbackHelper{
    TaskFuture execute(TaskFuture taskFuture , ITaskFunction taskFunction);


}
