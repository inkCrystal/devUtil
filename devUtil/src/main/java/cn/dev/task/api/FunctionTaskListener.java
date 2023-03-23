package cn.dev.task.api;

public interface FunctionTaskListener {
    void onStart();

    void onError();

    void onComplete();
}
