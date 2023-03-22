package cn.dev.task.runner;

import cn.dev.commons.datetime.TimeMillisClock;
import cn.dev.exception.TaskCanceledException;
import cn.dev.exception.TaskTimeoutException;

import java.io.Serial;
import java.util.concurrent.CompletableFuture;

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

    /** 构建时候 是 accept   ---by jason @ 2023/3/20 13:33 */
    private FunctionState state = FunctionState.ACCEPT;


    protected FunctionResult() {
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


    public T get() throws InterruptedException {
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



    public FunctionResult<T> thenApplyIfSuccess(IFunction function) {
        if(this.isSuccess()){
            try {
                T d = this.get();
                return TaskRunner.apply(this.get(),function);
            }catch (Exception e){}
        }
        return this;
    }

    public FunctionResult<T> thenApplyIfNotSuccess(IFunction function){
        return this;
    }
    public FunctionResult<T> thenApplyIfError(IFunction function){
        return this;
    }
}
