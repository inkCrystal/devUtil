package cn.dev.supports.spring.dataApi.query.filter;

import cn.dev.commons.verification.AssertTool;
import cn.dev.core.object.ObjectUtil;
import cn.dev.supports.spring.dataApi.query.OpEnum;

import java.io.Serializable;
import java.util.Optional;

class Node implements Serializable {
    private final String key;
    private final OpEnum opEnum;
    private final boolean keyParam ;
    private final Serializable[] values;
    private Node pre ;
    private Node next ;
    private Node inner ;
    private Node outer ;

    private void paramCheck(OpEnum opEnum, boolean keyParam,Serializable... arr){
        if(keyParam){
            AssertTool.throwIfFalse(opEnum.keyParamAble(),"该操作符["+opEnum+"]不允许使用key作为参数");
        }
        if (arr == null) {
            AssertTool.throwIfFalse(opEnum.paramNullable(), "该操作符["+opEnum+"]参数不允许为NULL");
        } else {
            int minLen = opEnum.paramMinLen();
            int maxLen = opEnum.paramMaxLen();
            int len = arr.length;
            if(minLen == maxLen){
                AssertTool.throwIfFalse(len == minLen, "该操作符["+opEnum+"]参数长度必须为" + minLen);
            }else{
                AssertTool.throwIfTrue( len< minLen , "该操作符["+opEnum+"]参数至少有" + minLen + "个");
            }
        }
    }



    protected Node(String key, OpEnum opEnum, boolean keyParam, Serializable... values) {
        this.paramCheck(opEnum, keyParam, values);
        this.key = key;
        this.opEnum = opEnum;
        this.keyParam = keyParam;
        this.values = values;
    }

    protected Node(String key, OpEnum opEnum, boolean keyParam, Serializable[] values, Node pre, Node next, Node inner, Node outer) {
        this.key = key;
        this.opEnum = opEnum;
        this.keyParam = keyParam;
        this.values = values;
        this.pre = pre;
        this.next = next;
        this.inner = inner;
        this.outer = outer;
    }

    //核心方法
    protected Node appendOrNoteEntry(Node t){
        AssertTool.throwIfNotNull(t.inner, "使用限制，该节点已经有Or节点关联，如需使用可在内部链路，或者其他节点使用");
        this.inner = t;
        t.outer = this;
        return t;
    }

    //核心方法
    protected Node appendNext(Node t){
        t.pre = this;
        this.next = t;
        return t;
    }


    protected Optional<Node> preNode(){
        return Optional.ofNullable(pre);
    }

    protected Optional<Node> nextNode(){
        return Optional.ofNullable(next);
    }

    protected Optional<Node> innerNode(){
        return Optional.ofNullable(inner);
    }

    protected Optional<Node> outerNode(){
        Node t = this;
        while (t.pre!=null){
            t = t.pre;
        }
        return Optional.ofNullable(t.outer);
    }

    protected String key() {
        return key;
    }

    protected OpEnum opEnum() {
        return opEnum;
    }

    protected boolean isKeyParam() {
        return keyParam;
    }

    /**
     * 获取 作为 变量参数的 参数 value数组
     * @return
     */
    protected Optional<Serializable[]> paramsValue() {
        if(keyParam){
            return Optional.empty();
        }
        return Optional.ofNullable(values);
    }

    protected Optional<Serializable[]> keyParamsValue() {
        if(keyParam){
            return Optional.ofNullable(values);
        }
        return Optional.empty();
    }

    /**
     * 直接 获取 value ，不关心 检查
     * @return
     */
    protected Serializable[] readValuesWithOutCheck() {
        return values;
    }

    /**
     * 该节点的深度
     * @return
     */
    protected int deep(){
        int deep = 0 ;
        Node t = this;
        while (t.outer!=null){
            deep++;
            t = t.outer;
        }
        return deep;
    }

    protected String toQueryPart(){
        StringBuilder part = new StringBuilder(" ");
        switch (opEnum){
            case EQUAL -> buildEqual(part, key, values[0], keyParam);
            case NOT_EQUAL -> buildNotEqual(part, key, values[0], keyParam);
            case GREATER_THAN -> buildGreaterThan(part, key, values[0], keyParam);
            case GREATER_THAN_OR_EQUAL -> buildGreaterThanOrEqual(part, key, values[0], keyParam);
            case LESS_THAN -> buildLessThan(part, key, values[0], keyParam);
            case LESS_THAN_OR_EQUAL -> buildLessThanOrEqual(part, key, values[0], keyParam);
            case LIKE -> buildLike(part, key, (String) values[0]);
            case NOT_LIKE -> buildNotLike(part, key, (String) values[0]);
            case IN -> buildIn(part, key, values, keyParam);
            case NOT_IN -> buildNotIn(part, key, values, keyParam);
            case BETWEEN_AND -> buildBetweenAnd(part, key, values[0], values[1], keyParam);
            case NOT_BETWEEN_AND -> buildNotBetweenAnd(part, key, values[0], values[1], keyParam);
            case IS_NULL -> buildIsNull(part, key);
            case IS_NOT_NULL -> buildIsNotNull(part, key);
        }
        return part.toString();
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

    private static void buildLike(StringBuilder sb ,String key, String value ){
        //LIKE 不支持key
        sb.append(key).append(" LIKE ");
        sb.append(" ? ");
    }

    private static void buildNotLike(StringBuilder sb ,String key, String value){
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

    private static void buildLessThanOrEqual(StringBuilder sb , String key, Serializable value, boolean paramIsKey){
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

    private static void buildGreaterThanOrEqual(StringBuilder sb , String key, Serializable value, boolean paramIsKey){
        sb.append(key).append(" >= ");
        if(paramIsKey){
            sb.append((String) value);
        }else{
            sb.append(" ? ");
        }
    }

    private static void buildIsNull(StringBuilder sb ,String key){
        sb.append(key).append(" IS NULL ");
    }

    private static void buildIsNotNull(StringBuilder sb ,String key){
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
}
