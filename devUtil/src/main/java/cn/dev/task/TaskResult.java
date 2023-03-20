package cn.dev.task;

import java.util.concurrent.CompletableFuture;

public class TaskResult<T> {


    /**是否是异步的   ---by jason @ 2023/3/20 13:15 */
    private boolean async ;

    /**是否已经完成   ---by jason @ 2023/3/20 13:13 */
    private boolean done;

    private T result ;


}
