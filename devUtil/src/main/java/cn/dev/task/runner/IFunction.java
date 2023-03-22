package cn.dev.task.runner;

public interface IFunction<D,V> extends IFunctionAble{

    V apply(D d);

}
