package cn.dev.commons.log;

import cn.dev.core.parallel.ThreadUtil;

public class DLog {

    private static final int TRACE =1 ,DEBUG =2 , INFO =3 , WARN =4 ,ERROR = 5;

    public static void trace(Object message){
        print(TRACE,message);
    }

    public static void debug(Object message){
        print(DEBUG,message);
    }


    public static void info(Object message){
        print(INFO,message);
    }

    public static void warn(Object message){
        print(WARN,message);
    }

    public static void error(Object message){
        print(ERROR,message);
    }


    /**
     * 继承到 其他 框架时候，可以 考虑这边 改造 兼容到其他日志系统
     * @param level
     * @param message
     */
    private static void print(int level ,Object message){
        defaultPrint(level,message);
    }

    private static void defaultPrint(int level , Object message){
        StringBuilder sb =new StringBuilder("[");
        sb.append(ThreadUtil.threadName());
        while (sb.length() < 12) {
            sb.insert(1," ");
        }
        sb.append("]");


        System.out.print(sb.toString());
        switch (level){
            case TRACE -> System.out.print("TRACE : ");
            case DEBUG -> System.out.print("DEBUG : ");
            case INFO -> System.out.print("INFO : ");
            case WARN -> System.out.print("WARN : ");
            case ERROR -> System.out.print("ERROR : ");
            default -> {
                return;
            }
        }
        System.out.println(message);

    }

    public static void main(String[] args) {
        DLog.info("你好");
    }
}
