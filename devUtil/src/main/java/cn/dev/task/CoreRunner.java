package cn.dev.task;

import java.util.concurrent.ExecutorService;

public class CoreRunner {


    protected static  <T> TaskResult<T>  execute(ExecutorService executorService ,ITaskFunction taskFunction){
        executorService.execute(()->{
            taskFunction.run();
        });

        return null;
    }

}
