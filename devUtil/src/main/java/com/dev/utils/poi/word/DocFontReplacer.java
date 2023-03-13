package com.dev.utils.poi.word;

/**
 * @author : JasonMao
 * @version : 1.0
 * @date : 2023/3/13 16:50
 * @description :
 */
import java.awt.*;
import java.io.*;
import java.util.HashSet;
import java.util.Set;

import org.apache.poi.common.usermodel.fonts.FontInfo;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.model.Ffn;
import org.apache.poi.hwpf.model.FontTable;
import org.apache.poi.hwpf.model.PAPX;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Notes;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Section;
import org.apache.poi.ss.usermodel.FontFamily;

public class DocFontReplacer {



    public static void replaceFont(String filePath, String targetFont, String replacementFont) throws IOException {

        try (InputStream is = new FileInputStream(filePath)) {
            HWPFDocument doc = new HWPFDocument(is);
            final FontTable fontTable = doc.getFontTable();
            doc.font
            int i = 0;
            Set<String> fontSets= new HashSet<>();
            for (Ffn fontName : fontTable.getFontNames()) {
//                fontSets.add(fontName.getMainFontName());
//                fontSets.add(fontName.getAltFontName());
                System.out.println(i + " Ffn" +  ":: "
                        + fontName.get_cbFfnM1() + " 》 "
                        + fontName.getMainFontName());
                i++ ;
            }

            int ascii =1;
            System.out.println("getHeaderStoryRange");
            final Range headerStoryRange = doc.getHeaderStoryRange();
            runChange(headerStoryRange,fontSets);
            System.out.println("getEndnoteRange");
            final Range endnoteRange = doc.getEndnoteRange();
            runChange(endnoteRange,fontSets);

            System.out.println("getFootnoteRange>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            final Range footnoteRange = doc.getFootnoteRange();
            runChange(footnoteRange,fontSets);
            System.out.println("getOverallRange");
            final Range overallRange = doc.getOverallRange();
            runChange(overallRange,fontSets);
            System.out.println("getRange>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            Range range = doc.getRange();
            runChange(range,fontSets);


            try (OutputStream os = new FileOutputStream(filePath)) {
                doc.write(os);
            }
        }
    }

    private static void runChange(Range range, Set<String> fonts){

         for (int i = 0; i < range.numCharacterRuns(); i++) {
            try{
                final CharacterRun run = range.getCharacterRun(i);
                System.out.println(run.text()  + "当前 ：" +run.getFontName());
            }catch (Exception e){

            }
        }
    }

    public static void main(String[] args) throws IOException {
//        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//        for (Font allFont : ge.getAllFonts()) {
//            System.out.println(allFont.getFontName() + "   ");
//        }
        System.out.println("-----------------------------------------------------------");
        replaceFont("D:\\UserData\\Desktop\\智能钥匙权限审批表(2).doc", "宋体", "微软雅黑");
    }
//        try {
//            replaceFont("D:\\UserData\\Desktop\\智能钥匙权限审批表 - 副本.doc", "宋体", "微软雅黑");
//        }catch (Exception e){}
//
}
