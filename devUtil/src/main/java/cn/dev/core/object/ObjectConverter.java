package cn.dev.core.object;

import org.slf4j.Logger;

import java.lang.reflect.*;
import java.time.LocalDateTime;
import java.util.*;

public class ObjectConverter {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(ObjectConverter.class);


    public static Object instance(Constructor constructor,Object... params) throws InvocationTargetException, InstantiationException, IllegalAccessException {
//        System.out.println("instance: " + constructor.getName() + " " + Arrays.toString(params));
        return constructor.newInstance(params);
    }

    @SuppressWarnings("unchecked")
    private static Object instanceByConstructor(Constructor constructor,Map<String,Object> map) throws Exception {
        final Parameter[] parameters = constructor.getParameters();
        List<Object> params = new ArrayList<>();
        for (Parameter parameter : parameters) {
            Class type = parameter.getType();
            if(type == int.class||type ==long.class || type == double.class || type == float.class || type == boolean.class || type == char.class || type == byte.class || type == short.class){
                params.add(0);
            }else{
                if(!ObjectUtil.isJdkClass(type) && ObjectUtil.isContainsField(type)){
                    Object v = mapTpBean(type, (Map<String, Object>) map.get(parameter.getName()));
                    params.add(v);
                }else{
                    String name = parameter.getName();
                    params.add(map.get(name));
                }
//                params.add(map.get(name));
            }
        }
        return instance(constructor, params.toArray());
    }

    private static Object instanceObject(Class clazz,Map<String,Object> map) throws Exception{
        Exception throwable =null;
//        try{
//            Constructor declaredConstructor = clazz.getDeclaredConstructor();
//            return declaredConstructor.newInstance();
//        }catch (Exception e){
//            logger.warn("对象"+ clazz +" 未包含默认无参构造方法，构造结果可能出现异常偏差！");
//        }
        Constructor[] declaredConstructors = clazz.getDeclaredConstructors();
        for (Constructor constructor : declaredConstructors) {
        }
        for (Constructor constructor : declaredConstructors) {
            if(constructor.getParameterCount() == 0){
                constructor.setAccessible(true);
                return instance(constructor);
            }
        }
        logger.warn("对象"+ clazz +" 未包含默认无参构造方法，构造结果可能出现异常偏差！");

        for (Constructor constructor : declaredConstructors) {
             try{
                 return instanceByConstructor(constructor,map);
             }catch (Exception e){
                 if (throwable ==null) {
                     throwable = new Exception("无法实例化对象");
                 }
                 e.printStackTrace();
                 throwable.addSuppressed(e);
             }
        }
        throw throwable;
    }


    public static Map<String,Object> beanToMap(Object obj){
        return objToMap(obj);
    }

    public static Map<String,Object> objToMap(Object obj){
        Field[] fullFields = ObjectUtil.getFullFields(obj.getClass());
        Map<String,Object> map = new HashMap();
        map.put("source_target_class_name" , obj.getClass().getName());
        for (Field f : fullFields) {
            if (!ObjectUtil.FieldUtil.isStatic(f)) {
                if(ObjectUtil.isJdkClass(f.getType())){
                    Object value = ObjectUtil.FieldUtil.getValue(f, obj);
                    map.put(f.getName(), value);
                }else if(ObjectUtil.isContainsField(f.getType())){
                    map.put(f.getName(), objToMap(ObjectUtil.FieldUtil.getValue(f, obj)));
                }else{
                    Object value = ObjectUtil.FieldUtil.getValue(f, obj);
                    map.put(f.getName(), value);
                }
            }
        }
        return map;
    }

    public static <T> T mapTpBean(Class<T> tClass,Map<String,Object> map) throws Exception {
        T t = (T) instanceObject(tClass,map);
        Field[] fullFields = ObjectUtil.getFullFields(tClass);
        for (Field f : fullFields) {
            if (map.containsKey(f.getName())) {
                Object value = null;
                if(ObjectUtil.isContainsField(f.getType()) && !ObjectUtil.isJdkClass(f.getType())) {
                    value = mapTpBean(f.getType(), (Map<String, Object>) map.get(f.getName()));
                }else{
                    value = map.get(f.getName());
                }
                ObjectUtil.FieldUtil.setValue(f,t,value);
            }
        }
        return t;
    }



    public static void main(String[] args) throws Exception {
//        p(String.class, OpenFilesEvent.class,A.class,B.class,Long.class,LocalDateTime.class, Runnable.class, RuntimeException.class);

    }
}

