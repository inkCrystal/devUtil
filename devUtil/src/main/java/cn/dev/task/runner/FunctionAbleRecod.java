package cn.dev.task.runner;

public record FunctionAbleRecod<R extends TaskFuture>(IFunctionAble functionAble, R r) {

    public IFunctionAble getFun(){
        return functionAble;
    }
    public R getR(){
        return r;
    }
}
