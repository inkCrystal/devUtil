package cn.dev.commons.string;

/**
 * @author : JasonMao
 * @version : 1.0
 * @date : 2023/3/15 9:52
 * @description : TODO
 */
public interface ZhCnStrUtil {

    boolean ZhCnContainsChinese(String wd);






    /**
     * 获取 完整的 拼音 字符串，携带音调的
     * @param w
     * @return
     */
    String ZhFullPinyinWithTone(String w);

    String ZhFullPinyinWithoutTone(String w);


    String[] ZhPinFulPinyinArray(String wd);

    String getShortPinyin(String str);




}
