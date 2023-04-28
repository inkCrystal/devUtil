package cn.dev.supports.spring.dataApi.query.filter;

import cn.dev.commons.verification.AssertTool;
import cn.dev.core.object.ObjectUtil;
import cn.dev.supports.spring.dataApi.DataModel;
import cn.dev.supports.spring.dataApi.query.OpEnum;

import java.io.Serializable;
import java.util.*;

/**
 * 用于构建查询的filter链路对象
 * @param <T>
 */
public class FilterBuilder<T extends DataModel> {
    private Class<T> modelType ;
    private Node firstEntry ;
    private Node currentEntry  ;

    // 准备拼接Or 标记
    private boolean prepareOr = false;

    private FilterBuilder(Class<?> modelType) {
        AssertTool.throwIfFalse(ObjectUtil.isChildTypeOfClass(modelType, DataModel.class),"当前的modelType并不是DataModel的子类");
        this.modelType = (Class<T>) modelType;
    }

    public static <T extends DataModel> FilterBuilder<T> getBuilder(Class<?> modelType){
        return new FilterBuilder<T>(modelType);
    }

    /**
     * 转换成 AvilableFilter
     * @return
     */
    public AvailableFilter toAvailableFilter(){
        AssertTool.throwIfFalse(prepareAvailable(),"当前的filter并不可用");
        StringBuilder queryBuilder =new StringBuilder(" WHERE");
        List<Serializable> valueList = new ArrayList<>();
        Node node = this.firstEntry;
        availableFilterNodeAppend(node,queryBuilder,valueList);
        return new AvailableFilter(queryBuilder.toString(),valueList);
    }


    private void availableFilterNodeAppend(Node node,StringBuilder queryBuilder , List<Serializable> valueList ){
        if(node.preNode().isPresent()){
            queryBuilder.append(" AND");
        }
        queryBuilder.append(" ").append(node.toQueryPart());
        Optional<Serializable[]> paramsOpt = node.paramsValue();
        if (paramsOpt.isPresent()) {
            Serializable[] params = paramsOpt.get();
            Collections.addAll(valueList, params);
        }
        if(node.innerNode().isPresent()){
            queryBuilder.append(" OR(");
            this.availableFilterNodeAppend(node.innerNode().get(),queryBuilder,valueList);
            // 拼接 or 逻辑
            queryBuilder.append(")");
        }
        if(node.nextNode().isPresent()){
            this.availableFilterNodeAppend(node.nextNode().get(),queryBuilder,valueList);
        }
    }


    /**
     * 构建 aviailableFilter 的 检查
     * @return
     */
    private boolean prepareAvailable(){
        return modelType != null && firstEntry!=null;
    }

    private void appendEntry(Node t){
        if(firstEntry == null){
            firstEntry = t;
            currentEntry = t;
        }else{
            if(prepareOr){
                currentEntry = currentEntry.appendOrNoteEntry(t);
            }else {
                currentEntry = currentEntry.appendNext(t);
            }
        }
        prepareOr = false;
    }

    public Class<T> getModelType() {
        return modelType;
    }

    public Node getCurrentEntry() {
        return currentEntry;
    }

    public Node getFirstEntry() {
        return firstEntry;
    }

    /**
     * 增加一个Or逻辑,只允许调用一次 ！
     * @return
     */
    public FilterBuilder<T> thenOR(){
        AssertTool.throwIfTrue(this.prepareOr,"已经可以增加OR逻辑，请勿重复调用");
        this.prepareOr = true;
        return this;
    }

    /**
     * 完成一个or 逻辑
     * @return
     */
    public FilterBuilder<T> breakOR(){
        Optional<Node> outerNodeOpt = this.currentEntry.outerNode();
        AssertTool.throwIfFalse(outerNodeOpt.isPresent(),"当前节点并不在开始的OR逻辑里");
        this.currentEntry = currentEntry.outerNode().get();
        return this;
    }


    public FilterBuilder<T> thenEqual(String key, Serializable value){
        this.appendEntry(new Node(key, OpEnum.EQUAL, false, value));
        return this;
    }

    public FilterBuilder<T> thenNotEqual(String key, Serializable value){
        this.appendEntry(new Node(key, OpEnum.NOT_EQUAL, false, value));
        return this;
    }

    public FilterBuilder<T> thenIn(String key, Serializable... values){
        this.appendEntry(new Node(key, OpEnum.IN, false, values));
        return this;
    }

    public FilterBuilder<T> thenNotIn(String key, Serializable... values){
        this.appendEntry(new Node(key, OpEnum.NOT_IN, false, values));
        return this;
    }

    public FilterBuilder<T> thenLike(String key, String value){
        this.appendEntry(new Node(key, OpEnum.LIKE, false, value));
        return this;
    }

    public FilterBuilder<T> thenNotLike(String key, String value){
        this.appendEntry(new Node(key, OpEnum.NOT_LIKE, false, value));
        return this;
    }

    public FilterBuilder<T> thenGreaterThan(String key, Number value){
        this.appendEntry(new Node(key, OpEnum.GREATER_THAN, false, value));
        return this;
    }

    public FilterBuilder<T> thenGreaterThanOrEqual(String key, Number value){
        this.appendEntry(new Node(key, OpEnum.GREATER_THAN_OR_EQUAL, false, value));
        return this;
    }

    public FilterBuilder<T> thenLessThan(String key, Number value){
        this.appendEntry(new Node(key, OpEnum.LESS_THAN, false, value));
        return this;
    }

    public FilterBuilder<T> thenLessThanOrEqual(String key, Number value){
        this.appendEntry(new Node(key, OpEnum.LESS_THAN_OR_EQUAL, false, value));
        return this;
    }

    public FilterBuilder<T> thenBetweenAnd(String key, Serializable value1, Serializable value2){
        this.appendEntry(new Node(key, OpEnum.BETWEEN_AND, false, value1, value2));
        return this;
    }

    public FilterBuilder<T> thenIsNull(String key){
        this.appendEntry(new Node(key, OpEnum.IS_NULL, false));
        return this;
    }

    public FilterBuilder<T> thenIsNotNull(String key){
        this.appendEntry(new Node(key, OpEnum.IS_NOT_NULL, false));
        return this;
    }

    public FilterBuilder<T> thenEqualKey(String key, String targetKey){
        this.appendEntry(new Node(key, OpEnum.EQUAL, true, targetKey));
        return this;
    }

    public FilterBuilder<T> thenNotEqualKey(String key, String targetKey){
        this.appendEntry(new Node(key, OpEnum.NOT_EQUAL, true, targetKey));
        return this;
    }

    public FilterBuilder<T> thenGreaterThanKey(String key, String targetKey){
        this.appendEntry(new Node(key, OpEnum.GREATER_THAN, true, targetKey));
        return this;
    }

    public FilterBuilder<T> thenGreaterThanOrEqualKey(String key, String targetKey){
        this.appendEntry(new Node(key, OpEnum.GREATER_THAN_OR_EQUAL, true, targetKey));
        return this;
    }

    public FilterBuilder<T> thenLessThanKey(String key, String targetKey){
        this.appendEntry(new Node(key, OpEnum.LESS_THAN, true, targetKey));
        return this;
    }

    public FilterBuilder<T> thenLessThanOrEqualKey(String key, String targetKey){
        this.appendEntry(new Node(key, OpEnum.LESS_THAN_OR_EQUAL, true, targetKey));
        return this;
    }






}

/**
 * filter 链路 节点 对象
 */
