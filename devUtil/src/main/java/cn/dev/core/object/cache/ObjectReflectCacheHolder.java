package cn.dev.core.object.cache;

import cn.dev.core.object.ObjectUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 对象反射缓存
 */
public class ObjectReflectCacheHolder {

    private static final Map<String, ClassFieldMapper> cache = new HashMap<>();

    public static void put( ClassFieldMapper mapper){
        cache.put(mapper.getClazz().getName(), mapper);
    }

    public static boolean contains(Class<?> clazz){
        if (Objects.nonNull(clazz)) {
            return cache.containsKey(clazz.getName());
        }
        return false;
    }

    public static ClassFieldMapper get(Class<?> clazz){
        if (clazz!=null) {
            if (contains(clazz)) {
                return cache.get(clazz.getName()).fire();
            }
        }
        return null;
    }

    private void test(){

        long freeMemory = Runtime.getRuntime().freeMemory();
        long totalMemory = Runtime.getRuntime().totalMemory();
        long usedMemory = totalMemory - freeMemory;

        for (String k : cache.keySet()) {
            if (cache.get(k).getFireCount() == 0) {

            }

        }
        

    }



}
