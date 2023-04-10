package cn.dev.commons.file;

import cn.dev.core.parallel.task.runner.TaskExecutor;

import java.io.*;
import java.util.stream.Stream;
import java.util.zip.InflaterInputStream;

public class FileRead {

    public void writeFileToEnd(File file,Stream<String> stringStream){
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file,true))) {
            stringStream.forEach(t->{
                try {
                    if(t.equals("end")) return;
                    System.out.println("write " + t);
                    bufferedWriter.write(t);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("beyu");
    }





    //将文件按行读取成流
    public Stream<String> readTextFileForStream(File file) throws FileNotFoundException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        return bufferedReader.lines();
    }

    public Stream<String> readTextFileForStream(String filePath) throws FileNotFoundException {
        return readTextFileForStream(new File(filePath));
    }

    public static void main(String[] args) throws IOException {
        System.out.println("xxx ");
           BufferedReader bufferedReader =new BufferedReader(new InputStreamReader(System.in));
       System.out.println("start !!");
        TaskExecutor.getRunner().execute(()->{
            new FileRead().writeFileToEnd(new File("C:\\Developer\\codeSpace\\incubate\\utils\\devUtil\\devUtil\\src\\main\\resources\\test.txt"),bufferedReader.lines());
        });
       String s = "start";
       while (!s.equals("end")) {
           s = bufferedReader.readLine();
           System.out.println(s + " " + s.length() + " is read ");
       }
        bufferedReader.close();




        System.out.println("godd bye !!");
    }


}
