package cn.dev.core.object;

import cn.dev.core.model.DataRecord;
import cn.dev.core.model.Identity;
import cn.dev.core.object.cache.ClassFieldMapper;
import cn.dev.core.object.cache.ObjectReflectCacheHolder;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Date;

public class ObjectUtil {


    public static final boolean isNull(Object o){
        return o == null;
    }

    public static final boolean isNotNull(Object o){
        return !isNull(o);
    }

    public static final Type beanType(Object bean){
        return bean.getClass();
    }

    /**
     * 获取类的所有字段
     * @param clazz
     * @return
     */
    public static final Field[] getFullFields(Class<?> clazz){
        if(ObjectReflectCacheHolder.contains(clazz)){
            return ObjectReflectCacheHolder.get(clazz).getDeclaredFields();
        }else {
            Field[] fields = clazz.getDeclaredFields();
            ObjectReflectCacheHolder.put(new ClassFieldMapper(clazz, fields));
            return getFullFields(clazz);
        }
    }

    /**
     * 获取类的所有字段,包括父类
     * @param clazz
     * @return
     */
    public static final Field[] getFullFieldsWithSuperClass(Class<?> clazz){
        return ObjectReflectCacheHolder.get(clazz).getFullFields();
    }


    /**
     * 是否存在字段
     * @param clazz
     * @return
     */
    public static final boolean isContainsField(Class clazz){
        return getFullFields(clazz).length > 0;
    }


    public static final boolean isJdkClass(Class clazz){
        return clazz.getClassLoader()==null;
    }


    static class FieldUtil{
        public static boolean isFinal(Field field){
            return Modifier.isFinal(field.getModifiers());
        }

        public static boolean isStatic(Field field){
            return Modifier.isStatic(field.getModifiers());
        }

        public static boolean isTransient(Field field){
            return Modifier.isTransient(field.getModifiers());
        }

        public static boolean isPrivate(Field field){
            return Modifier.isPrivate(field.getModifiers());
        }


        public static boolean isPublic(Field field){
            return Modifier.isPublic(field.getModifiers());
        }

        public static boolean isProtected(Field field){
            return Modifier.isProtected(field.getModifiers());
        }


        public static Object getValue(Field field,Object bean){
            try {
                if (!isPublic(field)) {
                    field.setAccessible(true);
                }
                field.setAccessible(true);
                return field.get(bean);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        }

        public static void setValue(Field field,Object bean,Object value){
            try {
                field.setAccessible(true);
                field.set(bean,value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        //private transient  int x=1;



    }

    /**
     * 序列化 对象
     * @param t
     * @return
     */
    public static byte[] serializeObject(Object t){
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(t);
            oos.flush();
            bytes = bos.toByteArray ();
            oos.close();
            bos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bytes;
    }

    /**
     * 反序列化
     * @param bytes
     * @param tClass
     * @return
     * @param <T>
     */
    public static <T> T deserialization(byte[] bytes,Class<T>  tClass) {
        Object obj = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream (bytes);
            ObjectInputStream ois = new ObjectInputStream (bis);
            obj = ois.readObject();
            ois.close();
            bis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return (T)obj;
    }





}
