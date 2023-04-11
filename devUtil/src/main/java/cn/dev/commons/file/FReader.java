package cn.dev.commons.file;

import java.io.*;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;

public class FReader implements AutoCloseable {
    SeekableByteChannel seekableByteChannel;

    public FReader(String path) throws IOException {
        seekableByteChannel = Files.newByteChannel(Path.of(path));

    }


    @Override
    public void close() throws Exception {

    }

    public static void main(String[] args) throws FileNotFoundException {
        BufferedReader bufferedReader =new BufferedReader(new FileReader("D:\\Temps\\test\\test.txt"));

//        bufferedReader.readLine();
    }
}
