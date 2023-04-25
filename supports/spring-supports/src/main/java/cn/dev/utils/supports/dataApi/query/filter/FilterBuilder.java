package cn.dev.utils.supports.dataApi.query.filter;

import cn.dev.utils.supports.dataApi.query.OpEnum;

import java.io.Serializable;

import static cn.dev.utils.supports.dataApi.query.filter.Filter.build;

public class FilterBuilder {
    // 初始构建 链路中的 第一个 filter


    public static Filter whereEqual(String key, Serializable value){
        return Filter.build(key, OpEnum.EQUAL, value)
                .queryInit(" = ?");
    }

    public static Filter whereNotEqual(String key, Serializable value){
        return build(key, OpEnum.NOT_EQUAL, value)
                .queryInit(" != ?");
    }

    public static Filter whereIn(String key, Serializable... values){
        return build(key, OpEnum.IN, values)
                .queryInit(" IN (?"+(",?").repeat(values.length-1)+")");
    }

    public static Filter whereNotIn(String key, Serializable... values){
        return build(key, OpEnum.NOT_IN, values)
                .queryInit(" NOT IN (?"+(",?").repeat(values.length-1)+")");
    }

    public static Filter whereLike(String key, String value){
        return build(key, OpEnum.LIKE, value)
                .queryInit(" LIKE ?");
    }

    public static Filter whereNotLike(String key, String value){
        return build(key, OpEnum.NOT_LIKE, value)
                .queryInit(" NOT LIKE ?");
    }

    public static Filter whereLessThan(String key, Number value){
        return build(key, OpEnum.LESS_THAN, value)
                .queryInit(" < ?");
    }

    public static Filter whereLessThanEqual(String key, Number value){
        return build(key, OpEnum.LESS_THAN_EQUAL, value)
                .queryInit(" <= ?");
    }

    public static Filter whereGreaterThan(String key, Number value){
        return build(key, OpEnum.GREATER_THAN, value)
                .queryInit(" > ?");
    }

    public static Filter whereGreaterThanEqual(String key, Number value){
        return build(key, OpEnum.GREATER_THAN_EQUAL, value)
                .queryInit(" >= ?");
    }

    @Deprecated
    public static Filter whereIsNull(String key){
        return build(key, OpEnum.IS_NULL)
                .queryInit(" IS NULL");
    }

    @Deprecated
    public static Filter whereIsNotNull(String key){
        return build(key, OpEnum.IS_NOT_NULL)
                .queryInit(" IS NOT NULL");
    }

    public static Filter whereBetweenAnd(String key, Serializable value1, Serializable value2){
        return build(key, OpEnum.BETWEEN_AND, value1, value2)
                .queryInit(" BETWEEN ? AND ?");
    }

    @Deprecated
    public static Filter whereEqualKey(String key , String key2){
        return build(key, OpEnum.EQUAL)
                .queryInit(" = "+key2);
    }

    @Deprecated
    public static Filter whereNotEqualKey(String key , String key2){
        return build(key, OpEnum.NOT_EQUAL)
                .queryInit(" != "+key2);
    }

    public static Filter whereLessThanKey(String key , String key2){
        return build(key, OpEnum.LESS_THAN)
                .queryInit(" < "+key2);
    }

    public static Filter whereLessThanEqualKey(String key , String key2){
        return build(key, OpEnum.LESS_THAN_EQUAL)
                .queryInit(" <= "+key2);
    }

    public static Filter whereGreaterThanKey(String key , String key2){
        return build(key, OpEnum.GREATER_THAN)
                .queryInit(" > "+key2);
    }

    public static Filter whereGreaterThanEqualKey(String key , String key2){
        return build(key, OpEnum.GREATER_THAN_EQUAL)
                .queryInit(" >= "+key2);
    }

    @Deprecated
    public static Filter whereBetweenAndKey(String key , String keyFrom, String keyTo){
        return build(key, OpEnum.BETWEEN_AND)
                .queryInit(" BETWEEN "+keyFrom+" AND "+ keyTo);
    }
}
