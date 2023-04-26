package cn.dev.supports.spring.dataApi.query;

import cn.dev.commons.verification.VerificationTool;
import cn.dev.supports.spring.dataApi.query.filter.Filter;

import java.io.Serializable;

public enum OpEnum {

    OR,                     // 拼接一个 OR ( ... ) 的子条件，即可以理解为一个 全新的 Filter 链
    IN,                     // 拼接 一个 k in ( ... ),不允许以Key 作为参数
    NOT_IN,                 // 拼接 一个 k not in ( ... ),不允许以Key 作为参数
    LIKE,                   // 拼接 一个 k like ?     ,不允许以Key 作为参数,参数类型为 String
    NOT_LIKE,               // 拼接 一个 k not like ? ,不允许以Key 作为参数,参数类型为 String
    EQUAL,                  // 拼接 一个 k = ? , 允许以Key 作为参数
    NOT_EQUAL,              // 拼接 一个 k != ? , 允许以Key 作为参数
    LESS_THAN,              // 拼接 一个 k < ? , 允许以Key 作为参数,参数类型是数值类型
    LESS_THAN_AND_EQUAL,    // 拼接 一个 k <= ? , 允许以Key 作为参数,参数类型是数值类型
    GREATER_THAN,           // 拼接 一个 k > ? , 允许以Key 作为参数,参数类型是数值类型
    GREATER_THAN_AND_EQUAL, // 拼接 一个 k >= ? , 允许以Key 作为参数,参数类型是数值类型
    IS_NULL,                // 拼接 一个 k is null , 无参，一般不推荐使用
    IS_NOT_NULL,            // 拼接 一个 k is not null , 无参，一般不推荐使用
    NOT_BETWEEN_AND,        // 拼接 一个 k not between ? and ? , 允许以Key 作为参数,参数类型是数值类型
    BETWEEN_AND,            // 拼接 一个 k between ? and ? , 允许以Key 作为参数,参数类型是数值类型
    AND                     // 保留用法，不会出现在 操作的方法中

    ;

    /**
     * 协助 filter 转成 正常的SQL filter 片段
     * @param filter
     * @return
     */
    public static String toFilterQuery(Filter filter){
        StringBuilder sb =new StringBuilder(" ");
        boolean paramIsKey = filter.isKeyParam();
        switch (filter.getOp()){
            case EQUAL -> buildEqual(sb,filter.getKey(),filter.getAllTypeValues()[0],paramIsKey);
            case NOT_EQUAL -> buildNotEqual(sb,filter.getKey(),filter.getAllTypeValues()[0],paramIsKey);
            case LIKE -> buildLike(sb,filter.getKey(),(String) filter.getAllTypeValues()[0],paramIsKey);
            case NOT_LIKE -> buildNotLike(sb,filter.getKey(),(String) filter.getAllTypeValues()[0],paramIsKey);
            case LESS_THAN -> buildLessThan(sb,filter.getKey(),filter.getAllTypeValues()[0],paramIsKey);
            case LESS_THAN_AND_EQUAL -> buildLessThanAndEqual(sb,filter.getKey(),filter.getAllTypeValues()[0],paramIsKey);
            case GREATER_THAN -> buildGreaterThan(sb,filter.getKey(),filter.getAllTypeValues()[0],paramIsKey);
            case GREATER_THAN_AND_EQUAL -> buildGreaterThanAndEqual(sb,filter.getKey(),filter.getAllTypeValues()[0],paramIsKey);
            case IN -> buildIn(sb,filter.getKey(),filter.getAllTypeValues(),paramIsKey);
            case NOT_IN -> buildNotIn(sb,filter.getKey(),filter.getAllTypeValues(),paramIsKey);
            case BETWEEN_AND -> buildBetweenAnd(sb,filter.getKey(),filter.getAllTypeValues()[0],filter.getAllTypeValues()[1],paramIsKey);
            case NOT_BETWEEN_AND -> buildNotBetweenAnd(sb,filter.getKey(),filter.getAllTypeValues()[0],filter.getAllTypeValues()[1],paramIsKey);
            case IS_NULL -> buildIsNull(sb,filter.getKey(),null,paramIsKey);
            case IS_NOT_NULL -> buildIsNotNull(sb,filter.getKey(),null,paramIsKey);
        }
        return sb.toString();
    }

    private static void buildIn(StringBuilder sb ,String key, Serializable[] values,boolean paramIsKey){
        sb.append(key).append(" IN (");
        for (int i = 0; i < values.length; i++) {
            if(paramIsKey){
                sb.append((String) values[i]);
            }else{
                sb.append(" ? ");
            }
            if(i != values.length - 1){
                sb.append(",");
            }
        }
        sb.append(")");
    }

    private static void buildNotIn(StringBuilder sb ,String key, Serializable[] values,boolean paramIsKey){
        sb.append(key).append(" NOT IN (");
        for (int i = 0; i < values.length; i++) {
            if(paramIsKey){
                sb.append((String) values[i]);
            }else{
                sb.append(" ? ");
            }
            if(i != values.length - 1){
                sb.append(",");
            }
        }
        sb.append(")");
    }

