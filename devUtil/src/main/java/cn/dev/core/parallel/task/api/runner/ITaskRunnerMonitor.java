package cn.dev.core.parallel.task.api.runner;

public interface ITaskRunnerMonitor {
    /**
     * 执行的 任务总数
     * 获取当前任务数量
     * @return
     */
    long getTaskCount();

    /**
     * 进行中的任务数量
     * @return
     */
    long getStarCount();

    /**
     * 完成的任务数量
     * @return
     */
    long getCompleteCount();

    /**
     * 执行出错的的任务数量
     * @return
     */
    long getErrorCount();
}
