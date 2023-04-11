package cn.dev.commons.file.reader;

import cn.dev.commons.file.BufferedRandomAccessFile;

import java.io.*;
import java.util.concurrent.atomic.AtomicInteger;

public class FileReadTest {

    public static final String testFilePath = "E:\\tessdata\\a.txt";
    public FileReadTest() throws FileNotFoundException {
    }

    public static void bufferReadTestStream() throws IOException {
        System.out.println("bufferReadTestStream");
        long start = System.currentTimeMillis();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(testFilePath));
        final long count = bufferedReader.lines().parallel().filter(t -> t.contains("J0>AC")).distinct().count();
        System.out.println(count);
        bufferedReader.close();
        System.out.println(System.currentTimeMillis() - start);

    }


    public static void bufferReadTestForLoop() throws IOException {
        System.out.println("bufferReadTestForLoop");
        long start = System.currentTimeMillis();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(testFilePath));
        AtomicInteger integer = new AtomicInteger(0);
        while (true){
            String s = bufferedReader.readLine();
            if(s==null) break;
            if(s.contains("J0>AC")) {
                integer.incrementAndGet();

            }
        }
        bufferedReader.close();
        System.out.println(integer.get() + " read ") ;
        System.out.println(System.currentTimeMillis() - start);

    }

    public static void lineReadTestStream() throws IOException {
        System.out.println("lineReadTestStream");
        long start = System.currentTimeMillis();
        LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(testFilePath));
        final long count = lineNumberReader.lines().filter(t -> t.contains("J0>AC")).distinct().count();
        System.out.println(count + " of " + lineNumberReader.getLineNumber() );
        lineNumberReader.close();
        System.out.println(System.currentTimeMillis() - start);

    }

    public static void lineNumberTestForLoop() throws IOException{
        System.out.println("lineNumberTestForLoop");
        long start = System.currentTimeMillis();
        LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(testFilePath));
        AtomicInteger integer = new AtomicInteger(0);
        while (true){
            String s = lineNumberReader.readLine();
            if(s==null) break;
            if(s.contains("DAMAOhhhhhhkkk")) {
                System.out.println("从 + " + lineNumberReader.getLineNumber() + " 读取到");
                integer.incrementAndGet();
            }
        }

        System.out.println(integer.get() +  " read  "  );
        lineNumberReader.close();

        System.out.println(System.currentTimeMillis() - start);

    }




    public static void randomAccessTest() throws IOException {
        System.out.println("randomAccessTest");
        long start = System.currentTimeMillis();

        RandomAccessFile randomAccessFile = new RandomAccessFile(testFilePath, "rw");

        randomAccessFile.seek(0);
        AtomicInteger integer = new AtomicInteger(0);
        while (true){
            String s = randomAccessFile.readLine();
            if(s==null) break;
            if(s.contains("J0>AC")) {
                integer.incrementAndGet();
            }
        }
        System.out.println(integer.get() + " read ");
        randomAccessFile.close();

        System.out.println(System.currentTimeMillis() - start);
    }

    public static void bufferedRandomAccessFileTest() throws IOException {
        System.out.println("bufferedRandomAccessFileTest");
        long start = System.currentTimeMillis();

        BufferedRandomAccessFile randomAccessFile = new BufferedRandomAccessFile(testFilePath, "rw");
        randomAccessFile.seek(1999999999L);
        AtomicInteger integer = new AtomicInteger(0);
        while (true){
            String s = randomAccessFile.readLine();
            if(s == null){
                break;
            }
//            integer.incrementAndGet();
            if(
                    s.contains("J0>AC")
            ) {

                System.out.println(s);
                System.out.println("读取到 " + s + "在 "+integer.get() +"行 ");
               /* randomAccessFile.write("DAMAOhhhhhhkkk".getBytes());
                randomAccessFile.write(System.getProperty("line.separator").getBytes());*/
            }

        }
        System.out.println(integer.get() + " read ");
        randomAccessFile.close();

        System.out.println(System.currentTimeMillis() - start);
    }


    public static void main(String[] args) throws IOException {
//        long pos = 0;
//        try (LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(testFilePath))) {
//            while (true){
//                String s = lineNumberReader.readLine();
//                System.out.println(s );
//                if(s==null) break;
//                pos += s.getBytes().length;
//                if(s.equals("3")){
//                    break;
//                }
//            }
//        }
//        System.out.println(pos + " is pos ");
//        try (RandomAccessFile lineNumberReader = new RandomAccessFile(testFilePath,"rw")) {
//            while (true){
//                String s = lineNumberReader.readLine();
//                System.out.println(s.getBytes().length);
//                if(s==null) break;
//
//                if(s.equals("33")){
//                    System.out.println(lineNumberReader.getFilePointer() +" ---- ");
//                }
//            }
//        }
//
//        System.out.println(pos);
//        RandomAccessFile randomAccessFile =new RandomAccessFile(testFilePath, "rw");
//        randomAccessFile.seek(pos);
//        randomAccessFile.write((System.getProperty("line.separator")).getBytes());
//        randomAccessFile.write("hello 中国".getBytes());
//        randomAccessFile.write((System.getProperty("line.separator")).getBytes());
//        randomAccessFile.close();
//


//
//        BufferedRandomAccessFile randomAccessFile = new BufferedRandomAccessFile(testFilePath, "rw");
//        System.out.println( randomAccessFile.readLine());
//        System.out.println("good");
//        randomAccessFile.close();
        bufferedRandomAccessFileTest();
        System.out.println("=====================================");
        bufferReadTestStream();
        System.out.println("=====================================");
        bufferReadTestForLoop();
        System.out.println("=====================================");
        lineReadTestStream();
        System.out.println("=====================================");
        lineNumberTestForLoop();
        System.out.println("=====================================");

    }
}
