package cn.dev.core.object.cache;

import cn.dev.core.object.ObjectUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 对象反射缓存
 */
public class ObjectReflectCacheHolder {

    private static final Map<String, ClassFieldMapper> cache = new HashMap<>();

    public static void put( ClassFieldMapper mapper){
        cache.put(mapper.getClazz().getName(), mapper);
    }

    public static boolean contains(Class<?> clazz){
        return cache.containsKey(clazz.getName());
    }

    public static ClassFieldMapper get(Class<?> clazz){
        if (contains(clazz)){
            return cache.get(clazz.getName());
        }
        ClassFieldMapper mapper = new ClassFieldMapper(clazz, ObjectUtil.getFullFields(clazz));
        put(mapper);
        return mapper;
    }

}
