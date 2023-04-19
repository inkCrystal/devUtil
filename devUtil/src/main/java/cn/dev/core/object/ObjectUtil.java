package cn.dev.core.object;

import cn.dev.core.object.cache.ClassFieldMapper;
import cn.dev.core.object.cache.ObjectReflectCacheHolder;
import cn.dev.core.parallel.task.api.ITaskFunction;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Arrays;

public class ObjectUtil {

    private static final Field[] EMPTY_FIELD_ARRAY = new Field[0];
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
     * 是否是 某个类的子类
     * @param clazz
     * @param typeClass
     * @return
     */
    public static boolean isChildTypeOfClass(Class clazz ,Class typeClass){
        Class superClass = clazz.getSuperclass();
        while ( superClass !=null && superClass != Object.class){
            clazz = clazz.getSuperclass();
            if(clazz == typeClass){
                return true;
            }
            return isChildTypeOfClass(clazz,typeClass);
        }
        return false;
    }

    /**
     * 获取类的所有字段
     * @param clazz
     * @return
     */
    public static final Field[] getSelfFullFields(Class<?> clazz){
        if (clazz ==null || clazz == Object.class) {
            return EMPTY_FIELD_ARRAY ;
        }
        if(ObjectReflectCacheHolder.contains(clazz)){
            return ObjectReflectCacheHolder.get(clazz).getDeclaredFields();
        }else {
            Field[] fields = clazz.getDeclaredFields();
            ObjectReflectCacheHolder.put(new ClassFieldMapper(clazz, fields));
            return fields;
        }
    }

    /**
     * 获取类的所有字段,包括父类
     * @param clazz
     * @return
     */
    public static final Field[] getFullFieldsWithSuperClass(Class<?> clazz){
        if(ObjectReflectCacheHolder.contains(clazz)) {
            Field[] fullFields = ObjectReflectCacheHolder.get(clazz).getFullFields();
            return fullFields;
        }else {
            getSelfFullFields(clazz);
            return getFullFieldsWithSuperClass(clazz);
        }
    }




//    public static void main(String[] args) throws InterruptedException {
//
//        for (int i =0 ;i<100 ;i ++ ) {
//            long start = System.nanoTime();
//            final Field[] fields = getFullFieldsWithSuperClass(BCC.class);
//            long end = System.nanoTime();
//            System.out.println("duration:" + (end - start));
//            System.out.print(Arrays.stream(fields).count() + " find ");
//        }
//    }

    /**
     * field 定义的 字符串
     * @param field
     * @return
     */
    public static final String fieldDefinedString(Field field){
        return new StringBuilder("")
                .append(Modifier.toString(field.getModifiers())).append(" ")
                .append(field.getType().getSimpleName()).append(" ")
                .append(field.getName())
                .toString();
    }


    /**
     * 是否存在字段
     * @param clazz
     * @return
     */
    public static final boolean isContainsField(Class clazz){
        return getSelfFullFields(clazz).length > 0;
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
