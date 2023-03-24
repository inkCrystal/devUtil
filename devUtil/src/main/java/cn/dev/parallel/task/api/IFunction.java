package cn.dev.parallel.task.api;

public interface IFunction<D,V> extends IFunctionAble{

    V apply(D d);

}
