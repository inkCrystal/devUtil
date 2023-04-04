package cn.dev.core.parallel.task.runner;

import cn.dev.commons.verification.VerificationTool;
import cn.dev.core.parallel.share.IThreadShareFilter;
import cn.dev.core.parallel.task.api.IFunction;
import cn.dev.core.parallel.task.api.ITaskFunction;

public class SharedTaskRunner extends AbstractTaskRunner{

    private IThreadShareFilter shareFilter;



    @Override
    public <D, V> FunctionResult<V> apply(D d, IFunction<D, V> function) {
        VerificationTool.isNotNull(shareFilter);
        return super.apply(d, function);
    }

    @Override
    public TaskFuture execute(ITaskFunction taskFunction) {
        return super.execute(taskFunction);
    }

    @Override
    public TaskFuture execute(TaskFuture taskFuture, ITaskFunction taskFunction) {
        return super.execute(taskFuture, taskFunction);
    }

    private  <T> Object sharedCall(TaskLocalSafeRunner.Fn function){
        try(CommonThreadLocalAccessor localAccessor=new CommonThreadLocalAccessor()){
            return function.call();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


}
