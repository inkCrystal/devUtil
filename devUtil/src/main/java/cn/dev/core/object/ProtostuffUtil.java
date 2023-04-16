package cn.dev.core.object;


import cn.dev.commons.BinaryTool;
import cn.dev.core.model.DataRecord;
import cn.dev.core.model.Identity;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Date :2021/4/23
 * @Name :ProtostuffUtil
 * @Description : 请输入
 */
public class ProtostuffUtil {
    private static Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<Class<?>, Schema<?>>();

    private static <T> Schema<T> getSchema(Class<T> clazz) {
        @SuppressWarnings("unchecked")
        Schema<T> schema = (Schema<T>) cachedSchema.get(clazz);
        if (schema == null) {
            schema = RuntimeSchema.getSchema(clazz);
            if (schema != null) {
                cachedSchema.put(clazz, schema);
            }
        }
        return schema;
    }

    /**
     * 将对象序列化
     * @param obj 对象
     * @return
     */
    public static <T> byte[] serializer(T obj) throws Exception{
        @SuppressWarnings("unchecked")
        Class<T> clazz = (Class<T>) obj.getClass();
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            Schema<T> schema = getSchema(clazz);
            return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        } catch (Exception e) {
    //            throw new IllegalStateException(e.getMessage(), e);
            throw e;
        } finally {
            buffer.clear();
        }
    }

    /**
     * 将字节数组数据反序列化
     * @param data 字节数组
     * @param clazz 对象
     * @return
     */
    public static <T> T deserializer(byte[] data, Class<T> clazz) throws Exception {
        try {
            T obj = clazz.newInstance();
            Schema<T> schema = getSchema(clazz);
            ProtostuffIOUtil.mergeFrom(data, obj, schema);
            return obj;
        } catch (Exception e) {
    //            throw new IllegalStateException(e.getMessage(), e);
            throw e ;
        }
    }

    public static void printHexString(String s, byte[] b)
{
 System.out.print(s);
 for (int i = 0; i < b.length; i++)
{
 String hex = Integer.toHexString(b[i] & 0xFF);
 if (hex.length() == 1)
{
hex = '0' + hex;
 }
System.out.print(hex.toUpperCase() + " ");

 }

}


    public static void main(String[] args) throws Exception {
        final String s = """
                00000000000
                0000000000000
                0000000000
                首页
                下载APP
                会员
                IT技术
                搜索
                输出16进制byte数据的java实现
                                
                未丑
                输出16进制byte数据的java实现
                                
                未丑
                IP属地: 上海
                2018.06.05 18:22:20
                字数 86
                阅读 1,696
                在byte数据流的应用场景中，很多时候需要输出log，以便查看是否正确。
                                
                可以使用以下方法实现：
                                
                public void printHexString(String s, byte[] b)
                                
                 {
                                
                        System.out.print(s);
                                
                        for (int i = 0; i < b.length; i++)
                                
                        {
                                
                            String hex = Integer.toHexString(b[i] & 0xFF);
                                
                            if (hex.length() == 1)
                                
                            {
                                
                                hex = '0' + hex;
                                
                            }
                                
                            System.out.print(hex.toUpperCase() + " ");
                                
                        }
                                
                 }
                                
                更多精彩内容，就在简书APP
                                
                "小礼物走一走，来简书关注我"
                还没有人赞赏，支持一下
                 \s
                未丑
                总资产1共写了7161字获得16个赞共2个粉丝
                ﻿宿命帝王心术
                正文 梦。 满是杀戮的梦，血腥，悲惨。 她在这梦境中大汗淋漓地惊醒，久久不能自己，可却无可奈何，这是她每个夜晚都会...
                                
                茶点故事
                阅读 2399
                评论 1
                赞 3
                ﻿古风故事｜万般相思赋予谁\s
                正文 飘荡这世间好些年，我已经不记得自己是谁了。 只因初入这座山时遍体鳞伤，旁人问我叫什么，怎么了，我也只是喃喃的...
                                
                茶点故事
                阅读 1189
                评论 0
                赞 1
                全校都在舔那个绿茶妹，没人知道我才是隐藏大佬
                求婚当天，戒指还没戴上手。 相恋三年的男友抛下我去给那个崴脚的绿茶送温暖。 他解释说因为对方是宋氏千金，能给他带来...
                                
                茶点故事
                阅读 12340大的考点卡iqwe-qed-ajca 倒就倒u单的残骸我切 西安可惜皮球
                随机产生一段文字
                请给我随机编写5000字的文字 生成
                
                
                
                
                评论 0 @@231312大大第哦啊多啊u大带哦都熬iu都 恶趣味二u确认去哈觉得怕了大量的骄傲了解到出卡片从拍哦破i到批发i破覅
                赞 3
                ﻿我遇到校园霸凌的那些日子
                正文 全力反抗校园霸凌！三年前我遇到霸凌不堪受辱，闺蜜加入了我的战队，她还真是帮了我个大忙！从初中就开始在霸凌的雷...
                                
                茶点故事
                阅读 1133
                评论 0
                赞 2
                ﻿人贩子窝点藏在我家
                正文 镇子上出现了人贩子，我义愤填膺的和老公说起这事时，他却表现得心不在焉。但后面陆续出现了小孩的哭声、来路不明的...
                                
                茶点故事
                阅读 1503
                评论 0
                赞 3
                我在陵园发现了男友给我立的墓碑，还有闺蜜、同学，甚至他自己
                我的男朋友很帅很帅，温柔体贴，但是他和我闺蜜上了床。 还在陵园给我立了一个碑。 恐怖的是，我男朋友自己的墓碑就在我...
                                
                茶点故事
                阅读 11374
                评论 1
                赞 6
                ﻿港岛之恋（遗憾婚礼）
                正文 为了忘掉前任，我火速办了婚礼，结果婚礼上，老公的妹妹穿的比我还像新娘。我一直安慰自己，他们只是感情好，可当我...
                                
                茶点故事
                阅读 983
                评论 0
                赞 0
                ﻿死亡面前的那三分钟
                正文 全体准备，倒计时三分钟！郭远飞，请确认！” 收到！” 郭大年对着对讲机喊了一声之后，突然想起来，自己还没有按...
                                
                茶点故事
                阅读 909
                评论 0
                赞 0
                ﻿这桩情杀背后的隐秘
                正文 阿凡打电话说她家里要么有鬼，要么有贼，到了晚上，卫生间一直有嘀嗒的水声，还听到有人跛着拖鞋在客厅里走来走去，...
                                
                茶点故事
                阅读 640
                评论 0
                赞 1
                女儿满月那天，老公说他找到真爱了，让我放过他
                我跟我前夫在商场里面给小孩买衣服，他现在的老婆不停地给他打电话。 前夫没办法，只能撒谎说自己在公司开会。 对方听清...
                                
                茶点故事
                阅读 11884
                评论 0
                赞 6
                ﻿护林员之死
                正文 独居荒郊野岭守林人离奇死亡，尸身上长有42处带血的脓包…… 初始之章·张勋 以下内容为张勋视角 年9月15日...
                                
                茶点故事
                阅读 659
                评论 0
                赞 0
                ﻿她不知道的九十九次
                正文 穿越到陌生朝代的万万，发现和自己成婚的太子竟然是自己现代的未婚夫蒋书培，在婚礼上她抛下蒋书培离开，没想到会在...
                                
                茶点故事
                阅读 773
                评论 0
                赞 0
                ﻿白月光启示录
                正文 我和宋清朗相恋三年，在试婚纱的时候发现自己被绿了。 大学时的朋友给我发了我未婚夫和他白月光在一起吃饭的照片。...
                                
                茶点故事
                阅读 882
                评论 0
                赞 0
                ﻿东方御灵
                正文 雪。 悄寂无声的夜，只有那窗外轻摇而落下的雪，园中梅花零零星星，在昏暗中，略显孤寂。 她无声地叹了口气，冰凉...
                                
                茶点故事
                阅读 526
                评论 0
                赞 0
                我娘被祖母用百媚生算计，被迫无奈找清倌解决，我爹全程陪同
                人人都说尚书府的草包嫡子修了几辈子的福气，才能尚了最受宠的昭宁公主。 只可惜公主虽容貌倾城，却性情淡漠，不敬公婆，...
                                
                茶点故事
                阅读 4294
                评论 1
                赞 6
                ﻿人间的恶魔
                正文 年9月1日，南京，一份《专报》材料放到了江苏省几位主要领导的案头。 专报》的标题是：《宿豫县发现重大拐骗少女...
                                
                茶点故事
                阅读 833
                评论 0
                赞 7
                ﻿日本核电站爆炸内幕
                正文 年R本政府宣布，位于F岛的核电站，受9级特大地震影响，放射性物质发生泄漏。R本人自食恶果不足惜，却给世界环境...
                                
                茶点故事
                阅读 592
                评论 0
                赞 0
                ﻿姗姗来迟
                正文 玩儿够了就从老子身上下去。」时峥说这话的时候，我正趴在他身上咬着他的耳朵。 他的女朋友孙雪漫站在他卧室门口看...
                                
                茶点故事
                阅读 1092
                评论 0
                赞 0
                ﻿男朋友约到阳性女，导致我全家被隔离
                正文 看着打印出来的流调，我气的手都在发抖。 张子峰一周接触了4个炮友，还不忘记周五去我家和我爸妈一起吃晚饭！ 多...
                                
                茶点故事
                阅读 1053
                评论 1
                赞 2
                代替公主和亲
                正文 我出身青楼，却偏偏与公主长得像，于是被迫代替她去往敌国和亲。 传闻我的和亲对象是个残疾皇子，可洞房花烛夜当晚...
                                
                茶点故事
                阅读 889
                评论 0
                赞 2
                写下你的评论...
                全部评论
                0
                推荐阅读
                更多精彩内容
                JAVA经典算法50题
                【程序1】 题目：古典问题：有一对兔子，从出生后第3个月起每个月都生一对兔子，小兔子长到第三个月后每个月又生一...
                                
                阿里高级软件架构师
                阅读 3,161
                评论 0
                赞 19
                java中文乱码解决之道（5）：java是如何编码解码的
                在上篇博客中LZ阐述了java各个渠道转码的过程，阐述了java在运行过程中那些步骤在进行转码，在这些转码过程中如...
                                
                皓云观
                阅读 327
                评论 0
                赞 1
                50道java初级编程题
                【程序1】 题目：古典问题：有一对兔子，从出生后第3个月起每个月都生一对兔子，小兔子长到第三个月后每个月又生一对兔...
                                
                叶总韩
                阅读 4,932
                评论 0
                赞 41
                Java初级面试题
                1. Java基础部分 基础部分的顺序：基本语法，类相关的语法，内部类的语法，继承相关的语法，异常的语法，线程的语...
                12313141415815767269028-09=的dasdaihfafbadaskjcnachakjfdhakhfabncbacvxnax,kanm ndkajdklahdiqhdoq ydoihdxaqoid
              `!@#$%%^&**())__++/t\t\n\t\r\b\\x
              、、\\\\ dada //da\\dad @daskda;k~       ，，，，,,,,,dadadadad  。。。。dadad                  dsadad
              xadka;dkakd          
              DASD DADAOFDUIQURIOURQ8YR8QWYRTQRWTWQRTUIQWYRWQURPQRI[]PDCSA]CPAc::cl
              'zcc<,MCZKMXCL;ZK;V MZX NZ NJKNNKHDKLHJAdihdasd;a/cx':'";;;''LL;;;,<>???
                子非鱼_t_
                阅读 30,810
                评论 18
                赞 399
                调笑令·兰
                海棠社第67社 主题：咏兰 文/寒霜 【原创】 调笑令·兰 兰朵，兰朵，附上茎端袅娜。 佳节添香增柔，万家厅前独幽...
                                
                刘寒霜
                阅读 269
                评论 19
                赞 17
                                
                未丑
                总资产1
                Android Binder的极简使用
                阅读 3,929
                跟笨又不觉得自己笨的人合作是种什么体验
                阅读 188
                热门故事
                听到我随口哄骗的话，皇帝竟红了眼：你说你仰慕我，可是真的？
                老同学找我借钱后, 竟然把这个送上门来做“抵押”
                被骗去柬埔寨的人有多惨? 器官被明码标价
                被调去男子监狱工作的女医生有多累?
                诈骗组织多可怕？我被绑进小黑屋遭受折磨
                去医院却撞见老公全家陪别的女人产检，我淡定拿出老公的不育报告，让他们狗咬狗
                前世渣男把我迷晕还叫我别怕，转世彻底黑化的我复仇反杀
                妹妹过失杀人，警察来时，我捡起了那把滴血的刀
                我被校霸堵在巷口，却发现他是我谈了三个月的网恋对象
                我和网恋对象奔现，却发现他是我们学校赫赫有名的海王
                写下你的评论...
                """;

        final byte[] bytes = s.getBytes("UTF-8");
        for (byte aByte : bytes) {
            String hex = Integer.toHexString(aByte & 0xFF);
            if (hex.length() == 1){
                hex = '0' + hex;
            }
            System.out.print("0x"+hex.toUpperCase()  );
//            System.out.print((byte)aByte);
            System.out.print(" ");


        }
        for (int i = 0; i < 10; i++) {

            mai1n(null);
        }
    }

    public static void mai1n(String[] args) throws Exception {

        AbcISData data =new AbcISData("abc不知道什么时候才会去你妈的。哈哈哈",12300031,new String[]{"西来打击大家西吗","东大大也就一般般啦","不知道合适才会出现按你吗","我的家啊大家啊利空打击案例"}
                ,new BigDecimal(3.14),"他的父亲叫做陈海关",new Identity(),123,new Date());

        long start = System.currentTimeMillis();
        byte[] bytes =  serializer(data);
        System.out.println("protostuff 序列化耗时："+(System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        final byte[] bytes1 = ObjectUtil.serializeObject(data);
        System.out.println("object 序列化耗时："+(System.currentTimeMillis() - start));

        System.out.println(bytes1 == bytes);

        System.out.println(0x12 ==18);
        System.out.println((byte)0xFF == 255);

        for (byte aByte : bytes) {
            String hex = Integer.toHexString(aByte & 0xFF);
            if (hex.length() == 1){
                hex = '0' + hex;
            }
            System.out.print("0x"+hex.toUpperCase() + " - ");
//            System.out.print((byte)aByte);
            System.out.print(" ");
        }
        System.out.println();
        for (byte aByte : bytes1) {
            String hex = Integer.toHexString(aByte & 0xFF);
            if (hex.length() == 1){
                hex = '0' + hex;
            }
            System.out.print("0x"+hex.toUpperCase() + " - ");
//            System.out.print((byte)aByte);
            System.out.print(" ");
        }


    }
}
