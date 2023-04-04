package cn.dev.core.parallel.task.runner;

import cn.dev.commons.string.StrUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 通用的 ThreadLocal 存储器连接管理
 *  1.主要服务于线程间 内存资源的 传递共享
 *
 */
public class CommonThreadLocalAccessor implements AutoCloseable{
    private static final ThreadLocal<Map<String, Object>> threadLocalContext = new ThreadLocal<>();

    public CommonThreadLocalAccessor() {}

    public Map<String,Object> getContextMap(){
        Map<String, Object> map = threadLocalContext.get();
        if (map==null) {
            map = new HashMap<>();
        }
        return map;
    }

    /**
     * 存储到 threadLocal
     * @param key
     * @param data
     */
    public void set(String key, Object data){
        if(StrUtils.isEmpty(key)){
            return;
        }
        Map<String, Object> contextMap = getContextMap();
        if(!Objects.nonNull(data)){
            this.removeKey(key);
        }
        contextMap.put(key,data);
        threadLocalContext.set(contextMap);
    }

    public void removeKey(String key){
        Map<String, Object> contextMap = getContextMap();
        contextMap.remove(key);
        flushToThreadLocal(contextMap);
    }

    private void flushToThreadLocal(Map<String, Object> contextMap ){
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
    public Object get(String key){
        return getContextMap().get(key);
    }

    /**
     * 获取对象 ，如果您能够确定 数据类型 是准确的
     * @param key
     * @param clazz
     * @return
     * @param <T>
     */
    public <T> T get(String key, Class<T> clazz){
        return (T) get(key);
    }

    @Override
    public void close() throws Exception {
        if (threadLocalContext.get()!=null) {
            threadLocalContext.remove();
        }
    }
}
