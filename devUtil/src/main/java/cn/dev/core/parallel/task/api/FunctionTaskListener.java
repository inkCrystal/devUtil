package cn.dev.core.parallel.task.api;

public interface FunctionTaskListener {
    void onStart();

    void onError();

    void onComplete();
}
