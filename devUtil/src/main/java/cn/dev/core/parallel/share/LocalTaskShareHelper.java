package cn.dev.core.parallel.share;

import cn.dev.core.parallel.task.api.FunctionTaskListener;

import java.util.Optional;

/**
 * 本地服务内线程分享帮助类
 *  用于 在多线程任务中共享数据，如 token、session 等支持。
 *  需要独立实现 IThreadSharer 接口，用于设置需要共享的数据
 */
public final class LocalTaskShareHelper {

    private static final ThreadLocal<TaskShareData> threadLocal = new ThreadLocal<>();

    private static IThreadSharer sharer;

    public static void setSharer(IThreadSharer sharer) {
        if(sharer == null) {
            LocalTaskShareHelper.sharer = sharer;
        }
    }

    public static boolean isSharerSet() {
        return sharer != null;
    }


    public static IThreadSharer getSharer() {
        return sharer;
    }

    public static Optional<FunctionTaskListener> getTaskListenerOptional() {
        if (sharer == null){
            return Optional.ofNullable(null);
        }
        return Optional.ofNullable(new LocalTaskShareListener(sharer.toShareData()));
    }

    //获取当前线程的数据
    protected static void set(TaskShareData taskShareData) {
        if (taskShareData!=null) {
            threadLocal.set(taskShareData);
        }
    }

    //获取当前线程的数据
    public static TaskShareData get() {
        return threadLocal.get();
    }

    //移除当前线程的数据
    protected static void remove() {
        threadLocal.remove();
    }


}
