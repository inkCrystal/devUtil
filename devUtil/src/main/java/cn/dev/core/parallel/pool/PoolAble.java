package cn.dev.core.parallel.pool;

public abstract class PoolAble {

    private volatile long threadId = -1;




    /**
     * 当资源初始化时事件
     */
    public void onInit() {
    }


    /**
     * 当资源使用时候事件
     */
    public void onUse(){}

    /**
     * 当资源释放时候事件
     */
    public void onFree(){}
}
