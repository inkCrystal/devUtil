package cn.dev.core.parallel.task.runner;

import cn.dev.core.parallel.task.api.IFunctionAble;

public record FunctionAbleRecord<R extends TaskFuture>(R r , IFunctionAble functionAble) {

    public IFunctionAble getFun(){
        return functionAble;
    }
    public R getR(){
        return r;
    }
}