    private static void buildLike(StringBuilder sb ,String key, String value,boolean paramIsKey){
        //LIKE 不支持key
        sb.append(key).append(" LIKE ");
        sb.append(" ? ");
    }

    private static void buildNotLike(StringBuilder sb ,String key, String value,boolean paramIsKey){
        //NOT LIKE 不支持key
        sb.append(key).append(" NOT LIKE ");
        sb.append(" ? ");
    }

    private static void buildEqual(StringBuilder sb ,String key, Serializable value,boolean paramIsKey){
        sb.append(key).append(" = ");
        if(paramIsKey){
            sb.append((String) value);
        }else{
            sb.append(" ? ");
        }
    }

    private static void buildNotEqual(StringBuilder sb ,String key, Serializable value,boolean paramIsKey){
        sb.append(key).append(" != ");
        if(paramIsKey){
            sb.append((String) value);
        }else{
            sb.append(" ? ");
        }
    }

    private static void buildLessThan(StringBuilder sb ,String key, Serializable value,boolean paramIsKey){
        sb.append(key).append(" < ");
        if(paramIsKey){
            sb.append((String) value);
        }else{
            sb.append(" ? ");
        }
    }

    private static void buildLessThanAndEqual(StringBuilder sb ,String key, Serializable value,boolean paramIsKey){
        sb.append(key).append(" <= ");
        if(paramIsKey){
            sb.append((String) value);
        }else{
            sb.append(" ? ");
        }
    }

    private static void buildGreaterThan(StringBuilder sb ,String key, Serializable value,boolean paramIsKey){
        sb.append(key).append(" > ");
        if(paramIsKey){
            sb.append((String) value);
        }else{
            sb.append(" ? ");
        }
    }

    private static void buildGreaterThanAndEqual(StringBuilder sb ,String key, Serializable value,boolean paramIsKey){
        sb.append(key).append(" >= ");
        if(paramIsKey){
            sb.append((String) value);
        }else{
            sb.append(" ? ");
        }
    }

    private static void buildIsNull(StringBuilder sb ,String key, Serializable value,boolean paramIsKey){
        sb.append(key).append(" IS NULL ");
    }

    private static void buildIsNotNull(StringBuilder sb ,String key, Serializable value,boolean paramIsKey){
        sb.append(key).append(" IS NOT NULL ");
    }

    private static void buildNotBetweenAnd(StringBuilder sb ,String key, Serializable value1,Serializable values2,boolean paramIsKey){
        sb.append(key).append(" NOT BETWEEN ")
                .append( paramIsKey? (String) value1 : " ? ")
                .append(" AND ")
                .append( paramIsKey? (String) values2 : " ? ");
    }

    private static void buildBetweenAnd(StringBuilder sb ,String key, Serializable value1,Serializable values2,boolean paramIsKey) {
        sb.append(key).append(" BETWEEN ")
                .append( paramIsKey? (String) value1 : " ? ")
                .append(" AND ")
                .append( paramIsKey? (String) values2 : " ? ");
    }


    /**
     * 校验参数
     * @param value
     */
    public void validParams(Serializable[] value){
        switch (this){
            case IN -> VerificationTool.throwIfArrayIsEmpty(value,"使用IN查询时，参数至少大于0");
            case NOT_IN -> VerificationTool.throwIfArrayIsEmpty(value,"使用NOT IN查询时，参数至少大于0");
            case LIKE -> VerificationTool.throwIfArrayLengthIsNotBetweenAnd(value,1,1,"使用LIKE查询时，参数只能有一个");
            case NOT_LIKE -> VerificationTool.throwIfArrayLengthIsNotBetweenAnd(value,1,1,"使用NOT LIKE查询时，参数只能有一个");
            case EQUAL -> VerificationTool.throwIfArrayLengthIsNotBetweenAnd(value,1,1,"使用EQUAL查询时，参数只能有一个");
            case NOT_EQUAL -> VerificationTool.throwIfArrayLengthIsNotBetweenAnd(value,1,1,"使用NOT EQUAL查询时，参数只能有一个");
            case LESS_THAN -> VerificationTool.throwIfArrayLengthIsNotBetweenAnd(value,1,1,"使用LESS THAN查询时，参数只能有一个");
            case LESS_THAN_AND_EQUAL -> VerificationTool.throwIfArrayLengthIsNotBetweenAnd(value,1,1,"使用LESS THAN AND EQUAL查询时，参数只能有一个");
            case GREATER_THAN -> VerificationTool.throwIfArrayLengthIsNotBetweenAnd(value,1,1,"使用GREATER THAN查询时，参数只能有一个");
            case GREATER_THAN_AND_EQUAL -> VerificationTool.throwIfArrayLengthIsNotBetweenAnd(value,1,1,"使用GREATER THAN AND EQUAL查询时，参数只能有一个");
            case BETWEEN_AND -> VerificationTool.throwIfArrayLengthIsNotBetweenAnd(value,2,2,"使用BETWEEN AND查询时，参数只能有两个");
            case NOT_BETWEEN_AND -> VerificationTool.throwIfArrayLengthIsNotBetweenAnd(value,2,2,"使用NOT BETWEEN AND查询时，参数只能有两个");

        }

    }





}

