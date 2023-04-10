package cn.dev.core.object;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public interface IObjectHelper {

    /**
     * 将map 转换为 bean
     * @param map
     * @param beanClass
     * @param <T>
     * @return
     */
    <T extends Serializable> T mapToBean(Map<String,Object> map, Class<T> beanClass);

    /**
     * 将bean 转换为 map
     * @param t
     * @param <T>
     * @return
     */
    <T extends Serializable> Map<String,Object> beanToMap(T t);

    /**
     * 序列化
     * @param t
     * @param <T>
     * @return
     */
    <T extends Serializable>byte[]  serialize(T t);

    /**
     * 反序列化
     * @param bytes
     * @param tClass
     * @param <T>
     * @return
     */
    <T extends Serializable> T deserialize(byte[] bytes,Class<T> tClass);

    /**
     * 序列化到文件
     * @param t
     * @param filePath
     * @param <T>
     */
    <T extends Serializable> void serializeToFile(T t, String filePath);

    /**
     * 从文件反序列化
     * @param filePath
     * @param tClass
     * @param <T>
     * @return
     */
    <T extends Serializable> T deserializeFromFile(String filePath,Class<T> tClass);

    /**
     * 序列化为字符串
     * @param t
     * @param <T>
     * @return
     */
    <T extends Serializable>  void serializeToString(T t);

    /**
     * 从字符串反序列化
     * @param str
     * @param tClass
     * @param <T>
     * @return
     */
    <T extends Serializable> T deserializeFromString(String str,Class<T> tClass);

    /**
     * 克隆
     * @param t
     * @param <T>
     * @return
     */
    <T extends Serializable>T deepClone(T t);


    /**
     * 获取类的所有字段
     * @param clazz
     * @return
     */
    List<Field> getFields(Class<?> clazz);

    /**
     * 为字段赋值
     * @param obj
     * @param field
     * @param value
     */
    void setValue(Object obj, Field field, Object value) throws Exception;

}
