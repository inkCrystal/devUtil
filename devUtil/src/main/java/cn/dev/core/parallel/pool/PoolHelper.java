package cn.dev.core.parallel.pool;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class PoolHelper {
    private PoolHelper() {}

    private static Map<String,SafePool> poolMap;


    public static <T extends PoolAble> SafePool<T> getPool(Class<T> clazz){
        return getPool(clazz.getName());
    }

    public static <T extends PoolAble> SafePool<T> getPool(String name){
        return poolMap.get(name);
    }

    public static final synchronized <T extends PoolAble>  SafePool getOrBuildPool(Class<T> clazz){
        if(getPool(clazz) == null){
            SafePool<T> pool = new SafePool<>(clazz,32);
            poolMap.put(clazz.getName(),pool);
        }
        return getPool(clazz);
    }

    public static void main(String[] args) {
        log.debug("dada");
        log.info("hel");

    }
}
