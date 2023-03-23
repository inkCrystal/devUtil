package cn.dev.task.service;

import cn.dev.task.api.IFunctionAble;

public record FunctionAbleRecord<R extends TaskFuture>(R r , IFunctionAble functionAble) {

    public IFunctionAble getFun(){
        return functionAble;
    }
    public R getR(){
        return r;
    }
}
