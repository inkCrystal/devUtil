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

    private static void fireLister(int event ){
        try {
            for (FunctionTaskListener taskListener : listener) {
                switch (event) {
                    case START:
                        taskListener.onStart();
                        break;
                    case ERROR:
                        taskListener.onError();
                        break;
                    case COMPLETE:
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
    public static  <D,V> FunctionResult<V> apply(D d,IFunction<D,V> function){
        FunctionResult<V> r = new FunctionResult<>();
        r.fireAccept();
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
        var taskFuture = new TaskFuture();
        return execute(taskFuture,taskFunction);
    }

    protected static TaskFuture execute(TaskFuture taskFuture , ITaskFunction taskFunction){
        taskFuture.fireAccept();
        executor.execute(()->{
            fireLister(START);
            taskFuture.fireStart();
            try {
                taskFunction.execute();
                fireLister(COMPLETE);
                taskFuture.fireSuccess();
            }catch (Exception e){
                fireLister(ERROR);
                taskFuture.fireError(e);
                e.printStackTrace();
            }
        });
        return taskFuture;
    }


    public static void main(String[] args) {
        TaskFuture taskFuture = execute(()->{
            System.out.println("hello");
        }).thenExecuteIfError(()->{
            System.out.println("遇到了异常");
        });
    }




}
