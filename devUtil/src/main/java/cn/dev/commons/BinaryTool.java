package cn.dev.commons;

import cn.dev.commons.string.StrUtils;
import cn.dev.commons.verification.VerificationTool;

/**
 * 二进制工具
 */
public class BinaryTool {

    // 左移 1位
    private static long leftMove(long target){
        return target<<1;
    }

    // 右移 1位
    private static long rightMove(long target){
        return target>>1;
    }

    // 左移 n位
    public static long leftMove(long target,int moveCount){
        for (int i = 0; i < moveCount; i++) {
            target = leftMove(target);
        }
        return target;
    }


    // 右移 n位
    public static long rightMove(long target,int moveCount){
        for (int i = 0; i < moveCount; i++) {
            target = rightMove(target);
        }
        return target;
    }


    public static long ofBinaryString(String str){
        VerificationTool.isMatch(str,s -> {
            for (char c : s.toCharArray()) {
                if(c !='0' &&  c!='1'){
                    return false;
                }
            }
            System.out.println("ok ");
            return true;
        },"并非二进制数据字符串");
        return Long.valueOf(str,2);
    }

    public static boolean checkPositionIsTrue(long v, int posIndex){
        for (int i = 0; i < posIndex; i++) {
            v = rightMove(v);
        }
        return  1== ( v&1);
    }

    public static boolean checkPositionIsOne(long v, int posIndex){
        return  checkPositionIsTrue(v,posIndex);
    }

//    public static boolean checkPositionIsZero(long v, int posIndex) {
//        return checkPositionIsTrue(v,posIndex) ==false;
//    }

    public static final String toHorizontalString(long number){
        boolean[] booleanArray = toBoolArray(number);
        StringBuilder sb= new StringBuilder("");
        sb.append("\033[1;31m Long:: ")
                .append(number).append(" \033[0m ")
                .append("[ HEX:: ").append(StrUtils.toHexString(number))
                .append("; Binary:: ").append(Long.toBinaryString(number))
                .append("]\n");
        for (int i = booleanArray.length; i >0 ; i--) {
            int index = i-1 ;
            if (booleanArray[index]) {
                sb.append("\033[1;43m");
            }else{
                sb.append("\033[30;47m");
            }
            sb.append(_formatTextAlignCenter(index + "",4))
                    .append("\033[0m");
        }
        sb.append("\n");
        for (int i = booleanArray.length; i >0 ; i--) {
            int index = i-1 ;
            if (booleanArray[index]) {
                sb.append("\033[1;31;46m");
                sb.append(_formatTextAlignCenter("1",4));
            }else{
                sb.append("\033[1;46m");
                sb.append(_formatTextAlignCenter("0",4));
            }
            sb.append("\033[0m");
        }

        return sb.toString();
    }

    private static String _formatTextAlignCenter(String v , int len){
        v =v!=null?v.trim():"";
        int length = v.length();
        if(length < len){
            int s = len -length ;
            StringBuilder sb = new StringBuilder("");
            sb.append(v);
            int  index = 0 ;
            while (s>0){
                s --;
                index++ ;
                if(index%2 ==0){
                    sb.append(" ");
                }else{
                    sb.insert(0," ");
                }
            }
            return sb.toString();
        }
        return v;
    }

    public static boolean[] toBoolArray(long value) throws RuntimeException{
        boolean[] array = new boolean[64];
        for (int i = 0; i < array.length; i++) {
            array[i] = checkPositionIsTrue(value,i);
        }
        return array;
    }

    public static long valueOfBooleanArray(boolean[] array){
        long x = 0 ;
        for (int i = 0; i < array.length; i++) {
            long target = array[i]?1:0;
            for (int j = 0; j < i; j++) {
                target = leftMove(target);
            }
            x=  x|target;
        }
        return x;
    }


//    public static void main(String[] args) {
//        long x = 1l;
//        for (int i = 0; i <12+31+1 ; i++) {
//            x = leftMove(x );
//            System.out.println(toHorizontalString(x));
//        }
//
//        System.out.println(x );
//        System.out.println(toHorizontalString(x));
//        System.out.println(toHorizontalString(ofBinaryString("001100")));
//    }
}
