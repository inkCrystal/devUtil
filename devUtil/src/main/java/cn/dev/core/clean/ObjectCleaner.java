package cn.dev.core.clean;

import java.io.IOException;
import java.lang.ref.Cleaner;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public class ObjectCleaner  {


    public static <T extends AutoCloseable> T createClean(IAutoCLeanBuilder builder,Class<T> clazz) throws Exception{
        Cleaner cleaner =Cleaner.create();
        T newedInstance = (T) builder.newInstance();
        cleaner.register(newedInstance,new DefaultCleanAction(newedInstance));
        return newedInstance;
    }


    public static void doTest() throws Exception {
        System.out.println("hello");
        Test test =   ObjectCleaner.createClean(()->{
            return new Test();
        },Test.class);
        test.doTest();
        System.out.println("end ");
    }

    public static void main(String[] args) throws Exception {


        CompletableFuture.runAsync(()->{
            try {
                doTest();
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            ;
        });

        while (true){
            Thread.sleep(10);
            System.gc();
        }

    }
}