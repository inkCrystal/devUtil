package cn.dev.commons;

import static org.junit.jupiter.api.Assertions.*;

class BinaryToolTest {
    public static void main(String[] args) {
        System.out.println(Math.pow(2, 123));

//        System.out.println(BinaryTool.toFullBinaryString(Long.MIN_VALUE));
//        System.out.println(BinaryTool.toFullBinaryString(-1L));
        System.out.println(Long.MAX_VALUE);
        System.out.println(BinaryTool.ofBinaryString("0111111111111111111111111111111111111111111111111111111111111111"));
    }

}