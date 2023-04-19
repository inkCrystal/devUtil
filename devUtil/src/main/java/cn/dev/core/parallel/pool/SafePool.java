package cn.dev.core.parallel.pool;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * 线程安全的 池， 池化 的核心容器
 */
public class SafePool<T extends PoolAble> {
    private final Class<T> tClass;
    private final int maxSize;
    private final Queue<T> freeQueue ;
    private final Queue<T> busyQueue ;

    protected SafePool(Class<T> tClass,int maxSize) {
        this.tClass = tClass;
        this.maxSize = maxSize;
        busyQueue = new ArrayBlockingQueue<>(maxSize);
        freeQueue = new ArrayBlockingQueue<>(maxSize);
    }


    public T obtain(){
        T t = freeQueue.poll();
        if(t == null){
            //t = (create);
        }
        busyQueue.offer(t);
        return t;
    }
}
