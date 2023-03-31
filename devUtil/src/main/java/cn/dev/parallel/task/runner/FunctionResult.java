package cn.dev.parallel.task.runner;

import cn.dev.clock.TimeMillisClock;
import cn.dev.exception.TaskCanceledException;
import cn.dev.exception.TaskTimeoutException;
import cn.dev.parallel.task.api.IFunction;

import java.io.Serial;
import java.util.Optional;

public class FunctionResult<T> extends TaskFuture {
    @Serial
    private static final long serialVersionUID = 2629881396709043646L;
    private T result ;

    /**默认是异步的  ，且无法显示改变
     * 当我们调用 ，只有当我们 调用 get 方法时候，将会转换为 同步
     * ---by jason @ 2023/3/20 13:42 */
    private boolean async =true;
    
    /**允许 timeout   ---by jason @ 2023/3/21 10:06 */
    private boolean timeoutAble ;

    private long expireTime ;

    private FunctionAbleRecord doneSuccess =null;

    /** 构建时候 是 accept   ---by jason @ 2023/3/20 13:33 */
    private FunctionState state = FunctionState.ACCEPT;


    protected FunctionResult() {
        super();
    }

    public FunctionResult(boolean isNull) {
        super(isNull);
    }
    private static final <T> FunctionResult<T> None(){
        return new FunctionResult(true);
    }

    protected FunctionResult(long expireTime) {
        this.expireTime = expireTime;
        this.timeoutAble = true;
    }


    protected void setResult(T result,FunctionState state) {
        this.result = result;
        this.state = state;
    }

    public boolean isSuccess(){
        return isDone() && this.state == FunctionState.SUCCESS;
    }


    public T get() {
        long begin = TimeMillisClock.currentTimeMillis();
        long end = TimeMillisClock.currentTimeMillis();
        while (true){
            if(timeoutAble){
                long now = TimeMillisClock.currentTimeMillis();
                if(now>expireTime){
                    throw new TaskTimeoutException();
                }
            }
            if(state.isDone()){
                break;
            }
            if(state ==FunctionState.CANCEL ){
                throw new TaskCanceledException("任务已经被取消");
            }
        }
        return result;
    }


    @Override
    protected void fireSuccess() {
        super.fireSuccess();
        if (this.doneSuccess !=null) {
            this.callFunction(this.get(), (IFunction<T, T>) doneSuccess.getFun());
        }
    }


    public FunctionResult<T> thenApplyIfSuccess(IFunction<T,T> function) {
        final Optional<FunctionResult<T>> optional = safeCtrl(() -> {
            if (this.isSuccess()) {
                //已经success ，直接执行
                return callFunction(this.get(), function);
            }
            if (this.doneSuccess == null) {
                this.doneSuccess = new FunctionAbleRecord(new FunctionResult<T>(), function);
                return (FunctionResult<T>)this.doneSuccess.getR();
            }
            return None();
        });
        if(optional.isPresent()){
            return optional.get();
        }
        return None();
    }

    private FunctionResult<T> callFunction(T t,IFunction<T,T> function){
        return AbstractTaskRunTool.apply(t,function);
    }


}
