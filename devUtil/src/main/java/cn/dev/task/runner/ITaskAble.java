package cn.dev.task.runner;

import cn.dev.task.TaskResult;

public interface ITaskAble {
    TaskResult<Void> apply();

}
