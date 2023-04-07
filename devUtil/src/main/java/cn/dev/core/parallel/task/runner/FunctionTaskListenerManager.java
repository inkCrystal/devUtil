package cn.dev.core.parallel.task.runner;

import cn.dev.core.parallel.share.LocalTaskShareHelper;
import cn.dev.core.parallel.task.api.FunctionTaskListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FunctionTaskListenerManager  {

    private static final List<FunctionTaskListener> USER_DEFINED_LISTENER =new ArrayList<>();
    protected static final short START = 1 ;
    protected static final short ERROR = 2;
    protected static final  short COMPLETE =3 ;
    private FunctionTaskListenerManager() {
    }

    protected static FunctionTaskListenerManager getManager(){
        return new FunctionTaskListenerManager();

    }

    public List<FunctionTaskListener> getUserDefinedTaskListener() {
        return USER_DEFINED_LISTENER;
    }

    protected void callFire(short event ){
        //执行内置的 listener，一般是动态的
        callBuiltInListener(event);
        // 执行用户设置的 listener
        getUserDefinedTaskListener().forEach(listener->{
            doFire(event,listener);
        });
    }


    /**
     * 这边 执行 内置的 listener，优先级最高
     * @param event
     */
    private void callBuiltInListener(short event){
        // 线程共享的 taskListener
        tryOptionalFire(event,LocalTaskShareHelper.getTaskListenerOptional());

    }

    private void tryOptionalFire(short event,Optional<FunctionTaskListener> optional){
        if (optional.isPresent()) {
            doFire(event,optional.get());
        }
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
