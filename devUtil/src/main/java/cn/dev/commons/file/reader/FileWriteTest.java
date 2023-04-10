package cn.dev.commons.file.reader;

import cn.dev.commons.RandomUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

public class FileWriteTest {

    private File file =new File("C:\\Developer\\codeSpace\\incubate\\utils\\devUtil\\devUtil\\src\\main\\resources\\test.txt");

    public static void main(String[] args) {

        File file =new File("C:\\Developer\\codeSpace\\incubate\\utils\\devUtil\\devUtil\\src\\main\\resources\\test.txt");
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file,true))) {
            for (int i = 0; i < 3000*1000; i++) {
                String txt =(i +1) + " "+(RandomUtil.randomStr(12))+ " "+ LocalDate.now() +" "+"true" ;
                for (int j = 0; j < 5; j++) {
                    txt += " J"+j+">"+(RandomUtil.randomStr(36));
                }
                txt += " "+(RandomUtil.randomStr(36));
                bufferedWriter.write(txt);
                bufferedWriter.newLine();
                if(i%100==0){
                    bufferedWriter.flush();
                }
            }
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);

        }
        System.out.println("beyu");
    }
}
