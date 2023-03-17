package com.dev.commons;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author : JasonMao
 * @version : 1.0
 * @date : 2023/2/3 10:49
 * @description :
 */
public class RandomUtil {

    private static final int cacheSize = 500 ;
    private static int int_index = 0 ;

    private static ThreadLocalRandom getRandom(){
        return ThreadLocalRandom.current();
    }
    private static final char[] chars = {
            '0','1','2','3','4','5','6','7','8','9',
            'A','B','C','D','E','F','G','H','I','J',
            'K','L','M','N','O','P','Q','R','S','T',
            'U','V','W','X','Y','Z'
    };

//    private static int charsLen = chars.length;
    /**
     * 随机数是 0 --- maxValueLimit中间的值，
     * 包含0 不包含maxVakueLimit
     * ---by JasonMao @ 2023/2/3 14:14 */
    private static final int maxValueLimit = 100;

    private static final int[] int_array = new int[cacheSize];


    /**
     * 初始化  int array ， 让 0 - maxValueLimit 的数字 填满 整个数组
     */
    static {
        reRandomArray();
    }


    /**交换  ---by JasonMao @ 2023/3/16 13:55 */
    private static void swap(int index1 ,int index2){
        int t = int_array[index1];
        int_array[index1] = int_array[index2];
        int_array[index2] = t ;
    }



    /** 用随机数注满 数组   ---by JasonMao @ 2023/3/16 14:56 */
    private static CompletableFuture reRandomArray(){
        return CompletableFuture.runAsync(() -> {
            for (int i = 0; i < int_array.length; i++) {
                int_array[i] = getRandom().nextInt(maxValueLimit);
            }
        });
    }

    private static synchronized int nextIndex(){
        int target = int_index ;
        int_index ++;
        if(int_index +1 > cacheSize){
            reRandomArray();
            int_index = 0 ;
        }
        return target;

    }
    public final static int nextInt(){
        int target = nextIndex();
        return int_array[target];
    }

//    public final static long nextLong(){
//        return nextInt();
//    }
    public static boolean nextBoolean(){
        return getRandom().nextBoolean();
//        return nextInt()<(maxValueLimit/2);
    }

    public static final char nextChar(){
        int index = nextInt()%chars.length;
        return chars[index];
    }

    public static final String randomStr(final int len){
        StringBuilder sb = new StringBuilder();
        while (sb.length() < len){
           sb.append(nextChar());
        }
        return sb.toString();
    }
    
}
