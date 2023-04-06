package cn.dev.core.parallel.share;

import cn.dev.core.parallel.task.api.FunctionTaskListener;

/**
 * 本地服务内线程分享复制器
 */
public final class LocalTaskShareHelper {

    private static final ThreadLocal<TaskShareData> threadLocal = new ThreadLocal<>();

    private static final FunctionTaskListener taskListener =new LocalTaskShareListener();


    //获取当前线程的数据
    private static void set(TaskShareData taskShareData) {
        threadLocal.set(taskShareData);
    }

    //获取当前线程的数据
    private static TaskShareData get() {
        return threadLocal.get();
    }

    //移除当前线程的数据
    private static void remove() {
        threadLocal.remove();
    }

    public static void putData(String key ,Object data){
        TaskShareData taskShareData = get();
        if(taskShareData==null){
            taskShareData = new TaskShareData();
        }
        taskShareData.put(key,data);
        set(taskShareData);
    }








}
