package cn.dev.core;

public class RuntimeUtil {

    private static int availableProcessors =-1;


    /**
     * 获得当前可用的处理器数量
     * @return
     */
    public static int availableProcessors(){
        if(availableProcessors < 0){
            availableProcessors = Runtime.getRuntime().availableProcessors();
        }
        return availableProcessors;
    }


    public static long freeMemory(){
        return Runtime.getRuntime().freeMemory();
    }



    public static void main(String[] args) {
        Runtime.getRuntime().freeMemory();

    }


}
