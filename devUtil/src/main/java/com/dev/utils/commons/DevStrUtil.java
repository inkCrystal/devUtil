package com.dev.utils.commons;

import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import org.apache.commons.codec.binary.Hex;

import javax.swing.plaf.PanelUI;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author : JasonMao
 * @version : 1.0
 * @date : 2023/3/13 16:29
 * @description :
 */
public class DevStrUtil {
    public static final String EMPTY ="";


    /**判断是否不为空  ---by JasonMao @ 2023/3/13 16:34 */
    public static final boolean isNotEmpty(String s ){
        return false == isEmpty(s) ;
    }
    
    /**判断是否是空的  ---by JasonMao @ 2023/3/13 16:33 */
    public static final boolean isEmpty(String s){
        return (s==null || s.isEmpty());
    }

    public static final String valueOfBytes(byte[] bytes){
        return new String(bytes);
    }

    public static final String valueOfBytes(byte[] bytes ,String charsetName) throws UnsupportedEncodingException {
        return new String(bytes,charsetName);
    }

    public String encodeBase64(String source){
        return Base64.getEncoder().encodeToString(source.getBytes());
    }
    public String decodeBase64(String source){
        return valueOfBytes(Base64.getDecoder().decode(source));
    }
    public static String encryptMd5(String source) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(source.getBytes(Charset.forName("UTF8")));
            final byte[] resultByte = messageDigest.digest();
            String result = Hex.encodeHexString(resultByte);
            return result;
        }catch (Exception e){}
        return EMPTY;
    }



    public static boolean containsChinese(String str) {
        String regEx = "[\\u4e00-\\u9fa5]";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }

    public static class PinYin{
        public static String getShortPinyin(String str) {
            try {
                return PinyinHelper.getShortPinyin(str);
            } catch (PinyinException e) {
            }
            return EMPTY;
        }

        public static final String getFullPinyinWithoutTone(String w){
            try{
                return PinyinHelper.convertToPinyinString(w,"",PinyinFormat.WITHOUT_TONE);
            } catch (PinyinException e) {
            }
            return EMPTY;
        }

        public static final String[] getFulPinyinArray(String wd){
            try {
                var convert = PinyinHelper.convertToPinyinString(wd, "#", PinyinFormat.WITHOUT_TONE);
                return convert.split("#");
            }catch (Exception e){}
            return new String[]{};

        }
    }


    public static class Encoder{

    }
    public static class Decoder{



    }



    public static void main(String[] args) throws PinyinException {


        System.out.println(PinyinHelper.getShortPinyin("重庆人民也很欢饮茜茜"));
        final String source = "单先生说：“重庆人民的体重都很重,余茜茜也认同这个观点。单凭这点，我们就可以庆祝了！”";
        System.out.println(source);
        System.out.println(PinyinHelper.convertToPinyinString(source + "！"," ", PinyinFormat.WITH_TONE_MARK));
        System.out.println(PinyinHelper.convertToPinyinString(source + "！"," ", PinyinFormat.WITH_TONE_NUMBER));

        System.out.println(PinyinHelper.convertToPinyinString(source + "！"," ", PinyinFormat.WITHOUT_TONE));
        System.out.println(containsChinese("x啊"));
    }







}
