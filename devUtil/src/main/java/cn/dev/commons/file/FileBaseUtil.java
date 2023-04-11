package cn.dev.commons.file;

import cn.dev.commons.RandomUtil;

import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Stack;

public class FileBaseUtil {

    public static final File getOrInitFile(String path) throws IOException {
        File file =new File(path);
        if(!file.exists()){
            File pathFile = file.getParentFile();
            Stack<File> files =new Stack<>();
            while (!pathFile.exists()){
                files.push(pathFile);
                pathFile = pathFile.getParentFile();
            }
            while (!files.empty()){
                files.pop().mkdir();
            }
            file.createNewFile();
        }
        return new File(path);
    }



    private static final void initFile() throws IOException {

        String path = "E:\\tessdata\\a.txt";
        int index = 0 ;
        long start = System.currentTimeMillis();
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(path,true))) {
            while (index < 1000000) {
                index++;
                fileWriter.write(RandomUtil.randomStr(1024));
                fileWriter.newLine();
                if (index % 100 == 0) {
                    fileWriter.flush();
                }
                if(index % 5000 == 0){
                    System.out.println(index + " has write  cost : " + (System.currentTimeMillis()-start ));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("finish ");
    }

    public static final long fileLength(String path) {
        try (FileInputStream fis = new FileInputStream(path)) {
            return fis.getChannel().size();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) throws IOException {
//        props.getProperty("line.separator");
        final byte[] bytes = System.getProperty("line.separator").getBytes();
        System.out.println(Arrays.toString(bytes));
        final File file = getOrInitFile("E:\\tessdata\\x\\dada\\daa2\\xa\\d\\x\\12\\a.txt");
        FileWriter fileWriter = new FileWriter(file);
        BufferedWriter writer =new BufferedWriter(fileWriter);
        writer.newLine();

        new BufferedReader(new FileReader("E:\\tessdata\\b.txt")).readLine();

        FileInputStream inputStream = new FileInputStream("E:\\tessdata\\b.txt");
        final FileChannel channel = inputStream.getChannel();

        final long size = channel.size();
        System.out.println(size);
        channel.position(4);
        System.out.println("do read ");
        Reader reader = Channels.newReader(channel, "UTF-8");
        try (BufferedReader bufferedReader = new BufferedReader(reader)) {
            String line = bufferedReader.readLine();
            System.out.println(line);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



    }




}
