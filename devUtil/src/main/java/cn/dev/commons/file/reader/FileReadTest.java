package cn.dev.commons.file.reader;

import java.io.*;
import java.util.concurrent.atomic.AtomicInteger;

public class FileReadTest {

    public FileReadTest() throws FileNotFoundException {
    }

    public static void bufferReadTestStream() throws IOException {
        System.out.println("bufferReadTestStream");
        long start = System.currentTimeMillis();
        BufferedReader bufferedReader = new BufferedReader(new FileReader("D:\\Temps\\test\\tess.txt"));
        final long count = bufferedReader.lines().parallel().filter(t -> t.contains("J0>AC")).distinct().count();
        System.out.println(count);
        bufferedReader.close();
        System.out.println(System.currentTimeMillis() - start);

    }


    public static void bufferReadTestForLoop() throws IOException {
        System.out.println("bufferReadTestForLoop");
        long start = System.currentTimeMillis();
        BufferedReader bufferedReader = new BufferedReader(new FileReader("D:\\Temps\\test\\tess.txt"));
        AtomicInteger integer = new AtomicInteger(0);
        while (true){
            String s = bufferedReader.readLine();
            if(s==null) break;
            if(s.contains("J0>AC")) {
                integer.incrementAndGet();

            }
        }
        bufferedReader.close();
        System.out.println(integer.get());
        System.out.println(System.currentTimeMillis() - start);

    }

    public static void lineReadTestStream() throws IOException {
        System.out.println("lineReadTestStream");
        long start = System.currentTimeMillis();
        LineNumberReader lineNumberReader = new LineNumberReader(new FileReader("D:\\Temps\\test\\tess.txt"));
        final long count = lineNumberReader.lines().filter(t -> t.contains("J0>AC")).distinct().count();
        System.out.println(count + " of " + lineNumberReader.getLineNumber() );
        lineNumberReader.close();
        System.out.println(System.currentTimeMillis() - start);

    }

    public static void lineNumberTestForLoop() throws IOException{
        System.out.println("lineNumberTestForLoop");
        long start = System.currentTimeMillis();
        LineNumberReader lineNumberReader = new LineNumberReader(new FileReader("D:\\Temps\\test\\test.txt"));
        AtomicInteger integer = new AtomicInteger(0);
        while (true){
            String s = lineNumberReader.readLine();
            if(s==null) break;
            if(s.contains("J0>AC")) {
                integer.incrementAndGet();
            }
        }

        System.out.println(integer.get() + " of " + lineNumberReader.getLineNumber() );
        lineNumberReader.close();

        System.out.println(System.currentTimeMillis() - start);

    }


    public static void randomAccessTest() throws IOException {

        RandomAccessFile randomAccessFile = new RandomAccessFile("D:\\Temps\\test\\test.txt", "rw");
        randomAccessFile.seek(0);
        randomAccessFile.readLine();
        randomAccessFile.readLine();
        randomAccessFile.write("xxx".getBytes());
    }


    public static void main(String[] args) throws IOException {

        bufferReadTestStream();
        System.out.println("=====================================");
        bufferReadTestForLoop();
        System.out.println("=====================================");
        lineReadTestStream();
        System.out.println("=====================================");
        lineNumberTestForLoop();
    }
}
