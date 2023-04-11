package cn.dev.commons.file.test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

public class FileReadTest {

    public static final String testLockFileName ="D:\\Temps\\test\\test.txt";
    public static void main(String[] args) throws IOException {


        final SeekableByteChannel seekableByteChannel = Files.newByteChannel(Path.of(testLockFileName));

        final ByteBuffer allocate = ByteBuffer.allocate(1024);
        seekableByteChannel.read(allocate);
        System.out.println( seekableByteChannel.position());
        System.out.println(new String(allocate.array()).getBytes().length);

//
//
//        Files.lines(Path.of(testLockFileName));
//        Scanner scanner = new Scanner(System.in);
//        RandomAccessFile randomAccessFile = new RandomAccessFile(testLockFileName,"rw");
//        FileChannel channel = randomAccessFile.getChannel();
//        final FileLock lock = channel.tryLock(0,Long.MAX_VALUE, true);
//        for (int i = 0; i < 100; i++) {
//            randomAccessFile.write("123\r".getBytes());
//        }
//
//
//        CompletableFuture.runAsync(()->{
//            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(testLockFileName))) {
//                while (true) {
//                    final String line = bufferedReader.readLine();
//                    System.out.println(line);
//                    if (line == null) {
//                        break;
//                    }
//                }
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//                throw new RuntimeException(e);
//            } catch (IOException e) {
//                e.printStackTrace();
//                throw new RuntimeException(e);
//            }
//        });
//
//
//
//
//        while (scanner.hasNext()){
//            String s = scanner.nextLine();
//            if(s.equals("u")){
////                lock.release();
//                System.out.println("unlock");
//            }
//        }
    }
}
