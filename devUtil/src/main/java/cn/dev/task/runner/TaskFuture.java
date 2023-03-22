package cn.dev.task.runner;

import cn.dev.commons.datetime.TimeMillisClock;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TaskFuture  implements Serializable {

    @Serial
    private static final long serialVersionUID = -5750213243564716861L;

    private FunctionState state = FunctionState.NONE;

    private List<FunctionStateRecord> stateRecords;

    private Throwable throwable;

    private FunctionAbleRecod<TaskFuture> onTaskDone;

    private FunctionAbleRecod<TaskFuture> onTaskError;



    public TaskFuture() {
    }

    /**
     * 变更任务状态
     * @param state
     */
    protected void stateChange(FunctionState state){
        while (stateLock() ==false){

        }
        try {
            System.out.println("fire state change ->" + state);
            if (this.stateRecords == null) {
                this.stateRecords = new ArrayList<>();
            }
            long time = TimeMillisClock.currentTimeMillis();
            this.state = state;
            stateRecords.add(new FunctionStateRecord(time, state));
        }catch (Exception e){}finally {
            stateUnLock();
        }
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

    public FunctionAbleRecod<TaskFuture> getOnTaskDone() {
        return onTaskDone;
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

    /**
     * 触发任务 异常退出
     */
    protected void fireError( Throwable throwable){
        this.stateChange(FunctionState.ERROR);
        this.throwable = throwable;
        System.out.println("fire error !!");
        if(this.onTaskError!=null){
            System.out.println("提交执行 ");
            callTask(onTaskError.getR(), (ITaskFunction) onTaskError.getFun());
        }

    }

    protected void fireCancel(){
        this.stateChange(FunctionState.CANCEL);
    }

    /**
     * 触发任务 成功
     */
    protected void fireSuccess(){
        this.stateChange(FunctionState.SUCCESS);
    }


    public boolean isDone(){
        return state.isDone();
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
        while (!stateLock()){

        }
        try {
            System.out.println(this.state);
            if (isDone()) {
                System.out.println(this.state);
                System.out.println("已经完成了！！");
                if (this.state == FunctionState.ERROR) {
                    return callTask(new TaskFuture(), taskFunction);
                }
                return new TaskFuture();
            } else {
                System.out.println("注册 error");
                final TaskFuture taskFuture = new TaskFuture();
                this.onTaskError = new FunctionAbleRecod<>(taskFunction, taskFuture);
                return taskFuture;
            }
        }finally {
            stateUnLock();
        }
    }

    /**
     * 当任务完成后 执行的 新任务
     * @param taskFunction
     * @return 返回新任务的 taskFuture
     */
    public TaskFuture thenExecuteIfDone(ITaskFunction taskFunction)  {
        while (!stateLock()) {}
        try {
            // 当注册的时候，已经可能是 done 了，我们锁住这个方法是为了，不让异步线程 修改装填
            if (isDone()) {
                return callTask(new TaskFuture(), taskFunction);
            } else {
                final TaskFuture taskFuture = new TaskFuture();
                this.onTaskDone = new FunctionAbleRecod<>(taskFunction, taskFuture);
                return taskFuture;
            }
        }finally {
            stateUnLock();
        }
    }


    private TaskFuture callTask(TaskFuture future,ITaskFunction taskFunction){
        return TaskRunner.execute(future,taskFunction);

    }
    public static void main(String[] args) throws InterruptedException {
        TaskFuture future = TaskRunner.execute(()->{
            System.out.println("hello ");

            throw new RuntimeException("----");
        }).thenExecuteIfError(()->{
            System.out.println("hello then if done ");
        });

    }





}
