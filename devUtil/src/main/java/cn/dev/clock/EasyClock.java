package cn.dev.clock;

public class EasyClock {

    private static EasyClock instance ;
    private EasyClock() {
    }

    public static EasyClock getInstance() {
        if(instance == null){
            instance = new EasyClock();
        }
        return instance;
    }

    public synchronized void start(){
        //        todo 实现 后台线程 一直run run run
    }











}
