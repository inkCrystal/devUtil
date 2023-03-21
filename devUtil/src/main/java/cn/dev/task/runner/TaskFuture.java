package cn.dev.task.runner;

import cn.dev.commons.datetime.TimeMillisClock;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TaskFuture  implements Serializable {
    @Serial
    private static final long serialVersionUID = -5750213243564716861L;
    private FunctionState state;

    private List<FunctionStateRecord> stateRecords;

    private Throwable throwable;

    public TaskFuture() {
        this.fireAccept();
    }

    /**
     * 变更任务状态
     * @param state
     */
    protected void stateChange(FunctionState state){
        if (this.stateRecords == null) {
            this.stateRecords =new ArrayList<>();
        }
        long time = TimeMillisClock.currentTimeMillis();
        this.state = state;
        stateRecords.add(new FunctionStateRecord(time,state));
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
}
