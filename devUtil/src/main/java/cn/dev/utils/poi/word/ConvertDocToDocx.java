package cn.dev.utils.poi.word;

/**
 * @author : JasonMao
 * @version : 1.0
 * @date : 2023/3/13 17:39
 * @description :
 */
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ConvertDocToDocx {

    public static void main(String[] args) throws IOException {
        String docFilePath = "D:\\UserData\\Desktop\\智能钥匙权限审批表.doc";
        String docxFilePath = "D:\\UserData\\Desktop\\智能钥匙权限审批表.docx";

        // 读取.doc文件
        FileInputStream fis = new FileInputStream(docFilePath);
        HWPFDocument doc = new HWPFDocument(fis);

        // 转换为.docx文件
        XWPFDocument docx = new XWPFDocument();
        docx.createParagraph().createRun().setText(String.valueOf(doc.getText()));

        // 将.docx文件写入磁盘
        FileOutputStream fos = new FileOutputStream(docxFilePath);
        docx.write(fos);

        // 关闭输入输出流
        fos.close();
        fis.close();

        // 关闭HWPFDocument和XWPFDocument对象
        doc.close();
        docx.close();
    }
}
