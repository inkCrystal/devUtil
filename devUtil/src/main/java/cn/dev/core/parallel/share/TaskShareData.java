package cn.dev.core.parallel.share;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TaskShareData implements Serializable {
    @Serial
    private static final long serialVersionUID = -4395919015258844814L;

    public TaskShareData() {
        this.holder =new HashMap<>();
    }

    private Map<String,Object> holder ;

    /**
     * 存储数据
     * @param key
     * @param data
     */
    public void put(String key, Object data){
        if(key==null){
            return;
        }
        holder.put(key,data);
    }

    /**
     * 获取数据
     * @param key
     * @return
     */
    public Object get(String key){
        return holder.get(key);
    }

    /**
     * 获取指定类型的数据
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T get(String key, Class<T> clazz){
        return (T) holder.get(key);
    }


    /**
     * 移除指定key的数据
     * @param key
     */
    public void remove(String key){
        holder.remove(key);
    }


    /**
     * 获取所有的key
     * @return
     */
    public Set<String> keySet(){
        return holder.keySet();
    }

    /**
     * 清空数据
     */
    public void clear() {
        holder.clear();
    }


}
