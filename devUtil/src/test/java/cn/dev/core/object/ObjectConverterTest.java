package cn.dev.core.object;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ObjectConverterTest {

    @Test
    void objToMap() throws Exception {
        A a =new A("a1","a2","a3");
        B b =new B();
        b.setSex(1);
        b.setAdmin(3);
        b.setLocalDateTime(LocalDateTime.now());
        a.setB("BBBB");
        a.setBentry(b);
        a.setA("xadaaaa");
        final Map<String, Object> map = ObjectConverter.objToMap(a);
        final A newA = ObjectConverter.mapTpBean(A.class, map);
        System.out.println(map);
        System.out.println(ObjectConverter.objToMap(newA));
        System.out.println(a);
        System.out.println(newA);

    }
}