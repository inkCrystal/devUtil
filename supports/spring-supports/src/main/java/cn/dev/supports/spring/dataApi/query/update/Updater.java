package cn.dev.supports.spring.dataApi.query.update;

import cn.dev.commons.verification.VerificationTool;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Updater implements Serializable {
    @Serial
    private static final long serialVersionUID = -7659699134902021959L;

    private Updater pre ;
    private Updater next;

    private String key;

    private Serializable value ;

    private String opStr ;

    protected Updater() {}

    protected Updater(String key, Serializable value, String opStr) {
        this.key = key;
        this.value = value;
        this.opStr = opStr;
    }



    public static UpdateBuilder builder(){
        return UpdateBuilder.builder();
    }


    public boolean isFirst(){
        return pre == null ;
    }

    public boolean isLast(){
        return next == null ;
    }

    /**
     * 判断是否有下一个节点
     * @return
     */
    public boolean hasNext(){
        return !isLast();
    }

    /**
     * 找到 链路中第一个节点
     * @return
     */
    public Updater findFirst(){
        if(isFirst()){
            return this ;
        }
        return pre.findFirst();
    }


    /**
     * 获取当前update链路中的最后一个  Updater 对象
     * @return
     */
    public Updater findLast(){
        if(isLast()){
            return this ;
        }
        return next.findLast();
    }

    /**
     * 获取节点的上一个 Updater 对象
     * @return
     */
    public Updater getPre() {
        return pre;
    }

    /**
     * 获取节点的下一个 Updater 对象
     * @return
     */
    public Updater getNext() {
        return next;
    }

    /**
     * 获取当前节点的 key ，即需要修改的 字段 key
     * @return
     */
    public String getKey() {
        return key;
    }

    /**
     * 获取当前节点的 value ，即需要修改的 字段 value
     * @return
     */
    public Serializable getValue() {
        return value;
    }

    /**
     * 判断当前节点是否有值
     * @return
     */
    public boolean hasValue(){
        return value != null ;
    }

    /**
     * 尝试获取 值
     * @return
     */
    public Optional<Serializable> tryGetValue(){
        return Optional.ofNullable(value);
    }

    /**
     * 获取当前节点的 opStr ，即需要修改的 字段 opStr
     * @return
     */
    public String getOpStr() {
        return opStr;
    }

    private void commonCheckBeforeThen(final String key){
        VerificationTool
                .throwIfNotMatch(this,Updater::isLast,"当前节点不是最后一个节点，无法继续添加节点");
        var entry = this.findFirst();
        while (entry.hasNext()){
            VerificationTool
                    .throwIfMatch(entry,
                            (t)->t.getKey().equals(key),
                            "当前节点已经存在 key 为 "+key+" 的节点");
        }
    }

    private Updater appendEntry(Updater newUpdater){
        this.next = newUpdater;
        newUpdater.pre = this ;
        return newUpdater ;
    }


    /**
     * 产生一个新的节点，并将该节点的值等于指定的value
     * @param key
     * @param value
     * @return
     */
    public Updater thenSet(final String key , Serializable value){
        this.commonCheckBeforeThen(key);
        var entry= UpdateBuilder.setValue(key, value);
        return appendEntry(entry);
    }

    /**
     * 产生一个新的节点，并将该节点的值等于另外一个 （key）键的值
     * @param key
     * @param anotherKey
     * @return
     */
    public Updater thenEqualToAnotherKey(final String key , String anotherKey){
        this.commonCheckBeforeThen(key);
        var entry= UpdateBuilder.equalsToAnotherKey(key, anotherKey);
        return appendEntry(entry);
    }

    /**
     * 产生一个新的节点，并将该节点的值加上一个数值
     * @param key
     * @param number
     * @return
     */
    public Updater thenPlus(final String key , Number number){
        this.commonCheckBeforeThen(key);
        var entry= UpdateBuilder.plusNumber(key, number);
        return appendEntry(entry);
    }


    /**
     * 产生一个新的节点，并将该节点的值减去一个数值
     * @param key
     * @param number
     * @return
     */
    public Updater thenMinus(final String key , Number number){
        this.commonCheckBeforeThen(key);
        var entry= UpdateBuilder.minusNumber(key, number);
        return appendEntry(entry);
    }

    /**
     * 产生一个新的节点，并将该节点的值乘以一个数值
     * @param key
     * @param number
     * @return
     */
    public Updater thenDivide(final String key , Number number){
        this.commonCheckBeforeThen(key);
        var entry= UpdateBuilder.divideNumber(key, number);
        return appendEntry(entry);
    }

    /**
     * 产生一个新的节点，并将该节点的值除以一个数值
     * @param key
     * @param number
     * @return
     */
    public Updater thenMultiply(final String key , Number number){
        this.commonCheckBeforeThen(key);
        var entry= UpdateBuilder.multiplyNumber(key, number);
        return appendEntry(entry);
    }



    /**
     * 转化成  AvailableUpdater 对象
     * @return
     */
    public  AvailableUpdater toAvailableUpdater(){
        Updater temp = findFirst();
        StringBuilder querySb = new StringBuilder("SET ");
        List<Serializable> values =new ArrayList<>();
        do{
            querySb.append(temp.opStr);
            if (this.getValue()!=null) {
                values.add(value);
            }
            if(temp.hasNext()){
                querySb.append(" , ");
                temp = temp.getNext();
            }
        }while (temp.hasNext());
        return new AvailableUpdater(querySb.toString(),values.toArray(new Serializable[0]));
    }




}
