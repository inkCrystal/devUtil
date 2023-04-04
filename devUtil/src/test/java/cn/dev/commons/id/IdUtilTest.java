package cn.dev.commons.id;

import cn.dev.clock.CommonTimeClock;
import cn.dev.commons.log.DLog;
import org.junit.jupiter.api.Test;

class IdUtilTest {

    @Test
    public void test(){
        IdHelper idHelper = IdHelper.getInstance();
        Long now = CommonTimeClock.currentTimeMillis();
        long idFirst = IdHelper.getInstance().getFirstIdOfTime(now);
        long id = IdHelper.getInstance().getNextId();

        DLog.info(IdHelper.getInstance().getTimeMillisById(idFirst));
        DLog.info(IdHelper.getInstance().getTimeMillisById(id));
        DLog.info(now);
        DLog.info(id );
        DLog.info(idFirst);
        DLog.info(IdHelper.getInstance().getNextId());




    }

}