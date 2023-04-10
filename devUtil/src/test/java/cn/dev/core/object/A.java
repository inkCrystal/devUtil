package cn.dev.core.object;

import cn.dev.commons.log.DLog;

import java.util.HashMap;
import java.util.Map;

public class A {

    private String a;
    private String b;
    private final String c ;

    private A() {
        c= "defaultC";
        System.out.println("默认构造函数");
    }

    private B bentry;

    public A(String a, String b, String c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public String toString() {
        return "A{" +
                "a='" + a + '\'' +
                ", b='" + b + '\'' +
                ", c='" + c + '\'' +
                ", bentry=" + bentry +
                '}';

    }

    public void setA(String a) {
        this.a = a;
    }

    public void setB(String b) {
        this.b = b;
    }


    public String getA() {
        return a;
    }

    public String getB() {
        return b;
    }

    public String getC() {
        return c;
    }

    public void setBentry(B bentry) {
        this.bentry = bentry;
    }

    public B getBentry() {
        return bentry;
    }
}
