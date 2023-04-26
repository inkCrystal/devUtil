package cn.dev.supports.spring.dataApi.query.update;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Updater implements Serializable {
    @Serial
    private static final long serialVersionUID = -7659699134902021959L;

    private Updater pre ;
    private Updater next;

    private String key;

    private Serializable value ;

    private String opStr ;

    private Updater() {
    }

    public static Updater buildSet(String key , Serializable value){
        Updater updater = new Updater();
        updater.key = key ;
        updater.value = value ;
        updater.opStr = key+" = ? " ;
        return updater ;
    }

    public static Updater buildInc(String key , Number incValue){
        Updater updater = new Updater();
        updater.key = key ;
        updater.value = incValue ;
        updater.opStr = key+" = "+key+" + ? " ;
        return updater ;
    }

    public static Updater buildEqualKey(String key , String toKey){
        Updater updater = new Updater();
        updater.key = key ;
        updater.value = toKey ;
        updater.opStr = key+" = " +toKey ;
        return updater ;
    }



    public boolean isFirst(){
        return pre == null ;
    }

    public boolean isLast(){
        return next == null ;
    }

    public boolean hasNext(){
        return !isLast();
    }
    public Updater findFirst(){
        if(isFirst()){
            return this ;
        }
        return pre.findFirst();
    }

    public Updater findLast(){
        if(isLast()){
            return this ;
        }
        return next.findLast();
    }

    public Updater getPre() {
        return pre;
    }

    public Updater getNext() {
        return next;
    }

    public String getKey() {
        return key;
    }

    public Serializable getValue() {
        return value;
    }

    public String getOpStr() {
        return opStr;
    }

    /**
     * 自增 指定数值
     * @param key
     * @param incValue
     * @return
     */
    public Updater inc(String key ,Number incValue){
        Updater updater = new Updater();
        updater.key = key ;
        updater.value = incValue ;
        updater.opStr = key+" = "+key+" + ? " ;
        this.next = updater ;
        updater.pre = this ;
        return updater ;
    }

    /**
     * 修改值
     * @param key
     * @param value
     * @return
     */
    public Updater set(String key ,Serializable value){
        Updater updater = new Updater();
        updater.key = key ;
        updater.value = value ;
        updater.opStr = key+" = ? " ;
        this.next = updater ;
        updater.pre = this ;
        return updater ;
    }

    /**
     * 修改为 = toKey 的值
     * @param key
     * @param toKey
     * @return
     */
    public Updater equalKey(String key ,String toKey){
        Updater updater = new Updater();
        updater.key = key ;
        updater.value = toKey ;
        updater.opStr = key+" = " +toKey ;
        this.next = updater ;
        updater.pre = this ;
        return updater ;
    }

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


    public static void main(String[] args) {
        Updater updater = Updater.buildInc("values",3);
        updater.set("a",1).inc("b",2).equalKey("c","d");
        AvailableUpdater availableUpdater = updater.toAvailableUpdater();
        System.out.println(availableUpdater.getUpdateQuery());
    }




}
