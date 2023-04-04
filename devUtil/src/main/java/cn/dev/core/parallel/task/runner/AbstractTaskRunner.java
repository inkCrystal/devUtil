package cn.dev.core.parallel.task.runner;

import cn.dev.core.parallel.share.IThreadShareFilter;
import cn.dev.core.parallel.share.TaskShareData;
import cn.dev.core.parallel.task.api.IFunction;
import cn.dev.core.parallel.task.api.runner.ITaskRunner;
import cn.dev.core.parallel.task.api.FunctionTaskListener;
import cn.dev.core.parallel.task.api.ITaskFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 抽象任务 Runner
 */
public class AbstractTaskRunner implements  IRunnerCallbackHelper {
    private static final int START = 1 ;
    private static final int ERROR = 2;
    private static final  int COMPLETE =3 ;
    private static final List<FunctionTaskListener> listener =new ArrayList<>();
    private static final ExecutorService executor = Executors.newFixedThreadPool(8);


    final void fireLister(int event ){
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



    @Override
    public <D, V> FunctionResult<V> apply(D d, IFunction<D, V> function) {
        FunctionResult<V> r = new FunctionResult<>(this);
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

    @Override
    public TaskFuture execute(ITaskFunction taskFunction) {
        TaskFuture taskFuture = new TaskFuture(this);
        taskFuture.fireAccept();
        executor.execute(()->{
            fireLister(START);
            try {
                taskFuture.fireStart();
                taskFunction.execute();
                fireLister(COMPLETE);
                taskFuture.fireSuccess();
            }catch (Exception e){
                taskFuture.fireError(e);
                fireLister(ERROR);
            }
        });
        return taskFuture;
    }

    @Override
    public TaskFuture execute(TaskFuture taskFuture, ITaskFunction taskFunction) {
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


}

/**
 * 一个抽象的接口 用于执行后续的任务
 */
interface IRunnerCallbackHelper extends ITaskRunner {
    TaskFuture execute(TaskFuture taskFuture , ITaskFunction taskFunction);
}
