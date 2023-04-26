package cn.dev.supports.spring.dataApi.query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * 值类型的 常量
 * @Author: JasonMao
 */
public class ValueTypeConstance {
    private static final Class[] allowValueTypes;
    static {
        allowValueTypes = new Class[]{
                int.class, long.class, float.class, double.class, boolean.class, short.class, byte.class,
                Integer.class, Long.class, Float.class, Double.class, Boolean.class, Short.class, Byte.class,
                String.class, Date.class, java.sql.Date.class, LocalDateTime.class,
                LocalDate.class, LocalTime.class
        };
    }

    public static boolean isAllowValueType(Object value){
        for (Class allowValueType : allowValueTypes) {
            if(allowValueType.equals(value.getClass())){
                return true ;
            }
        }
        return false ;
    }


}
