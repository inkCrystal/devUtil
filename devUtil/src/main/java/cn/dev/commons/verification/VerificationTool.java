package cn.dev.commons.verification;

import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;

/**
 * @author : JasonMao
 * @version : 1.0
 * @date : 2023/3/15 13:09
 * @description : 该工具不会主动抛出异常
 */
public class VerificationTool {
    /**************************************************************************************/

    public static final void isNotNull(Object o ,String errorMessage ){
        VerificationHelper.isNotNull(o).throwIfNotSuccess(errorMessage);
    }

    public static final void isNotNull(Object o  ){
        isNotNull(o,"目标对象为NULL");
    }

    public static final void isTrue(Boolean b){
        isTrue(b,"目标结果不是并非预期的 TRUE" );
    }
    public static final void isTrue(Boolean b,String errMsg){
        VerificationHelper.isTrue(b).throwIfNotSuccess(errMsg);
    }

    public static final void isFalse(Boolean b){
        isFalse(b,"目标结果不是并非预期的 FALSE");
    }
    public static final void isFalse(Boolean b,String errMsg){
        VerificationHelper.isFalse(b).throwIfNotSuccess(errMsg);
    }

    /**
     * (谨慎使用)全部不位NULL
     * @param o
     * @return
     */
    public static final void isArrayAllNotNull(Object[] o){
        isArrayAllNotNull(o,"数组中包含有NULL");
    }
    public static final void isArrayAllNotNull(Object[] o,String errMsg){
        VerificationHelper.isArrayAllNotNull(o).throwIfNotSuccess(errMsg);
    }

    public static final <C extends Collection> void collectionIsNotEmpty(C col,String errMsg){
        VerificationHelper.collectionIsNotEmpty(col).throwIfNotSuccess(errMsg);

    }
    public static final <C extends Collection> void collectionIsNotEmpty(C col){
        collectionIsNotEmpty(col,"Collection is Empty");
    }

    public static final <T> void arrayIsNotEmpty(T[] arr,String errMsg){
        VerificationHelper.arrayIsNotEmpty(arr).throwIfNotSuccess(errMsg);
    }

    public static final <T> void arrayIsNotEmpty(T[] arr){
        arrayIsNotEmpty(arr,"Array is Empty ");
    }


    public static final <T extends Map> void mapContainsKey(T map,final String key,String errMsg){
        VerificationHelper.mapContainsKey(map,key).throwIfNotSuccess(errMsg);
    }

    public static final <T extends Map> void mapContainsKey(T map,final String key){
        mapContainsKey(map,"map not contains "+ key);
    }

    public static final <T extends Map> void mapIsNotEmpty(T map){
        mapIsNotEmpty(map,"MAP IS EMPTY");
    }
    public static final <T extends Map> void mapIsNotEmpty(T map,String errMsg){
        VerificationHelper.mapIsNotEmpty(map).throwIfNotSuccess(errMsg);
    }

    public static final <T> void isNotMatch(T t,Predicate<T> predicate){
        isNotMatch(t,predicate,null);
    }
    public static final <T> void isNotMatch(T t,Predicate<T> predicate,String errMsg){
        VerificationHelper.isNotMatch(t,predicate).throwIfNotSuccess(errMsg);
    }

    public static final <T> void isMatch(T t,Predicate<T> predicate){
        isMatch(t,predicate,null);
    }

    public static final <T> void isMatch(T t,Predicate<T> predicate,String errMsg){
        VerificationHelper.isMatch(t,predicate).throwIfNotSuccess(errMsg);
    }



}
