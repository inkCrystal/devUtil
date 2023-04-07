package cn.dev.core.parallel.task.runner;

import cn.dev.commons.string.StrUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Deprecated
public class ThreadLocalHelper {
    private static final ThreadLocal<Map<String, Object>> threadLocalContext = new ThreadLocal<>();

    public static Map<String,Object> getContextMap(){
        Map<String, Object> map = threadLocalContext.get();
        if (map==null) {
            map = new HashMap<>();
        }
        return map;
    }

    public static void set(String key, Object data){
        if(StrUtils.isEmpty(key)){
            return;
        }
        Map<String, Object> contextMap = getContextMap();
        if(!Objects.nonNull(data)){
            removeKey(key);
        }
        contextMap.put(key,data);
        threadLocalContext.set(contextMap);
    }


    public static void removeKey(String key){
        Map<String, Object> contextMap = getContextMap();
        contextMap.remove(key);
        flushToThreadLocal(contextMap);
    }

    /**
     * 刷新到 threadLocal
     * @param contextMap
     */
    private static void flushToThreadLocal(Map<String, Object> contextMap ){
        if (contextMap.isEmpty() ) {
            threadLocalContext.remove();
        }else{
            threadLocalContext.set(contextMap);
        }
    }

    /**
     * 从 threadLocal 读取 信息
     * @param key
     * @return
     */
    public static Object get(String key){
        return getContextMap().get(key);
    }

    /**
     * 获取对象 ，如果您能够确定 数据类型 是准确的
     * @param key
     * @param clazz
     * @return
     * @param <T>
     */
    public static <T> T get(String key, Class<T> clazz){
        return (T) get(key);
    }



}
