package cn.dev.core.parallel.task.api;

public interface FunctionTaskListener {

//    /**这是一个 跨线程的 方法，即只有这个方法实在 主线程完成的，其他都在 异步的新线程内完成   ---by jason @ 2023/4/7 15:23 */
//    default void onAccept(){};
    void onStart();

    void onError();

    void onComplete();
}
