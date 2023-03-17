package com.dev.commons.id;

import com.dev.commons.datetime.TimeMillisClock;
import org.junit.jupiter.api.Test;

class IdUtilTest {

    @Test
    public void test(){
        IdHelper idHelper = IdHelper.getInstance();
        Long now = TimeMillisClock.currentTimeMillis();
        long idFirst = IdHelper.getInstance().getFirstIdOfTime(now);
        long id = IdHelper.getInstance().getNextId();

        System.out.println(IdHelper.getInstance().getTimeMillisById(idFirst));
        System.out.println(IdHelper.getInstance().getTimeMillisById(id));
        System.out.println(now);
        System.out.println(id );
        System.out.println(idFirst);
        System.out.println(IdHelper.getInstance().getNextId());




    }

}