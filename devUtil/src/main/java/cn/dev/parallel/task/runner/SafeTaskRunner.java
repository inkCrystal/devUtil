package cn.dev.parallel.task.runner;

import cn.dev.parallel.task.api.IFunction;
import cn.dev.parallel.task.api.ITaskFunction;
import cn.dev.parallel.task.api.runner.ITaskRunner;

public class SafeTaskRunner implements ITaskRunner {
    @Override
    public <D, V> FunctionResult<V> apply(D d, IFunction<D, V> function) {
        return (FunctionResult<V>) safeCall(()->{
            return TaskRunTool.apply(d, function);
        });
    }

    @Override
    public TaskFuture execute(ITaskFunction taskFunction) {
        return (TaskFuture) safeCall(()->{
            return TaskRunTool.execute(taskFunction);
        });
    }

    public TaskFuture execute(TaskFuture taskFuture, ITaskFunction taskFunction) {
        return (TaskFuture) safeCall(()->{
            return TaskRunTool.execute(taskFuture,taskFunction);
        });
    }


    private  <T> Object safeCall(Fn function){
        try(CommonThreadLocalAccessor localAccessor=new CommonThreadLocalAccessor()){
            return function.call();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    interface Fn{
        Object call();
    }
}

