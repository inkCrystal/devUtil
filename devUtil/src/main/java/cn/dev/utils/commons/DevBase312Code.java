package cn.dev.utils.commons;

import cn.dev.commons.string.StrUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author : JasonMao
 * @version : 1.0
 * @date : 2023/2/7 16:15
 * @description :
 */
public class DevBase312Code {
    final static char[] dic = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z',
            'α', 'β', 'γ', 'δ', 'ε', 'ζ', 'η', 'θ', 'ι', 'κ',
            'λ', 'μ', 'ν', 'ξ', 'ο', 'π', 'ρ', 'ς', 'σ', 'τ',
            'υ', 'φ', 'ψ', 'ω',
            '①','②','③','④','⑤','⑥','⑦','⑧','⑨','⑩',
            '⑪','⑫','⑬','⑭','⑮','⑯','⑰','⑱','⑲','⑳',
            'Ⓐ','Ⓑ','Ⓒ','Ⓓ','Ⓔ','Ⓕ','Ⓖ','Ⓗ','Ⓘ','Ⓙ',
            'Ⓚ','Ⓛ','Ⓜ','Ⓝ','Ⓞ','Ⓟ','Ⓠ','Ⓡ','Ⓢ','Ⓣ',
            'Ⓤ','Ⓥ','Ⓦ','Ⓧ','Ⓨ','Ⓩ',
            '♔','♕','♖','♗','♘','♙','♚','♛','♜','♝','♞','♟',
            '♠','♡','♢','♣','♤','♥','♦','♧','♨','♩','♪','♫','♬',
            '♭','♮','♯','⚐','⚑','⚒','⚓','⚔','⚕','⚖','⚗','⚘','⚙',
            '⚚','⚛','⚜','⚝','⚞','⚟','⚠','⚡','⚢','⚣','⚤','⚥','⚦',
            '⚧','⚨','⚩',
            'Ⲉ','ⲉ','Ⲋ','ⲋ','Ⲯ','ⲯ','Ⲱ','ⲱ','Ⲳ','ⲳ','Ⲵ','ⲵ','Ⲷ','ⲷ','Ⲹ','ⲹ',
            '⨶','⨷','⨸','⨹','⨺','⨻','⨼','⨽','⨾','⨿',
            '⩀','⩁','⩂','⩃','⩄','⩅','⩆','⩇','⩈','⩉','⩊','⩋',
            '⩌','⩍','⩎','⩏','⩐','⩑','⩒','⩓','⩔','⩕','⩖','⩗',
            '⩘','⩙','⩚','⩛','⩜','⩝','⩞','⩟','⩠','⩡','⩢','⩣',
            '⩤','⩥','⩦','⩧','⩨','⩩','⩪','⩫','⩬','⩭','⩮','⩯',
            '⩰','⩱','⩲','⩳',
            '⬟','⬠','⬡','⬢','⬣','⬤','⬥','⬦','⬧','⬨',
            '⬪','⬫','⬬','⬭','⬮','⬯','⮈','⮉','⮊','⮋',
            '⮌','⮍','⮎','⮏','⮐','⮑','⮒','⮓','⮔',
            'Ⰲ','Ⰳ','Ⰴ','Ⰵ','Ⰶ','Ⰷ','Ⰸ','Ⰹ','Ⰺ','Ⰻ','Ⰼ','Ⰽ','Ⰾ','Ⰿ',
            'Ⱀ','Ⱁ','Ⱂ','Ⱃ','Ⱄ','Ⱅ','Ⱆ','Ⱇ','Ⱈ','Ⱉ','Ⱊ','Ⱋ','Ⱌ','Ⱍ','Ⱎ',
            'Ⱏ','Ⱐ','Ⱑ','Ⱒ','Ⱓ','Ⱔ','Ⱕ','Ⱖ','Ⱗ','Ⱘ','Ⱙ','Ⱚ','Ⱛ','Ⱜ','Ⱝ','Ⱞ'

    };
    final static int base = dic.length;

    private static final int indexOf(char c){
        for (int i = 0 ; i < dic.length;i++){
            if(dic[i] == c){
                return i;
            }
        }
        return -1;
    }


    /**
     * 编码
     * @param source
     * @return
     */
    public static final String encodeString(String source){
        StringBuilder sb =new StringBuilder();
        for (char c : source.toCharArray()) {
            sb.append( encodeChar(c,2));
        }

        return sb.toString();
    }

    /**
     * 解码
     * @param encodeString
     * @return
     */
    public static final String decodeString(String encodeString){
        StringBuilder tempStr = new StringBuilder();
        StringBuilder decode =new StringBuilder();
        char[] chars = encodeString.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            tempStr.append(chars[i]);
            if(tempStr.length() ==2){
                decode.append(decodeChar(tempStr.toString()));
                tempStr = new StringBuilder();
            }
        }
        return decode.toString();
    }


    /**
     * 单字符编码
     * @param c
     * @param requiredLen
     * @return
     */
    private static final String encodeChar(char c , int requiredLen){
        int n = c;
        StringBuilder s = new StringBuilder();
        while (n >= base){
            int t = n % base;
            s.insert(0,dic[t]);
            n = n /base;
        }
        s.insert(0,dic[n]);
        while (s.length()<requiredLen){
            s.insert(0,dic[0]);
        }
        return s.toString();
    }

    /**
     * 解码
     * @param code
     * @return
     */
    private static final char decodeChar(String code){
        char[] chars = code.toCharArray();
        int source = 0;
        int pos =0;
        for (int i = chars.length - 1; i >= 0; i--) {
            int index = indexOf(chars[i]);
            int tpos = pos;
            while (tpos>0){
                tpos--;
                index*=base;
            }
            source+= index;
            pos++;
        }
        return (char) source;
    }

    public static String parallelEncode(String str ){
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {

        }
        return null;

    }



}
