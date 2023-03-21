package cn.dev.structs;

import cn.dev.core.model.Identity;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class CloseAbleHolder implements AutoCloseable{
    record  Entry<T>(Identity identity,T t){}

    private Queue<Entry> queue ;

    public CloseAbleHolder() {
        this.queue = new LinkedList<>();
    }

    @Override
    public void close() throws Exception {
        if(!this.queue.isEmpty()){
            throw new Exception("hold 尚未清理，代码即将退出 ");
        }
    }
}
