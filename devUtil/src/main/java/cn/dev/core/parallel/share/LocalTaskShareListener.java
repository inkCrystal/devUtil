package cn.dev.core.parallel.share;

import cn.dev.core.parallel.task.api.FunctionTaskListener;

public class LocalTaskShareListener implements FunctionTaskListener {
    private TaskShareData taskShareData;
    protected LocalTaskShareListener(TaskShareData taskShareData) {
        this.taskShareData = taskShareData;
    }

//    @Override
//    public void onAccept() {
//        LocalTaskShareHelper.getTaskListenerOptional();
//    }

    @Override
    public void onStart() {
        // 首先 必须 要 插入到 threadLocal 中，这样可以给其他方法调用
        LocalTaskShareHelper.set(taskShareData);
    }

    @Override
    public void onError() {
        clearThreadLocal();

    }

    @Override
    public void onComplete() {
        clearThreadLocal();
    }


    private void clearThreadLocal(){
        LocalTaskShareHelper.remove();
    }
}
