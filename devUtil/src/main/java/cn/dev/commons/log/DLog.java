package cn.dev.commons.log;

import cn.dev.core.parallel.ThreadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DLog {

    static Logger logger = LoggerFactory.getLogger(DLog.class);

    private static final int TRACE =1 ,DEBUG =2 , INFO =3 , WARN =4 ,ERROR = 5;

    private static int CONFIG_LEVEL = INFO;

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
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        defaultPrint(level,stackTraceElements,message);
    }

    private static void defaultPrint(int level ,StackTraceElement[] stackTraceElements , Object message){
        StringBuilder sb =new StringBuilder("[");
        switch (level){
            case TRACE  -> sb.append("TRACE:");
            case DEBUG  -> sb.append("DEBUG:");
            case INFO   -> sb.append("INFO :");
            case WARN   -> sb.append("WARN :");
            case ERROR  -> sb.append("ERROR:");
            default -> {
                return;
            }
        }
        int offSet = sb.length();
        sb.append(ThreadUtil.threadName());
        while (sb.length() < 23) {
            sb.insert( offSet,".");
        }
        sb.append("] ");
        if(stackTraceElements.length>=3) {
            String className = fixClassName(stackTraceElements[3].getClassName());
            sb.append(className).append("->").append(stackTraceElements[3].getMethodName()).append(" ")
                    .append("(").append(stackTraceElements[3].getLineNumber()).append(") ::");
        }else{
            sb.append(" ::");
        }


        sb.append(" ").append(message);
//        for (StackTraceElement stackTraceElement : stackTraceElements) {
//            sb.append("\n\t ").append(stackTraceElement.toString());
//        }

        System.out.println(sb.toString());
    }

    private static String fixClassName(String className){
        final String[] split = className.split("\\.");
        if(split.length<3){
            return className;
        }else{
            return split[0] +"..." + split[split.length-2]+"."+split[split.length-1];

        }
    }

    public static final boolean isTraceEnabled() {
        return isEnabled(TRACE);
    }

    public static final boolean isDebugEnabled() {
        return isEnabled(DEBUG);
    }

    public static final boolean isInfoEnabled() {
        return isEnabled(INFO);
    }

    public static final boolean isWarnEnabled() {
        return isEnabled(WARN);
    }

    public static final boolean isErrorEnabled() {
        return isEnabled(ERROR);
    }

    private static boolean isEnabled(int level){
        return level >= CONFIG_LEVEL;
    }


}
