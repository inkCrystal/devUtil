package com.dev.utils.commons;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author : JasonMao
 * @version : 1.0
 * @date : 2023/3/13 16:29
 * @description :
 */
public class DevStrUtil {


    /**判断是否不为空  ---by JasonMao @ 2023/3/13 16:34 */
    public static final boolean isNotEmpty(String s ){
        return false == isEmpty(s) ;
    }
    
    /**判断是否是空的  ---by JasonMao @ 2023/3/13 16:33 */
    public static final boolean isEmpty(String s){
        return (s==null || s.isEmpty());
    }


    public static boolean containsChinese(String str) {
        String regEx = "[\\u4e00-\\u9fa5]";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }

    public static void main(String[] args) {
        System.out.println(containsChinese("x啊"));
    }







}
