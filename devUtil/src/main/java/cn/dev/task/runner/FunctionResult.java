package cn.dev.task.runner;

import cn.dev.commons.datetime.DateTimeUtil;
import cn.dev.commons.datetime.TimeMillisClock;

import java.io.Serial;
import java.io.Serializable;
import java.util.concurrent.CompletableFuture;

public class FunctionResult<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 2629881396709043646L;
    private long initTime ;
    private long exitTime = 0 ;
    private T result ;

    /**默认是异步的  ，且无法显示改变
     * 当我们调用 ，只有当我们 调用 get 方法时候，将会转换为 同步
     * ---by jason @ 2023/3/20 13:42 */
    private boolean async =true;
    private Throwable throwable;
    /** 构建时候 是 accept   ---by jason @ 2023/3/20 13:33 */
    private FunctionState state = FunctionState.ACCEPT;


    public FunctionResult() {
        this.initTime = TimeMillisClock.currentTimeMillis();
    }


    public boolean isDone(){
        return state.isDone();
    }


    public T get() throws InterruptedException {
        while (!isDone()){
            System.out.printf("--" + this.state);
        }
        System.out.println("");
        System.out.println("");
        return result;
    }


    public static void main(String[] args) throws InterruptedException {
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
