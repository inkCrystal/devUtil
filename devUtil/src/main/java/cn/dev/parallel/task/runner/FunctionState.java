package cn.dev.parallel.task.runner;

public enum FunctionState {
    /** 表示不存在 任务，未构建任务Function  ---by jason @ 2023/3/22 22:45 */
    NONE,
    /**默认值   等待---by jason @ 2023/3/23 12:57 */
    WAIT ,

    /**代码 任务 方法被接受，初始化完成，准备执行  ---by jason @ 2023/3/21 11:02 */
    ACCEPT ,
    /**代表项目开始执行  ---by jason @ 2023/3/21 11:02 */
    START ,
    /**代表任务被取消，这个状态 暂时保留，很少有场景会出现   ---by jason @ 2023/3/21 11:02 */
    CANCEL ,
    /**代表 任务出现异常推出，也同等完成状态 ---by jason @ 2023/3/21 11:02 */
    ERROR ,
    /**代表 任务被暂停，等待资源 后继续执行  ---by jason @ 2023/3/21 11:03 */
    PAUSE ,
    /**任务暂停取消，继续执行  ---by jason @ 2023/3/21 11:16 */
    RE_PAUSE,

    /**代表 任务执行成功  ---by jason @ 2023/3/21 11:03 */
    SUCCESS ;


    /** 状态是否 完成，包含 异常完成 和 正常完成   ---by jason @ 2023/3/21 11:04 */
    public boolean isDone(){
        switch (this){
            case ERROR, SUCCESS: return true;
            default:    return false;
        }
    }

    /**是否正在执行  ---by jason @ 2023/3/21 11:06 */
    public boolean isRunning(){
        return this == START;
    }




}
