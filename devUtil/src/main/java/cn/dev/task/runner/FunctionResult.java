package cn.dev.task.runner;

import cn.dev.commons.datetime.TimeMillisClock;
import cn.dev.exception.TaskCanceledException;
import cn.dev.exception.TaskTimeoutException;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;

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
        this.fireAccept();
    }

    protected FunctionResult(long expireTime) {
        this.expireTime = expireTime;
        this.timeoutAble = true;
        this.fireAccept();
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



    public static void main(String[] args) throws InterruptedException {

        Thread thread =new Thread(()->{
           while (true){
               //
           }
        });
        FunctionResult<Integer> result = new FunctionResult<>();

        CompletableFuture.runAsync(()->{
            result.state = FunctionState.START;
            for (int i = 0; i < 100; i++) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if(i == 50){
                    result.state = FunctionState.PAUSE;
                }
            }
            result.result = 300;
            result.state = FunctionState.SUCCESS;
        });
        System.out.println(result.get());




    }



}
