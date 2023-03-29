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
public class VerificationHelper {

    private static <T> Validator<T> build(T data, Predicate<T> predicate){
        return new Validator<>(data,predicate);
    }
    private static <T> VerificationResult<T>  build(boolean isValid ,T t){
        return new VerificationResult<T>(t,isValid,null,null);
    }

    /**************************************************************************************/

    public static final VerificationResult isNotNull(Object o ){
        return Validator.isNotNull(o).toResult();
    }


    public static final VerificationResult isTrue(Boolean b){
        return build(b,t->t).toResult();
    }

    public static final VerificationResult isFalse(Boolean b){
        return build(b,t->!t).toResult();
    }

    /**
     * (谨慎使用)全部不位NULL
     * @param o
     * @return
     */
    public static final VerificationResult isArrayAllNotNull(Object[] o){
        return build(o, (arr) -> {
            for (Object o1 : arr) {
                if (o1 == null) return false;
            }
            return true;
        }).toResult();
    }

    public static final <C extends Collection> VerificationResult<C> collectionIsNotEmpty(C col){
        return build(col,(t)->!t.isEmpty()).toResult();
    }

    public static final <T> VerificationResult<T[]> arrayIsNotEmpty(T[] arr){
        return build(arr, t->t!=null && t.length>0).toResult();
    }

    public static final <T extends Map> VerificationResult<T> mapContainsKey(T map,final String key){
        return build(map,t->t!=null && t.containsKey(key)).toResult();
    }

    public static final <T extends Map> VerificationResult<T> mapIsNotEmpty(T map){
        return build(map,t->t!=null && !t.isEmpty()).toResult();
    }

    public static final <T> VerificationResult<T> isNotMatch(T t,Predicate<T> predicate){
        return build(t, predicate.negate()).toResult();
    }

    public static final <T> VerificationResult isMatch(T t,Predicate<T> predicate){
        return build(t,predicate).toResult();
    }


}
