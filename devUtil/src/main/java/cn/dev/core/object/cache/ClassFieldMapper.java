package cn.dev.core.object.cache;

import cn.dev.clock.CommonTimeClock;
import cn.dev.commons.ArrayUtil;
import cn.dev.core.object.ObjectUtil;

import java.lang.reflect.Field;
import java.util.*;

public class ClassFieldMapper {

    private long lastFire = CommonTimeClock.currentMillis();
    /**
     * 目标对象类
     */
    private final Class<?> clazz;
    /**
     * 目标对象类的所有直接字段
     */
    private final  Field[] declaredFields;

    /**
     * 目标对象类 有无 非 Object 的父类
     */
    private boolean hasSuperClass;

    public ClassFieldMapper(Class<?> clazz, Field[]  declaredFields) {
        if(clazz == null){
            throw new RuntimeException("ClassFieldMapper clazz is null");
        }
        if(declaredFields == null){
            throw new RuntimeException("ClassFieldMapper declaredFields is null");
        }
        this.clazz = clazz;
        this.declaredFields = declaredFields;
        this.hasSuperClass = (clazz.getSuperclass() != Objects.class);
    }

    /**
     * 获得 目标对象类
     * @return
     */
    public Class<?> getClazz() {
        return clazz;
    }

    /**
     * 获得 目标对象类 field list
     * @return
     */
    public List<Field> getDeclaredFieldList() {
        if(getDeclaredFields()!=null && getDeclaredFields().length>0){
            return ArrayUtil.toList(getDeclaredFields());
        }
        return Collections.emptyList();
//        return ArrayUtil.toList(getDeclaredFields());
    }

    /**
     * 获得 本类 field array
     * @return
     */
    public Field[] getDeclaredFields() {
        // 每次 调用此方法时候，更新 最后访问时间
        this.lastFire = CommonTimeClock.currentMillis();
        return declaredFields;
    }

    /**
     * 目标对象是否有父类
     * @return
     */
    public boolean isHasSuperClass() {
        return hasSuperClass;
    }

    /**
     * 获得 完整 field list
     *  为何不直接存完整的，主要还是为了稍微节省点内存
     * @return
     */
    public List<Field> getFullFieldList(){
        List<Field> list =getDeclaredFieldList(); //ArrayUtil.toList(getDeclaredFields());
        if (!isHasSuperClass()){
            return list;
        }
        Class superClass = clazz.getSuperclass();
        while (superClass !=null && superClass!= Object.class){
            Field[] superFields = ObjectUtil.getSelfFullFields(superClass);
            for (Field superField : superFields) {
                if (list.stream().filter(f->f.getName().equals(superField.getName())).count()==0) {
                    list.add(superField);
                }
            }
            superClass = superClass.getSuperclass();
        }
        return list;

    }

    /**
     * 获得 完整 field array
     * @return
     */
    public Field[] getFullFields(){
        return getFullFieldList().toArray(new Field[0]);
    }

    public boolean containFieldName(String fieldName){
        for (Field field : getFullFields()) {
            if (field.getName().equals(fieldName)){
                return true;
            }
        }
        return false;
    }
}
