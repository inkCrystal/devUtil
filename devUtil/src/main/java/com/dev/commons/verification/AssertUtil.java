package com.dev.commons.verification;

import com.dev.exception.VerificationException;

/**
 * @author : JasonMao
 * @version : 1.0
 * @date : 2023/3/15 10:52
 * @description :
 */
public class AssertUtil {

    void throwIfNull(Object o) throws VerificationException{
        if (o==null) {
            throw new VerificationException("对象为NULL");
        }
    }

    void throwIfNotNull(Object o){}

//    default void throwIfMatch(Object o,Predicate predicate){
//        if (predicate.test(o)) {
//            throw new VerificationException();
//        }
//    }


}
