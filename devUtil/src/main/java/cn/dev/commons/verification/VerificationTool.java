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

    public static void throwIfNull(Object o , String errorMessage ){
        VerificationHelper.isNotNull(o).throwIfNotSuccess(errorMessage);
    }

    public static void throwIfNull(Object o  ){
        throwIfNull(o,"目标对象为NULL");
    }



    public static void throwIfFalse(Boolean b){
        throwIfFalse(b,"目标结果不是并非预期的 TRUE" );
    }
    public static void throwIfFalse(Boolean b, String errMsg){
        VerificationHelper.isTrue(b).throwIfNotSuccess(errMsg);
    }

    public static void throwIfTrue(Boolean b){
        throwIfTrue(b,"目标结果不是并非预期的 FALSE");
    }
    public static void throwIfTrue(Boolean b, String errMsg){
        VerificationHelper.isFalse(b).throwIfNotSuccess(errMsg);
    }

    /**
     * (谨慎使用)全部不位NULL
     * @param o
     * @return
     */
    public static void throwIfArrayValueContainsNull(Object[] o){
        throwIfArrayValueContainsNull(o,"数组中包含有NULL");
    }
    public static void throwIfArrayValueContainsNull(Object[] o, String errMsg){
        VerificationHelper.isArrayAllNotNull(o).throwIfNotSuccess(errMsg);
    }

    public static <C extends Collection> void throwIfCollectionIsEmpty(C col, String errMsg){
        VerificationHelper.collectionIsNotEmpty(col).throwIfNotSuccess(errMsg);

    }
    public static <C extends Collection> void throwIfCollectionIsEmpty(C col){
        throwIfCollectionIsEmpty(col,"Collection is Empty");
    }

    public static <T> void throwIfArrayIsEmpty(T[] arr, String errMsg){
        VerificationHelper.arrayIsNotEmpty(arr).throwIfNotSuccess(errMsg);
    }

    public static <T> void throwIfArrayIsEmpty(T[] arr){
        throwIfArrayIsEmpty(arr,"Array is Empty ");
    }

    /**
     * 检查数组长度是否 间于 min and max 之间
     * @param arr
     * @param min
     * @param max
     * @param <T>
     */
    public static <T> void throwIfArrayLengthIsNotBetweenAnd(T[] arr, int min, int max){
        throwIfArrayLengthIsNotBetweenAnd( arr, min, max,"Array length is not between "+min+" and "+max);
    }

    public static <T> void throwIfArrayLengthIsNotBetweenAnd(T[] arr, int min, int max, String errMsg){
        throwIfNull(arr,"数组不能为空");
        throwIfNotMatch(arr, a -> a.length >= min && a.length <= max,errMsg);
    }


    public static void throwIfMapNotContainsKey(Map<String,?> map, String key, String errMsg){
        VerificationHelper.mapContainsKey(map,key).throwIfNotSuccess(errMsg);
    }

    public static void throwIfMapNotContainsKey(Map<String,?> map, String key){
        throwIfMapNotContainsKey(map,key,"map not contains "+ key);
    }

    public static void throwIfMapIsEmpty(Map<?,?> map){
        throwIfMapIsEmpty(map,"MAP IS EMPTY");
    }
    public static void throwIfMapIsEmpty(Map<?,?> map, String errMsg){
        VerificationHelper.mapIsNotEmpty(map).throwIfNotSuccess(errMsg);
    }

    public static <T> void throwIfMatch(T t, Predicate<T> predicate){
        throwIfMatch(t,predicate,null);
    }
    public static <T> void throwIfMatch(T t, Predicate<T> predicate, String errMsg){
        VerificationHelper.isNotMatch(t,predicate).throwIfNotSuccess(errMsg);
    }

    public static <T> void throwIfNotMatch(T t, Predicate<T> predicate){
        throwIfNotMatch(t,predicate,null);
    }

    public static <T> void throwIfNotMatch(T t, Predicate<T> predicate, String errMsg){
        VerificationHelper.isMatch(t,predicate).throwIfNotSuccess(errMsg);
    }



}
