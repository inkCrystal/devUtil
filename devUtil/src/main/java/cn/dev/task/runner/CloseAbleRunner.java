package cn.dev.task.runner;

import cn.dev.structs.CloseAbleHolder;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CloseAbleRunner implements AutoCloseable {

    private List<String> list =new ArrayList<>();

    private CloseAbleHolder holder;









    @Override
    public void close() throws IOException, InterruptedException {

        Thread.sleep(10000L);
        System.out.println("END ");
//        if(list.size() > 0){
//            throw new RuntimeException("不安全的调用，托管的任务未被调用！");
//        }
    }
}

