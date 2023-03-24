package cn.dev.parallel.task.api;

public interface FunctionTaskListener {
    void onStart();

    void onError();

    void onComplete();
}
