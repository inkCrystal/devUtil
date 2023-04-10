package cn.dev.core.object.cache;

import cn.dev.clock.CommonTimeClock;
import cn.dev.commons.ArrayUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
        this.clazz = clazz;
        this.declaredFields = declaredFields;
        this.hasSuperClass = clazz.getSuperclass() != Objects.class;
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
        return ArrayUtil.toList(getDeclaredFields());
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
        if (!isHasSuperClass()){
            return getDeclaredFieldList();
        }
        List<Field> list = ArrayUtil.toList(getDeclaredFields());
        Class superClass = clazz.getSuperclass();
        List<Field> superClassFullFieldList = ObjectReflectCacheHolder.get(superClass).getFullFieldList();
        for (Field field : superClassFullFieldList) {
            if (!containFieldName(field.getName())){
                list.add(field);
            }
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
