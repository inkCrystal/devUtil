package cn.dev.core.parallel.task.runner;

import cn.dev.core.parallel.task.api.FunctionTaskListener;

import java.util.ArrayList;
import java.util.List;

public class FunctionTaskListenerManager  {

    protected static final short START = 1 ;
    protected static final short ERROR = 2;
    protected static final  short COMPLETE =3 ;
    private static final List<FunctionTaskListener> listener =new ArrayList<>();
    private static final List<FunctionTaskListener> defaultListener = new ArrayList<>();


    protected List<FunctionTaskListener> getDefaultListener() {
        return defaultListener;
    }

    public List<FunctionTaskListener> getListener() {
        return listener;
    }


    protected void callFire(short event ){
        getDefaultListener().forEach(listener->{
            doFire(event,listener);
        });
        getListener().forEach(listener->{
            doFire(event,listener);
        });
    }


    /**
     * 该方法主要为了 防止 listener 中的方法抛出异常，进而影响 任务的 执行
     * @param event
     * @param listener
     */
    private void doFire(int event,FunctionTaskListener listener){
        if(listener==null){
            return;
        }
        switch (event) {
            case START -> {
                try {
                    listener.onStart();
                } catch (Exception e) {}
            }
            case ERROR -> {
                try {
                    listener.onError();
                } catch (Exception e) {}
            }
            case COMPLETE -> {
                try {
                    listener.onComplete();
                } catch (Exception e) {}
            }
            default -> {
                break;
            }
        }
    }
}
