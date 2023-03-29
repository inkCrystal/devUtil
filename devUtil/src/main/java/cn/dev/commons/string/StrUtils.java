package cn.dev.commons.string;

import java.util.*;

/**
 * @author : JasonMao
 * @version : 1.0
 * @date : 2023/3/15 9:51
 * @description : 字符串工具类
 */
public class StrUtils {
    private static final String EMPTY = "";

    public static  boolean hasLength(String wd){
        return wd != null && wd.length() > 0;
    }

    public static  boolean isEmpty(String wd){
        return wd==null || wd.length() == 0;
    }

    public static  String nullToEmpty(String wd){
        return isEmpty(wd)?"":wd;
    }

    public static  boolean isNotEmpty(String wd){
        return !isEmpty(wd);
    }

    public static boolean containsWhitespace( String str){
        if (isNotEmpty(str)) {
            return str.contains(" ");
        }
        return false;
    }


    public static  String trim(String wd){
        return isNotEmpty(wd)?wd.trim():nullToEmpty(wd).trim();
    }


    /**
     * 数组 转成字符串
     * @param array     数组
     * @param separator 分割字符串
     * @return
     */
    public static String arrayToString(Object[] array ,String separator){
        if (Objects.nonNull(array) && array.length >1 ) {
            String[] strArray = Arrays.stream(array).map(x -> Objects.toString(x)).toArray(String[]::new);
            return String.join(separator,strArray);
        }
        return EMPTY;


    }

    /**
     * 数组 转 字符串，通过空格分割
     * @param array
     * @return
     */
    public static  String arrayToStringSeparatedBySpace(Object[] array) {
        return arrayToString(array," ");
    }

    public static  String arrayToStringSeparatedByComma(Object[] array) {
        return arrayToString(array,",");
    }

    public static String collectionToString(Collection<?> collection,String separator){
        if (Objects.nonNull(separator) && collection.size()>0) {
            List<String> list = collection.stream().map(Objects::toString).toList();
            return String.join(separator,list);
        }
        return EMPTY;

    }

    public static  String collectionToStringSeparatedBySpace(Collection<?> collection){
        return collectionToString(collection," ");
    }

    public static  String collectionToStringSeparatedByComma(Collection<?> collection){
        return collectionToString(collection,",");
    }


//    String cleanPath(String path) {
//        return null;
//    }

    /**
     * 反转字符串
     * @param wd
     * @return dw
     */
    String revers(String wd){
        if (isNotEmpty(wd)) {
            char[] chars = wd.toCharArray();

        }


        return null;
    }

    public static String base64Encode(String base64String){
        return Base64.getEncoder().encodeToString(base64String.getBytes());
    }

    public static String base644Decode(String sourceString){
        return new String( Base64.getDecoder().decode(sourceString));
    }


    public static final  ZhCnStrUtil ZhCn(){
        return null;
    }



    public static String toHexString(Number v ){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("0x");
        String hex = Long.toHexString(v.longValue());

        int appendLength = 0 ;


        if(hex.length() <2){
            appendLength = 2 - hex.length();
        }else if(hex.length() < 4){
            appendLength = 4- hex.length();
        }else if(hex.length()<8){
            appendLength = 8 - hex.length();
        }
        while (appendLength  > 0){
            appendLength --;
            stringBuilder.append("0");
        }

        stringBuilder.append(hex);
        return stringBuilder.toString();
    }



    public static void main(String[] args) {
        StringBuilder sb =new StringBuilder("");
        for (int i = 0; i < 65566; i++) {
            sb.append("" +i);
        }
        final String source = sb.toString();

    }



}
