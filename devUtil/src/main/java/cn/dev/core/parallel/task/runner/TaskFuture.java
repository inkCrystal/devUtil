package cn.dev.core.parallel.task.runner;

import cn.dev.clock.CommonTimeClock;
import cn.dev.core.parallel.task.api.ITaskFunction;
import cn.dev.core.parallel.task.api.runner.IRunnerCallbackHelper;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public sealed class TaskFuture implements Serializable
        permits FunctionResult{

    @Serial
    private static final long serialVersionUID = 3784389255159604500L;

    private IRunnerCallbackHelper runner ;

    private FunctionState state = FunctionState.WAIT;

    private List<FunctionStateRecord> stateRecords;

    private Throwable throwable;

    private FunctionAbleRecord<TaskFuture> onTaskDone;

    private FunctionAbleRecord<TaskFuture> onTaskError;


    //仅仅为内部使用！！
    private static TaskFuture None(){
        return new TaskFuture(true);
    }

    protected TaskFuture() {
        this.state = FunctionState.WAIT;
    }

    protected TaskFuture(boolean isNull) {
        if (isNull) {
            this.state = FunctionState.NONE;
        }else {
            this.state = FunctionState.WAIT;
        }
    }

    /**
     * 变更任务状态
     * @param state
     */
    protected void stateChange(FunctionState state){
        if(this.state== FunctionState.NONE){
            throw new RuntimeException("任务未构建或者不存在，状态变更无意义");
        }else if(this.isDone()){
            return;
        }
        this.safeCtrl(()->{
            if (this.stateRecords == null) {
                this.stateRecords = new ArrayList<>();
            }
            long time = CommonTimeClock.currentTimeMillis();
            this.state = state;
            stateRecords.add(new FunctionStateRecord(time, state));
            return 0;
        });


    }

    private volatile boolean ctrLock = false;
    private synchronized boolean stateLock(){
        if(ctrLock == false){
            ctrLock = true;
            return true;
        }
        return false;
    }
    private void stateUnLock(){
        this.ctrLock = false;
    }

    /**
     * 锁住 对象，防止 其他线程修改
     * 唯一受保护的 暴露方法
     * @return
     */
    protected boolean tryLock(){
        int maxTimes = 9_999;
        while (!stateLock()){
            maxTimes -- ;
            if(maxTimes < 1){
                return false;
            }
        }
        return true;
    }



    /**
     * 触发任务初始化
     */
    protected void fireAccept(){
        this.stateChange(FunctionState.ACCEPT);
    }

    /**
     * 触发任务启动
     */
    protected void fireStart(){
        this.stateChange(FunctionState.START);
    }

    /**
     * 触发任务暂停
     */
    protected void firePause(){
        this.stateChange(FunctionState.PAUSE);
    }

    /**
     * 触发任务继续执行
     */
    protected void fireRePause(){
        this.stateChange(FunctionState.RE_PAUSE);
    }

    protected void fireCancel(){
        this.stateChange(FunctionState.CANCEL);
    }

    /**
     * 触发任务 异常退出
     */
    protected void fireError( Throwable throwable){
        this.stateChange(FunctionState.ERROR);
        this.throwable = throwable;
        if(this.onTaskError!=null){
            thenCallTask(this.onTaskError);
            this.onTaskError = null;
        }
        this.whileDone();
    }

    /**
     * 触发任务 成功
     */
    protected void fireSuccess(){
        this.stateChange(FunctionState.SUCCESS);
        this.whileDone();
    }

    private void whileDone(){
        if(this.onTaskDone !=null){
            thenCallTask(this.onTaskDone);
            this.onTaskDone = null;
        }
    }


    public boolean isDone(){
        return state.isDone();
    }
    public boolean isNone(){
        return state == FunctionState.NONE;
    }


    public Throwable getThrowable() {
        return throwable;
    }


    /**后续操作 注入   ---by jason @ 2023/3/22 22:01 */
    /**
     * 当 任务 执行 出现异常时候 执行的 新任务
     * @param taskFunction
     * @return 返回的是 新任务的 taskFuture
     */
    public TaskFuture thenExecuteIfError(ITaskFunction taskFunction) {
        final Optional<TaskFuture> taskFuture = this.safeCtrl(() -> {
            FunctionAbleRecord<TaskFuture> record = new FunctionAbleRecord<>(new TaskFuture(), taskFunction);
            if (this.isDone()) {
                if (this.state == FunctionState.ERROR) {
                    return thenCallTask(record);
                }
            } else {
                this.onTaskError = record;
            }
            return record.r();
        });
        if(taskFuture.isPresent()){
            return taskFuture.get();
        }
        return None();
    }

    /**
     * 当任务完成后 执行的 新任务
     * @param taskFunction
     * @return 返回新任务的 taskFuture
     */
    public TaskFuture thenExecuteIfDone(ITaskFunction taskFunction)  {
        final Optional<TaskFuture> taskFuture = this.safeCtrl(() -> {
            FunctionAbleRecord<TaskFuture> record = new FunctionAbleRecord<>(new TaskFuture(), taskFunction);
            if (this.isDone()) {
                return this.thenCallTask(record);
            } else {
                this.onTaskDone = record;
                return record.r();
            }
        });
        if (taskFuture.isPresent()) {
            return taskFuture.get();
        }
        return None();
    }

    /**调用后续的任务执行   ---by jason @ 2023/3/23 9:27 */
    protected TaskFuture thenCallTask(FunctionAbleRecord record){
        return TaskExecutor.getCallbackHelper().execute(record.r(), (ITaskFunction) record.getFun());
    }




    /** 线程安全的 保护 ，防止数据被串改   ---by jason @ 2023/3/23 9:27 */
    protected  <T> Optional<T> safeCtrl(SimpleConsumer<T> simpleConsumer){
        if ( !isNone() && this.tryLock()) {
            try{
                return Optional.ofNullable(simpleConsumer.apply());
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                stateUnLock();
            }
            //return functionAbleRecord.getR();
        }
        return Optional.ofNullable(null);
    }


    interface  SimpleConsumer<T>{
        T apply();
    }

}
