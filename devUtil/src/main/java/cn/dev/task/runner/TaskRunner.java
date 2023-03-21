package cn.dev.task.runner;

import cn.dev.task.TaskResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Predicate;

public class TaskRunner {
    private static final int START = 1 ;
    private static final int ERROR = 2;
    private static final  int COMPLETE =3 ;
    private static final List<FunctionTaskListener> listener =new ArrayList<>();

    private static final ExecutorService executor = Executors.newFixedThreadPool(8);

    private static void fireLister(int event){
        try {
            for (FunctionTaskListener taskListener : listener) {
                switch (event) {
                    case 1:
                        taskListener.onStart();
                        break;
                    case 2:
                        taskListener.onError();
                        break;
                    case 3:
                        taskListener.onComplete();
                        break;
                    default:
                        break;
                }
            }
        }catch (Exception e){
        }
    }



    /**
     * 执行 FUNCTION
     * @param d
     * @param function
     * @return
     * @param <D>
     * @param <V>
     */
    public static  <D,V> FunctionResult<V> execute(D d,IFunction<D,V> function){
        FunctionResult<V> r = new FunctionResult<>();
        executor.execute(()->{
            fireLister(START);
            try {
                r.fireStart();
                V v = function.apply(d);
                fireLister(COMPLETE);
                r.setResult(v, FunctionState.SUCCESS);
                r.fireSuccess();
            }catch (Exception e){
                r.fireError(e);
                fireLister(ERROR);
            }
        });

        return r;
    }

    public static TaskFuture execute(ITaskFunction taskFunction){
        var r = new TaskFuture();
        r.fireAccept();
        executor.execute(()->{
            fireLister(START);
            r.fireStart();
            try {
                taskFunction.execute();
                fireLister(COMPLETE);
                r.fireSuccess();
            }catch (Exception e){
                fireLister(ERROR);
                r.fireError(e);
            }
        });
        return r;
    }




}
