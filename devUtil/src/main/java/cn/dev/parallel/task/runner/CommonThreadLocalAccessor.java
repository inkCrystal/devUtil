package cn.dev.parallel.task.runner;

import cn.dev.commons.string.StrUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CommonThreadLocalAccessor implements AutoCloseable{
    private static final ThreadLocal<Map<String, Object>> context = new ThreadLocal<>();

    public CommonThreadLocalAccessor() {}





    public Map<String,Object> getContextMap(){
        Map<String, Object> map = context.get();
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
        context.set(contextMap);
    }

    public void removeKey(String key){
        Map<String, Object> contextMap = getContextMap();
        contextMap.remove(key);
        flushToThreadLocal(contextMap);
    }

    private void flushToThreadLocal(Map<String, Object> contextMap ){
        if (contextMap.isEmpty() ) {
            context.remove();
        }else{
            context.set(contextMap);
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

    @Override
    public void close() throws Exception {
        if (context.get()!=null) {
            context.remove();
        }
    }
}
