package cn.dev.task.runner;

public interface FunctionTaskListener {
    void onStart();

    void onError();

    void onComplete();
}
