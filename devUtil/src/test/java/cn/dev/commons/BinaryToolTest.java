package cn.dev.commons;

import cn.dev.commons.log.DLog;

import static org.junit.jupiter.api.Assertions.*;

class BinaryToolTest {
    public static void main(String[] args) {
        DLog.info(""+Math.pow(2, 123));


        DLog.info(Long.MAX_VALUE);
        DLog.info(BinaryTool.ofBinaryString("0111111111111111111111111111111111111111111111111111111111111111"));
    }


    //从一堆无序的数组当中找到 其中最大的一个数字
    public int findMax(int[] arr){
        int max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if(arr[i]>max){
                max = arr[i];
            }
        }
        return max;
    }

    //从一堆无序的数组当中找到 其中最小的一个数字
    public int findMin(int[] arr){
        int min = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if(arr[i]<min){
                min = arr[i];
            }
        }
        return min;
    }






}