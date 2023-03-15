package com.dev.commons.string;

import java.util.Collection;

/**
 * @author : JasonMao
 * @version : 1.0
 * @date : 2023/3/15 9:51
 * @description : 字符串工具类
 */
public interface StrUtils {

    default boolean hasLength(String wd){
        return wd != null && wd.length() > 0;
    }

    default boolean isEmpty(String wd){
        return wd==null || wd.length() == 0;
    }

    default String nullToEmpty(String wd){
        return isEmpty(wd)?"":wd;
    }

    default boolean isNotEmpty(String wd){
        return !isEmpty(wd);
    }

    boolean containsWhitespace( String str);


    default String trim(String wd){
        return isNotEmpty(wd)?wd.trim():nullToEmpty(wd).trim();
    }


    /**
     * 数组 转成字符串
     * @param array     数组
     * @param Separator 分割字符串
     * @return
     */
    String arrayToString(Object[] array ,String Separator);

    /**
     * 数组 转 字符串，通过空格分割
     * @param array
     * @return
     */
    default String arrayToStringSeparatedBySpace(Object[] array) {
        return arrayToString(array," ");
    }

    default String arrayToStringSeparatedByComma(Object[] array) {
        return arrayToString(array,",");
    }

    String collectionToString(Collection<?> collection,String Separator);

    default String collectionToStringSeparatedBySpace(Collection<?> collection){
        return collectionToString(collection," ");
    }

    default String collectionToStringSeparatedByComma(Collection<?> collection){
        return collectionToString(collection,",");
    }


    String cleanPath(String path) ;

    /**
     * 反转字符串
     * @param wd
     * @return dw
     */
    String revers(String wd);

    String base64Encode(String base64String);

    String base644Decode(String sourceString);

    ZhCnStrUtil ZhCn();



}
