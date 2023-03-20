package cn.dev.task;

public interface ITaskFun<T> {

    TaskResult<T> apply();

}
