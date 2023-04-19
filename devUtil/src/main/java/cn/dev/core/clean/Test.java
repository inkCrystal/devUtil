package cn.dev.core.clean;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class Test implements AutoCloseable{


    private FileChannel fileChannel ;

    public Test() throws IOException {
        fileChannel = FileChannel.open(Path.of("E:\\tessdata\\a.txt"), StandardOpenOption.READ);
    }

    public void  doTest() throws Exception {
        System.out.println("hello");
        System.out.println(fileChannel.read(ByteBuffer.allocate(1)));
        System.out.println("end ");
    }

    @Override
    public void close() throws Exception {
        fileChannel.close();
        System.out.println("close ");
    }
}
