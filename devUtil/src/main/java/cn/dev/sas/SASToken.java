package cn.dev.sas;

import com.sun.source.util.TaskListener;

import java.io.Serializable;
import java.util.Map;

/**
 *  线程共享的token
 */
public final class SASToken implements Serializable {
    private long id ;

    private long expire ;

    private Map<String,Object> tokenMap;

    public long getExpire() {
        return expire;
    }

    public long getId() {
        return id;
    }




}
