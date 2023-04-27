package cn.dev.supports.spring.dataApi.query.update;

import java.io.Serializable;
import java.util.Map;

public class UpdateBuilder {

    private UpdateBuilder(){}

    private static final UpdateBuilder builder = new UpdateBuilder();

    protected static UpdateBuilder builder(){
        return builder ;
    }

    /**
     * 构建一个全新的 update 节点 ，并指定操作键的值为另一个键的值
     * @param key
     * @param anotherKey
     * @return
     */
    public static Updater equalsToAnotherKey(String key , String anotherKey){
        return new Updater(key, null, key+" = "+anotherKey);
    }

    /**
     * 构建一个全新的 update 节点 ，并指定操作键的值 指定的值
     * @param key
     * @param value
     * @return
     */
    public static Updater setValue(String key , Serializable value){
        return new Updater(key, value, key+" = ? ");
    }

    /**
     * 构建一个全新的 update 节点，并指定操作键的值加上一个数值
     * @param key
     * @param number
     * @return
     */
    public static Updater plusNumber(String key , Number number){
        return new Updater(key, number, key+" = "+key+" + ? ");
    }

    /**
     * 构建一个全新的 update 节点，并指定操作键的值减去一个数值
     * @param key
     * @param number
     * @return
     */
    public static Updater minusNumber(String key , Number number){
        return new Updater(key, number, key+" = "+key+" - ? ");
    }

    /**
     * 构建一个全新的 update 节点，并指定操作键的值乘以一个数值
     * @param key
     * @param number
     * @return
     */
    public static Updater multiplyNumber(String key , Number number){
        return new Updater(key, number, key+" = "+key+" * ? ");
    }

    /**
     * 构建一个全新的 update 节点，并指定操作键的值除以一个数值
     * @param key
     * @param number
     * @return
     */
    public static Updater divideNumber(String key , Number number){
        return new Updater(key, number, key+" = "+key+" / ? ");
    }


    /**
     * 仅仅支持 设置为一个新的值
     * @param setMapper
     * @return
     */
    public static Updater fromSetMap(Map<String,Serializable> setMapper){
        Updater updater = null ;
        for (Map.Entry<String, Serializable> entry : setMapper.entrySet()) {
             if(updater == null){
                updater = setValue(entry.getKey(),entry.getValue());
            }else{
                updater.thenSet( entry.getKey(),entry.getValue());
            }
        }
        return updater ;
    }
}
