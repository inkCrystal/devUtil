package com.dev.utils.poi.word;

import java.io.*;

import org.apache.poi.hwpf.HWPFOldDocument;
import org.apache.poi.hwpf.model.OldFfn;
import org.apache.poi.hwpf.model.OldFontTable;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

/**
 * @author : JasonMao
 * @version : 1.0
 * @date : 2023/3/13 16:38
 * @description :
 */

public class WordFontReplacer {

    /**
     * 替换Word文档中的指定字体为新的字体
     * @param srcPath 原Word文档路径
     * @param destPath 新Word文档路径
     * @param oldFontName 需要替换的字体名称
     * @param newFontName 新的字体名称
     * @throws IOException
     */
    public static void replaceFont(String srcPath, String destPath, String oldFontName, String newFontName) throws IOException {
        // 读取原Word文档
        FileInputStream fis = new FileInputStream(srcPath);
        XWPFDocument doc = new XWPFDocument(fis);

        // 获取所有段落
        for (XWPFParagraph para : doc.getParagraphs()) {
            // 替换段落中的指定字体
            replaceFontInParagraph(para, oldFontName, newFontName);
        }

        // 获取所有表格
        for (XWPFTable table : doc.getTables()) {
            // 获取表格中所有行
            for (XWPFTableRow row : table.getRows()) {
                // 获取行中所有单元格
                for (XWPFTableCell cell : row.getTableCells()) {
                    // 替换单元格中的指定字体
                    for (XWPFParagraph para : cell.getParagraphs()) {
                        replaceFontInParagraph(para, oldFontName, newFontName);
                    }
                }
            }
        }

        // 写入新Word文档
        FileOutputStream fos = new FileOutputStream(destPath);
        doc.write(fos);
        fos.close();
        fis.close();
    }

    /**
     * 在段落中替换指定字体
     * @param para 段落对象
     * @param oldFontName 需要替换的字体名称
     * @param newFontName 新的字体名称
     */
    private static void replaceFontInParagraph(XWPFParagraph para, String oldFontName, String newFontName) {
        // 获取段落中的所有文本
        for (XWPFRun run : para.getRuns()) {
            // 获取当前文本的字体名称
            String fontName = run.getFontFamily();
            // 如果字体名称与需要替换的名称相同，则进行替换
            if (fontName != null && fontName.equals(oldFontName)) {
                CTRPr rpr = run.getCTR().getRPr();
                if (rpr == null) {
                    rpr = run.getCTR().addNewRPr();
                }


//                CTFonts fonts = rpr.isSetRFonts() ? rpr.getRFonts() : rpr.addNewRFonts();
//                fonts.setAscii(newFontName);
//                fonts.setHAnsi(newFontName);
//                fonts.setCs(newFontName);
            }
        }
    }
}
